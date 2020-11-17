package com.app.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.miniproject.Adapters.CourseListAdapter;
import com.app.miniproject.Adapters.ExpertiseAdapter;
import com.app.miniproject.Adapters.ProjectAdapter;
import com.app.miniproject.Adapters.UserTeamAdapter;
import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.CourseInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.UserTeamResponse;
import com.app.miniproject.Database.UserDetail.UserData;
import com.app.miniproject.Database.UserDetail.UserResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Models.ExpertiseModel;
import com.app.miniproject.Models.ProjectModel;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity implements CourseListAdapter.CourseClickEvents,UserTeamAdapter.UserTeamClickEvents {

    ChipGroup chipGroup;
    RecyclerView recyclerTeams,recyclerCourses;
    ImageButton imgBtnBack;
    RelativeLayout relMessage;

    Integer userId;

    DataProcessor dataProcessor;

    CourseListAdapter courseListAdapter;
    UserTeamAdapter userTeamAdapter;
    RelativeLayout relaup;
    ContentLoadingProgressBar progress;

    CircleImageView circularImageProfile;
    TextView txtUserName,txtUserCollege,txtUserDesc;
    String name,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeView();

        userId=getIntent().getIntExtra("userId",-1);
        dataProcessor=DataProcessor.getInstance(this);

        courseListAdapter=new CourseListAdapter(this,this);
        recyclerCourses.setAdapter(courseListAdapter);

        userTeamAdapter=new UserTeamAdapter(this,this);
        recyclerTeams.setAdapter(userTeamAdapter);

        if (userId==dataProcessor.getInteger(DataProcessor.USER_ID)){
            relMessage.setVisibility(View.GONE);
        }else {
            relMessage.setVisibility(View.VISIBLE);
        }

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        relMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,ChatScreen.class);
                intent.putExtra("conversationId",-1);
                intent.putExtra("receiverId",userId);
                intent.putExtra("receiverName",name);
                intent.putExtra("receiverImage",image);
                intent.putExtra("convType","MONO");
                startActivity(intent);
            }
        });

        relaup.setVisibility(View.GONE);

        getUserDetails();
        getUserTeams();
        getMyCourses();
    }

    private void getUserTeams(){
        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.getUserTeam(userId).enqueue(new Callback<UserTeamResponse>() {
            @Override
            public void onResponse(Call<UserTeamResponse> call, Response<UserTeamResponse> response) {
                if (response.isSuccessful()){
                    userTeamAdapter.setUserTeamList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<UserTeamResponse> call, Throwable t) {

            }
        });
    }

    private void getMyCourses(){
        CourseInterface courseInterface=Database.getClient(this).create(CourseInterface.class);
        courseInterface.getMyCourses(userId).enqueue(new Callback<CourseListResponse>() {
            @Override
            public void onResponse(Call<CourseListResponse> call, Response<CourseListResponse> response) {
                if (response.isSuccessful()){
                    courseListAdapter.setCourseList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CourseListResponse> call, Throwable t) {

            }
        });
    }

    private void getUserDetails(){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.getUserDetailsById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    setUserData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void setUserData(UserData userData){
        if (userData!=null){
            if (userData.getUserImage()!=null)
                Glide.with(this).load(userData.getUserImage()).into(circularImageProfile);
            txtUserName.setText(userData.getName());
            txtUserCollege.setText(userData.getCollege().getCollegeName());
            txtUserDesc.setText("");
            name=userData.getName();
            image=userData.getUserImage();
            setSkills(userData.getSkills());
        }
    }

    private void setSkills(List<SkillsListData>skillsListData){
        for (SkillsListData skills:skillsListData){
            Chip chip=new Chip(this);
            chip.setText(skills.getSkillName());
            chip.setCloseIconVisible(false);
            chip.setChipStrokeWidth(1);
            chip.setTag(String.valueOf(skills.getId()));
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setChecked(false);
            chip.setClickable(false);

            chipGroup.addView(chip);
        }
        relaup.setVisibility(View.VISIBLE);
        progress.hide();
    }

    private void initializeView(){
        chipGroup=findViewById(R.id.chipGroup);
        recyclerCourses=findViewById(R.id.recyclerCourses);
        recyclerCourses.setHasFixedSize(true);
        recyclerCourses.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerTeams=findViewById(R.id.recyclerTeams);
        recyclerTeams.setHasFixedSize(true);
        recyclerTeams.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        imgBtnBack=findViewById(R.id.imgBtnBack);
        relMessage=findViewById(R.id.relMessage);

        circularImageProfile=findViewById(R.id.circularImageProfile);
        txtUserName=findViewById(R.id.txtUserName);
        txtUserCollege=findViewById(R.id.txtUserCollege);
        txtUserDesc=findViewById(R.id.txtUserDesc);
        relaup=findViewById(R.id.relaup);
        progress=findViewById(R.id.progress);
    }


    @Override
    public void onCourseClick(Integer courseId) {
        Intent intent=new Intent(UserProfile.this,CourseDetails.class);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }

    @Override
    public void onUserTeamClick(Integer teamId) {
        Intent intent=new Intent(UserProfile.this,TeamDetails.class);
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }
}