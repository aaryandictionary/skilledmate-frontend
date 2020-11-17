package com.app.miniproject.Database.Course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateContentModel {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("course_id")
    @Expose
    private Integer course_id;

    @SerializedName("content_title")
    @Expose
    private String content_title;

    @SerializedName("content_details")
    @Expose
    private String content_details;

    @SerializedName("content_time")
    @Expose
    private String content_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent_time() {
        return content_time;
    }

    public void setContent_time(String content_time) {
        this.content_time = content_time;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public String getContent_details() {
        return content_details;
    }

    public void setContent_details(String content_details) {
        this.content_details = content_details;
    }
}
