package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class NurseLoginData {
    @SerializedName("HHUUserID")
    private String userID;
    @SerializedName("HHUPasswd")
    private String userPassword;
    @SerializedName("UserName")
    private String userName;

    public NurseLoginData(String userID, String userPassword, String userName) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
