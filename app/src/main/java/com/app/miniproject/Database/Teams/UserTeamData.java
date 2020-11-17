package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTeamData {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("team_title")
    @Expose
    private String team_title;

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

    public String getTeam_icon() {
        return team_icon;
    }

    public void setTeam_icon(String team_icon) {
        this.team_icon = team_icon;
    }
}
