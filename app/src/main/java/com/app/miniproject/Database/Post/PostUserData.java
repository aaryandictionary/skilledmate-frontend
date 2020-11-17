package com.app.miniproject.Database.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostUserData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("college_name")
    @Expose
    private String collegeName;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
