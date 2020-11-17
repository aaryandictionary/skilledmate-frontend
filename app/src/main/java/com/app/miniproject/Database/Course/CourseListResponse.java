package com.app.miniproject.Database.Course;

import com.app.miniproject.Database.Post.CreatePostData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseListResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<CourseListData> data;

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

    public List<CourseListData> getData() {
        return data;
    }

    public void setData(List<CourseListData> data) {
        this.data = data;
    }
}

