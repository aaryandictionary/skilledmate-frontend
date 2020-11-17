package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.CourseContentAdapter;
import com.app.miniproject.Database.Course.CourseContentResponse;
import com.app.miniproject.Database.Course.CourseData;
import com.app.miniproject.Database.Course.CourseResponse;
import com.app.miniproject.Database.Course.CreateContentModel;
import com.app.miniproject.Database.Course.CreateContentResponse;
import com.app.miniproject.Database.CourseInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetails extends AppCompatActivity {

    CircleImageView img_user;
    TextView txtName,txtCollegeName,txtPostTime;
    ImageView imgCourse;
    TextView txtCourseDuration,txtCoursePrice,txtCourseTitle,txtCourseDescription;
    RecyclerView recyclerCourseContent;
    RelativeLayout relMessage,relUser,relacd;
    FloatingActionButton floatingAddContent;
    AlertDialog alertDialog;
    ImageButton imgBtnEdit,imgBtnShare;

    CourseContentAdapter courseContentAdapter;

    ProgressDialog progressDialog;

    DataProcessor dataProcessor;
    ContentLoadingProgressBar progress;

    int courseId,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        courseId=getIntent().getIntExtra("courseId",-1);

        courseContentAdapter=new CourseContentAdapter(this);
        recyclerCourseContent.setAdapter(courseContentAdapter);

        floatingAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddContentDialog();
            }
        });
        relUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CourseDetails.this,UserProfile.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLink("COURSE",String.valueOf(courseId));
            }
        });

        relacd.setVisibility(View.GONE);

        getCourseDetails();
        getCourseContent();
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
                .addOnCompleteListener(CourseDetails.this, new OnCompleteListener<ShortDynamicLink>() {
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


    private void openAddContentDialog(){
         alertDialog=new AlertDialog.Builder(this).create();
         View view= LayoutInflater.from(this).inflate(R.layout.dialog_course_content,null,false);
         alertDialog.setView(view);

        final EditText textContentTitle=view.findViewById(R.id.textContentTitle);
        final EditText textContentDuration=view.findViewById(R.id.textContentDuration);
        final EditText textContentDescription=view.findViewById(R.id.textContentDescription);
        RelativeLayout relAdd=view.findViewById(R.id.relAdd);

        relAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(textContentTitle.getText().toString())&&!TextUtils.isEmpty(textContentDescription.getText().toString())&&!TextUtils.isEmpty(textContentDuration.getText().toString())){
                    createContent(textContentTitle.getText().toString(),textContentDuration.getText().toString(),textContentDescription.getText().toString());
                }else {
                    Toast.makeText(CourseDetails.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    private void createContent(String title,String duration,String details){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        CreateContentModel createContentModel=new CreateContentModel();
        createContentModel.setContent_details(details);
        createContentModel.setContent_time(duration);
        createContentModel.setCourse_id(courseId);
        createContentModel.setContent_title(title);
        CourseInterface courseInterface=Database.getClient(this).create(CourseInterface.class);
        courseInterface.createCourseContent(createContentModel).enqueue(new Callback<CreateContentResponse>() {
            @Override
            public void onResponse(Call<CreateContentResponse> call, Response<CreateContentResponse> response) {
                if (response.isSuccessful()){
                    courseContentAdapter.addCourseContent(response.body().getData());
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    Toast.makeText(CourseDetails.this,"Content created successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CourseDetails.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreateContentResponse> call, Throwable t) {
                Toast.makeText(CourseDetails.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getCourseContent(){
        CourseInterface courseInterface=Database.getClient(this).create(CourseInterface.class);
        courseInterface.getCourseContent(courseId).enqueue(new Callback<CourseContentResponse>() {
            @Override
            public void onResponse(Call<CourseContentResponse> call, Response<CourseContentResponse> response) {
                if (response.isSuccessful()){
                    courseContentAdapter.setCourseContent(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CourseContentResponse> call, Throwable t) {

            }
        });
    }

    private void getCourseDetails(){
        CourseInterface courseInterface= Database.getClient(this).create(CourseInterface.class);
        courseInterface.getCourseDetails(courseId).enqueue(new Callback<CourseResponse>() {
            @Override
            public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                if (response.isSuccessful()){
                    setCourseDetails(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CourseResponse> call, Throwable t) {

            }
        });
    }

    private void setCourseDetails(CourseData courseData){
        if (courseData.getUser_image()!=null)
            Glide.with(this).load(courseData.getUser_image()).into(img_user);
        if (courseData.getCourse_image()!=null)
            Glide.with(this).load(courseData.getCourse_image()).into(imgCourse);
        txtName.setText(courseData.getName());
        txtCollegeName.setText(courseData.getCollege_name());
        txtPostTime.setText(courseData.getCreated_at());
        txtCourseTitle.setText(courseData.getCollege_name());
        txtCourseDescription.setText(courseData.getCourse_details());
        txtCourseDuration.setText(courseData.getCourse_duration());
        txtCoursePrice.setText(courseData.getCourse_fee());
        userId=courseData.getUser_id();

        if (courseData.getUser_id()==dataProcessor.getInteger(DataProcessor.USER_ID)){
            floatingAddContent.setVisibility(View.VISIBLE);
            imgBtnEdit.setVisibility(View.VISIBLE);
            relMessage.setVisibility(View.GONE);
        }else {
            imgBtnEdit.setVisibility(View.GONE);
            floatingAddContent.setVisibility(View.GONE);
            relMessage.setVisibility(View.VISIBLE);
        }
        relacd.setVisibility(View.VISIBLE);
        progress.hide();
    }

    private void initializeView(){
        imgBtnEdit=findViewById(R.id.imgBtnEdit);
        imgBtnShare=findViewById(R.id.imgBtnShare);
        floatingAddContent=findViewById(R.id.floatingAddContent);
        img_user=findViewById(R.id.img_user);
        txtName=findViewById(R.id.txtName);
        txtCollegeName=findViewById(R.id.txtCollegeName);
        txtPostTime=findViewById(R.id.txtPostTime);
        imgCourse=findViewById(R.id.imgCourse);
        txtCourseDuration=findViewById(R.id.txtCourseDuration);
        txtCoursePrice=findViewById(R.id.txtCoursePrice);
        txtCourseTitle=findViewById(R.id.txtCourseTitle);
        txtCourseDescription=findViewById(R.id.txtCourseDescription);
        relMessage=findViewById(R.id.relMessage);
        relUser=findViewById(R.id.relUser);
        relacd=findViewById(R.id.relacd);
        progress=findViewById(R.id.progress);

        recyclerCourseContent=findViewById(R.id.recyclerCourseContent);
        recyclerCourseContent.setLayoutManager(new LinearLayoutManager(this));
    }
}