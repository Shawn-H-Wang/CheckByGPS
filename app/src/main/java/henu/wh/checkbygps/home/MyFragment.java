package henu.wh.checkbygps.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.dbHelper.JdbcUtil;
import henu.wh.checkbygps.role.User;

public class MyFragment extends Fragment implements View.OnClickListener {

    private Button mBtnexit, mBtnchange;
    private volatile Button mBtnbangding;
    private TextView eTname, eTid, eTiden, eTclass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_my, container, false);
        initViews(v);
        getMessage(HomeActivity.user, eTname, eTid, eTiden, eTclass);
        mBtnexit.setOnClickListener(this::onClick);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_my:
                if (getActivity() instanceof HomeActivity) {
                    Toast.makeText(getActivity(), "退出成功！", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;
            case R.id.change_my:
                break;
        }
    }

    public void initViews(View v) {
        mBtnexit = (Button) v.findViewById(R.id.exit_my);
        mBtnchange = (Button) v.findViewById(R.id.change_my);
        mBtnbangding = (Button) v.findViewById(R.id.bangding_class);
        eTname = (TextView) v.findViewById(R.id.my_name);
        eTid = (TextView) v.findViewById(R.id.my_id);
        eTiden = (TextView) v.findViewById(R.id.my_identify);
        eTclass = (TextView) v.findViewById(R.id.my_class);
        getMessage(HomeActivity.user, eTname, eTid, eTiden, eTclass);
    }

    public void getMessage(User user, TextView eTname, TextView eTid, TextView eTiden, TextView eTclass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = JdbcUtil.conn();
                String name = "", id = "", iden = "", clas = "";
                name = "姓名：" + user.getName();
                id = "账号：" + user.getPhone();
                iden = "身份：" + (user.isIdentify() ? "学生" : "老师");
                eTname.setText(name);
                eTid.setText(id);
                eTiden.setText(iden);
                if (iden.equals("身份：老师")) {
                    JdbcUtil.selectClass(conn, id, clas, "TEACHER");
                    eTclass.setText(clas);
                }
                if (iden.equals("身份：学生")) {
                    JdbcUtil.selectClass(conn, id, clas, "STUDENT");
                    eTclass.setText(clas);
                }
                if (clas.equals("班级：未绑定班级") || clas.isEmpty()) {
                    mBtnbangding.setVisibility(View.VISIBLE);
                }
                else {
                    mBtnbangding.setVisibility(View.INVISIBLE);
                }
                JdbcUtil.close(conn);
            }
        }).start();
    }

}
