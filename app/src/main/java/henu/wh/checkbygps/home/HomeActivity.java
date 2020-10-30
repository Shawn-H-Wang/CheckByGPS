package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.help.Init;

public class HomeActivity extends AppCompatActivity implements Init {

    private TextView TVtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        TVtitle.setSelected(true);
    }

    @Override
    public void initViews() {
        TVtitle = (TextView) findViewById(R.id.tv_home_t1);
    }
}