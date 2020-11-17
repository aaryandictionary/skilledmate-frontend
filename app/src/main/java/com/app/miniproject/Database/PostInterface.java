package com.app.miniproject.Database;

import com.app.miniproject.Database.Post.CreatePostModel;
import com.app.miniproject.Database.Post.CreatePostResponse;
import com.app.miniproject.Database.Post.PostDataResponse;
import com.app.miniproject.Database.Post.SinglePostResponse;
import com.app.miniproject.Database.PostComment.PostCommentListResponse;
import com.app.miniproject.Database.PostComment.PostCommentModel;
import com.app.miniproject.Database.PostComment.PostCommentResponse;
import com.app.miniproject.Database.PostLike.PostLikeListResponse;
import com.app.miniproject.Database.PostLike.PostLikeModel;
import com.app.miniproject.Database.PostLike.PostLikeResponse;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.Teams.SetRoleModel;
import com.app.miniproject.Database.Teams.SetRoleResponse;
import com.app.miniproject.Database.Teams.TeamDetailsResponse;
import com.app.miniproject.Database.Teams.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PostInterface {

    @Multipart
    @POST("skilledmate/public/api/post")
    Call<CreatePostResponse> createPost(
            @Part("post_type") RequestBody post_type,
            @Part("user_id")RequestBody user_id,
            @Part("event_id")RequestBody event_id,
            @Part MultipartBody.Part post_image,
            @Part("team_id")RequestBody team_id,
            @Part("tags[]") ArrayList<Integer> tags,
            @Part("post_content")RequestBody postContent
    );

    @Multipart
    @POST("skilledmate/public/api/post")
    Call<CreatePostResponse> createPost(
            @Part("post_type") RequestBody post_type,
            @Part("user_id")RequestBody user_id,
            @Part("event_id")RequestBody event_id,
            @Part("team_id")RequestBody team_id,
            @Part("tags[]") ArrayList<Integer> tags,
            @Part("post_content")RequestBody postContent
    );


    @Multipart
    @POST("skilledmate/public/api/updatePost")
    Call<CreatePostResponse> updatePost(
            @Part("id") RequestBody id,
            @Part("user_id")RequestBody user_id,
            @Part("event_id")RequestBody event_id,
            @Part MultipartBody.Part post_image,
            @Part("post_type")RequestBody post_type,
            @Part("tags[]") ArrayList<Integer> tags,
            @Part("post_content")RequestBody postContent
    );

    @Multipart
    @POST("skilledmate/public/api/updatePost")
    Call<CreatePostResponse> updatePost(
            @Part("id") RequestBody id,
            @Part("user_id")RequestBody user_id,
            @Part("event_id")RequestBody event_id,
            @Part("post_type")RequestBody post_type,
            @Part("tags[]") ArrayList<Integer> tags,
            @Part("post_content")RequestBody postContent
    );

    @GET("skilledmate/public/api/getTeamPosts/{teamId}/{userId}")
    Call<PostDataResponse> getTeamPosts(@Path("teamId") Integer teamId,@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getPostDetails/{postId}")
    Call<SinglePostResponse> getPostDetails(@Path("postId") Integer postId);

    @GET("skilledmate/public/api/getNontags/{type}/{id}")
    Call<SkillsListResponse> getNontags(@Path("type") String type, @Path("id")Integer id);

    @GET("skilledmate/public/api/posts/{user_id}")
    Call<PostDataResponse> getAllPosts(@Path("user_id") Integer user_id);

    @POST("skilledmate/public/api/createlike")
    Call<PostLikeResponse> swapPostLike(@Body PostLikeModel postLikeModel);

    @POST("skilledmate/public/api/createcomment")
    Call<PostCommentResponse> addPostComment(@Body PostCommentModel postCommentModel);

    @GET("skilledmate/public/api/getPostLikes/{postId}")
    Call<PostLikeListResponse> getPostLikes(@Path("postId") Integer postId);

    @GET("skilledmate/public/api/getPostComments/{postId}")
    Call<PostCommentListResponse> getPostComments(@Path("postId") Integer postId);
}
