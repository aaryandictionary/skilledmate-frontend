package com.app.miniproject.Database.UserDetail;

import com.app.miniproject.Database.CollegeList.CollegeListData;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("college_id")
    @Expose
    private Integer collegeId;
    @SerializedName("college")
    @Expose
    private CollegeListData college;
    @SerializedName("tags")
    @Expose
    private List<SkillsListData> skills = null;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public CollegeListData getCollege() {
        return college;
    }

    public void setCollege(CollegeListData college) {
        this.college = college;
    }

    public List<SkillsListData> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillsListData> skills) {
        this.skills = skills;
    }
}
