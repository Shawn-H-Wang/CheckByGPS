package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
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
            return 0;
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
            return null;
        }
    }
}