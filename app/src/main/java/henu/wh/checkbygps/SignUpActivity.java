package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button mBtnregister, mBtnbackup, mBtntestcode;
    private EditText eTsid, eTpassword, eTpawdagain, eTphone;
    private RadioButton rBtnmale, rBtnfemale, rBtnstudent, rBtnteacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initButton();   // 初始化Button按钮
        initEditText(); // 初始化EditText编辑
        initRadioButton(); // 初始化RadioButton单选框

        setListeners();
    }

    public void setListeners() {
        OnClick onClick = new OnClick();
        mBtnregister.setOnClickListener(onClick);
        mBtnbackup.setOnClickListener(onClick);
        mBtntestcode.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_signup_back:
                    // 回到主界面
                    back();
                    break;
                case R.id.btn_signup_register:
                    // 注册信息
                    registerInformation();
                    break;
                case R.id.btn_getmodcode:
                    // 获取验证码

                    break;
            }
        }
    }

    public void initButton() {
        mBtnregister = (Button) findViewById(R.id.btn_signup_register);
        mBtnbackup = (Button) findViewById(R.id.btn_signup_back);
        mBtntestcode = (Button) findViewById(R.id.btn_getmodcode);
    }

    public void initEditText() {
        eTsid = (EditText) findViewById(R.id.edit_signup_name);
        eTpassword = (EditText) findViewById(R.id.edit_signup_passwd);
        eTphone = (EditText) findViewById(R.id.edit_signup_email);
        eTpawdagain = (EditText) findViewById(R.id.edit_signup_passwd_again);
    }

    public void initRadioButton() {
        rBtnmale = (RadioButton) findViewById(R.id.rbt_male);
        rBtnfemale = (RadioButton) findViewById(R.id.rbt_female);
        rBtnstudent = (RadioButton) findViewById(R.id.rbt_student);
        rBtnteacher = (RadioButton) findViewById(R.id.rbt_teacher);
    }

    public void back() {
        SignUpActivity.this.finish();
    }

    private void registerInformation() {
        String SID = eTsid.getText().toString();
        String PASSWORD = eTpassword.getText().toString();
        String PASSWORD_AGAIN = eTpawdagain.getText().toString();
        String PHONE = eTphone.getText().toString();
        if (TextUtils.isEmpty(SID)) {
            showText("请输入用户ID");
            return;
        } else if (!rBtnmale.isChecked() && !rBtnfemale.isChecked()) {
            showText("请选择性别");
            return;
        } else if (TextUtils.isEmpty(PHONE)) {
            showText("请输入用户手机号");
            return;
        } else if (TextUtils.isEmpty(PASSWORD)) {
            showText("请输入用户密码");
            return;
        } else if (!PASSWORD.equals(PASSWORD_AGAIN)) {
            showText("两次输入密码不一致，请重新输入");
            return;
        } else if (!rBtnstudent.isChecked() && !rBtnteacher.isChecked()) {
            showText("请选择身份");
            return;
        } else {
            showText("注册成功");
        }
        back();
    }

    private void showText(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}