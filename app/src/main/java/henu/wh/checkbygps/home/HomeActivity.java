package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.chat.ChatActivity;
import henu.wh.checkbygps.chat.PersonChat;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.message.MessageActivity;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class HomeActivity extends AppCompatActivity implements Init {

    private Button searchView;
    private ImageView imageView;

    public static String[][] childString1 = new String[2][];

    public volatile static JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_groupmanager));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonObject = new JSONObject();
                jsonObject.put("want", "groupInfo");
                Client.getClient().send(jsonObject);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = Client.getClient().read();
                try {
                    jsonObject = JSONObject.parseObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonObject = new JSONObject();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setChildString();
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
                // 普通群事件监听
                String group = childString1[groupPosition][childPosition];
                /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("群聊" + childString1[groupPosition][childPosition].substring(childString1[groupPosition][childPosition].indexOf(":")));
                builder.setMessage("你想做什么操作？");
                builder.setIcon(R.mipmap.ic_launcher);*/
                if (Arrays.asList(childString1[0]).contains(group)) {
                    MGroupManagerActivity.setGname(group);
                    Intent intent = new Intent(HomeActivity.this, MGroupManagerActivity.class);
                    startActivity(intent);
                } else {
                    IGroupManagerActivity.setGname(group);
                    Intent intent = new Intent(HomeActivity.this, IGroupManagerActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        setListeners();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonObject = new JSONObject();
                jsonObject.put("want", "groupInfo");
                Client.getClient().send(jsonObject);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = Client.getClient().read();
                try {
                    jsonObject = JSONObject.parseObject(s);
                    while (jsonObject.getString("code") != null) {
                        s = Client.getClient().read();
                        jsonObject = JSONObject.parseObject(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonObject = new JSONObject();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("JSON", jsonObject.toJSONString());
        JSONArray o1 = (JSONArray) jsonObject.get("manager");
        JSONArray o2 = (JSONArray) jsonObject.get("member");
        List<String> list1 = o1.toJavaList(String.class);
        List<String> list2 = o2.toJavaList(String.class);
        Log.d("M", list1.toString());
        if (list1.size() <= 0) {
            childString1[0] = null;
        }
        childString1[0] = new String[list1.size()];
        for (int i = 0; i < list1.size(); i++) {
            childString1[0][i] = list1.get(i);
        }
        Log.d("M", list2.toString());
        if (list2.size() <= 0) {
            childString1[1] = null;
        }
        childString1[1] = new String[list2.size()];
        for (int i = 0; i < list2.size(); i++) {
            childString1[1][i] = list2.get(i);
        }
        for (String[] strings : childString1) {
            System.out.println(strings);
        }
    }
}