package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import henu.wh.checkbygps.dbHelper.JdbcUtil;

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
        boolean sex = rBtnmale.isChecked();
        boolean identify = rBtnstudent.isChecked();
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
            if (insertInfo(userphone, username, userpasswd, sex, identify)) {
                showMessage("注册成功");
            }
        }
    }

    /**
     * <p>注册输入信息</p>
     *
     * @param userphone 用户手机号，表主键
     * @param username  用户名
     * @param password  用户密码
     * @param sex       true为男性，false为女性
     * @param identify  true为学生，false为教师
     * @return true表示注册成功，false表示注册失败，失败并弹出错误信息
     */
    public boolean insertInfo(String userphone, String username, String password, boolean sex, boolean identify) {
        JdbcUtil jdbcUtil = JdbcUtil.getInstance();
        Connection conn = jdbcUtil.getConnection("AUDB", "root", "12345678");
        if (conn == null) {
            showMessage("数据库连接失败，请稍后尝试！！");
            return false;
        } else {
//            String select = "SELECT ";
            String sql = "INSERT INTO USER(phone, user, passwd, sex, identify) VALUES(?,?,?,?,?);";
            try {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1, userphone);
                pre.setString(2, username);
                pre.setString(3, password);
                pre.setBoolean(4, sex);
                pre.setBoolean(5, identify);
                return pre.execute();
            } catch (SQLException e) {
                showMessage("注册失败");
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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