package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.role.User;

public class PersonActivity extends AppCompatActivity {

    private TextView phone, name, sex;
    private Button back, exit, change;

    public static User user1;

    public static void setUser(User user) {
        PersonActivity.user1 = user;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        phone = (TextView) findViewById(R.id.person_phone);
        name = (TextView) findViewById(R.id.person_name);
        sex = (TextView) findViewById(R.id.person_sex);
        back = (Button) findViewById(R.id.person_back);
        exit = (Button) findViewById(R.id.person_exit);
        change = (Button) findViewById(R.id.person_change);

        phone.setText("账号：" + user1.getPhone());
        name.setText("姓名：" + user1.getName());
        String s = user1.isSex() ? "男性" : "女性";
        sex.setText("性别：" + s);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.setUser(PersonActivity.user1);
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
                    LoginActivity.client.close();
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

    @Override
    protected void onResume() {
        super.onResume();
        phone.setText("账号：" + user1.getPhone());
        name.setText("姓名：" + user1.getName());
        String s = user1.isSex() ? "男性" : "女性";
        sex.setText("性别：" + s);
    }
}