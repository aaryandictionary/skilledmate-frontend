package com.app.miniproject.Database;

import com.app.miniproject.Database.Course.CourseContentResponse;
import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.Course.CourseResponse;
import com.app.miniproject.Database.Course.CreateContentModel;
import com.app.miniproject.Database.Course.CreateContentResponse;
import com.app.miniproject.Database.Course.CreateCourseResponse;
import com.app.miniproject.Database.PostComment.PostCommentModel;
import com.app.miniproject.Database.PostComment.PostCommentResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CourseInterface {

    @Multipart
    @POST("skilledmate/public/api/createCourse")
    Call<CreateCourseResponse> createCourse(
            @Part("course_title") RequestBody course_title,
            @Part("user_id") RequestBody user_id,
            @Part("course_details") RequestBody course_details,
            @Part("course_duration") RequestBody course_duration,
            @Part("course_fee")RequestBody course_fee,
            @Part("active")RequestBody active,
            @Part MultipartBody.Part course_image,
            @Part("tags[]") ArrayList<Integer> tags
    );

    @Multipart
    @POST("skilledmate/public/api/createCourse")
    Call<CreateCourseResponse> createCourse(
            @Part("course_title") RequestBody course_title,
            @Part("user_id") RequestBody user_id,
            @Part("course_details") RequestBody course_details,
            @Part("course_duration") RequestBody course_duration,
            @Part("course_fee")RequestBody course_fee,
            @Part("active")RequestBody active,
            @Part("tags[]") ArrayList<Integer> tags
    );

    @Multipart
    @POST("skilledmate/public/api/updateCourse")
    Call<CreateCourseResponse> updateCourse(
            @Part("course_title") RequestBody course_title,
            @Part("user_id") RequestBody user_id,
            @Part("course_details") RequestBody course_details,
            @Part("course_duration") RequestBody course_duration,
            @Part("course_fee")RequestBody course_fee,
            @Part MultipartBody.Part course_image,
            @Part("tags[]") ArrayList<Integer> tags
    );

    @GET("skilledmate/public/api/getMyCourses/{userId}")
    Call<CourseListResponse>getMyCourses(@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getCourseSuggestion/{userId}")
    Call<CourseListResponse>getCourseSuggestions(@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getCourseDetails/{courseId}")
    Call<CourseResponse>getCourseDetails(@Path("courseId")Integer courseId);

    @GET("skilledmate/public/api/getCourseContent/{courseId}")
    Call<CourseContentResponse>getCourseContent(@Path("courseId")Integer courseId);

    @POST("skilledmate/public/api/createCourseContent")
    Call<CreateContentResponse> createCourseContent(@Body CreateContentModel createContentModel);

    @POST("skilledmate/public/api/updateCourseContent")
    Call<CreateContentResponse> updateCourseContent(@Body CreateContentModel createContentModel);
}
