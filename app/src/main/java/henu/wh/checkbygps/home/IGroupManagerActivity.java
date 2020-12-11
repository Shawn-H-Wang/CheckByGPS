package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import henu.wh.checkbygps.LoginActivity;
import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;

public class IGroupManagerActivity extends AppCompatActivity {

    private Button quitGroup, signInfo, back;
    private TextView tvGroup;
    private static String gname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_group_manager);
        initView();
        tvGroup.setText(IGroupManagerActivity.gname);
        quitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IGroupManagerActivity.this);
                builder.setMessage("您是否要退出群：" + gname.substring(gname.indexOf(":") + 1) + "?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String s = gname.substring(0, gname.indexOf(":"));
                                JSONObject quitGroup = new JSONObject();
                                quitGroup.put("operation", "quitG");
                                quitGroup.put("gid", s);
                                quitGroup.put("uid", LoginActivity.user.getPhone());
                                Client.getClient().send(quitGroup);
                            }
                        }).start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(IGroupManagerActivity.this, "您已退出群聊，请刷新查看", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("否", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        signInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = gname.substring(0, gname.indexOf(":"));
                SignInfoActivity.setGid(gid);
                SignInfoActivity.setIsManager(false);
                Intent intent = new Intent(IGroupManagerActivity.this, SignInfoActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        quitGroup = (Button) findViewById(R.id.quitG);
        signInfo = (Button) findViewById(R.id.signInfoIG);
        back = (Button) findViewById(R.id.backIG);
        tvGroup = (TextView) findViewById(R.id.igname);
    }

    public static void setGname(String gname) {
        IGroupManagerActivity.gname = gname;
    }

}
