package henu.wh.checkbygps.forgetpasswd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.register.RegisterActivity;

public class ChoseActivity extends AppCompatActivity implements Init {

    private Button mBtnchangepassword, mBtnchangeinformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_forget_password));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        mBtnchangepassword = (Button) findViewById(R.id.btn_choose_passwd);
        mBtnchangeinformation = (Button) findViewById(R.id.btn_choose_info);
    }

    public void initListeners() {
        OnClick onClick = new OnClick();
        mBtnchangepassword.setOnClickListener(onClick);
        mBtnchangeinformation.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_choose_passwd:
                    // 跳转至学生登陆界面
                    intent = new Intent(ChoseActivity.this, ChangePawdActivity.class);
                    break;
                case R.id.btn_choose_info:
                    // 跳转至注册界面
                    intent = new Intent(ChoseActivity.this, ChangeInfoActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}