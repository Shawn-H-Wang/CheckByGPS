package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Helper;
import henu.wh.checkbygps.help.Init;

public class CreateGroupActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtncreate;
    private EditText eTgroupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initViews();
        setListeners();
    }

    @Override
    public void initViews() {
        mBtnback = (Button) findViewById(R.id.group_back);
        mBtncreate = (Button) findViewById(R.id.group_create);
        eTgroupname = (EditText) findViewById(R.id.edit_groupname);
    }

    public void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtncreate.setOnClickListener(onClick);
    }

    public void createGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String groupname = eTgroupname.getText().toString();
                Connection conn = JdbcUtil.conn();
                JdbcUtil.insertGroup(conn, Helper.randomGID(), groupname, HomeActivity.user1.getPhone());
                JdbcUtil.close(conn);
            }
        }).start();
    }

    public void showMessage(String message) {
        Toast.makeText(CreateGroupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.group_back:
                    finish();
                    break;
                case R.id.group_create:
                    if (eTgroupname.getText().toString().isEmpty()) {
                        showMessage("请输入创建的群名！！");
                    } else {
                        createGroup();
                        try {
                            Thread.sleep(500); // 这里一定要设置等待时间，让子线程对信号量进行修改
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        showMessage("创建成功！");
                    }
                    finish();
                    break;
            }
        }
    }
}