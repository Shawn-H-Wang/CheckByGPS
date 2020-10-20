package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnlogin, mBtnforgetpassword;
    private EditText eTuser, eTpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initButton();
        initEditText();

        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnlogin.setOnClickListener(onClick);
        mBtnforgetpassword.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_signin:
                    String username = eTuser.getText().toString();
                    String password = eTpassword.getText().toString();
                    if (username.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else if (isRight(username, password)) {
                        Toast.makeText(LoginActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登陆成功！！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_forgetpasswd:
                    break;
            }
        }
    }

    public void initButton() {
        mBtnlogin = (Button) findViewById(R.id.btn_signin);
        mBtnforgetpassword = (Button) findViewById(R.id.btn_forgetpasswd);
    }

    public void initEditText() {
        eTuser = (EditText) findViewById(R.id.edittext_number);
        eTpassword = (EditText) findViewById(R.id.edittext_passwd);
    }

    /**
     * <p>判断用户名与密码是否匹配，如果不匹配，返回false，否则返回true</p>
     * <p>需要调用数据库进行查找</p>
     *
     * @return
     */
    private boolean isRight(String username, String password) {
        return false;
    }
}