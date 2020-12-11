package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;


public class ChangePersonActivity extends AppCompatActivity {

    private Button submit, cancel;
    private EditText name, newpd, newpda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_person);

        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        name = (EditText) findViewById(R.id.ch_name);
        newpd = (EditText) findViewById(R.id.new_pd);
        newpda = (EditText) findViewById(R.id.new_pd_again);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() ||
                        newpda.getText().toString().isEmpty() ||
                        newpd.getText().toString().isEmpty()) {
                    showMessage("请输入信息");
                } else if (!newpd.getText().toString().equals(newpda.getText().toString())) {
                    showMessage("两次密码输入不一致，请重新输入");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                String newname = name.getText().toString();
                                String newpwd = newpd.getText().toString();
                                jsonObject.put("newname", newname);
                                jsonObject.put("newpwd", newpwd);
                                jsonObject.put("operation", "updateInfo");
                                Client.getClient().send(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    final boolean[] tag = {false};
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tag[0] = Client.getClient().readBoolean();
                        }
                    }).start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tag[0])
                        finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(ChangePersonActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}