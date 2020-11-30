package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.chat.ChatActivity;
import henu.wh.checkbygps.chat.PersonChat;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class HomeActivity extends AppCompatActivity implements Init {

    private Button searchView;
    private ImageView imageView;

    public static String[][] childString1;
    public static User user1;
    public static List<Group> groupList = new ArrayList<>();
    public volatile static List<PersonChat> mlist = new ArrayList<>();

    public static void setUser(User user) {
        HomeActivity.user1 = user;
    }

    public static void setChildString(String[][] childString) {
        HomeActivity.childString1 = childString;
    }

    public static void setGroupList(List<Group> groupList) {
        MenuActivity.groupList = groupList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        MyExtendableListViewAdapter myExtendableListViewAdapter = new MyExtendableListViewAdapter(childString1);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expend_list);
        expandableListView.setAdapter(myExtendableListViewAdapter);
        // 设置分组的监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        // 设置子项布局监听
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Group group = groupList.get(childPosition);
//                ChatActivity.setGroup(group);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Connection conn = JdbcUtil.conn();
//                        JdbcUtil.selectMessage(conn, HomeActivity.user1, group.getGid(), mlist);
//                        JdbcUtil.close(conn);
//                    }
//                });
//                ChatActivity.setMList(HomeActivity.mlist);
//                Intent intent = new Intent();
//                intent.setClass(HomeActivity.this, ChatActivity.class);
//                startActivity(intent);
                return true;
            }
        });
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setChildString();
    }

    @Override
    public void initViews() {
        searchView = (Button) findViewById(R.id.search_view);
        imageView = (ImageView) findViewById(R.id.img_plus_group);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.img_plus_group:
                    intent = new Intent(HomeActivity.this, CreateGroupActivity.class);
                    startActivity(intent);
                    break;
                case R.id.search_view:
                    intent = new Intent(HomeActivity.this, SearchGroupActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public void setListeners() {
        OnClick onClick = new OnClick();
        imageView.setOnClickListener(onClick);
        searchView.setOnClickListener(onClick);
    }

    public void setChildString() {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                Connection conn = JdbcUtil.conn();
                JdbcUtil.selectManageGroup(conn, user1);
                JdbcUtil.selectInGroup(conn, user1, groupList);
                System.out.println(groupList.toString());
//                System.out.println(Arrays.deepToString(childString));
                JdbcUtil.close(conn);
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(user1.getManagegroup());
        System.out.println(user1.getIngroup());
        List<String> list = user1.getManagegroup();
        if (list.size() <= 0) {
            childString1[0] = null;
        }
        childString1[0] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            childString1[0][i] = list.get(i);
        }
        list = user1.getIngroup();
        if (list.size() <= 0) {
            childString1[1] = null;
        }
        childString1[1] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            childString1[1][i] = list.get(i);
        }
    }
}