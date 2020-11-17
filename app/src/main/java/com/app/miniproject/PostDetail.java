package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.miniproject.Adapters.PostCommentAdapter;
import com.app.miniproject.Adapters.PostLikeAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Post.PostData;
import com.app.miniproject.Database.Post.SinglePostResponse;
import com.app.miniproject.Database.PostComment.PostCommentListResponse;
import com.app.miniproject.Database.PostComment.PostCommentModel;
import com.app.miniproject.Database.PostComment.PostCommentResponse;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.PostLike.PostLikeListResponse;
import com.app.miniproject.Models.PostLikeModel;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetail extends AppCompatActivity implements PostLikeAdapter.PostLikeClickEvents{

    TextView txtCommentsTab,txtLikesTab;
    RecyclerView recyclerLikeComment;
    EditText textComment;
    RelativeLayout relBtnComment,relFooter,relUser;

    PostCommentAdapter postCommentAdapter;
    PostLikeAdapter postLikeAdapter;

    CircleImageView img_user;
    TextView txtName,txtCollegeName,txtPostTime,txtPostText;
    ImageView imgPost;
    TextView txtLiked;
    LinearLayout linLike,linComment,linShare;
    TextView txtLike,txtComment,txtShare;

    DataProcessor dataProcessor;
    ContentLoadingProgressBar progress;
    RelativeLayout relapd;

    int type=0;
    Integer userId,postId;
    Integer likeCount=0,commentCount=0;

    String postType;

    ImageButton imgBtnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        initializeView();
        dataProcessor=DataProcessor.getInstance(this);

        postCommentAdapter=new PostCommentAdapter(this);
        postLikeAdapter=new PostLikeAdapter(this,this,dataProcessor.getInteger(DataProcessor.USER_ID));

        postId=getIntent().getIntExtra("postId",-1);

        relapd.setVisibility(View.GONE);

        txtCommentsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;
                relFooter.setVisibility(View.VISIBLE);
                recyclerLikeComment.setAdapter(postCommentAdapter);
                getCommentsList();
            }
        });

        txtLikesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                relFooter.setVisibility(View.GONE);
                recyclerLikeComment.setAdapter(postLikeAdapter);
                getLikesList();
            }
        });

        relUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(postType,"USERPOST")){
                    Intent intent=new Intent(PostDetail.this,UserProfile.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(PostDetail.this,TeamDetails.class);
                    intent.putExtra("teamId",userId);
                    startActivity(intent);
                }

            }
        });

        relBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(textComment.getText().toString())){
                    addComment();
                }
            }
        });

        linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLink("POST",String.valueOf(postId));
            }
        });

        imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopup();
            }
        });

        getPostDetails();

        txtCommentsTab.callOnClick();
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
                "&sd=Post from Skilled Mate" +
                "&si=http://kvgames.com/logo.jpg";

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinkText))
                .buildShortDynamicLink()
                .addOnCompleteListener(PostDetail.this, new OnCompleteListener<ShortDynamicLink>() {
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

    private void openPopup(){
        PopupMenu popupMenu=new PopupMenu(this,imgBtnMore);
        popupMenu.inflate(R.menu.post_more_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.itemEditPost){

                    return true;
                }
                return false;
            }
        });
    }

    private void addComment(){
        PostCommentModel postCommentModel=new PostCommentModel();
        postCommentModel.setCommenter_id(dataProcessor.getInteger(DataProcessor.USER_ID));
        postCommentModel.setPost_id(postId);
        postCommentModel.setComment(textComment.getText().toString());
        textComment.setEnabled(false);
        relBtnComment.setEnabled(false);

        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.addPostComment(postCommentModel).enqueue(new Callback<PostCommentResponse>() {
            @Override
            public void onResponse(Call<PostCommentResponse> call, Response<PostCommentResponse> response) {
                if (response.isSuccessful()){
                    postCommentAdapter.insetCommentAtTop(response.body().getData());
                    textComment.setText("");
                    textComment.setEnabled(true);
                    relBtnComment.setEnabled(true);
                    commentCount++;
                    txtComment.setText(commentCount+" Comments");
                    txtCommentsTab.setText(commentCount+" Comments");
                }else {
                    txtComment.setEnabled(true);
                    relBtnComment.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponse> call, Throwable t) {
                textComment.setEnabled(true);
                relBtnComment.setEnabled(true);
            }
        });
    }

    private void getPostDetails(){
        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.getPostDetails(postId).enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {
                if (response.isSuccessful()){
                    getPostData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {

            }
        });
    }

    private void getCommentsList(){
        PostInterface postInterface= Database.getClient(this).create(PostInterface.class);
        postInterface.getPostComments(postId).enqueue(new Callback<PostCommentListResponse>() {
            @Override
            public void onResponse(Call<PostCommentListResponse> call, Response<PostCommentListResponse> response) {
                if (response.isSuccessful()){
                    postCommentAdapter.setPostCommentList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PostCommentListResponse> call, Throwable t) {

            }
        });
    }

    private void getLikesList(){
        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.getPostLikes(postId).enqueue(new Callback<PostLikeListResponse>() {
            @Override
            public void onResponse(Call<PostLikeListResponse> call, Response<PostLikeListResponse> response) {
                if (response.isSuccessful()){
                    postLikeAdapter.setPostLike(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PostLikeListResponse> call, Throwable t) {

            }
        });
    }

    private void getPostData(PostData postData){
        String userImg,userName,userCollege;
        if (TextUtils.equals(postData.getPostType(),"USERPOST")){
            userImg=postData.getUser().getUserImage();
            userName=postData.getUser().getName();
            userCollege=postData.getUser().getCollegeName();
            userId=postData.getUserId();
            if (userId==dataProcessor.getInteger(DataProcessor.USER_ID)){
                imgBtnMore.setVisibility(View.VISIBLE);
            }else {
                imgBtnMore.setVisibility(View.GONE);
            }
        }else{
            userImg=postData.getTeam().getTeam_icon();
            userName=postData.getTeam().getTeam_title();
            userCollege=postData.getTeam().getTeam_tagline();
            userId=postData.getTeam().getId();
            imgBtnMore.setVisibility(View.GONE);
        }

        String postTime=postData.getCreatedAt();
        String postContent=postData.getPostContent();
        String postImg=postData.getPostImage();
        Integer eventId=postData.getEventId();
        likeCount=postData.getLikesCount();
        commentCount=postData.getCommentsCount();

        postType=postData.getPostType();
        String isLiked=postData.getIsLiked();

        if (userImg!=null)
            Glide.with(this).load(userImg).into(img_user);
        if (postImg!=null)
            Glide.with(this).load(postImg).into(imgPost);
        else imgPost.setVisibility(View.GONE);

        txtName.setText(userName);
        txtCollegeName.setText(userCollege);
        txtPostTime.setText(getTimeStamp(postTime));
        txtPostText.setText(postContent);
        txtLike.setText(likeCount+" Likes");
        txtComment.setText(commentCount+" Comments");
        txtLikesTab.setText(likeCount+" Likes");
        txtCommentsTab.setText(commentCount+" Comments");

        if (TextUtils.equals(isLiked,"true")){
            txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_enabled,0,0,0);
        }else {
            txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24,0,0,0);
        }

        relapd.setVisibility(View.VISIBLE);
        progress.hide();
    }


    private String getTimeStamp(String  dateCreated){
        int difference = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        Date today = calendar.getTime();
        sdf.format(today);
        Date timeStamp = today;
        final String photoTimeStamp = dateCreated;
        try{
            timeStamp = sdf.parse(photoTimeStamp);
            //Log.d(TAG, "getTimeStamp: timestamp " + timeStamp);
            difference = Math.round((today.getTime() - timeStamp.getTime())/1000 / 60);

        } catch (ParseException e){

            difference = 0;
        }
        if(difference < 1){
            return "Just Now";
        } else if(difference <= 59) {
            return (difference + " mins ago");
        } else if(difference >= 60 && difference < 1440) {
            return ((Math.round(difference / 60)) + " hours ago");
        } else  {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yy 'at' K:mm a");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        }
    }



    private void initializeView(){
        txtCommentsTab=findViewById(R.id.txtCommentsTab);
        txtLikesTab=findViewById(R.id.txtLikesTab);
        recyclerLikeComment=findViewById(R.id.recyclerLikeComment);
        recyclerLikeComment.setLayoutManager(new LinearLayoutManager(this));
        textComment=findViewById(R.id.textComment);
        relBtnComment=findViewById(R.id.relBtnComment);
        relFooter=findViewById(R.id.relFooter);
        relUser=findViewById(R.id.relUser);
        imgBtnMore=findViewById(R.id.imgBtnMore);

        img_user=findViewById(R.id.img_user);
        txtName=findViewById(R.id.txtName);
        txtCollegeName=findViewById(R.id.txtCollegeName);
        txtPostTime=findViewById(R.id.txtPostTime);
        txtPostText=findViewById(R.id.txtPostText);
        imgPost=findViewById(R.id.imgPost);
        txtLiked=findViewById(R.id.txtLiked);
        linLike=findViewById(R.id.linLike);
        linComment=findViewById(R.id.linComment);
        linShare=findViewById(R.id.linShare);
        txtLike=findViewById(R.id.txtLike);
        txtComment=findViewById(R.id.txtComment);
        txtShare=findViewById(R.id.txtShare);
        relapd=findViewById(R.id.relapd);
        progress=findViewById(R.id.progress);
    }

    @Override
    public void onUserClick(Integer userId) {
        Intent intent=new Intent(PostDetail.this,UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    @Override
    public void onMessageClick(Integer userId,String name,String image) {
        Intent intent=new Intent(PostDetail.this,ChatScreen.class);
        intent.putExtra("conversationId",-1);
        intent.putExtra("receiverId",userId);
        intent.putExtra("receiverName",name);
        intent.putExtra("receiverImage",image);
        intent.putExtra("convType","MONO");
        startActivity(intent);
    }
}