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
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;

public class ChangeInfoActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtncpd;
    private EditText newname;

    private static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_change_info));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        initViews();
        setListeners();
    }

    private boolean changeName(String phone, String newname) {
        boolean flag = false;
        Connection conn = JdbcUtil.conn();  // 建立与数据库的连接
        if (!JdbcUtil.isExistUSER(conn, phone)) {
            Log.e("Connect-MySQL", "账号不存在，请先进行注册！");
            // 这里开启了一个新进程，Toast调用法法师需要用到Looper的prepare进行准备
            Looper.prepare();
            Toast.makeText(ChangeInfoActivity.this, "账号不存在，请先进行注册！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        } else {
            JdbcUtil.updatename(conn, phone, newname);
            flag = true;
        }
        JdbcUtil.close(conn);
        return flag;
    }

    public static void getData(String phone) {
        ChangeInfoActivity.phone = phone;
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_change_back1:
                    ChangeInfoActivity.this.finish();
                    break;
                case R.id.btn_change_info:
                    String name = newname.getText().toString();
                    if (name.isEmpty()) {
                        showMessage("请输入信息");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (changeName(ChangeInfoActivity.phone, name)) {
                                    Looper.prepare();
                                    Toast.makeText(ChangeInfoActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                        Intent intent = new Intent();
                        intent.setClass(ChangeInfoActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
            }

        }
    }

    private void showMessage(String message) {
        Toast.makeText(ChangeInfoActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initViews() {
        mBtnback = (Button) findViewById(R.id.btn_change_back1);
        mBtncpd = (Button) findViewById(R.id.btn_change_info);
        newname = (EditText) findViewById(R.id.edit_change_name);
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtncpd.setOnClickListener(onClick);
    }
}