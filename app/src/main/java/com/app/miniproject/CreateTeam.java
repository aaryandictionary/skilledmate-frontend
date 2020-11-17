package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.TeamPositionAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.TeamDetailsData;
import com.app.miniproject.Database.Teams.TeamDetailsResponse;
import com.app.miniproject.Database.Teams.TeamResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTeam extends AppCompatActivity{

    EditText textTeamDescription,textTeamTitle,textTeamShortDesc,textTeamRole;
    ImageView imgTeam;
    ImageButton imgBtnBack;
    Button btnCreateTeam;

    ChipGroup chipGroup;

    ProgressDialog progressDialog;
    DataProcessor dataProcessor;
    File selectedFile=null;
    String type;
    ArrayList<Integer>tags=new ArrayList<>();

    Integer teamId=-1;
    TeamDetailsData teamDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        initializeView();

        progressDialog=new ProgressDialog(this);
        dataProcessor=DataProcessor.getInstance(this);

        type=getIntent().getStringExtra("type");

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    if (TextUtils.equals(type,"ADD")){
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        createTeam();
                    }else {
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        updateTeam();
                    }

                }

            }
        });

        imgTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(CreateTeam.this);
            }
        });

        if (TextUtils.equals(type,"ADD")){
            getTagList();
        }else{
            teamId=getIntent().getIntExtra("teamId",-1);
            textTeamRole.setVisibility(View.GONE);
            btnCreateTeam.setText("Save");
            getTeamDetails();
            getNonTags();
        }
    }

    private void getTeamDetails(){
        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.getTeamDetails(teamId,dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<TeamDetailsResponse>() {
            @Override
            public void onResponse(Call<TeamDetailsResponse> call, Response<TeamDetailsResponse> response) {
                if (response.isSuccessful()){
                    teamDetails=response.body().getData();
                    if (teamDetails!=null){
                        Glide.with(CreateTeam.this).load(teamDetails.getTeamIcon()).into(imgTeam);
                        textTeamTitle.setText(teamDetails.getTeamTitle());
                        textTeamDescription.setText(teamDetails.getTeamDescription());
                        textTeamShortDesc.setText(teamDetails.getTeamTagline());
                    }

                }
            }

            @Override
            public void onFailure(Call<TeamDetailsResponse> call, Throwable t) {

            }
        });
    }


    private boolean validateFields(){
        if (TextUtils.isEmpty(textTeamTitle.getText().toString())){
            Toast.makeText(CreateTeam.this,"Enter team title",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(textTeamDescription.getText().toString())){
            Toast.makeText(CreateTeam.this,"Enter team description",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(textTeamRole.getText().toString())&&TextUtils.equals(type,"ADD")){
            Toast.makeText(CreateTeam.this,"Enter team role",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updateTeam(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);

        RequestBody Id=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(teamId));
        RequestBody ConversationId=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(teamDetails.getConversation_id()));
        RequestBody TeamTitle=RequestBody.create(MediaType.parse("text/plain"),textTeamTitle.getText().toString());
        RequestBody UserId=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody TagLine=RequestBody.create(MediaType.parse("text/plain"),textTeamShortDesc.getText().toString());
        RequestBody TeamDesc=RequestBody.create(MediaType.parse("text/plain"),textTeamDescription.getText().toString());

        Call<TeamResponse> call;

        if (selectedFile!=null){
            MultipartBody.Part ImgTeam=MultipartBody.Part.createFormData("team_icon",
                    selectedFile.getName(),RequestBody.create(MediaType.parse("image/*"),selectedFile));
            call=teamInterface.updateTeam(Id,TeamTitle,UserId,TagLine,TeamDesc,ConversationId,tags,ImgTeam);
        }else{
            call=teamInterface.updateTeam(Id,TeamTitle,UserId,TagLine,TeamDesc,ConversationId,tags);
        }
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CreateTeam.this,"Team updated successfully",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void createTeam(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);

        RequestBody TeamTitle=RequestBody.create(MediaType.parse("text/plain"),textTeamTitle.getText().toString());
        RequestBody Id=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody TagLine=RequestBody.create(MediaType.parse("text/plain"),textTeamShortDesc.getText().toString());
        RequestBody TeamDesc=RequestBody.create(MediaType.parse("text/plain"),textTeamDescription.getText().toString());
        RequestBody RoleTitle=RequestBody.create(MediaType.parse("text/plain"),textTeamRole.getText().toString());


        Call<TeamResponse> call;

        if (selectedFile!=null){
            MultipartBody.Part ImgTeam=MultipartBody.Part.createFormData("team_icon",
                    selectedFile.getName(),RequestBody.create(MediaType.parse("image/*"),selectedFile));
            call=teamInterface.createTeam(TeamTitle,Id, TagLine,TeamDesc,RoleTitle,tags,ImgTeam);
        }else{
            call=teamInterface.createTeam(TeamTitle,Id, TagLine,TeamDesc,RoleTitle,tags);
        }

        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode()==1){
                        progressDialog.dismiss();
                        Toast.makeText(CreateTeam.this,"Team Created successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(CreateTeam.this,"Something went wrong. Try again later"+response.message(),Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(CreateTeam.this,"Something went wrong. Try again later"+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateTeam.this,"Something went wrong. Try again later"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTagList(){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.getSkillsList().enqueue(new Callback<SkillsListResponse>() {
            @Override
            public void onResponse(Call<SkillsListResponse> call, Response<SkillsListResponse> response) {
                if (response.isSuccessful()){
                    /*tagListAdapter=new TagListAdapter(AddSkills.this,R.layout.items_tag_list_selection,response.body().getData());
                    autoCompleteSkills.setAdapter(tagListAdapter);*/
                    renderTags(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SkillsListResponse> call, Throwable t) {

            }
        });
    }


    private void getNonTags(){
        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.getNontags("TEAM",teamId).enqueue(new Callback<SkillsListResponse>() {
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
            final Chip chip=new Chip(CreateTeam.this);
            chip.setText(skills.getSkillName());
            chip.setTag(String.valueOf(skills.getId()));
            chip.setCloseIconVisible(false);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setCheckable(true);
            chip.setClickable(true);
            if (skills.getMy_tag()!=null){
                if (TextUtils.equals(skills.getMy_tag(),"true")){
                    chip.setChecked(true);
                    tags.add(skills.getId());
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (chip.isChecked()){
                        tags.add(Integer.valueOf(String.valueOf(chip.getTag())));
                    }else {
                        tags.remove(Integer.valueOf(String.valueOf(chip.getTag())));
                    }
                }
            });

//            selectedSkills.add(tagListAdapter.getItem(position).getId());
            chipGroup.addView(chip);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){
                assert data != null;
//                imgTeam.setImageURI(null);
//                Glide.with(CreateTeam.this).load(data.getData()).into(imgTeam);
                imgTeam.setImageURI(result.getUri());
                selectedFile=new File(Objects.requireNonNull(result.getUri().getPath()));

            }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e=result.getError();
                Toast.makeText(this,"File selection Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initializeView(){
        textTeamDescription=findViewById(R.id.textTeamDescription);
        textTeamTitle=findViewById(R.id.textTeamTitle);
        imgTeam=findViewById(R.id.imgTeam);
        imgBtnBack=findViewById(R.id.imgBtnBack);
        btnCreateTeam=findViewById(R.id.btnCreateTeam);
        textTeamShortDesc=findViewById(R.id.textTeamShortDesc);
        textTeamRole=findViewById(R.id.textTeamRole);
        chipGroup=findViewById(R.id.chipGroup);

    }

}