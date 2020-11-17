package com.app.miniproject.Database.PostComment;

import com.app.miniproject.Database.PostLike.PostLikeModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostCommentResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private PostCommentListData data = null;

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

    public PostCommentListData getData() {
        return data;
    }

    public void setData(PostCommentListData data) {
        this.data = data;
    }
}
