package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.miniproject.Utils.DataProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout frame_container;
    BottomNavigationView bottom_navigation_main;
    FloatingActionButton floatBtnAdd;

    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        setSupportActionBar(toolbar);

        bottom_navigation_main.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Home()).commit();

        floatBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });

        if (!dataProcessor.getBoolean(DataProcessor.SHARE_VIEWED)){
            dataProcessor.saveBoolean(DataProcessor.SHARE_VIEWED,true);
            String type=dataProcessor.getString(DataProcessor.SHARE_TYPE);
            Integer id=Integer.parseInt(dataProcessor.getString(DataProcessor.SHARE_ID));

            if (TextUtils.equals(type,"POST")){
                Intent intent=new Intent(MainActivity.this,PostDetail.class);
                intent.putExtra("postId",id);
                startActivity(intent);
            }else if (TextUtils.equals(type,"TEAM")){
                Intent intent=new Intent(MainActivity.this,TeamDetails.class);
                intent.putExtra("teamId",id);
                startActivity(intent);
            }else if (TextUtils.equals(type,"EVENT")){
                Intent intent=new Intent(MainActivity.this,EventDetails.class);
                intent.putExtra("eventId",id);
                startActivity(intent);
            }else if (TextUtils.equals(type,"USER")){
                Intent intent=new Intent(MainActivity.this,UserProfile.class);
                intent.putExtra("userId",id);
                startActivity(intent);
            }
        }
    }

    private void openCreateDialog(){
        final AlertDialog alert=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_create,null,false);
        alert.setView(view);

        Button btnPost=view.findViewById(R.id.btnPost);
        Button btnTeam=view.findViewById(R.id.btnTeam);
        Button btnExpertise=view.findViewById(R.id.btnExpertise);
        Button btnProject=view.findViewById(R.id.btnProject);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent=new Intent(MainActivity.this,CreatePost.class);
                startActivity(intent);
            }
        });

        btnTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent=new Intent(MainActivity.this,CreateTeam.class);
                intent.putExtra("type","ADD");
                startActivity(intent);
            }
        });

        btnExpertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent=new Intent(MainActivity.this, CreateCourse.class);
                startActivity(intent);
            }
        });

        btnProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent=new Intent(MainActivity.this,CreateProject.class);
                startActivity(intent);
            }
        });

        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }




    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment =null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new Home();
                    toolbar.setTitle("Home");
                    break;
                case R.id.nav_myteams:
                    selectedFragment = new MyTeams();
                    toolbar.setTitle("My Teams");
                    break;
                case R.id.nav_messages:
                    toolbar.setTitle("Messages");
                    selectedFragment = new Messages();
                    break;
                case R.id.nav_profile:
                    toolbar.setTitle("Profile");
                    selectedFragment = new Profile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectedFragment).commit();

            return true;
        }
    };

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finishAffinity();
        } else {

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }


    private void initializeView(){
        toolbar=findViewById(R.id.toolbar);
        frame_container=findViewById(R.id.frame_container);
        bottom_navigation_main=findViewById(R.id.bottom_navigation_main);
        floatBtnAdd=findViewById(R.id.floatBtnAdd);
    }
}