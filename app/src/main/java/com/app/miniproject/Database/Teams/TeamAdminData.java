package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamAdminData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("role_title")
    @Expose
    private String roleTitle;
    @SerializedName("college_name")
    @Expose
    private String collegeName;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
