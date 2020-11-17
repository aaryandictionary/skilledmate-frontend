package com.app.miniproject.Database.PostLike;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLikeModel {
    @SerializedName("post_id")
    @Expose
    private Integer post_id;
    @SerializedName("liker_id")
    @Expose
    private Integer liker_id;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getLiker_id() {
        return liker_id;
    }

    public void setLiker_id(Integer liker_id) {
        this.liker_id = liker_id;
    }
}
