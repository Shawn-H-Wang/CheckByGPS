package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.role.User;

public class PersonActivity extends AppCompatActivity {

    private TextView phone, name, sex;
    private Button back, exit, change;

    public volatile static JSONObject jsonObject = new JSONObject();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_persion));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        phone = (TextView) findViewById(R.id.person_phone);
        name = (TextView) findViewById(R.id.person_name);
        sex = (TextView) findViewById(R.id.person_sex);
        back = (Button) findViewById(R.id.person_back);
        exit = (Button) findViewById(R.id.person_exit);
        change = (Button) findViewById(R.id.person_change);

        getMSG();

        phone.setText("账号：" + jsonObject.getString("phone"));
        name.setText("姓名：" + jsonObject.getString("name"));
//        String s = jsonObject.getBoolean("sex") ? "男性" : "女性";
//        sex.setText("性别：" + s);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    Client.getClient().close();
                    startActivity(intent);
                    Toast.makeText(PersonActivity.this, "退出成功！！", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PersonActivity.this, "服务器异常，请稍后重试！", Toast.LENGTH_LONG).show();
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonActivity.this, ChangePersonActivity.class));
            }
        });
    }

    public void getMSG() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("want", "info");
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMSG();
        phone.setText("账号：" + jsonObject.getString("phone"));
        name.setText("姓名：" + jsonObject.getString("name"));
        String s = jsonObject.getBoolean("sex") ? "男性" : "女性";
        sex.setText("性别：" + s);
    }
}