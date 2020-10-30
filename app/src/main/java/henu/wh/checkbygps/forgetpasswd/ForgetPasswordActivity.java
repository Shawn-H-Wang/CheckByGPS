package henu.wh.checkbygps.forgetpasswd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Helper;
import henu.wh.checkbygps.help.Init;

public class ForgetPasswordActivity extends AppCompatActivity implements Init {

    private Button mBtnback, mBtngetvc, mBtnnext;
    private EditText userphone, verifycode;

    private final String APP_KEY = "3107a8f5189f8";
    private final String APP_SECRET = "70088e2559f5bde6bd112fd9414b7b17";

    private boolean flag;
    private String code;

    EventHandler eventHandler;
    TimeCount timeCount;

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    // 这里规定60s后才可以重新发送，即总时间为60s，每次变化的速率为1s
                    showMessage("验证码发送成功");
                    timeCount = new TimeCount(60000, 1000);
                    timeCount.start();
                }
            }
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Log.d("SMSSDK", "验证码输入正确");
                    startActivity(new Intent(ForgetPasswordActivity.this, ChangePawdActivity.class));
                }
            } else {    //回调失败
                if (flag) {
                    Toast.makeText(getApplicationContext(), "验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        MobSDK.init(ForgetPasswordActivity.this, APP_KEY, APP_SECRET);

        initViews();
        setListeners();

        eventHandler = new EventHandler() {
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
    }

    public void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtngetvc.setOnClickListener(onClick);
        mBtnnext.setOnClickListener(onClick);
    }

    public void showMessage(String message) {
        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null)
            timeCount.cancel();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String phone = userphone.getText().toString();
            switch (v.getId()) {
                case R.id.btn_back_pawd:
                    ForgetPasswordActivity.this.finish();
                    break;
                case R.id.btn_get_vc_pawd:
                    if (phone.isEmpty()) {
                        showMessage("请输入手机号");
                    } else {
                        if (!Helper.checkPhone(phone)) {
                            showMessage("请输入正确的手机号！");
                        } else {
                            ChangePawdActivity.getData(phone);
                            SMSSDK.getVerificationCode("86", phone);   // 获取你的手机号的验证码
                            verifycode.requestFocus();  //判断是否获得焦点
                        }
                    }
                    break;
                case R.id.btn_next_pawd:
                    if (judgeIsEmpty())
                        SMSSDK.submitVerificationCode("86", phone, code);
                    flag = false;
                    break;
            }
        }
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 处理数值变化
            int time = (int) Math.round((double) millisUntilFinished / 1000) - 1;
            mBtngetvc.setText(time + "s后重新发送");
            mBtngetvc.setClickable(false);
        }

        @Override
        public void onFinish() {
            mBtngetvc.setText("重新发送");
            mBtngetvc.setClickable(true);
        }
    }

    @Override
    public void initViews() {
        mBtnback = (Button) findViewById(R.id.btn_back_pawd);
        mBtngetvc = (Button) findViewById(R.id.btn_get_vc_pawd);
        mBtnnext = (Button) findViewById(R.id.btn_next_pawd);
        userphone = (EditText) findViewById(R.id.edit_forgetpawd_phone);
        verifycode = (EditText) findViewById(R.id.edit_forgetpawd_verifycode);
    }

    public boolean judgeIsEmpty() {
        boolean tag = false;
        if (userphone.getText().toString().isEmpty()) {
            showMessage("请输入手机号码！");
        } else if (!Helper.checkPhone(userphone.getText().toString())) {
            showMessage("请输入正确的手机号码！");
        } else if (verifycode.getText().toString().isEmpty()) {
            showMessage("请输入验证码！");
        } else {
            if (verifycode.getText().toString().length() != 6) {
                showMessage("请输入正确的验证码！");
            } else {
                code = verifycode.getText().toString();
                tag = true;
            }
        }
        return tag;
    }

}