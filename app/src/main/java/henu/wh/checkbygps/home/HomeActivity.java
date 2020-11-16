package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Init;
import henu.wh.checkbygps.role.User;

public class HomeActivity extends FragmentActivity implements Init, View.OnClickListener {

    private LinearLayout tx_sy, tx_ms, tx_cl, tx_my;
    private ImageView sy, ms, cl, my;
    public static User user;
    private Fragment homeFragment;//当前fragment
    private Fragment classFragment;//当前fragment
    private Fragment messageFragment;//当前fragment
    private Fragment myFragment;//当前fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        initViews();
        initEvents();
        selectTab(0);
    }

    public static void setUser(User user) {
        HomeActivity.user = user;
    }

    @Override
    public void onClick(View v) {
        // 先将四个Image设置为灰色
        resetImage();
        switch (v.getId()) {
            case R.id.tx_sy:
                selectTab(0);
                break;
            case R.id.tx_cl:
                selectTab(1);
                break;
            case R.id.tx_ms:
                selectTab(2);
                break;
            case R.id.tx_my:
                selectTab(3);
                break;
        }
    }

    private void selectTab(int i) {
        // 获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        // 获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            // 设置选中页面的Image为红色
            case 0:
                sy.setBackgroundColor(Color.parseColor("#FF0000"));
                // 如果对应的Fragment没有实例化，则进行实例化，并显示出来
                if (homeFragment == null) {
                    homeFragment = new SyFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                }
                else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                cl.setBackgroundColor(Color.parseColor("#FF0000"));
                if (classFragment == null) {
                    classFragment = new ClFragment();

                    transaction.add(R.id.fl_content, classFragment);
                }
                else {
                    transaction.show(classFragment);
                }
                break;
            case 2:
                ms.setBackgroundColor(Color.parseColor("#FF0000"));
                if (messageFragment == null) {
                    messageFragment = new MsFragment();
                    transaction.add(R.id.fl_content, messageFragment);
                }
                else {
                    transaction.show(messageFragment);
                }
                break;
            case 3:
                my.setBackgroundColor(Color.parseColor("#FF0000"));
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fl_content, myFragment);
                }
                else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public void initViews() {
        tx_sy = (LinearLayout) findViewById(R.id.tx_sy);
        tx_ms = (LinearLayout) findViewById(R.id.tx_ms);
        tx_cl = (LinearLayout) findViewById(R.id.tx_cl);
        tx_my = (LinearLayout) findViewById(R.id.tx_my);

        sy = (ImageView) findViewById(R.id.sy);
        ms = (ImageView) findViewById(R.id.ms);
        cl = (ImageView) findViewById(R.id.cl);
        my = (ImageView) findViewById(R.id.my);
    }

    public void initEvents() {
        tx_sy.setOnClickListener(this);
        tx_ms.setOnClickListener(this);
        tx_cl.setOnClickListener(this);
        tx_my.setOnClickListener(this);
    }

    public void resetImage() {
        sy.setBackgroundColor(Color.parseColor("#D3D3D3"));
        ms.setBackgroundColor(Color.parseColor("#D3D3D3"));
        cl.setBackgroundColor(Color.parseColor("#D3D3D3"));
        my.setBackgroundColor(Color.parseColor("#D3D3D3"));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (classFragment != null) {
            transaction.hide(classFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

}