package henu.wh.checkbygps.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class MenuActivity extends AppCompatActivity {

    private Button personal, managegroup;

    public static User user1;
    public static String[][] childString1;
    public static List<Group> groupList = new ArrayList<>();

    public static void setUser(User user) {
        MenuActivity.user1 = user;
    }

    public static void setChildString(String[][] childString) {
        MenuActivity.childString1 = childString;
    }

    public static void setGroupList(List<Group> groupList) {
        MenuActivity.groupList = groupList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        personal = (Button) findViewById(R.id.personal);
        managegroup = (Button) findViewById(R.id.managegroup);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonActivity.setUser(user1);
                startActivity(new Intent(MenuActivity.this, PersonActivity.class));
            }
        });
        managegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setUser(user1);
                HomeActivity.setChildString(childString1);
                HomeActivity.setGroupList(groupList);
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
            }
        });
    }
}