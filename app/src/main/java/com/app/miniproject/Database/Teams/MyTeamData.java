package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTeamData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_title")
    @Expose
    private String teamTitle;
    @SerializedName("team_tagline")
    @Expose
    private String teamTagline;
    @SerializedName("team_icon")
    @Expose
    private String teamIcon;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("role_title")
    @Expose
    private String roleTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public String getTeamTagline() {
        return teamTagline;
    }

    public void setTeamTagline(String teamTagline) {
        this.teamTagline = teamTagline;
    }

    public String getTeamIcon() {
        return teamIcon;
    }

    public void setTeamIcon(String teamIcon) {
        this.teamIcon = teamIcon;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }
}
