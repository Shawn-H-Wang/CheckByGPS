package henu.wh.checkbygps.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import henu.wh.checkbygps.MainActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.teacher.TeacherSigninActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button mBtnnext, mBtnback;
    private RadioButton rBstudent, rBteacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initButton();
        initRadioButtion();

        setListeners();
    }

    public void setListeners() {
        OnClick onClick = new OnClick();
        mBtnback.setOnClickListener(onClick);
        mBtnnext.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_register_back:
                    // 回退到主界面
                    intent = new Intent(RegisterActivity.this, MainActivity.class);
                    break;
                case R.id.btn_register_next:
                    // 进入相应页面
                    if (rBstudent.isChecked()) {
                        intent = new Intent(RegisterActivity.this, StudentSignUpActivity.class);
                    } else if (rBteacher.isChecked()) {
                        intent = new Intent(RegisterActivity.this, TeacherSignUpActivity.class);
                    } else {
                        Toast.makeText(RegisterActivity.this, "请选择身份信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }
            startActivity(intent);
        }
    }

    public void initButton() {
        mBtnback = (Button) findViewById(R.id.btn_register_back);
        mBtnnext = (Button) findViewById(R.id.btn_register_next);
    }

    public void initRadioButtion() {
        rBstudent = (RadioButton) findViewById(R.id.rbt_student);
        rBteacher = (RadioButton) findViewById(R.id.rbt_teacher);
    }
}