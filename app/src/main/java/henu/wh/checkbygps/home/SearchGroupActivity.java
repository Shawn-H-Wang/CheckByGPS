package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.role.Group;

public class SearchGroupActivity extends AppCompatActivity {

    private EditText searchGroup;
    private Button search;
    private ListView Glist;

    private volatile static List<Group> glist = new ArrayList<>();
    private volatile int addG = -100;
    public volatile static JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_search_group));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        search = (Button) findViewById(R.id.search);
        searchGroup = (EditText) findViewById(R.id.search_group);
        Glist = (ListView) findViewById(R.id.searchgroup_list);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGroup.clearFocus();
                if (searchGroup.getText().toString().isEmpty()) {
                    Toast.makeText(SearchGroupActivity.this, "请输入需要查找的内容！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String searchmessage = searchGroup.getText().toString();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("want", "searchG");
                        jsonObject.put("keyword", searchmessage);
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
                JSONArray ja = (JSONArray) jsonObject.get("grouplist");
                glist = ja.toJavaList(Group.class);
                MyListAdapter myListAdapter = new MyListAdapter(glist);
                Glist.setAdapter(myListAdapter);
            }
        });
        Glist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(SearchGroupActivity.this).setTitle(glist.get(position).getGname())
                        .setMessage("是否加入此群聊？")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("operation", "addG");
                                        jsonObject.put("userid", LoginActivity.user.getPhone());
                                        jsonObject.put("groupid", glist.get(position).getGid());
                                        jsonObject.put("groupowner", glist.get(position).getGroupowner());
                                        jsonObject.put("groupname", glist.get(position).getGname());
                                        Client.getClient().send(jsonObject);
                                    }
                                }).start();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addG = Integer.parseInt(Client.getClient().read());
                                    }
                                }).start();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("addG=" + addG);
                                if (addG == 1) {
                                    Toast.makeText(SearchGroupActivity.this, "请求已发送，请等待管理员同意", Toast.LENGTH_SHORT).show();
                                } else if (addG == 0) {
                                    Toast.makeText(SearchGroupActivity.this, "你已经加入群聊，不要重复申请", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SearchGroupActivity.this, "服务异常，请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog2.show();
            }
        });
    }

    private class MyListAdapter extends BaseAdapter {

        private List<Group> glist;

        public MyListAdapter(List<Group> glist) {
            this.glist = glist;
        }

        @Override
        public int getCount() {
            return glist.size();
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
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                holder = new ViewHolder();
                convertView = li.inflate(R.layout.group_item, null);
                holder.tv_gname = (TextView) convertView.findViewById(R.id.tv_gname);
                holder.tv_gid = (TextView) convertView.findViewById(R.id.tv_gid);
                holder.tv_gowner = (TextView) convertView.findViewById(R.id.tv_gowner);
                convertView.setTag(holder);
                holder.tv_gname.setText("群名：" + glist.get(position).getGname());
                holder.tv_gid.setText("群ID：" + glist.get(position).getGid());
                holder.tv_gowner.setText("群主ID：" + glist.get(position).getGroupowner());
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        public class ViewHolder {
            TextView tv_gname;
            TextView tv_gid;
            TextView tv_gowner;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }
}