package com.app.miniproject.Database.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostTeamData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_title")
    @Expose
    private String team_title;
    @SerializedName("team_tagline")
    @Expose
    private String team_tagline;
    @SerializedName("team_icon")
    @Expose
    private String team_icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeam_title() {
        return team_title;
    }

    public void setTeam_title(String team_title) {
        this.team_title = team_title;
    }

    public String getTeam_tagline() {
        return team_tagline;
    }

    public void setTeam_tagline(String team_tagline) {
        this.team_tagline = team_tagline;
    }

    public String getTeam_icon() {
        return team_icon;
    }

    public void setTeam_icon(String team_icon) {
        this.team_icon = team_icon;
    }
}
