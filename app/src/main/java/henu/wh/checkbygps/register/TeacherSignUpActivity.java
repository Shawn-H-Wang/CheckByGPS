package henu.wh.checkbygps.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import henu.wh.checkbygps.R;

public class TeacherSignUpActivity extends AppCompatActivity {

    private Button mBtnregister, mBtnbackup, mBtntestcode;
    private EditText eTtid, eTpassword, eTpawdagain, eTphone;
    private RadioButton rBtnmale, rBtnfemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);

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
                case R.id.btn_student_signup_back:
                    // 回到主界面
                    back();
                    break;
                case R.id.btn_student_signup_register:
                    // 注册信息
                    registerInformation();
                    break;
                case R.id.btn_student_getmodcode:
                    // 获取验证码

                    break;
            }
        }
    }

    public void initButton() {
        mBtnregister = (Button) findViewById(R.id.btn_teacher_signup_register);
        mBtnbackup = (Button) findViewById(R.id.btn_teacher_signup_back);
        mBtntestcode = (Button) findViewById(R.id.btn_teacher_getmodcode);
    }

    public void initEditText() {
        eTtid = (EditText) findViewById(R.id.edit_signup_teacherid);
        eTpassword = (EditText) findViewById(R.id.edit_signup_teacherpd);
        eTphone = (EditText) findViewById(R.id.edit_signup_teacherphone);
        eTpawdagain = (EditText) findViewById(R.id.edit_signup_teacherpd_again);
    }

    public void initRadioButton() {
        rBtnmale = (RadioButton) findViewById(R.id.rbt_teacher_male);
        rBtnfemale = (RadioButton) findViewById(R.id.rbt_teacher_female);
    }

    public void back() {
        TeacherSignUpActivity.this.finish();

    }

    private void registerInformation() {
        String TID = eTtid.getText().toString();
        String PASSWORD = eTpassword.getText().toString();
        String PASSWORD_AGAIN = eTpawdagain.getText().toString();
        String PHONE = eTphone.getText().toString();
        if (TextUtils.isEmpty(TID)) {
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
        } else {
            showText("注册成功");
        }
        back();
    }

    private void showText(String message) {
        Toast.makeText(TeacherSignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}