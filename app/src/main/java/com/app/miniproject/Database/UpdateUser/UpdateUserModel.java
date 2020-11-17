package com.app.miniproject.Database.UpdateUser;

import com.app.miniproject.Database.Register.SkillModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateUserModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_image")
    @Expose
    private String user_image;
    @SerializedName("college_id")
    @Expose
    private Integer collegeId;
    @SerializedName("skills")
    @Expose
    private List<SkillModel> skills = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public List<SkillModel> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillModel> skills) {
        this.skills = skills;
    }
}
