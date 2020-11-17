package com.app.miniproject.Database.Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastSeenModel {

    @SerializedName("last_seen")
    @Expose
    private String last_seen;

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }
}
