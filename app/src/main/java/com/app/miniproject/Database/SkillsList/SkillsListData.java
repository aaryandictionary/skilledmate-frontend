package com.app.miniproject.Database.SkillsList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillsListData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String skillName;
    @SerializedName("my_tag")
    @Expose
    private String my_tag;

    public String getMy_tag() {
        return my_tag;
    }

    public void setMy_tag(String my_tag) {
        this.my_tag = my_tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
