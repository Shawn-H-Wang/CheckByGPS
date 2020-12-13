package henu.wh.checkbygps.home;

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

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.help.ExcelUtils;
import henu.wh.checkbygps.role.MemSignInfo;
import henu.wh.checkbygps.role.User;

public class FullMemberSignInfoActivity extends AppCompatActivity {

    private static Button button;
    public static Button daochu;
    private ListView listView;
    private File file;
    private String fileName;
    private static String gid;
    private static String signid;
    private volatile JSONObject jsonObject;
    private volatile String[] title = {"编号", "姓名", "手机号", "群ID", "签到状态", "签到经度", "签到纬度", "签到时间"};
    private volatile static List<MemSignInfo> memSignInfos;
    private volatile static ArrayList<ArrayList<String>> recordList;
    private volatile static List<User> userList;

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
                        getSignFullInfo();
                        exportExcel();
                        // Intent sendIntent = new Intent();
                        // sendIntent.setAction(Intent.ACTION_SEND);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();
            }
        });
    }

    private void setJSON() {
        System.out.println(gid);
        System.out.println(signid);
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

    private void getSignFullInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject want = new JSONObject();
                List<String> uids = new ArrayList<>();
                for (MemSignInfo signInfo : memSignInfos) {
                    uids.add(signInfo.getUid());
                }
                want.put("want", "users_info");
                want.put("uids", uids);
                Client.getClient().send(want);
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
                JSONObject o = JSONObject.parseObject(s);
                JSONArray jsonArray = (JSONArray) o.get("users");
                userList = jsonArray.toJavaList(User.class);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outer:
        for (MemSignInfo info : memSignInfos) {
            for (User user : userList) {
                if (info.getUid().equals(user.getPhone())) {
                    info.setName(user.getName());
                    continue outer;
                }
            }
        }
    }

    public void exportExcel() {
        file = new File("sdcard/Android/data/henu.wh.checkbygps/files");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/" + signid + ".xls", title);
        fileName = "sdcard/Android/data/henu.wh.checkbygps/files/" + signid + ".xls";
        ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
    }


    private ArrayList<ArrayList<String>> getRecordData() {
        recordList = new ArrayList<>();
        for (int i = 0; i < memSignInfos.size(); i++) {
            MemSignInfo info = memSignInfos.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(String.valueOf(i));
            beanList.add(info.getName());
            beanList.add(info.getUid());
            beanList.add(info.getGid());
            beanList.add(info.getSignType());
            beanList.add(null == info.getJingdu() ? "-" : info.getJingdu());
            beanList.add(null == info.getWeidu() ? "-" : info.getWeidu());
            beanList.add(null == info.getSigndate() ? "-" : info.getSigndate());
            recordList.add(beanList);
        }
        return recordList;
    }

    public void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
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
                viewHolder.isSign.setText(memSignInfos.get(position).getSign() ? "已签到:" + memSignInfos.get(position).getSignType() : memSignInfos.get(position).getSignType());
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