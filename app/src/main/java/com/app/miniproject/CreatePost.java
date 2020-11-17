package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Post.CreatePostModel;
import com.app.miniproject.Database.Post.CreatePostResponse;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePost extends AppCompatActivity {

    EditText textContent;
    ImageButton imgBtnAddImage;
    ImageView imgContent;
    RelativeLayout relRemoveImage, relImage;
    TextView txtPost;
    File selectedFile = null;
    ArrayList<Integer>tags=new ArrayList<>();

    ProgressDialog progressDialog;

    DataProcessor dataProcessor;

    ChipGroup chipGroup;

    Integer teamId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        initializeView();

        progressDialog = new ProgressDialog(this);
        dataProcessor = DataProcessor.getInstance(this);

        if (getIntent().hasExtra("teamId")){
            teamId=getIntent().getIntExtra("teamId",-1);
        }

        imgBtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(CreatePost.this);
            }
        });

        relRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relImage.setVisibility(View.GONE);
            }
        });

        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                if (!TextUtils.isEmpty(textContent.getText().toString()) || selectedFile != null) {
                    createPost();
                }

            }
        });

        getTagList();
    }

    private void createPost() {
        PostInterface postInterface = Database.getClient(this).create(PostInterface.class);

        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody TeamId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(teamId));
        RequestBody EventID,PostType;
        if (teamId!=-1){
             EventID = RequestBody.create(MediaType.parse("text/plain"), "-1");
             PostType = RequestBody.create(MediaType.parse("text/plain"), "TEAMPOST");
        }else {
             EventID = RequestBody.create(MediaType.parse("text/plain"), "-1");
             PostType = RequestBody.create(MediaType.parse("text/plain"), "USERPOST");
        }
        RequestBody PostContent = RequestBody.create(MediaType.parse("text/plain"), textContent.getText().toString());

        Call<CreatePostResponse> call;

//        RequestBody Tag = RequestBody.create(MediaType.parse("text/plain"), textContent.getText().toString());


        if (selectedFile != null) {
            MultipartBody.Part imgTitle = MultipartBody.Part.createFormData("post_image",
                    selectedFile.getName(), RequestBody.create(MediaType.parse("image/*"), selectedFile));
            call = postInterface.createPost(PostType, Id, EventID, imgTitle,TeamId,tags, PostContent);
        } else {
            call = postInterface.createPost(PostType, Id, EventID,TeamId,tags, PostContent);
        }
        call.enqueue(new Callback<CreatePostResponse>() {
            @Override
            public void onResponse(Call<CreatePostResponse> call, Response<CreatePostResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePost.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePost.this,"Something went wrong. Try again later"+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreatePostResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreatePost.this,"Something went wrong. Try again later"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert data != null;
//                imgContent.setImageURI(null);
                imgContent.setImageURI(result.getUri());
                selectedFile = new File(Objects.requireNonNull(result.getUri().getPath()));

                relImage.setVisibility(View.VISIBLE);
//                Glide.with(CreatePost.this).load(data.getData()).into(imgContent);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "File selection Failed", Toast.LENGTH_SHORT).show();
            }
        }
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


    private void renderTags(List<SkillsListData>skillsListData){
        for (SkillsListData skills:skillsListData){
            final Chip chip=new Chip(CreatePost.this);
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

    private void initializeView() {
        textContent = findViewById(R.id.textContent);
        imgBtnAddImage = findViewById(R.id.imgBtnAddImage);
        imgContent = findViewById(R.id.imgContent);
        relRemoveImage = findViewById(R.id.relRemoveImage);
        relImage = findViewById(R.id.relImage);
        txtPost = findViewById(R.id.txtPost);
        chipGroup = findViewById(R.id.chipGroup);
    }
}