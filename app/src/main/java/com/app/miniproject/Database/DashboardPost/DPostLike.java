package com.app.miniproject.Database.DashboardPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DPostLike {
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("liker_id")
    @Expose
    private Integer likerId;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getLikerId() {
        return likerId;
    }

    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }
}
