package com.app.miniproject.Database.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillModel {

    @SerializedName("skill_id")
    @Expose
    private Integer skillId;

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }
}
