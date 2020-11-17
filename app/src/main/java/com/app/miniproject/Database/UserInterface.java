package com.app.miniproject.Database;

import com.app.miniproject.Database.CollegeList.CollegeListResponse;
import com.app.miniproject.Database.Login.LoginModel;
import com.app.miniproject.Database.Login.LoginResponse;
import com.app.miniproject.Database.Register.RegisterModel;
import com.app.miniproject.Database.Register.RegisterResponse;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.Tag.AddTagModel;
import com.app.miniproject.Database.Tag.TagResponse;
import com.app.miniproject.Database.Tag.UserTagModel;
import com.app.miniproject.Database.Teams.MyTeamResponse;
import com.app.miniproject.Database.UpdateUser.UpdateUserModel;
import com.app.miniproject.Database.UpdateUser.UpdateUserResponse;
import com.app.miniproject.Database.UserDetail.UserByCollegeIdResponse;
import com.app.miniproject.Database.UserDetail.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserInterface {

    @POST("skilledmate/public/api/register")
    Call<RegisterResponse> registerUser(@Body RegisterModel registerModel);

    @POST("skilledmate/public/api/login")
    Call<LoginResponse> loginUser(@Body LoginModel loginModel);

    @POST("skilledmate/public/api/addSkill")
    Call<TagResponse> addUserSkill(@Body AddTagModel addTagModel);

    @Multipart
    @POST("skilledmate/public/api/updateuser")
    Call<UpdateUserResponse> updateUser(
            @Part("id")RequestBody id,
            @Part("name")RequestBody name,
            @Part("email")RequestBody email,
            @Part("college_id")RequestBody college_id,
            @Part MultipartBody.Part user_image
    );
    @Multipart
    @POST("skilledmate/public/api/updateuser")
    Call<UpdateUserResponse> updateUser(
            @Part("id")RequestBody id,
            @Part("name")RequestBody name,
            @Part("email")RequestBody email,
            @Part("college_id")RequestBody college_id
    );

    @GET("skilledmate/public/api/user/{user_id}")
    Call<UserResponse> getUserDetailsById(@Path("user_id") Integer userId);

    @GET("skilledmate/public/api/collegeslist")
    Call<CollegeListResponse> getCollegeList();

    @GET("skilledmate/public/api/removeSkill/{userId}/{skillId}")
    Call<CollegeListResponse> removeSkill(@Path("userId")Integer userId,@Path("skillId")Integer skillId);

    @GET("skilledmate/public/api/getAddableSkillList/{userId}")
    Call<SkillsListResponse> getAddableSkillList(@Path("userId")Integer userId);

    @GET("skilledmate/public/api/skillslist")
    Call<SkillsListResponse> getSkillsList();

    @GET("skilledmate/public/api/usersByCollege/{college_id}")
    Call<UserByCollegeIdResponse> getUsersByCollege(@Path("college_id") Integer collegeId);
}
