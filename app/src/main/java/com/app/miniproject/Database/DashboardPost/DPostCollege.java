package com.app.miniproject.Database.DashboardPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DPostCollege {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("college_name")
    @Expose
    private String collegeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
