package henu.wh.checkbygps.message;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.sql.Date;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;

public class MessageActivity extends AppCompatActivity {

    private ListView mlist;
    private volatile static List<Message> Mlist;
    public volatile static JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_message));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mlist = (ListView) findViewById(R.id.message_list);
        getMessage();
        setMessageList();
        MyMessageAdapter myMessageAdapter = new MyMessageAdapter(Mlist);
        mlist.setAdapter(myMessageAdapter);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Mlist.get(position).isRead()) {
                    return;
                }
                String mtype = Mlist.get(position).getMtype();
                if (mtype.equals("addG")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).
                            setMessage("你是否同意该成员加入群聊？").
                            setIcon(R.mipmap.ic_launcher).setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject agree = new JSONObject();
                                    JSONObject msg = JSONObject.parseObject(Mlist.get(position).getMessage());
                                    agree.put("info", msg);
                                    agree.put("operation", "agreeInG");
                                    agree.put("date", Mlist.get(position).getMdate().toString());
                                    agree.put("mid", Mlist.get(position).getMid());
                                    Client.getClient().send(agree);
                                }
                            }).start();
                            Mlist.get(position).setRead(true);
                        }
                    }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {//添加取消
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject deny = new JSONObject();
                                    JSONObject msg = JSONObject.parseObject(Mlist.get(position).getMessage());
                                    deny.put("info", msg);
                                    deny.put("operation", "denyInG");
                                    deny.put("date", Mlist.get(position).getMdate().toString());
                                    deny.put("mid", Mlist.get(position).getMid());
                                    Client.getClient().send(deny);
                                }
                            }).start();
                            Mlist.get(position).setRead(true);
                        }
                    }).create();
                    alertDialog.show();
                } else if (mtype.equals("sign")) {

                }
            }
        });
    }

    private void setMessageList() {
        if (jsonObject == null) {
            getMessage();
        }
        JSONArray msgs = (JSONArray) jsonObject.get("messagelist");
        Mlist = msgs.toJavaList(Message.class);
    }

    private void getMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject sendMessage = new JSONObject();
                sendMessage.put("want", "msgs");
                Client.getClient().send(sendMessage);
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
    }

    private class MyMessageAdapter extends BaseAdapter {

        List<Message> Mlist;

        public MyMessageAdapter(List<Message> mlist) {
            Mlist = mlist;
        }

        @Override
        public int getCount() {
            return Mlist.size();
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
                viewHolder.m_type = (TextView) convertView.findViewById(R.id.message_Type);
                viewHolder.m_content = (TextView) convertView.findViewById(R.id.message_content);
                viewHolder.m_date = (TextView) convertView.findViewById(R.id.message_date);
                convertView.setTag(viewHolder);
                String mtype = Mlist.get(position).getMtype();
                String message = Mlist.get(position).getMessage();
                Date mdate = Mlist.get(position).getMdate();
                viewHolder.m_date.setText(mdate.toString());
                if (mtype.equals("addG")) {
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    mtype = "请求加群";
                    String memid = jsonObject.getString("memid");
                    String gid = jsonObject.getString("gid");
                    String gname = jsonObject.getString("gname");
                    viewHolder.m_content.setText("用户：" + memid + "，请求加入群聊：" + gid + ":" + gname);
                } else if (mtype.equals("sign")) {
                    mtype = "签到";
                } else if (mtype.equals("mesg")) {
                    mtype = "群聊处理";
                    viewHolder.m_content.setText(message);
                }
                if (!Mlist.get(position).isRead()) {    // 未读信息变红
                    viewHolder.m_type.setTextColor(Color.parseColor("#ff0000"));
                }
                viewHolder.m_type.setText(mtype);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        public class ViewHolder {
            TextView m_type;
            TextView m_content;
            TextView m_date;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}