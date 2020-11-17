package com.app.miniproject.Database.Course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("college_name")
    @Expose
    private String college_name;
    @SerializedName("user_image")
    @Expose
    private String user_image;
    @SerializedName("course_title")
    @Expose
    private String course_title;
    @SerializedName("course_details")
    @Expose
    private String course_details;
    @SerializedName("course_duration")
    @Expose
    private String course_duration;
    @SerializedName("course_fee")
    @Expose
    private String course_fee;
    @SerializedName("course_image")
    @Expose
    private String course_image;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
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

    public String getCourse_duration() {
        return course_duration;
    }

    public void setCourse_duration(String course_duration) {
        this.course_duration = course_duration;
    }

    public String getCourse_fee() {
        return course_fee;
    }

    public void setCourse_fee(String course_fee) {
        this.course_fee = course_fee;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }
}
