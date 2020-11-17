package com.app.miniproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.CourseListAdapter;
import com.app.miniproject.Adapters.UserTeamAdapter;
import com.app.miniproject.Database.CollegeList.CollegeListResponse;
import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.CourseInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.Tag.AddTagModel;
import com.app.miniproject.Database.Tag.TagResponse;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.UserTeamResponse;
import com.app.miniproject.Database.UserDetail.UserData;
import com.app.miniproject.Database.UserDetail.UserResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment implements View.OnClickListener,CourseListAdapter.CourseClickEvents, UserTeamAdapter.UserTeamClickEvents {

    CircleImageView circularImageProfile;
    TextView txtUserName,txtUserCollege,txtUserAbout,txtAddNew;
    ChipGroup chipMySkills;
    TextView tT,tC;
    RecyclerView recyclerMyTeams,recyclerCourses;
    ChipGroup chipGroup;
    ImageButton imgBtnMore;

    DataProcessor dataProcessor;
    CourseListAdapter courseListAdapter;
    UserTeamAdapter userTeamAdapter;
    List<Integer>selectedSkills=new ArrayList<>();

    FirebaseAuth mAuth;
    RelativeLayout relp;
    ContentLoadingProgressBar progress;

    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    Integer userId=-1;

    boolean editProfileOpened=false;
    boolean addSkillOpened=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        initializeView(view);
        dataProcessor=DataProcessor.getInstance(getContext());
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getContext());

        courseListAdapter=new CourseListAdapter(getContext(),this);
        recyclerCourses.setAdapter(courseListAdapter);

        userTeamAdapter=new UserTeamAdapter(getContext(),this);
        recyclerMyTeams.setAdapter(userTeamAdapter);

        relp.setVisibility(View.GONE);

        getUserDetails();
        getMyCourses();
        getUserTeams();

        txtAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSkillOpened=true;
                openAddSkillDialog();
            }
        });

        imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileMore();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (editProfileOpened||addSkillOpened){
            getUserDetails();
            editProfileOpened=false;
            addSkillOpened=false;
        }
    }

    private void openLogoutDialog(){
        final AlertDialog logoutDialog=new AlertDialog.Builder(getContext()).create();
        logoutDialog.setMessage("Sure want to logout?");
        logoutDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutDialog.dismiss();
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAuth.signOut();
                        dataProcessor.deleteAll();
                        Intent intent=new Intent(getContext(),Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                },1500);
            }
        });

        logoutDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutDialog.dismiss();
            }
        });

        logoutDialog.setCanceledOnTouchOutside(true);
        logoutDialog.show();
    }

    private void openProfileMore(){
        PopupMenu popupMenu=new PopupMenu(getContext(),imgBtnMore);
        popupMenu.inflate(R.menu.profile_more_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.itemEditProfile:
                        editProfileOpened=true;
                        Intent intent=new Intent(getContext(),UpdateProfile.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                        return true;
                    case R.id.itemLogout:
                        openLogoutDialog();
                        return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void openAddSkillDialog(){
        alertDialog=new AlertDialog.Builder(getContext()).create();
        View view=LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_skill,null,false);
        alertDialog.setView(view);

        ImageButton imgBtnCancel=view.findViewById(R.id.imgBtnCancel);
        chipGroup=view.findViewById(R.id.chipGroup);
        Button btnSave=view.findViewById(R.id.btnSave);

        selectedSkills.clear();
        getAddableSkillList();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSkills.size()!=0){
                    saveSkillsList();
                }
            }
        });
        imgBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void saveSkillsList(){
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AddTagModel addTagModel=new AddTagModel();
        addTagModel.setUserId(dataProcessor.getInteger(DataProcessor.USER_ID));

        Integer ss[]=new Integer[selectedSkills.size()];

        int i=0;
        for (Integer skill:selectedSkills){
            ss[i]=skill;
            i++;
        }

        addTagModel.setTags(ss);
        UserInterface userInterface=Database.getClient(getContext()).create(UserInterface.class);
        userInterface.addUserSkill(addTagModel).enqueue(new Callback<TagResponse>() {
            @Override
            public void onResponse(Call<TagResponse> call, Response<TagResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TagResponse> call, Throwable t) {

            }
        });
    }

    private void getAddableSkillList(){
        UserInterface userInterface=Database.getClient(getContext()).create(UserInterface.class);
        userInterface.getAddableSkillList(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<SkillsListResponse>() {
            @Override
            public void onResponse(Call<SkillsListResponse> call, Response<SkillsListResponse> response) {
                if (response.isSuccessful()){
                    renderTags(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SkillsListResponse> call, Throwable t) {

            }
        });
    }

    private void renderTags(List<SkillsListData>skillsListData){
        for (SkillsListData skills:skillsListData){
            final Chip chip=new Chip(getContext());
            chip.setText(skills.getSkillName());
            chip.setTag(String.valueOf(skills.getId()));
            chip.setCloseIconVisible(false);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (chip.isChecked()){
                        selectedSkills.add(Integer.valueOf(String.valueOf(chip.getTag())));
                    }else {
                        selectedSkills.remove(Integer.valueOf(String.valueOf(chip.getTag())));
                    }
                }
            });

//            selectedSkills.add(tagListAdapter.getItem(position).getId());
            chipGroup.addView(chip);
        }
    }

    private void getUserTeams(){
        TeamInterface teamInterface=Database.getClient(getContext()).create(TeamInterface.class);
        teamInterface.getUserTeam(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<UserTeamResponse>() {
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
        CourseInterface courseInterface=Database.getClient(getContext()).create(CourseInterface.class);
        courseInterface.getMyCourses(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<CourseListResponse>() {
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
        UserInterface userInterface= Database.getClient(getContext()).create(UserInterface.class);
        userInterface.getUserDetailsById(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<UserResponse>() {
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

    private void setSkills(List<SkillsListData> skillsListData){
        for (final SkillsListData skills:skillsListData){
            final Chip chip=new Chip(getContext());
            chip.setText(skills.getSkillName());
            chip.setCloseIconVisible(true);
            chip.setChipStrokeWidth(1);
            chip.setTag(String.valueOf(skills.getId()));
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setChecked(false);
            chip.setClickable(false);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeSkill(Integer.parseInt(String.valueOf(chip.getTag())));
                }
            });

            chipMySkills.addView(chip);
        }
        relp.setVisibility(View.VISIBLE);
        progress.hide();
    }

    private void removeSkill(final Integer skillId){
        UserInterface userInterface=Database.getClient(getContext()).create(UserInterface.class);
        userInterface.removeSkill(dataProcessor.getInteger(DataProcessor.USER_ID),skillId).enqueue(new Callback<CollegeListResponse>() {
            @Override
            public void onResponse(Call<CollegeListResponse> call, Response<CollegeListResponse> response) {
                if (response.isSuccessful()){
                    removeSkillFromList(skillId);
                }
                Toast.makeText(getContext(),"CODE"+response.code(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CollegeListResponse> call, Throwable t) {
                Toast.makeText(getContext(),"MESSAGE "+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void removeSkillFromList(Integer skillId){
        Chip chip= chipMySkills.findViewWithTag(String.valueOf(skillId));
        if (chip!=null)
        chipMySkills.removeView(chip);
    }

    private void setUserData(UserData userData){
        if (userData.getUserImage()!=null)
            Glide.with(this).load(userData.getUserImage()).into(circularImageProfile);
        txtUserName.setText(userData.getName());
        txtUserCollege.setText(userData.getCollege().getCollegeName());
        txtUserAbout.setText("");
        userId=userData.getId();

        setSkills(userData.getSkills());
    }

    private void initializeView(View view){
        relp=view.findViewById(R.id.relp);
        progress=view.findViewById(R.id.progress);
        imgBtnMore=view.findViewById(R.id.imgBtnMore);
        circularImageProfile=view.findViewById(R.id.circularImageProfile);
        txtUserName=view.findViewById(R.id.txtUserName);
        txtUserCollege=view.findViewById(R.id.txtUserCollege);
        txtUserAbout=view.findViewById(R.id.txtUserAbout);
        txtAddNew=view.findViewById(R.id.txtAddNew);
        chipMySkills=view.findViewById(R.id.chipMySkills);
        tT=view.findViewById(R.id.tT);
        tC=view.findViewById(R.id.tC);
        recyclerMyTeams=view.findViewById(R.id.recyclerMyTeams);
        recyclerMyTeams.setHasFixedSize(true);
        recyclerMyTeams.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerCourses=view.findViewById(R.id.recyclerCourses);
        recyclerCourses.setHasFixedSize(true);
        recyclerCourses.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onCourseClick(Integer courseId) {
        Intent intent=new Intent(getContext(),CourseDetails.class);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }

    @Override
    public void onUserTeamClick(Integer teamId) {
        Intent intent=new Intent(getContext(),TeamDetails.class);
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }
}