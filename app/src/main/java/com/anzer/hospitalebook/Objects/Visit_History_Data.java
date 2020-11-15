package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 5/15/2018.
 */

public class Visit_History_Data {

    private String id;
    private String RegNum;
    private String RegDate;
    private String RegAttDoc;
    private String RegQueueNum;
    private String RegDispDate;
    private String RegCancelComment;

    public Visit_History_Data(String id, String RegNum, String RegDate, String RegAttDoc, String RegQueueNum, String RegDispDate, String RegCancelComment) {
        this.id = id;
        this.RegNum = RegNum;
        this.RegDate = RegDate;
        this.RegAttDoc = RegAttDoc;
        this.RegQueueNum = RegQueueNum;
        this.RegDispDate = RegDispDate;
        this.RegCancelComment = RegCancelComment;
    }

    public String getRegNum() {
        return RegNum;
    }

    public void setRegNum(String regNum) {
        RegNum = regNum;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getRegAttDoc() {
        return RegAttDoc;
    }

    public void setRegAttDoc(String regAttDoc) {
        RegAttDoc = regAttDoc;
    }

    public String getRegQueueNum() {
        return RegQueueNum;
    }

    public void setRegQueueNum(String regQueueNum) {
        RegQueueNum = regQueueNum;
    }

    public String getRegDispDate() {
        return RegDispDate;
    }

    public void setRegDispDate(String regDispDate) {
        RegDispDate = regDispDate;
    }

    public String getRegCancelComment() {
        return RegCancelComment;
    }

    public void setRegCancelComment(String regCancelDate) {
        RegCancelComment = regCancelDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
