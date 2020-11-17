package com.app.miniproject.Database.UserDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSkillData {
    @SerializedName("skill_id")
    @Expose
    private Integer skillId;
    @SerializedName("skill_name")
    @Expose
    private String skillName;
    @SerializedName("skill_category")
    @Expose
    private String skillCategory;

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }
}
