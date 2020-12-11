package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.forgetpasswd.ForgetPasswordActivity;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.home.HomeActivity;
import henu.wh.checkbygps.home.MenuActivity;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class LoginActivity extends AppCompatActivity implements Init {

    private Button mBtnlogin, mBtnforgetpassword, mBtnback;
    private EditText eTuser, eTpassword;

    public volatile static boolean FLAG = false;
    public volatile static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_login));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client.getClient();
            }
        }).start();
        initViews();
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnlogin.setOnClickListener(onClick);
        mBtnforgetpassword.setOnClickListener(onClick);
        mBtnback.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_signin:
                    if (eTuser.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (eTpassword.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                user = new User();
                                user.setPhone(eTuser.getText().toString());
                                user.setPassword(eTpassword.getText().toString());
                                user.setOpeartion("login");
                                Client.getClient().sendLogin(user);
                            }
                        }).start();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.FLAG = Client.getClient().readBoolean();
                        }
                    }).start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (LoginActivity.FLAG) {
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "登陆失败，请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_forgetpasswd:
                    startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                    break;
                case R.id.btn_login_back:
                    LoginActivity.this.finish();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client.getClient();
            }
        }).start();
    }

    @Override
    public void initViews() {
        mBtnlogin = (Button) findViewById(R.id.btn_signin);
        mBtnback = (Button) findViewById(R.id.btn_login_back);
        mBtnforgetpassword = (Button) findViewById(R.id.btn_forgetpasswd);
        eTuser = (EditText) findViewById(R.id.edittext_number);
        eTpassword = (EditText) findViewById(R.id.edittext_passwd);
    }

}