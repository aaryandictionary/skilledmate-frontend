package com.app.miniproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.TeamAdminAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.SetRoleModel;
import com.app.miniproject.Database.Teams.SetRoleResponse;
import com.app.miniproject.Database.Teams.TeamAdminData;
import com.app.miniproject.Database.Teams.TeamAdminResponse;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserList extends AppCompatActivity implements TeamAdminAdapter.TeamAdminClickEvents {

    TextView title;
    RecyclerView recyclerUsers;
    int type,teamId,eventId;

    TeamAdminAdapter teamAdminAdapter;

    String selectedRole="";
    DataProcessor dataProcessor;

    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initializeView();

        teamAdminAdapter=new TeamAdminAdapter(this,this);
        recyclerUsers.setAdapter(teamAdminAdapter);
        dataProcessor=DataProcessor.getInstance(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        type=getIntent().getIntExtra("type",-1);
        teamId=getIntent().getIntExtra("teamId",-1);

        if (type==0){
            title.setText("Team Followers");
            getFollowersList();
        }else if (type==1){
            title.setText("Team Members");
            getMembersList();
        }else if (type==2){
            eventId=getIntent().getIntExtra("eventId",-1);
            title.setText("Event Participants");
            getEventParticipantsList();
        }

    }

    private void getEventParticipantsList(){
        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.getEventParticipants(eventId,teamId).enqueue(new Callback<TeamAdminResponse>() {
            @Override
            public void onResponse(Call<TeamAdminResponse> call, Response<TeamAdminResponse> response) {
                if (response.isSuccessful()){
                    teamAdminAdapter.setTeamAdmin(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<TeamAdminResponse> call, Throwable t) {

            }
        });
    }


    private void getFollowersList(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);
        teamInterface.getTeamFollowers(teamId).enqueue(new Callback<TeamAdminResponse>() {
            @Override
            public void onResponse(Call<TeamAdminResponse> call, Response<TeamAdminResponse> response) {
                if (response.isSuccessful()){
                    teamAdminAdapter.setTeamAdmin(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<TeamAdminResponse> call, Throwable t) {

            }
        });
    }

    private void getMembersList(){
        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.getTeamMembers(teamId).enqueue(new Callback<TeamAdminResponse>() {
            @Override
            public void onResponse(Call<TeamAdminResponse> call, Response<TeamAdminResponse> response) {
                if (response.isSuccessful()){
                    teamAdminAdapter.setTeamAdmin(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<TeamAdminResponse> call, Throwable t) {

            }
        });
    }

    private void initializeView(){
        title=findViewById(R.id.txtTitle);
        recyclerUsers=findViewById(R.id.recyclerUsers);
        recyclerUsers.setHasFixedSize(true);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    }


    private void openRoleDialog(final TeamAdminData teamAdminData, final Integer position){
        alertDialog=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_user_role_selector,null,false);
        alertDialog.setView(view);

        CircleImageView circularImgUser=view.findViewById(R.id.circularImgUser);
        TextView txtName=view.findViewById(R.id.txtName);
        TextView txtCollege=view.findViewById(R.id.txtCollege);
        final Spinner spinnerUserRole=view.findViewById(R.id.spinnerUserRole);
        final EditText textRoleTitle=view.findViewById(R.id.textRoleTitle);
        RelativeLayout relAssignRole=view.findViewById(R.id.relAssignRole);

        Glide.with(this).load(teamAdminData.getUserImage()).into(circularImgUser);
        txtName.setText(teamAdminData.getName());
        txtCollege.setText(teamAdminData.getCollegeName());
        textRoleTitle.setText(teamAdminData.getRoleTitle());

        selectedRole=teamAdminData.getRole();

        ArrayList<String> roleList=new ArrayList<>();
        roleList.add("Admin");
        roleList.add("Member");
        roleList.add("Follower");

        ArrayAdapter<String> roleAdapter=new ArrayAdapter<>(UserList.this,android.R.layout.simple_spinner_dropdown_item,roleList);
        spinnerUserRole.setAdapter(roleAdapter);

        if (TextUtils.equals(selectedRole,"ADMIN")){
            spinnerUserRole.setSelection(0);
        }else if (TextUtils.equals(selectedRole,"MEMBER")){
            spinnerUserRole.setSelection(1);
        }else if (TextUtils.equals(selectedRole,"FOLLOWER")){
            spinnerUserRole.setSelection(2);
        }

        spinnerUserRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    selectedRole="ADMIN";
                }else if (i==1){
                    selectedRole="MEMBER";
                }else if (i==2){
                    selectedRole="FOLLOWER";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        relAssignRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(textRoleTitle.getText().toString())){
                    saveRole(textRoleTitle.getText().toString(),teamAdminData.getId(),position);
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void saveRole(final String roleTitle, Integer userId, final Integer position){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        SetRoleModel setRoleModel=new SetRoleModel();
        setRoleModel.setRole(selectedRole);
        setRoleModel.setRole_title(roleTitle);
        setRoleModel.setTeam_id(teamId);
        setRoleModel.setUser_id(userId);

        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.setTeamRole(setRoleModel).enqueue(new Callback<SetRoleResponse>() {
            @Override
            public void onResponse(Call<SetRoleResponse> call, Response<SetRoleResponse> response) {
                if (response.isSuccessful()){
                    teamAdminAdapter.updateTeamAdmin(selectedRole,roleTitle,position);
                    Toast.makeText(UserList.this,"User role updated successfully",Toast.LENGTH_SHORT).show();
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                }else {
                    Toast.makeText(UserList.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SetRoleResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserList.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onTeamLongClick(TeamAdminData teamAdminData,Integer position) {
        /*Intent intent=new Intent(UserList.this,UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);*/
        openRoleDialog(teamAdminData,position);
    }

    @Override
    public void onTeamUserClick(Integer userId) {
        Intent intent=new Intent(UserList.this,UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
}