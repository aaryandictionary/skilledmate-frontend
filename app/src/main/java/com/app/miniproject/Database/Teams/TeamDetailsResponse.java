package com.app.miniproject.Database.Teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamDetailsResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private TeamDetailsData data;

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

    public TeamDetailsData getData() {
        return data;
    }

    public void setData(TeamDetailsData data) {
        this.data = data;
    }
}
