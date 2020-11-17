package com.app.miniproject.Database.Tag;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTagModel {
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("tag_id")
    @Expose
    private Integer tag_id;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTag_id() {
        return tag_id;
    }

    public void setTag_id(Integer tag_id) {
        this.tag_id = tag_id;
    }
}
