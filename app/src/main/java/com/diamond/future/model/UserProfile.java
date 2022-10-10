package com.diamond.future.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    int  status;
    String msg;
    LoginModel.User UserProfile;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginModel.User getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(LoginModel.User userProfile) {
        UserProfile = userProfile;
    }
}
