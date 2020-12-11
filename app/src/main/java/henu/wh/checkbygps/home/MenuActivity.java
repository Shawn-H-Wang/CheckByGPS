package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.message.MessageActivity;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class MenuActivity extends AppCompatActivity {

    private Button personal, managegroup, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getResources().getText(R.string.app_menu));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        personal = (Button) findViewById(R.id.personal);
        managegroup = (Button) findViewById(R.id.managegroup);
        message = (Button) findViewById(R.id.message);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PersonActivity.class));
            }
        });
        managegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MessageActivity.class));
            }
        });
    }
}