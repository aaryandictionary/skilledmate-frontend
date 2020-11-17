package com.app.miniproject.Database.DashboardPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DPostUser {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("college_id")
    @Expose
    private Integer collegeId;
    @SerializedName("user_image")
    @Expose
    private Object userImage;
    @SerializedName("college")
    @Expose
    private DPostCollege college;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public Object getUserImage() {
        return userImage;
    }

    public void setUserImage(Object userImage) {
        this.userImage = userImage;
    }

    public DPostCollege getCollege() {
        return college;
    }

    public void setCollege(DPostCollege college) {
        this.college = college;
    }
}
