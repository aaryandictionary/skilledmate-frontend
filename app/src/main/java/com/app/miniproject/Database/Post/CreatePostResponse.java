package com.app.miniproject.Database.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePostResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private CreatePostData data;

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

    public CreatePostData getData() {
        return data;
    }

    public void setData(CreatePostData data) {
        this.data = data;
    }
}
