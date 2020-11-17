package com.app.miniproject.Database.PostLike;

import com.app.miniproject.Database.PostComment.PostCommentListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostLikeListResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<PostLikeListData> data=null;

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

    public List<PostLikeListData> getData() {
        return data;
    }

    public void setData(List<PostLikeListData> data) {
        this.data = data;
    }
}
