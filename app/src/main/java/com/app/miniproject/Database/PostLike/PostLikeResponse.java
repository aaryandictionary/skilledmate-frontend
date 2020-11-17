package com.app.miniproject.Database.PostLike;

import com.app.miniproject.Database.Teams.TeamAdminData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostLikeResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private PostLikeModel data = null;

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

    public PostLikeModel getData() {
        return data;
    }

    public void setData(PostLikeModel data) {
        this.data = data;
    }
}
