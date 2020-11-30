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
import java.util.Arrays;
import java.util.List;

import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.forgetpasswd.ForgetPasswordActivity;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.help.MD5Utils;
import henu.wh.checkbygps.home.HomeActivity;
import henu.wh.checkbygps.home.MenuActivity;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class LoginActivity extends AppCompatActivity implements Init {

    private Button mBtnlogin, mBtnforgetpassword, mBtnback;
    private EditText eTuser, eTpassword;
    private CheckBox cBremeber;

    public volatile static boolean FLAG = false;
    public volatile static User user = new User();
    public volatile static List<Group> list = new ArrayList<>();
    public volatile static String[][] childString = new String[2][];
    public volatile static Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
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
                    String userphone = eTuser.getText().toString();
                    String password = eTpassword.getText().toString();
                    if (userphone.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        login(user, userphone, password);
                        try {
                            Thread.sleep(500); // 这里一定要设置等待时间，让子线程对信号量进行修改
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (LoginActivity.FLAG) {
                            setChildString();
                            MenuActivity.setUser(user);
                            MenuActivity.setChildString(childString);
                            MenuActivity.setGroupList(list);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        LoginActivity.client = new Client();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        LoginActivity.FLAG = false;
                                        Looper.prepare();
                                        Toast.makeText(LoginActivity.this, "服务器异常，请稍后重试！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (LoginActivity.FLAG) {
                                eTuser.setText("");
                                eTpassword.setText("");
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(intent);    // 如果登陆成功，跳转至主界面
                            }
                        }
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
    public void initViews() {
        mBtnlogin = (Button) findViewById(R.id.btn_signin);
        mBtnback = (Button) findViewById(R.id.btn_login_back);
        mBtnforgetpassword = (Button) findViewById(R.id.btn_forgetpasswd);
        eTuser = (EditText) findViewById(R.id.edittext_number);
        eTpassword = (EditText) findViewById(R.id.edittext_passwd);
    }

    /**
     * <p>判断用户名与密码是否匹配，如果不匹配，返回false，否则返回true</p>
     * <p>需要调用数据库进行查找</p>
     *
     * @return
     */
    private boolean isRight(User user, String username, String password) {
        boolean flag = false;
        Connection conn = JdbcUtil.conn();
        if (JdbcUtil.isExistUSER(conn, username)) {
            password = MD5Utils.Encrypt(password);
            if (JdbcUtil.selectPassword(conn, user, username, password)) {
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

    private void login(User user, String userphone, String password) {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                if (isRight(user, userphone, password)) {
                    LoginActivity.FLAG = true;
                }
            }
        }).start();
    }

    public void setChildString() {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                Connection conn = JdbcUtil.conn();
                JdbcUtil.selectManageGroup(conn, user);
                JdbcUtil.selectInGroup(conn, user, list);
                System.out.println(list.toString());
//                System.out.println(Arrays.deepToString(childString));
                JdbcUtil.close(conn);
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(user.getManagegroup());
        System.out.println(user.getIngroup());
        List<String> list = user.getManagegroup();
        if (list.size() <= 0) {
            childString[0] = null;
        }
        childString[0] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            childString[0][i] = list.get(i);
        }
        list = user.getIngroup();
        if (list.size() <= 0) {
            childString[1] = null;
        }
        childString[1] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            childString[1][i] = list.get(i);
        }
    }
}