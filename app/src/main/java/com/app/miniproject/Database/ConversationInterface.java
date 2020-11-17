package com.app.miniproject.Database;

import com.app.miniproject.Database.Conversation.ConversationResponse;
import com.app.miniproject.Database.Course.CourseContentResponse;
import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.Course.CourseResponse;
import com.app.miniproject.Database.Course.CreateContentModel;
import com.app.miniproject.Database.Course.CreateContentResponse;
import com.app.miniproject.Database.Course.CreateCourseResponse;
import com.app.miniproject.Database.Message.MessageResponse;
import com.app.miniproject.Database.Message.SingleMessageResponse;
import com.app.miniproject.Database.Post.CreatePostResponse;

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

public interface ConversationInterface {

    @GET("skilledmate/public/api/getMyConversations/{userId}")
    Call<ConversationResponse>getMyConversations(@Path("userId") Integer userId);

    @GET("skilledmate/public/api/getChatMessages/{convId}/{userId}/{from}/{paginate}")
    Call<MessageResponse>getMessages(@Path("convId") Integer convId,@Path("userId")Integer userId,@Path("from")Integer from,@Path("paginate")Integer paginate);

    @Multipart
    @POST("skilledmate/public/api/sendMessage")
    Call<SingleMessageResponse> sendImageMessage(
            @Part("conversation_id") RequestBody conversation_id,
            @Part("sender_id")RequestBody sender_id,
            @Part("receiver_id")RequestBody receiver_id,
            @Part("text_msg")RequestBody textMsg,
            @Part("convType")RequestBody convType,
            @Part MultipartBody.Part content,
            @Part("content_type")RequestBody contentType
    );
}
