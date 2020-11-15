package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class PtVisitHistory {
    @SerializedName("ORegRegNum")
    private String regNum;
    @SerializedName("ORegDate")
    private String regDate;
    @SerializedName("ORegAttDoc")
    private String attDoc;
    @SerializedName("ORegQueueNumber")
    private String queueNumber;
    @SerializedName("ORegDispDate")
    private String dischargeDate;
    @SerializedName("Cancel")
    private String cancelReason;

    public PtVisitHistory(String regNum, String regDate, String attDoc, String queueNumber, String dischargeDate, String cancelReason) {
        this.regNum = regNum;
        this.regDate = regDate;
        this.attDoc = attDoc;
        this.queueNumber = queueNumber;
        this.dischargeDate = dischargeDate;
        this.cancelReason = cancelReason;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getAttDoc() {
        return attDoc;
    }

    public void setAttDoc(String attDoc) {
        this.attDoc = attDoc;
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
