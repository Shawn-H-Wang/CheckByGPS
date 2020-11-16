package henu.wh.checkbygps.forgetpasswd;

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
import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;

public class ChangePawdActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtncpd;
    private EditText password, repet_password;

    private static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pawd);

        initViews();
        setListeners();
    }

    public static void getData(String phone) {
        ChangePawdActivity.phone = phone;
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtncpd.setOnClickListener(onClick);
    }

    private void showMessage(String message) {
        Toast.makeText(ChangePawdActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean changePassword(String phone, String newPassword) {
        boolean flag = false;
        Connection conn = JdbcUtil.conn();  // 建立与数据库的连接
        if (!JdbcUtil.isExist(conn, phone)) {
            Log.e("Connect-MySQL", "账号不存在，请先进行注册！");
            // 这里开启了一个新进程，Toast调用法法师需要用到Looper的prepare进行准备
            Looper.prepare();
            Toast.makeText(ChangePawdActivity.this, "账号不存在，请先进行注册！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        } else {
            JdbcUtil.updatepassword(conn, phone, newPassword);
            flag = true;
        }
        JdbcUtil.close(conn);
        return flag;
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_change_back:
                    ChangePawdActivity.this.finish();
                    break;
                case R.id.btn_change_cpd:
                    String pd = password.getText().toString();
                    String repd = repet_password.getText().toString();
                    if (pd.isEmpty()) {
                        showMessage("请输入密码");
                    } else if (repd.isEmpty()) {
                        showMessage("请再次输入密码");
                    } else if (!pd.equals(repd)) {
                        showMessage("两次输入密码不正确，请重新输入！");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (changePassword(ChangePawdActivity.phone, pd)) {
                                    Looper.prepare();
                                    Toast.makeText(ChangePawdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                        Intent intent = new Intent();
                        intent.setClass(ChangePawdActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
            }

        }
    }

    @Override
    public void initViews() {
        mBtnback = (Button) findViewById(R.id.btn_change_back);
        mBtncpd = (Button) findViewById(R.id.btn_change_cpd);
        password = (EditText) findViewById(R.id.edit_change_pawd);
        repet_password = (EditText) findViewById(R.id.edit_change_repete);
    }
}