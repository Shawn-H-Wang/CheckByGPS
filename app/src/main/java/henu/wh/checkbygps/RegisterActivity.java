package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Button mBtnback, mBtnregister, mBtngettestcode;
    private EditText eTuser, eTphone, eTtestcode, eTpasswd, eTpdagain;
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
        mBtnregister.setOnClickListener(onClick);
        mBtngettestcode.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    RegisterActivity.this.finish();
                    break;
                case R.id.btn_getmodcode:
                    if (eTphone.getText().toString().isEmpty()) {
                        showMessage("请输入手机号");
                        break;
                    }
                    break;
                case R.id.btn_register:
                    register();
                    break;
            }
        }
    }

    private void register() {
        String username = eTuser.getText().toString();
        String userphone = eTphone.getText().toString();
        String usertestcode = eTtestcode.getText().toString();
        String userpasswd = eTpasswd.getText().toString();
        String userpdagain = eTpdagain.getText().toString();
        if (username.isEmpty()) {
            showMessage("请输入用户名");
        } else if (!rBtnstudent.isChecked() && !rBtnteacher.isChecked()) {
            showMessage("请选择身份信息");
        } else if (!rBtnfemale.isChecked() && !rBtnmale.isChecked()) {
            showMessage("请选择性别");
        } else if (userphone.isEmpty()) {
            showMessage("请输入验证码");
        } else if (usertestcode.isEmpty()) {
            showMessage("请输入验证码");
        } else if (userpasswd.isEmpty()) {
            showMessage("请输入密码");
        } else if (userpdagain.isEmpty()) {
            showMessage("请再次输入密码确认");
        } else if (!userpdagain.equals(userpasswd)) {
            showMessage("两次输入密码不一致，请重新输入");
        } else {
            showMessage("注册成功");
        }
    }

    public void showMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void initButton() {
        mBtnback = (Button) findViewById(R.id.btn_back);
        mBtnregister = (Button) findViewById(R.id.btn_register);
        mBtngettestcode = (Button) findViewById(R.id.btn_getmodcode);
    }

    public void initEditText() {
        eTuser = (EditText) findViewById(R.id.edit_signup);
        eTphone = (EditText) findViewById(R.id.edit_signup_phone);
        eTtestcode = (EditText) findViewById(R.id.edit_signup_tc);
        eTpasswd = (EditText) findViewById(R.id.edit_signup_pd);
        eTpdagain = (EditText) findViewById(R.id.edit_signup_again);
    }

    public void initRadioButton() {
        rBtnstudent = (RadioButton) findViewById(R.id.rbt_student);
        rBtnteacher = (RadioButton) findViewById(R.id.rbt_teacher);
        rBtnmale = (RadioButton) findViewById(R.id.rbt_male);
        rBtnfemale = (RadioButton) findViewById(R.id.rbt_female);
    }

}