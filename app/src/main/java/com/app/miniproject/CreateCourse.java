package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.Course.CreateCourseResponse;
import com.app.miniproject.Database.CourseInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
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

public class CreateCourse extends AppCompatActivity {

    EditText textCourseTitle,textCourseDescription,textCoursePrice,textCourseDuration;
    TextView btnCourseNext;
    RelativeLayout relCourseImage;
    ImageView imgCourse;
    File selectedFile = null;

    ChipGroup chipGroup;
    DataProcessor dataProcessor;

    ProgressDialog progressDialog;
    ArrayList<Integer> tags=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        btnCourseNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    createCourse();
                }
            }
        });

        relCourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(CreateCourse.this);
            }
        });

        getTagList();
    }

    private void createCourse(){
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        CourseInterface courseInterface= Database.getClient(this).create(CourseInterface.class);

        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody CourseTitle = RequestBody.create(MediaType.parse("text/plain"), textCourseTitle.getText().toString());
        final RequestBody CourseDetails = RequestBody.create(MediaType.parse("text/plain"), textCourseDescription.getText().toString());
        RequestBody CourseDuration = RequestBody.create(MediaType.parse("text/plain"), textCourseDuration.getText().toString());
        RequestBody CourseFee = RequestBody.create(MediaType.parse("text/plain"), textCoursePrice.getText().toString());
        RequestBody Active = RequestBody.create(MediaType.parse("text/plain"), "1");

        Call<CreateCourseResponse> call;


        if (selectedFile != null) {
            MultipartBody.Part ImgCourse = MultipartBody.Part.createFormData("course_image",
                    selectedFile.getName(), RequestBody.create(MediaType.parse("image/*"), selectedFile));
            call = courseInterface.createCourse(CourseTitle,UserId,CourseDetails,CourseDuration,CourseFee,Active,ImgCourse,tags);
        } else {
            call = courseInterface.createCourse(CourseTitle,UserId,CourseDetails,CourseDuration,CourseFee,Active,tags);
        }

        call.enqueue(new Callback<CreateCourseResponse>() {
            @Override
            public void onResponse(Call<CreateCourseResponse> call, Response<CreateCourseResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CreateCourse.this,"Course created successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(CreateCourse.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CreateCourseResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateCourse.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateFields(){
        if (TextUtils.isEmpty(textCourseTitle.getText().toString())){
            Toast.makeText(CreateCourse.this,"Enter course title",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(textCourseDescription.getText().toString())){
            Toast.makeText(CreateCourse.this,"Enter course Description",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(textCourseDuration.getText().toString())){
            Toast.makeText(CreateCourse.this,"Enter course duration",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert data != null;
//                imgContent.setImageURI(null);
                imgCourse.setImageURI(result.getUri());
                selectedFile = new File(Objects.requireNonNull(result.getUri().getPath()));

//                relImage.setVisibility(View.VISIBLE);
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


    private void renderTags(List<SkillsListData> skillsListData){
        for (SkillsListData skills:skillsListData){
            final Chip chip=new Chip(CreateCourse.this);
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

    private void initializeView(){
        textCourseTitle=findViewById(R.id.textCourseTitle);
        textCourseDescription=findViewById(R.id.textCourseDescription);
        textCoursePrice=findViewById(R.id.textCoursePrice);
        textCourseDuration=findViewById(R.id.textCourseDuration);
        btnCourseNext=findViewById(R.id.btnCourseNext);
        relCourseImage=findViewById(R.id.relCourseImage);
        imgCourse=findViewById(R.id.imgCourse);
        chipGroup=findViewById(R.id.chipGroup);
    }
}