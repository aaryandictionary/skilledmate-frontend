package com.app.miniproject.Database.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventListData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_id")
    @Expose
    private Integer team_id;
    @SerializedName("team_title")
    @Expose
    private String team_title;
    @SerializedName("event_title")
    @Expose
    private String event_title;
    @SerializedName("event_time")
    @Expose
    private String event_time;
    @SerializedName("event_image")
    @Expose
    private String event_image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public String getTeam_title() {
        return team_title;
    }

    public void setTeam_title(String team_title) {
        this.team_title = team_title;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }
}
