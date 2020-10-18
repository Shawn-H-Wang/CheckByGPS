package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import henu.wh.checkbygps.dbHelper.JdbcUtil;

public class MainActivity extends AppCompatActivity {

    private Button mBtnlogin, mBtnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnlogin = (Button) findViewById(R.id.btn_log_in);
        mBtnsignup = (Button) findViewById(R.id.btn_signup);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnlogin.setOnClickListener(onClick);
        mBtnsignup.setOnClickListener(onClick);
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