package henu.wh.checkbygps.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import henu.wh.checkbygps.R;

public class StdentSigninActivity extends AppCompatActivity {

    private Button mBtnsignin;
    private EditText eTuser, eTpasswd;
    String username = null;
    String password = null;

    private final String AppKey = "3107a8f5189f8";
    private final String AppSecret = "70088e2559f5bde6bd112fd9414b7b17";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdent_signin);
        mBtnsignin = (Button) findViewById(R.id.btn_signin);
        eTuser = (EditText) findViewById(R.id.edittext_number);
        eTpasswd = (EditText) findViewById(R.id.edittext_passwd);

        final CheckBox mCheckBox = (CheckBox) findViewById(R.id.cb_issave);
        mBtnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) { // 如果选中，则保存信息至本地，下一次自动记录
                    username = eTuser.getText().toString();
                    password = eTpasswd.getText().toString();

                }
            }
        });

    }

}