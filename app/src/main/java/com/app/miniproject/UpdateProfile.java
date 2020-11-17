package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Register.RegisterUserData;
import com.app.miniproject.Database.UpdateUser.UpdateUserResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {

    ImageButton imgBtnBack;
    CircleImageView circularImageProfile;
    EditText textName,textEmail,textPhone;
    AutoCompleteTextView autoCompleteCollege;
    Button btnNext;
    File selectedFile = null;

    DataProcessor dataProcessor;
    ProgressDialog progressDialog;

    Integer collegeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        textName.setText(dataProcessor.getString(DataProcessor.NAME));
        textEmail.setText(dataProcessor.getString(DataProcessor.EMAIL));
        Glide.with(this).load(dataProcessor.getString(DataProcessor.USER_IMAGE)).into(circularImageProfile);
        autoCompleteCollege.setText(dataProcessor.getString(DataProcessor.COLLEGE));
        collegeId=dataProcessor.getInteger(DataProcessor.COLLEGE_ID);
        textPhone.setText("+91"+dataProcessor.getString(DataProcessor.MOBILE));

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    updateProfile();
                }
            }
        });

        circularImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(UpdateProfile.this);
            }
        });
    }

    private void updateProfile(){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);

        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody CollegeId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(collegeId));
        RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), textName.getText().toString());
        RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), textEmail.getText().toString());

        Call<UpdateUserResponse> call;

        if (selectedFile!=null){
            MultipartBody.Part ImgUser = MultipartBody.Part.createFormData("user_image",
                    selectedFile.getName(), RequestBody.create(MediaType.parse("image/*"), selectedFile));
            call=userInterface.updateUser(UserId,Name,Email,CollegeId,ImgUser);
        }else {
            call=userInterface.updateUser(UserId,Name,Email,CollegeId);
        }

        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.isSuccessful()){
                    saveData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {

            }
        });
    }

    private void saveData(RegisterUserData registerUserData){
        dataProcessor.saveString(DataProcessor.NAME,registerUserData.getName());
        dataProcessor.saveString(DataProcessor.EMAIL,registerUserData.getEmail());
        dataProcessor.saveString(DataProcessor.COLLEGE,autoCompleteCollege.getText().toString());
        dataProcessor.saveInteger(DataProcessor.COLLEGE_ID,collegeId);
        dataProcessor.saveString(DataProcessor.USER_IMAGE,registerUserData.getUser_image());

        progressDialog.dismiss();
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert data != null;
//                imgContent.setImageURI(null);
                circularImageProfile.setImageURI(result.getUri());
                selectedFile = new File(Objects.requireNonNull(result.getUri().getPath()));

//                relImage.setVisibility(View.VISIBLE);
//                Glide.with(CreatePost.this).load(data.getData()).into(imgContent);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "File selection Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateFields(){
        if (TextUtils.isEmpty(textName.getText().toString())){
            Toast.makeText(UpdateProfile.this,"Enter Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(textEmail.getText().toString())){
            Toast.makeText(UpdateProfile.this,"Enter Email",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(autoCompleteCollege.getText().toString())||collegeId==-1){
            Toast.makeText(UpdateProfile.this,"Enter your college",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initializeView(){
        imgBtnBack=findViewById(R.id.imgBtnBack);
        circularImageProfile=findViewById(R.id.circularImageProfile);
        textName=findViewById(R.id.textName);
        textEmail=findViewById(R.id.textEmail);
        autoCompleteCollege=findViewById(R.id.autoCompleteCollege);
        btnNext=findViewById(R.id.btnNext);
        textPhone=findViewById(R.id.textPhone);
    }
}