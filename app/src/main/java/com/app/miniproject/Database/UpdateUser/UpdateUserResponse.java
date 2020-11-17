package com.app.miniproject.Database.UpdateUser;

import com.app.miniproject.Database.Register.RegisterUserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private RegisterUserData data;

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

    public RegisterUserData getData() {
        return data;
    }

    public void setData(RegisterUserData data) {
        this.data = data;
    }
}
