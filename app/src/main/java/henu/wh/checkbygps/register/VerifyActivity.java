package henu.wh.checkbygps.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mob.MobSDK;

import java.sql.Connection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.help.Helper;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.role.User;

public class VerifyActivity extends AppCompatActivity implements Init {

    private static volatile boolean FLAG = false;
    private String code;
    private boolean flag;
    private static User user;

    private EditText userphone, verifycode;
    private Button mBtnverifyCode, mBtnregister, mBtnback;

    private final String APP_KEY = "3107a8f5189f8";
    private final String APP_SECRET = "70088e2559f5bde6bd112fd9414b7b17";

    EventHandler eventHandler;
    TimeCount timeCount;

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    showMessage("验证码发送成功");
                    // 这里规定60s后才可以重新发送，即总时间为60s，每次变化的速率为1s
                    timeCount = new TimeCount(60000, 1000);
                    timeCount.start();
                }
            }
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Log.d("SMSSDK", "验证码输入正确");
                    register(); // 连接服务器进行注册
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (VerifyActivity.FLAG) {
                        showMessage("注册成功！");
                        RegisterActivity.iSfinish = true;
                        finish();
                    } else {
                        showMessage("注册失败，请稍后重试");
                    }
                }
            } else {    //回调失败
                if (flag) {
                    Toast.makeText(getApplicationContext(), "验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    userphone.requestFocus();
                } else {
                    Toast.makeText(getApplicationContext(), "验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_register));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        MobSDK.init(VerifyActivity.this, APP_KEY, APP_SECRET);

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

        SMSSDK.registerEventHandler(eventHandler);  //注册短信回调（记得销毁，避免泄露内存）
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnregister.setOnClickListener(onClick);
        mBtnverifyCode.setOnClickListener(onClick);
        mBtnback.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_verback:
                    VerifyActivity.this.finish();
                    break;
                case R.id.btn_getverifycode:
                    if (userphone.getText().toString().trim().isEmpty()) {
                        showMessage("请输入手机号");
                    } else {
                        if (!Helper.checkPhone(userphone.getText().toString())) {
                            showMessage("请输入正确的手机号！");
                        } else {
                            SMSSDK.getVerificationCode("86", userphone.getText().toString());   // 获取验证码
                            verifycode.requestFocus();  //判断是否获得焦点
                        }
                    }
                    break;
                case R.id.btn_register:
                    // 获取验证码后要提交以判断是否正确，并登陆成功
                    if (judgeIsEmpty())
                        SMSSDK.submitVerificationCode("86", userphone.getText().toString(), code);
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
            mBtnverifyCode.setText(time + "s后重新发送");
            mBtnverifyCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            mBtnverifyCode.setText("重新发送");
            mBtnverifyCode.setClickable(true);
        }
    }

    public static void getUser(User user) {
        VerifyActivity.user = user;
    }

    public void showMessage(String message) {
        Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null)
            timeCount.cancel();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    public boolean judgeIsEmpty() {
        boolean tag = false;
        if (userphone.getText().toString().trim().isEmpty()) {
            showMessage("请输入手机号码！");
        } else if (!Helper.checkPhone(userphone.getText().toString())) {
            showMessage("请输入正确的手机号码！");
        } else if (verifycode.getText().toString().trim().isEmpty()) {
            showMessage("请输入验证码！");
        } else {
            if (verifycode.getText().toString().trim().length() != 6) {
                showMessage("请输入正确的验证码！");
            } else {
                code = verifycode.getText().toString();
                tag = true;
            }
        }
        return tag;
    }

    public void register() {
        JSONObject jsonObject = new JSONObject();
        try {
            VerifyActivity.user.setPhone(userphone.getText().toString());
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("passwd", user.getPassword());
            jsonObject.put("name", user.getName());
            jsonObject.put("sex", user.isSex());
            jsonObject.put("iden", user.isIdentify());
            jsonObject.put("operation", user.getOpeartion());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Client.getClient().send(jsonObject);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    VerifyActivity.FLAG = Client.getClient().readBoolean();
                }
            }).start();
            Thread.sleep(500);
        } catch (JSONException e) {
            e.printStackTrace();
            user.setOpeartion("");  // 清空操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user.setOpeartion("");  // 清空操作
    }

    @Override
    public void initViews() {
        mBtnverifyCode = (Button) findViewById(R.id.btn_getverifycode);
        mBtnregister = (Button) findViewById(R.id.btn_register);
        mBtnback = (Button) findViewById(R.id.btn_verback);
        userphone = (EditText) findViewById(R.id.edit_signup_phone);
        verifycode = (EditText) findViewById(R.id.edit_signup_verifycode);
    }

}
