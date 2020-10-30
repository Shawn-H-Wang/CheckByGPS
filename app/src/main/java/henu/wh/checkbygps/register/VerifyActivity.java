package henu.wh.checkbygps.register;

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
import android.widget.Toast;

import com.mob.MobSDK;

import java.sql.Connection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Helper;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;

public class VerifyActivity extends AppCompatActivity implements Init {

    private static String username, userpasswd;
    private static boolean sex, identify;
    private static boolean FLAG = false;
    private String code;
    private boolean flag;

    private EditText userphone, verifycode;
    private Button mBtnverifyCode, mBtnregister, mBtnback;

    private final String APP_KEY = "3107a8f5189f8";
    private final String APP_SECRET = "70088e2559f5bde6bd112fd9414b7b17";

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
                    register(); // 连接数据库进行注册
                    if (VerifyActivity.FLAG) {
                        Intent intent = new Intent();
                        intent.setClass(VerifyActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    public static void getData(String username, String userpasswd, boolean sex, boolean identify) {
        VerifyActivity.username = username;
        VerifyActivity.userpasswd = userpasswd;
        VerifyActivity.sex = sex;
        VerifyActivity.identify = identify;
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
        //由于Android Studio访问mysql数据库不能在主进程Activity中直接访问，所以访问mysql的方法要写在新的线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (insertInfo(userphone.getText().toString(), VerifyActivity.username, VerifyActivity.userpasswd, VerifyActivity.sex, VerifyActivity.identify)) {
                    VerifyActivity.FLAG = true; // 表示注册成功
                    // 这里开启了一个新进程，Toast调用法时需要用到Looper的prepare进行准备
                    Looper.prepare();
                    showMessage("注册成功");
                    Looper.loop();
                }
            }
        }).start();
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
    public boolean insertInfo(String userphone, String username, String password, boolean sex,
                              boolean identify) {
        boolean flag = false;
        // 与数据库建立连接
        Connection conn = JdbcUtil.conn();
        // 查询该账户是否被注册
        if (JdbcUtil.selectPhone(conn, userphone)) { // 返回为true说明未被注册，则可以注册，将注册信息传入方法中
            JdbcUtil.insert(conn, userphone, username, password, sex, identify);
            flag = true;
            VerifyActivity.FLAG = true;

        } else {
            // 这里开启了一个新进程，Toast调用法法师需要用到Looper的prepare进行准备
            Looper.prepare();
            showMessage("注册失败！该号码已被使用，请更换号码！");
            Looper.loop();
        }
        // 关闭数据库
        JdbcUtil.close(conn);
        return flag;
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
