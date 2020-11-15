package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class DocLoginData {
    @SerializedName("HHUUserID")
    private String userID;
    @SerializedName("HHUPasswd")
    private String userPassword;
    @SerializedName("DocCode")
    private String docCode;
    @SerializedName("DocSurname")
    private String docSurname;


    public DocLoginData(String userID, String userPassword, String docCode, String docSurname) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.docCode = docCode;
        this.docSurname = docSurname;
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

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getDocSurname() {
        return docSurname;
    }

    public void setDocSurname(String docSurname) {
        this.docSurname = docSurname;
    }
}
