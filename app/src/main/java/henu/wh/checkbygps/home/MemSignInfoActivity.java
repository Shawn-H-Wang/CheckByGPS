package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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

import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.role.MemSignInfo;

public class MemSignInfoActivity extends AppCompatActivity {

    private Button back;
    private ListView signInfo;
    private static String gid;
    private volatile static List<MemSignInfo> signInfoList;

    public static void setGid(String gid) {
        MemSignInfoActivity.gid = gid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_sign_info);
        signInfo = (ListView) findViewById(R.id.I_signinfo_list);
        back = (Button) findViewById(R.id.I_backSINFO);
        getSignInfoList();
        SignInfoAdapter signInfoAdapter = new SignInfoAdapter(signInfoList);
        signInfo.setAdapter(signInfoAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSignInfoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tag1", false);
                jsonObject.put("want", "signinfo");
                jsonObject.put("gid", gid);
                Client.getClient().send(jsonObject);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = Client.getClient().read();
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray o1 = (JSONArray) jsonObject.get("signInfo");
                signInfoList = o1.toJavaList(MemSignInfo.class);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SignInfoAdapter extends BaseAdapter {

        List<MemSignInfo> signInfoList;

        public SignInfoAdapter(List<MemSignInfo> signInfoList) {
            this.signInfoList = signInfoList;
        }

        @Override
        public int getCount() {
            return signInfoList.size();
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
                viewHolder = new ViewHolder();
                convertView = li.inflate(R.layout.message_item, null);
                viewHolder.signInfo = (TextView) convertView.findViewById(R.id.message_Type);
                viewHolder.signID = (TextView) convertView.findViewById(R.id.message_content);
                viewHolder.signDate = (TextView) convertView.findViewById(R.id.message_date);
                convertView.setTag(viewHolder);
                String signID = signInfoList.get(position).getSignid();
                String date = signInfoList.get(position).getSigndate();
                viewHolder.signInfo.setText("签到");
                viewHolder.signID.setText(signID);
                viewHolder.signDate.setText(date.isEmpty() ? "为签到" : date);
                viewHolder.signInfo.setTextColor(Color.parseColor("#000000"));
                viewHolder.signID.setTextColor(Color.parseColor("#000000"));
                viewHolder.signDate.setTextColor(Color.parseColor("#000000"));
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView signInfo;
            TextView signID;
            TextView signDate;
        }
    }
}