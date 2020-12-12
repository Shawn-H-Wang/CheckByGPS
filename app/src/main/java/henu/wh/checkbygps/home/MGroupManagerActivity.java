package henu.wh.checkbygps.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.client.Client;
import henu.wh.checkbygps.help.Helper;

public class MGroupManagerActivity extends AppCompatActivity {

    private Button deleteGroup, memGroup, signGroup, signInfo, back;
    private TextView tvGroup;
    private static String gname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_group_manager);
        initView();
        tvGroup.setText(MGroupManagerActivity.gname);
        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MGroupManagerActivity.this);
                builder.setMessage("您是否要删除群：" + gname.substring(gname.indexOf(":") + 1) + "?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String s = gname.substring(0, gname.indexOf(":"));
                                JSONObject deGroup = new JSONObject();
                                deGroup.put("operation", "deleteG");
                                deGroup.put("gid", s);
                                Client.getClient().send(deGroup);
                            }
                        }).start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MGroupManagerActivity.this, "群聊已经删除，请刷新查看", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("否", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        memGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = gname.substring(0, gname.indexOf(":"));
                MemberInfoActivity.setGid(gid);
                Intent intent = new Intent(MGroupManagerActivity.this, MemberInfoActivity.class);
                startActivity(intent);
            }
        });
        signGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MGroupManagerActivity.this);
                builder.setTitle(gname.substring(gname.indexOf(":") + 1));
                builder.setMessage("你是否要发起群签到?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    boolean b = false;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                String gid = gname.substring(0, gname.indexOf(":"));
                                // 这里设置的签到点是学校
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("operation", "sendsignG");
                                jsonObject.put("gid", gid);
                                jsonObject.put("Locatiojn1", "-122.084094");
                                jsonObject.put("Locatiojn2", "37.421946");
                                jsonObject.put("SignID", gid + Helper.randomGID());
                                Client.getClient().send(jsonObject);
                            }
                        }).start();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                b = Client.getClient().readBoolean();
                            }
                        }).start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (b) {
                            Toast.makeText(MGroupManagerActivity.this, "群聊签到已经发送", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MGroupManagerActivity.this, "群聊签到发送失败", Toast.LENGTH_SHORT).show();
                        }
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
                SignInfoActivity.setIsManager(true);
                Intent intent = new Intent(MGroupManagerActivity.this, SignInfoActivity.class);
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
        deleteGroup = (Button) findViewById(R.id.deleteG);
        memGroup = (Button) findViewById(R.id.memMG);
        signGroup = (Button) findViewById(R.id.signMG);
        signInfo = (Button) findViewById(R.id.signInfoMG);
        back = (Button) findViewById(R.id.backMG);
        tvGroup = (TextView) findViewById(R.id.mgname);
    }

    public static void setGname(String gname) {
        MGroupManagerActivity.gname = gname;
    }
}