package henu.wh.checkbygps.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.help.SysApplication;

public class RegisterActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtnnext;
    private EditText eTuser, eTpasswd, eTpdagain;
    private RadioButton rBtnmale, rBtnfemale, rBtnstudent, rBtnteacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initButton();
        initEditText();
        initRadioButton();

        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtnnext.setOnClickListener(onClick);
    }


    /**
     * 事件监听器
     */
    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_back:
                    RegisterActivity.this.finish();
                    break;
                case R.id.btn_next:
                    if (next()) {
                        SysApplication.getInstance().addActivity(RegisterActivity.this);
                        intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    private boolean next() {
        String username = eTuser.getText().toString();
        String userpasswd = eTpasswd.getText().toString();
        String userpdagain = eTpdagain.getText().toString();
        boolean sex = rBtnmale.isChecked();
        boolean identify = rBtnstudent.isChecked();
        boolean tag = false;
        if (username.isEmpty()) {
            showMessage("请输入用户名");
            eTuser.requestFocus();
        } else if (!rBtnstudent.isChecked() && !rBtnteacher.isChecked()) {
            showMessage("请选择身份信息");
        } else if (!rBtnfemale.isChecked() && !rBtnmale.isChecked()) {
            showMessage("请选择性别");
        } else if (userpasswd.isEmpty()) {
            showMessage("请输入密码");
            eTpasswd.requestFocus();
        } else if (userpdagain.isEmpty()) {
            showMessage("请再次输入密码确认");
            eTpdagain.requestFocus();
        } else if (!userpdagain.equals(userpasswd)) {
            showMessage("两次输入密码不一致，请重新输入");
            eTpdagain.requestFocus();
        } else {
            VerifyActivity.getData(username, userpasswd, sex, identify);
            tag = true;
        }
        return tag;
    }

    public void showMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initButton() {
        mBtnback = (Button) findViewById(R.id.btn_back);
        mBtnnext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void initEditText() {
        eTuser = (EditText) findViewById(R.id.edit_signup);
        eTpasswd = (EditText) findViewById(R.id.edit_signup_pd);
        eTpdagain = (EditText) findViewById(R.id.edit_signup_again);
    }

    @Override
    public void initRadioButton() {
        rBtnstudent = (RadioButton) findViewById(R.id.rbt_student);
        rBtnteacher = (RadioButton) findViewById(R.id.rbt_teacher);
        rBtnmale = (RadioButton) findViewById(R.id.rbt_male);
        rBtnfemale = (RadioButton) findViewById(R.id.rbt_female);
    }

}