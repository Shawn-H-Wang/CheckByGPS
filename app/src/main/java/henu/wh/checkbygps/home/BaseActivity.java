package henu.wh.checkbygps.home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;

import henu.wh.checkbygps.R;

public abstract class BaseActivity extends AppCompatActivity {

    private String menuStr;
    private int menuResId;
    private String menuStr2;
    private int menuResId2;
    private TextView tvTitle;
    private FrameLayout viewContent;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //1、设置支出，并不显示项目的title文字
        button = (Button) findViewById(R.id.backJSJSJSJ);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //2、将子类的布局解析到 FrameLayout 里面
        viewContent = (FrameLayout) findViewById(R.id.viewContent);
        LayoutInflater.from(this).inflate(getConentView(), viewContent);

        //3、初始化操作（此方法必须放在最后执行位置）
        init(savedInstanceState);
    }

    /**
     * 设置布局资源
     *
     * @return
     */
    protected abstract int getConentView();

    /**
     * 初始化操作
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 设置页面标题
     *
     * @param title 标题文字
     */
    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
    }

    /**
     * 设置拦截事件处理业务逻辑
     *
     * @param item 自定义菜单项
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_one:
                this.onClickRightListener.onClick();

                break;
            case R.id.menu_item_two:
                this.onClickRightListener2.onClick();

                break;
        }
        return true;//拦截系统处理事件
    }

    /**
     * 加载Toolbar标题右菜单图标和文字
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {//显示自定义右菜单
            getMenuInflater().inflate(R.menu.toolbar, menu);
        } else if (menuResId2 != 0 || !TextUtils.isEmpty(menuStr2)) {
            getMenuInflater().inflate(R.menu.toolbar, menu);
        } else {
            //如果把下面这行代码表示右侧菜单显示默认值。
            //显示的默认Menu、Item里的值必须在menu文件中配置好文字和icon。
            getMenuInflater().inflate(R.menu.toolbar, menu);
        }
        return true;
    }

    /**
     * 选择性显示图标或文字
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_item_one).setIcon(menuResId);
        } else if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_item_one).setTitle(menuStr);
        } else {
            menu.findItem(R.id.menu_item_one).setVisible(false);
        }

        if (menuResId2 != 0) {
            menu.findItem(R.id.menu_item_two).setIcon(menuResId2);
        } else if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_item_two).setTitle(menuStr2);
        } else {
            menu.findItem(R.id.menu_item_two).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private OnClickRightListener onClickRightListener;
    private OnClickRightListener onClickRightListener2;

    public interface OnClickRightListener {
        void onClick();
    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    private SDKReceiver mReceiver;

    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            String tx = "";

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {

                tx = "key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        + " ; 请在 AndroidManifest.xml 文件中检查 key 设置";
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                tx = "key 验证成功! 功能可以正常使用";
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                tx = "网络出错";
            }
            if (tx.contains("错")) {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(context);
                normalDialog.setTitle("提示");
                normalDialog.setMessage(tx);
                normalDialog.setPositiveButton("确定", null);
                normalDialog.setNegativeButton("关闭", null);
                // 显示
                normalDialog.show();
            } else {
                Toast.makeText(context, tx, Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected void RegisterBroadcast() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }
}
