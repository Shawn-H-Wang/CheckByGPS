package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class FullMemberSignInfoActivity extends AppCompatActivity {

    private Button button, daochu;
    private ListView listView;
    private static String gid;
    private static String signid;
    private volatile static List<MemSignInfo> memSignInfos;
    private volatile JSONObject jsonObject;

    public static void setGidSignid(String gid, String signid) {
        FullMemberSignInfoActivity.gid = gid;
        FullMemberSignInfoActivity.signid = signid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_member_sign_info);
        setJSON();
        button = (Button) findViewById(R.id.backINFO);
        daochu = (Button) findViewById(R.id.daochu);
        listView = (ListView) findViewById(R.id.full_member_sign_info);
        MemSignInfoAdapter memSignInfoAdapter = new MemSignInfoAdapter(memSignInfos);
        listView.setAdapter(memSignInfoAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        daochu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FullMemberSignInfoActivity.this);
                builder.setMessage("你是否要导出记录？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();
            }
        });
    }

    private void setJSON() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject send = new JSONObject();
                send.put("want", "memsign");
                send.put("gid", gid);
                send.put("signid", signid);
                Client.getClient().send(send);
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
                jsonObject = JSONObject.parseObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("memlist");
                memSignInfos = jsonArray.toJavaList(MemSignInfo.class);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class MemSignInfoAdapter extends BaseAdapter {

        List<MemSignInfo> memSignInfos;

        public MemSignInfoAdapter(List<MemSignInfo> memSignInfos) {
            this.memSignInfos = memSignInfos;
        }

        @Override
        public int getCount() {
            return memSignInfos.size();
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
                viewHolder.uid = (TextView) convertView.findViewById(R.id.message_Type);
                viewHolder.isSign = (TextView) convertView.findViewById(R.id.message_content);
                viewHolder.signDate = (TextView) convertView.findViewById(R.id.message_date);
                viewHolder.isSign.setText(memSignInfos.get(position).getSign() ? "已签到" : "未签到");
                viewHolder.uid.setText(memSignInfos.get(position).getUid());
                if (!memSignInfos.get(position).getSign()) {
                    viewHolder.uid.setTextColor(Color.parseColor("#FF0000"));
                    viewHolder.isSign.setTextColor(Color.parseColor("#FF0000"));
                    viewHolder.signDate.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    viewHolder.signDate.setText(memSignInfos.get(position).getSigndate());
                    viewHolder.uid.setTextColor(Color.parseColor("#000000"));
                    viewHolder.isSign.setTextColor(Color.parseColor("#000000"));
                    viewHolder.signDate.setTextColor(Color.parseColor("#000000"));
                }
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView uid;
            TextView isSign;
            TextView signDate;
        }
    }
}