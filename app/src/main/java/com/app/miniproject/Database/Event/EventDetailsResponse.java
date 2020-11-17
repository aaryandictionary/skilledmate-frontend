package com.app.miniproject.Database.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailsResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private EventDetailsData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public EventDetailsData getData() {
        return data;
    }

    public void setData(EventDetailsData data) {
        this.data = data;
    }
}
