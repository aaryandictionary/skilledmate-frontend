package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.PostAdapter;
import com.app.miniproject.Adapters.TeamAdminAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Post.PostData;
import com.app.miniproject.Database.Post.PostDataResponse;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.PostLike.PostLikeModel;
import com.app.miniproject.Database.PostLike.PostLikeResponse;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.FollowTeamModel;
import com.app.miniproject.Database.Teams.FollowTeamResponse;
import com.app.miniproject.Database.Teams.SetRoleModel;
import com.app.miniproject.Database.Teams.SetRoleResponse;
import com.app.miniproject.Database.Teams.TeamAdminData;
import com.app.miniproject.Database.Teams.TeamAdminResponse;
import com.app.miniproject.Database.Teams.TeamDetailsData;
import com.app.miniproject.Database.Teams.TeamDetailsResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamDetails extends AppCompatActivity implements PostAdapter.PostClickEvents, TeamAdminAdapter.TeamAdminClickEvents {

    TeamAdminAdapter teamAdminAdapter;
    RecyclerView recyclerTeamAdmins,recyclerTeamPosts;

    FloatingActionButton floatingAddInTeam;

    int teamId=-1;

    TextView txtTeamName,txtAboutTeam,txtFollowerCount,txtMembersCount;
    ImageView imgTeam;
    RelativeLayout relJoinNow;

    DataProcessor dataProcessor;
    ImageButton imgBtnEdit,imgBtnShare;

    String selectedRole="",myTeam="",myrole="";

    RelativeLayout relatd;
    ContentLoadingProgressBar progress;

    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    PostAdapter postAdapter;

    NestedScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        teamAdminAdapter=new TeamAdminAdapter(this,this);
        recyclerTeamAdmins.setAdapter(teamAdminAdapter);

        postAdapter =new PostAdapter(this,this,1);
        recyclerTeamPosts.setAdapter(postAdapter);

        progressDialog=new ProgressDialog(this);

        teamId=getIntent().getIntExtra("teamId",-1);

        relatd.setVisibility(View.GONE);

        loadData();
        loadTeamAdmins();
        getTeamPosts();

        floatingAddInTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddInTeamDialog();
            }
        });

        relJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followTeam();
            }
        });

        txtFollowerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeamDetails.this,UserList.class);
                intent.putExtra("type",0);
                intent.putExtra("teamId",teamId);
                startActivity(intent);
            }
        });

        txtMembersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeamDetails.this,UserList.class);
                intent.putExtra("type",1);
                intent.putExtra("teamId",teamId);
                startActivity(intent);
            }
        });

        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>oldScrollY){
                    floatingAddInTeam.setVisibility(View.GONE);
                }else {
                    if (TextUtils.equals(myrole,"ADMIN"))
                    floatingAddInTeam.setVisibility(View.VISIBLE);
                }
            }
        });

        imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeamDetails.this,CreateTeam.class);
                intent.putExtra("teamId",teamId);
                intent.putExtra("type","EDIT");
                startActivity(intent);
            }
        });

        imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLink("TEAM",String.valueOf(teamId));
            }
        });

    }


    private void followTeam(){
        FollowTeamModel followTeamModel=new FollowTeamModel();
        followTeamModel.setTeam_id(teamId);
        followTeamModel.setUser_id(dataProcessor.getInteger(DataProcessor.USER_ID));

        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.followTeam(followTeamModel).enqueue(new Callback<FollowTeamResponse>() {
            @Override
            public void onResponse(Call<FollowTeamResponse> call, Response<FollowTeamResponse> response) {
                if (response.isSuccessful()){
                    relJoinNow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FollowTeamResponse> call, Throwable t) {

            }
        });
    }


    private void openAddInTeamDialog(){
        final AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_add_in_team,null,false);
        alertDialog.setView(view);

        Button btnPost=view.findViewById(R.id.btnPost);
        Button btnEvent=view.findViewById(R.id.btnEvent);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent=new Intent(TeamDetails.this,CreatePost.class);
                intent.putExtra("teamId",teamId);
                startActivity(intent);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent=new Intent(TeamDetails.this,CreateEvent.class);
                intent.putExtra("type","ADD");
                intent.putExtra("teamId",teamId);
                startActivity(intent);
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void getTeamPosts(){
        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.getTeamPosts(teamId,dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<PostDataResponse>() {
            @Override
            public void onResponse(Call<PostDataResponse> call, Response<PostDataResponse> response) {
                if (response.isSuccessful()){
                    postAdapter.addPostToList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PostDataResponse> call, Throwable t) {

            }
        });
    }

    private void loadData(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);
        teamInterface.getTeamDetails(teamId,dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<TeamDetailsResponse>() {
            @Override
            public void onResponse(Call<TeamDetailsResponse> call, Response<TeamDetailsResponse> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    TeamDetailsData teamDetailsData=response.body().getData();
                    txtTeamName.setText(teamDetailsData.getTeamTitle());
                    txtAboutTeam.setText(teamDetailsData.getTeamDescription());
                    txtFollowerCount.setText(teamDetailsData.getFollowers_count()+" Followers");
                    txtMembersCount.setText(teamDetailsData.getMembers_count()+" Members");
                    myrole=teamDetailsData.getMy_team();

                    if (TextUtils.equals(teamDetailsData.getMy_team(),"false")){
                        relJoinNow.setVisibility(View.VISIBLE);
                        imgBtnEdit.setVisibility(View.GONE);
                    }else {
                        relJoinNow.setVisibility(View.GONE);
                        imgBtnEdit.setVisibility(View.GONE);
                        if (TextUtils.equals(teamDetailsData.getMy_team(),"ADMIN")){
                            imgBtnEdit.setVisibility(View.VISIBLE);
                            floatingAddInTeam.setVisibility(View.VISIBLE);
                        }else {
                            floatingAddInTeam.setVisibility(View.GONE);
                        }
                        myTeam=teamDetailsData.getMy_team();
                    }
                    Glide.with(TeamDetails.this).load(teamDetailsData.getTeamIcon()).into(imgTeam);
                    progress.hide();
                    relatd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TeamDetailsResponse> call, Throwable t) {

            }
        });
    }

    private void loadTeamAdmins(){
        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.getTeamAdmins(teamId).enqueue(new Callback<TeamAdminResponse>() {
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
        imgBtnEdit=findViewById(R.id.imgBtnEdit);
        imgBtnShare=findViewById(R.id.imgBtnShare);
        relatd=findViewById(R.id.relatd);
        progress=findViewById(R.id.progress);

        scroll=findViewById(R.id.scroll);
        floatingAddInTeam=findViewById(R.id.floatingAddInTeam);
        txtTeamName=findViewById(R.id.txtTeamName);
        txtAboutTeam=findViewById(R.id.txtAboutTeam);
        recyclerTeamPosts=findViewById(R.id.recyclerTeamPosts);
        recyclerTeamPosts.setHasFixedSize(true);
        recyclerTeamPosts.setLayoutManager(new LinearLayoutManager(this));

        imgTeam=findViewById(R.id.imgTeam);
        relJoinNow=findViewById(R.id.relJoinNow);
        txtMembersCount=findViewById(R.id.txtMembersCount);
        txtFollowerCount=findViewById(R.id.txtFollowerCount);

        recyclerTeamAdmins=findViewById(R.id.recyclerTeamAdmins);
        recyclerTeamAdmins.setHasFixedSize(true);
        recyclerTeamAdmins.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLikeClick(Integer postId, Integer position) {
        postAdapter.swapLike(position);
        likePost(postId,position);
    }

    @Override
    public void onCommentClick(PostData postData) {
        openPostDetails(postData);
    }

    @Override
    public void onShareClick(Integer postId) {

    }

    @Override
    public void onUserClick(Integer userId) {
        Intent intent=new Intent(TeamDetails.this,UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    @Override
    public void onPostClick(PostData postData) {
        openPostDetails(postData);
    }

    @Override
    public void onTeamClick(Integer teamId) {
        Intent intent=new Intent(TeamDetails.this,TeamDetails.class);
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }

    @Override
    public void onEventDetailsClick(Integer eventId) {
        Intent intent=new Intent(TeamDetails.this,EventDetails.class);
        intent.putExtra("eventId",eventId);
        startActivity(intent);
    }

    private void likePost(Integer postId, final Integer position){
        PostLikeModel postLikeModel=new PostLikeModel();
        postLikeModel.setPost_id(postId);
        postLikeModel.setLiker_id(dataProcessor.getInteger(DataProcessor.USER_ID));

        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.swapPostLike(postLikeModel).enqueue(new Callback<PostLikeResponse>() {
            @Override
            public void onResponse(Call<PostLikeResponse> call, Response<PostLikeResponse> response) {
                if (response.isSuccessful()){

                }else {
                    postAdapter.swapLike(position);
                }
            }

            @Override
            public void onFailure(Call<PostLikeResponse> call, Throwable t) {
                postAdapter.swapLike(position);
            }
        });
    }


    private void openPostDetails(PostData postData){
        Intent intent=new Intent(TeamDetails.this,PostDetail.class);
        intent.putExtra("userImg",postData.getUser().getUserImage());
        intent.putExtra("userName",postData.getUser().getName());
        intent.putExtra("userCollege",postData.getUser().getCollegeName());
        intent.putExtra("postTime",postData.getCreatedAt());
        intent.putExtra("userId",postData.getUser().getId());
        intent.putExtra("postContent",postData.getPostContent());
        intent.putExtra("postImg",postData.getPostImage());
        intent.putExtra("postId",postData.getId());
        intent.putExtra("eventId",postData.getEventId());
        intent.putExtra("likeCount",postData.getLikesCount());
        intent.putExtra("commentCount",postData.getCommentsCount());
        intent.putExtra("postType",postData.getPostType());
        intent.putExtra("isLiked",postData.getIsLiked());
        startActivity(intent);
    }

    @Override
    public void onTeamLongClick(TeamAdminData teamAdminData,Integer position) {
        /*Intent intent=new Intent(TeamDetails.this,UserProfile.class);
        intent.putExtra("userId",teamAdminData.getId());
        startActivity(intent);*/
        openRoleDialog(teamAdminData,position);
    }

    @Override
    public void onTeamUserClick(Integer userId) {
        Intent intent=new Intent(TeamDetails.this,UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
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

        ArrayAdapter<String> roleAdapter=new ArrayAdapter<>(TeamDetails.this,android.R.layout.simple_spinner_dropdown_item,roleList);
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
                    Toast.makeText(TeamDetails.this,"User role updated successfully",Toast.LENGTH_SHORT).show();
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                }else {
                    Toast.makeText(TeamDetails.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<SetRoleResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TeamDetails.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLink(String type,String id){
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.kvgames.com/"))
                .setDomainUriPrefix("mpgcet.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        String shareLinkText="https://mpgcet.page.link/?" +
                "link=https://www.kvgames.com/".concat(type).concat("/").concat(id)+
                "&apn="+this.getPackageName() +
                "&st=Skilled Mate" +
                "&sd=Download Skilled Mate App" +
                "&si=http://kvgames.com/logo.jpg";

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinkText))
                .buildShortDynamicLink()
                .addOnCompleteListener(TeamDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        }
                    }
                });
    }
}
