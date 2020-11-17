package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamDetailsData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_title")
    @Expose
    private String teamTitle;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("team_icon")
    @Expose
    private String teamIcon;
    @SerializedName("team_tagline")
    @Expose
    private String teamTagline;
    @SerializedName("team_description")
    @Expose
    private String teamDescription;
    @SerializedName("conversation_id")
    @Expose
    private Integer conversation_id;
    @SerializedName("followers_count")
    @Expose
    private Integer followers_count;
    @SerializedName("members_count")
    @Expose
    private Integer members_count;
    @SerializedName("my_team")
    @Expose
    private String my_team;

    public Integer getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getMy_team() {
        return my_team;
    }

    public void setMy_team(String my_team) {
        this.my_team = my_team;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Integer getMembers_count() {
        return members_count;
    }

    public void setMembers_count(Integer members_count) {
        this.members_count = members_count;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTeamIcon() {
        return teamIcon;
    }

    public void setTeamIcon(String teamIcon) {
        this.teamIcon = teamIcon;
    }

    public String getTeamTagline() {
        return teamTagline;
    }

    public void setTeamTagline(String teamTagline) {
        this.teamTagline = teamTagline;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }
}
