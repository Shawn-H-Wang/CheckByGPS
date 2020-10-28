package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.forgetpasswd.ForgetPasswordActivity;
import henu.wh.checkbygps.help.Init;

public class LoginActivity extends AppCompatActivity implements Init {

    private Button mBtnlogin, mBtnforgetpassword, mBtnback;
    private EditText eTuser, eTpassword;
    private CheckBox cBremeber;

    public static boolean flag = false; // 登陆成功标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButton();
        initEditText();
        cBremeber = (CheckBox) findViewById(R.id.cb_issave);

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
                    String username = eTuser.getText().toString();
                    String password = eTpassword.getText().toString();
                    if (username.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (isRight(username, password)) {
                                    LoginActivity.flag = true;
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                    }
                    if (LoginActivity.flag) {
//                        startActivity(new Intent(LoginActivity.this, null));    // 如果登陆成功，跳转至主界面
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
    public void initButton() {
        mBtnlogin = (Button) findViewById(R.id.btn_signin);
        mBtnback = (Button) findViewById(R.id.btn_login_back);
        mBtnforgetpassword = (Button) findViewById(R.id.btn_forgetpasswd);
    }

    @Override
    public void initEditText() {
        eTuser = (EditText) findViewById(R.id.edittext_number);
        eTpassword = (EditText) findViewById(R.id.edittext_passwd);
    }

    @Override
    public void initRadioButton() {

    }

    /**
     * <p>判断用户名与密码是否匹配，如果不匹配，返回false，否则返回true</p>
     * <p>需要调用数据库进行查找</p>
     *
     * @return
     */
    private boolean isRight(String username, String password) {
        boolean flag = false;
        Connection conn = JdbcUtil.conn();
        if (JdbcUtil.isExist(conn, username)) {
            if (JdbcUtil.selectPassword(conn, username, password)) {
                flag = true;    // 表示匹配成功
            } else {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "密码输入错误，请重新输入！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        } else {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "用户不存在，请先注册！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        JdbcUtil.close(conn);
        return flag;
    }
}