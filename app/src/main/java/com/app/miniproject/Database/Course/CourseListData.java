package com.app.miniproject.Database.Course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseListData {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("course_title")
    @Expose
    private String course_title;

    @SerializedName("course_details")
    @Expose
    private String course_details;

    @SerializedName("course_image")
    @Expose
    private String course_image;

    @SerializedName("course_fee")
    @Expose
    private String course_fee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourse_details() {
        return course_details;
    }

    public void setCourse_details(String course_details) {
        this.course_details = course_details;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public String getCourse_fee() {
        return course_fee;
    }

    public void setCourse_fee(String course_fee) {
        this.course_fee = course_fee;
    }
}
