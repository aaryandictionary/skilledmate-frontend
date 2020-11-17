package com.app.miniproject.Database.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyEventModel {

    @SerializedName("event_id")
    @Expose
    private Integer event_id;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
