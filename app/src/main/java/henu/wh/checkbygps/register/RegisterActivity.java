package henu.wh.checkbygps.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.role.User;

public class RegisterActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtnnext;
    private EditText eTuser, eTpasswd, eTpdagain;
    private RadioButton rBtnmale, rBtnfemale, rBtnstudent, rBtnteacher;
    public volatile static boolean iSfinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_register));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (iSfinish) {
            finish();
        }
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
        if (username.trim().isEmpty()) {
            showMessage("请输入用户名");
            eTuser.requestFocus();
        } else if (!rBtnstudent.isChecked() && !rBtnteacher.isChecked()) {
            showMessage("请选择身份信息");
        } else if (!rBtnfemale.isChecked() && !rBtnmale.isChecked()) {
            showMessage("请选择性别");
        } else if (userpasswd.trim().isEmpty()) {
            showMessage("请输入密码");
            eTpasswd.requestFocus();
        } else if (userpdagain.trim().isEmpty()) {
            showMessage("请再次输入密码确认");
            eTpdagain.requestFocus();
        } else if (!userpdagain.equals(userpasswd)) {
            showMessage("两次输入密码不一致，请重新输入");
            eTpdagain.requestFocus();
        } else {
            User user = new User();
            user.setName(username);
            user.setPassword(userpasswd);
            user.setSex(sex);
            user.setIdentify(identify);
            user.setOpeartion("register");
            VerifyActivity.getUser(user);
            tag = true;
        }
        return tag;
    }

    public void showMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initViews() {
        mBtnback = (Button) findViewById(R.id.btn_back);
        mBtnnext = (Button) findViewById(R.id.btn_next);
        eTuser = (EditText) findViewById(R.id.edit_signup);
        eTpasswd = (EditText) findViewById(R.id.edit_signup_pd);
        eTpdagain = (EditText) findViewById(R.id.edit_signup_again);
        rBtnstudent = (RadioButton) findViewById(R.id.rbt_student);
        rBtnteacher = (RadioButton) findViewById(R.id.rbt_teacher);
        rBtnmale = (RadioButton) findViewById(R.id.rbt_male);
        rBtnfemale = (RadioButton) findViewById(R.id.rbt_female);
    }

}