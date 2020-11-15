package com.anzer.hospitalebook.vo;

public class HWPtInfo {
    private String doctorCode;
    private String ptCPI;
    private String ptVisitNumber;
    private String hospInsti;
    private String insertDataDate;
    private Integer isSaved;
    private Integer urgentFlag;
    private Integer isProcessed;
    private Integer isPatientVital;

    public HWPtInfo(String doctorCode, String ptCPI, String ptVisitNumber, String hospInsti, String insertDataDate, Integer isSaved, Integer urgentFlag, Integer isProcessed, Integer isPatientVital) {
        this.doctorCode = doctorCode;
        this.ptCPI = ptCPI;
        this.ptVisitNumber = ptVisitNumber;
        this.hospInsti = hospInsti;
        this.insertDataDate = insertDataDate;
        this.isSaved = isSaved;
        this.urgentFlag = urgentFlag;
        this.isProcessed = isProcessed;
        this.isPatientVital = isPatientVital;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getPtCPI() {
        return ptCPI;
    }

    public void setPtCPI(String ptCPI) {
        this.ptCPI = ptCPI;
    }

    public String getPtVisitNumber() {
        return ptVisitNumber;
    }

    public void setPtVisitNumber(String ptVisitNumber) {
        this.ptVisitNumber = ptVisitNumber;
    }

    public String getHospInsti() {
        return hospInsti;
    }

    public void setHospInsti(String hospInsti) {
        this.hospInsti = hospInsti;
    }

    public String getInsertDataDate() {
        return insertDataDate;
    }

    public void setInsertDataDate(String insertDataDate) {
        this.insertDataDate = insertDataDate;
    }

    public Integer getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Integer isSaved) {
        this.isSaved = isSaved;
    }

    public Integer getUrgentFlag() {
        return urgentFlag;
    }

    public void setUrgentFlag(Integer urgentFlag) {
        this.urgentFlag = urgentFlag;
    }

    public Integer getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Integer isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Integer getIsPatientVital() {
        return isPatientVital;
    }

    public void setIsPatientVital(Integer isPatientVital) {
        this.isPatientVital = isPatientVital;
    }
}
