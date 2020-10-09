package henu.wh.checkbygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import henu.wh.checkbygps.student.StdentSigninActivity;
import henu.wh.checkbygps.teacher.TeacherSigninActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnstudent, mBtnteacher, mBtnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnstudent = (Button) findViewById(R.id.btn_student);
        mBtnteacher = (Button) findViewById(R.id.btn_teacher);
        mBtnsignup = (Button) findViewById(R.id.btn_signup);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnstudent.setOnClickListener(onClick);
        mBtnteacher.setOnClickListener(onClick);
        mBtnsignup.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_student:
                    // 跳转至学生登陆界面
                    intent = new Intent(MainActivity.this, StdentSigninActivity.class);
                    break;
                case R.id.btn_teacher:
                    // 跳转至教师登陆界面
                    intent = new Intent(MainActivity.this, TeacherSigninActivity.class);
                    break;
                case R.id.btn_signup:
                    // 跳转至注册界面
                    intent = new Intent(MainActivity.this, SignUpActivity.class);
            }
            startActivity(intent);
        }
    }
}