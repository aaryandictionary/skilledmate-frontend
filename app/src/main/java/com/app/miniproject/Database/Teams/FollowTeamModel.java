package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowTeamModel {
    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("team_id")
    @Expose
    private Integer team_id;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }
}
