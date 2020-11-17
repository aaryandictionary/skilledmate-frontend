package com.app.miniproject.Database;

import com.app.miniproject.Database.Event.ApplyEventModel;
import com.app.miniproject.Database.Event.ApplyEventResponse;
import com.app.miniproject.Database.Event.EventDetailsResponse;
import com.app.miniproject.Database.Event.EventListResponse;
import com.app.miniproject.Database.Tag.TagResponse;
import com.app.miniproject.Database.Tag.UserTagModel;
import com.app.miniproject.Database.TeamEvent.TeamEventResponse;
import com.app.miniproject.Database.Teams.FollowTeamModel;
import com.app.miniproject.Database.Teams.FollowTeamResponse;
import com.app.miniproject.Database.Teams.MyTeamResponse;
import com.app.miniproject.Database.Teams.SetRoleModel;
import com.app.miniproject.Database.Teams.SetRoleResponse;
import com.app.miniproject.Database.Teams.TeamAdminResponse;
import com.app.miniproject.Database.Teams.TeamDetailsResponse;
import com.app.miniproject.Database.Teams.TeamResponse;
import com.app.miniproject.Database.Teams.UserTeamResponse;
import com.app.miniproject.Database.UpdateUser.UpdateUserResponse;
import com.app.miniproject.Database.UserDetail.UserResponse;

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

public interface TeamInterface {

    @Multipart
    @POST("skilledmate/public/api/createTeam")
    Call<TeamResponse> createTeam(
            @Part("team_title") RequestBody team_title,
            @Part("user_id")RequestBody user_id,
            @Part("team_tagline")RequestBody team_tagline,
            @Part("team_description")RequestBody team_description,
            @Part("role_title")RequestBody role_title,
            @Part("tags[]")ArrayList<Integer>tags,
            @Part MultipartBody.Part team_icon
            );

    @Multipart
    @POST("skilledmate/public/api/createTeam")
    Call<TeamResponse> createTeam(
            @Part("team_title") RequestBody team_title,
            @Part("user_id")RequestBody user_id,
            @Part("team_tagline")RequestBody team_tagline,
            @Part("team_description")RequestBody team_description,
            @Part("role_title")RequestBody role_title,
            @Part("tags[]")ArrayList<Integer>tags

    );


    @Multipart
    @POST("skilledmate/public/api/createEvent")
    Call<TeamEventResponse> createTeamEvent(
            @Part("team_id") RequestBody team_id,
            @Part("user_id")RequestBody user_id,
            @Part("event_title")RequestBody event_title,
            @Part("event_details")RequestBody event_details,
            @Part("event_time")RequestBody event_time,
            @Part("event_privacy")RequestBody event_privacy,
            @Part("event_deadline")RequestBody event_deadline,
            @Part("event_organiser")RequestBody event_organiser,
            @Part("tags[]")ArrayList<Integer>tags
    );

    @Multipart
    @POST("skilledmate/public/api/createEvent")
    Call<TeamEventResponse> createTeamEvent(
            @Part("team_id") RequestBody team_id,
            @Part("user_id")RequestBody user_id,
            @Part("event_title")RequestBody event_title,
            @Part("event_details")RequestBody event_details,
            @Part("event_time")RequestBody event_time,
            @Part("event_privacy")RequestBody event_privacy,
            @Part("event_deadline")RequestBody event_deadline,
            @Part("event_organiser")RequestBody event_organiser,
            @Part("tags[]")ArrayList<Integer>tags,
            @Part MultipartBody.Part event_image
    );

    @Multipart
    @POST("skilledmate/public/api/updateTeam")
    Call<TeamResponse> updateTeam(
            @Path("id")RequestBody id,
            @Part("team_title") RequestBody team_title,
            @Part("user_id")RequestBody user_id,
            @Part("team_tagline")RequestBody team_tagline,
            @Part("team_description")RequestBody team_description,
            @Part("conversation_id")RequestBody conversation_id,
            @Part("tags[]")ArrayList<Integer>tags,
            @Part MultipartBody.Part team_icon
    );

    @Multipart
    @POST("skilledmate/public/api/updateTeam")
    Call<TeamResponse> updateTeam(
            @Path("id")RequestBody id,
            @Part("team_title") RequestBody team_title,
            @Part("user_id")RequestBody user_id,
            @Part("team_tagline")RequestBody team_tagline,
            @Part("team_description")RequestBody team_description,
            @Part("conversation_id")RequestBody conversation_id,
            @Part("tags[]")ArrayList<Integer>tags
    );


    @Multipart
    @POST("skilledmate/public/api/updateEvent")
    Call<TeamEventResponse> updateTeamEvent(
            @Part("id")RequestBody id,
            @Part("conversation_id")RequestBody conversation_id,
            @Part("team_id") RequestBody team_id,
            @Part("user_id")RequestBody user_id,
            @Part("event_title")RequestBody event_title,
            @Part("event_details")RequestBody event_details,
            @Part("event_time")RequestBody event_time,
            @Part("event_privacy")RequestBody event_privacy,
            @Part("event_deadline")RequestBody event_deadline,
            @Part("event_organiser")RequestBody event_organiser,
            @Part("tags[]")ArrayList<Integer>tags,
            @Part MultipartBody.Part event_image
    );

    @Multipart
    @POST("skilledmate/public/api/updateEvent")
    Call<TeamEventResponse> updateTeamEvent(
            @Part("id")RequestBody id,
            @Part("conversation_id")RequestBody conversation_id,
            @Part("team_id") RequestBody team_id,
            @Part("user_id")RequestBody user_id,
            @Part("event_title")RequestBody event_title,
            @Part("event_details")RequestBody event_details,
            @Part("event_time")RequestBody event_time,
            @Part("event_privacy")RequestBody event_privacy,
            @Part("event_deadline")RequestBody event_deadline,
            @Part("event_organiser")RequestBody event_organiser,
            @Part("tags[]")ArrayList<Integer>tags
    );

    @POST("skilledmate/public/api/followTeam")
    Call<FollowTeamResponse> followTeam(@Body FollowTeamModel followTeamModel);

    @POST("skilledmate/public/api/setRole")
    Call<SetRoleResponse> setTeamRole(@Body SetRoleModel setRoleModel);

    @GET("skilledmate/public/api/getTeamDetails/{teamId}/{userId}")
    Call<TeamDetailsResponse> getTeamDetails(@Path("teamId") Integer teamId,@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getTeamAdmins/{teamId}")
    Call<TeamAdminResponse> getTeamAdmins(@Path("teamId") Integer teamId);

    @GET("skilledmate/public/api/getTeamMembers/{teamId}")
    Call<TeamAdminResponse> getTeamMembers(@Path("teamId") Integer teamId);

    @GET("skilledmate/public/api/getTeamFollowers/{teamId}")
    Call<TeamAdminResponse> getTeamFollowers(@Path("teamId") Integer teamId);

    @GET("skilledmate/public/api/getMyTeams/{userId}")
    Call<MyTeamResponse> getMyTeams(@Path("userId") Integer userId);

    @GET("skilledmate/public/api/getMyCreatedTeams/{userId}")
    Call<UserTeamResponse>getUserTeam(@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getEventsForMe/{userId}")
    Call<EventListResponse>getEventsForMe(@Path("userId")Integer userId);

    @GET("skilledmate/public/api/getEventDetails/{eventId}/{userId}")
    Call<EventDetailsResponse>getEventDetails(@Path("eventId")Integer eventId,@Path("userId")Integer userId);

    @POST("skilledmate/public/api/joinEvent")
    Call<ApplyEventResponse>applyEvent(@Body ApplyEventModel applyEventModel);

    @GET("skilledmate/public/api/checkParticipantsList/{eventId}/{teamId}")
    Call<TeamAdminResponse> getEventParticipants(@Path("eventId")Integer eventId,@Path("teamId") Integer teamId);

}
