package henu.wh.checkbygps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mob.MobSDK;

import java.sql.Connection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.dbHelper.Helper;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;

public class RegisterActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtnnext;
    private EditText eTuser, eTpasswd, eTpdagain;
    private RadioButton rBtnmale, rBtnfemale, rBtnstudent, rBtnteacher;



    private EventHandler eventHandler;
    private Handler handler;
    private boolean flag;   // 检查验证码是否发送成功

    private final String APP_KEY = "3107a8f5189f8";
    private final String APP_SECRET = "70088e2559f5bde6bd112fd9414b7b17";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MobSDK.init(RegisterActivity.this, APP_KEY, APP_SECRET);

        initButton();
        initEditText();
        initRadioButton();

        init_handler();

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

    // 初始化SMSSDK
    public void init_SMSSDK(String phone) {
        EventHandler eventHandler = new EventHandler() { // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getVerificationCode("86", phone);    // 86代表中国区号
    }

    // 初始化Handler
    public void init_handler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 若操作成功
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 获取校验码
                        showMessage("验证码已发送");
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        showMessage("验证成功");
                        Log.e("", "");
                    }
                } else {
                    // 若操作失败
                    if (flag) {
                        showMessage("验证码获取失败，请重新获取");
                    } else {
                        ((Throwable) data).printStackTrace();
                        showMessage("验证码错误");
                    }
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();  // 注销回调接口
    }

}