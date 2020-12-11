package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.message.Message;
import henu.wh.checkbygps.role.User;

public class MemberInfoActivity extends AppCompatActivity {

    private Button back;
    private ListView listView;
    private static String gid;
    private volatile static List<User> memList;

    public static void setGid(String gid) {
        MemberInfoActivity.gid = gid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        back = (Button) findViewById(R.id.backLM);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getMemInfo();
        listView = (ListView) findViewById(R.id.meminfo);
        MemberInfoAdapter memberInfoAdapter = new MemberInfoAdapter(memList);
        listView.setAdapter(memberInfoAdapter);
    }

    private void getMemInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("want", "meminfo");
                jsonObject.put("gid", gid);
                Client.getClient().send(jsonObject);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = Client.getClient().read();
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("memInfo");
                memList = jsonArray.toJavaList(User.class);
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class MemberInfoAdapter extends BaseAdapter {

        List<User> memList;

        public MemberInfoAdapter(List<User> memList) {
            this.memList = memList;
        }

        @Override
        public int getCount() {
            return memList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                viewHolder = new MemberInfoAdapter.ViewHolder();
                convertView = li.inflate(R.layout.group_item, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.tv_gname);
                viewHolder.id = (TextView) convertView.findViewById(R.id.tv_gid);
                viewHolder.sex = (TextView) convertView.findViewById(R.id.tv_gowner);
                convertView.setTag(viewHolder);
                String name = memList.get(position).getName();
                String phone = memList.get(position).getPhone();
                String sex = memList.get(position).isSex() ? "男性" : "女性";
                viewHolder.name.setText("姓名：" + name);
                viewHolder.id.setText("ID：" + phone);
                viewHolder.sex.setText("性别：" + sex);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        public class ViewHolder {
            TextView name;
            TextView id;
            TextView sex;
        }
    }
}