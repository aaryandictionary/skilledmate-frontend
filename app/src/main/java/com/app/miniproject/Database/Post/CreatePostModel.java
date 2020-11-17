package com.app.miniproject.Database.Post;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreatePostModel {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;

    @SerializedName("post_image")
    @Expose
    private String post_image;
    @SerializedName("tags")
    @Expose
    private List<Integer> tags;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }
}
