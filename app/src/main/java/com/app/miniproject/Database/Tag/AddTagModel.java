package com.app.miniproject.Database.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTagModel {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("tags")
    @Expose
    private Integer[]tags;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer[] getTags() {
        return tags;
    }

    public void setTags(Integer[] tags) {
        this.tags = tags;
    }
}
