package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.Text;

import java.util.List;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.message.MessageActivity;
import henu.wh.checkbygps.role.SignInfo;

public class SignInfoActivity extends AppCompatActivity {

    private Button back;
    private ListView signInfo;
    private static String gid;
    private static boolean isManager;
    private volatile static List<SignInfo> signInfoList;


    public static void setGid(String gid) {
        SignInfoActivity.gid = gid;
    }

    public static void setIsManager(boolean isManager) {
        SignInfoActivity.isManager = isManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);
        signInfo = (ListView) findViewById(R.id.signinfo_list);
        back = (Button) findViewById(R.id.backSINFO);
        getSignInfoList();
        SignInfoAdapter signInfoAdapter = new SignInfoAdapter(signInfoList);
        signInfo.setAdapter(signInfoAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FullMemberSignInfoActivity.setGidSignid(gid, signInfoList.get(position).getSignID());
                Intent intent = new Intent(SignInfoActivity.this, FullMemberSignInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getSignInfoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tag1", isManager);
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
                signInfoList = o1.toJavaList(SignInfo.class);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SignInfoAdapter extends BaseAdapter {

        List<SignInfo> signInfoList;

        public SignInfoAdapter(List<SignInfo> signInfoList) {
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
                String signID = signInfoList.get(position).getSignID();
                String date = signInfoList.get(position).getDate();
                viewHolder.signInfo.setText("群签到");
                viewHolder.signID.setText(signID);
                viewHolder.signDate.setText(date);
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