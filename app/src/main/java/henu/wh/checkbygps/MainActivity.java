package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.register.RegisterActivity;

public class MainActivity extends AppCompatActivity implements Init {

    private Button mBtnlogin, mBtnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client.getClient();
            }
        }).start();

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnlogin.setOnClickListener(onClick);
        mBtnsignup.setOnClickListener(onClick);
    }

    @Override
    public void initViews() {
        mBtnlogin = (Button) findViewById(R.id.btn_log_in);
        mBtnsignup = (Button) findViewById(R.id.btn_signup);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_log_in:
                    // 跳转至学生登陆界面
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    break;
                case R.id.btn_signup:
                    // 跳转至注册界面
                    intent = new Intent(MainActivity.this, RegisterActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}