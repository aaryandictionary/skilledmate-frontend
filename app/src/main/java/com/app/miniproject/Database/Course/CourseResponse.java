package com.app.miniproject.Database.Course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private CourseData data;

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

    public CourseData getData() {
        return data;
    }

    public void setData(CourseData data) {
        this.data = data;
    }
}
