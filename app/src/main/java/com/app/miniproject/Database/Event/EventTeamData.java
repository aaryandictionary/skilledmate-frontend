package com.app.miniproject.Database.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventTeamData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_title")
    @Expose
    private String teamTitle;
    @SerializedName("team_icon")
    @Expose
    private String teamIcon;
    @SerializedName("team_tagline")
    @Expose
    private String teamTagline;

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
}
