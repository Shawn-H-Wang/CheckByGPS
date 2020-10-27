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
import android.widget.Toast;

import java.sql.Connection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import henu.wh.checkbygps.dbHelper.Helper;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.help.Init;

public class VerifyActivity extends AppCompatActivity implements Init {

    private static String username, userpasswd;
    private static boolean sex, identify;
    private String code;
    private boolean flag;

    private EditText userphone, verifycode;
    private Button mBtnverifyCode, mBtnregister;

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
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                if (result == SMSSDK.RESULT_COMPLETE) {
                    showMessage("验证码发送成功");
                    timeCount = new TimeCount(60000, 1000);
                    timeCount.start();
                    //回调完成
                    boolean smart = (Boolean) data;
                    if (smart) {
                        Toast.makeText(getApplicationContext(), "该手机号已经注册过，请重新输入", Toast.LENGTH_LONG).show();
                        userphone.requestFocus();//焦点
                        return;
                    }
                }
            }
            //回调完成
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Log.d("SMSSDK", "验证码输入正确");
                    register();
                    startActivity(new Intent(VerifyActivity.this, MainActivity.class));
                }
            } else {//其他出错情况
                if (flag) {
                    verifycode.setVisibility(View.VISIBLE);
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

        initButton();
        initEditText();

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

        SMSSDK.registerEventHandler(eventHandler);//注册短信回调（记得销毁，避免泄露内存）*/
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnregister.setOnClickListener(onClick);
        mBtnverifyCode.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getverifycode:
                    if (userphone.getText().toString().isEmpty()) {
                        showMessage("请输入手机号");
                    } else {
                        if (!Helper.checkPhone(userphone.getText().toString())) {
                            showMessage("请输入正确的手机号！");
                        } else {
                            SMSSDK.getVerificationCode("86", userphone.getText().toString());   // 获取你的手机号的验证码
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

    public void register() {
        String phone = userphone.getText().toString();
        //由于Android Studio访问mysql数据库不能在主进程Activity中直接访问，所以访问mysql的方法要写在新的线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (insertInfo(phone, VerifyActivity.username, VerifyActivity.userpasswd, VerifyActivity.sex, VerifyActivity.identify)) {
                    // 这里开启了一个新进程，Toast调用法法师需要用到Looper的prepare进行准备
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
        if (JdbcUtil.select(conn, userphone)) { // 返回为true说明未被注册，则可以注册，将注册信息传入方法中
            JdbcUtil.insert(conn, userphone, username, password, sex, identify);
            flag = true;
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
    public void initButton() {
        mBtnverifyCode = findViewById(R.id.btn_getverifycode);
        mBtnregister = findViewById(R.id.btn_register);
    }

    @Override
    public void initEditText() {
        userphone = findViewById(R.id.edit_signup_phone);
        verifycode = findViewById(R.id.edit_signup_verifycode);
    }

    @Override
    public void initRadioButton() {

    }
}
