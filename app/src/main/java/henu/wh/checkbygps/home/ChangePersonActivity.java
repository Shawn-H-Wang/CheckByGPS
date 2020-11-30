package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.forgetpasswd.ChangeInfoActivity;

public class ChangePersonActivity extends AppCompatActivity {

    private Button submit, cancel;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_person);

        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        name = (EditText) findViewById(R.id.ch_name);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = name.getText().toString();
                if (newname.isEmpty()) {
                    showMessage("请输入信息");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (changeName(PersonActivity.user1.getPhone(), newname)) {
                                Looper.prepare();
                                Toast.makeText(ChangePersonActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                    finish();
                }
                PersonActivity.user1.setName(newname);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(ChangePersonActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean changeName(String phone, String newname) {
        boolean flag = false;
        Connection conn = JdbcUtil.conn();  // 建立与数据库的连接
        if (!JdbcUtil.isExistUSER(conn, phone)) {
            Log.e("Connect-MySQL", "账号不存在，请先进行注册！");
            // 这里开启了一个新进程，Toast调用法法师需要用到Looper的prepare进行准备
            Looper.prepare();
            Toast.makeText(ChangePersonActivity.this, "账号不存在，请先进行注册！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        } else {
            JdbcUtil.updatename(conn, phone, newname);
            flag = true;
        }
        JdbcUtil.close(conn);
        return flag;
    }
}