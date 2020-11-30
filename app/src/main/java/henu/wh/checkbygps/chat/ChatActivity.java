package henu.wh.checkbygps.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.role.Group;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private ListView lv_chat_dialog;
    private TextView tv_gname;
    private static List<PersonChat> personChats = new ArrayList<PersonChat>();
    private static Group group = new Group();


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    lv_chat_dialog.setSelection(personChats.size());
                    break;
                default:
                    break;
            }
        }
    };

    public static void setGroup(Group g) {
        ChatActivity.group = g;
    }

    public static void setMList(List<PersonChat> personChats) {
        ChatActivity.personChats = personChats;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tv_gname = (TextView) findViewById(R.id.chat_gname);
        tv_gname.setText(ChatActivity.group.getGname());

        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);

        Button btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);

        chatAdapter = new ChatAdapter(this, personChats);
        lv_chat_dialog.setAdapter(chatAdapter);
        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
                    Toast.makeText(ChatActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonChat personChat = new PersonChat();
                //代表自己发送
                personChat.setMeSend(true);
                //得到发送内容
                personChat.setChatMessage(et_chat_message.getText().toString());
                //加入集合
                personChats.add(personChat);
                //清空输入框
                et_chat_message.setText("");
                //刷新ListView
                chatAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
            }
        });
    }
}