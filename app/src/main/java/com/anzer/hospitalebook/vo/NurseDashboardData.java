package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;


public class NurseDashboardData {

    @SerializedName("PatientName")
    private String PatientName;
    @SerializedName("ORegCPI")
    private String PatientCPI;
    @SerializedName("ORegRegNum")
    private String RegNumber;
    @SerializedName("ORegAttDoc")
    private String RegAttDoctor;
    @SerializedName("ORegDoctorName")
    private String RegAttDoctorName;
    @SerializedName("ORegQueueNumber")
    private String QueueNumber;
    @SerializedName("Room")
    @Nullable
    private String PatientRoom;
    @SerializedName("ORegDate")
    private String RegDate;
    @SerializedName("EWS")
    @Nullable
    private Integer EWSScore;
    @SerializedName("IsSaved")
    @Nullable
    private Boolean isImageSaved;

    public NurseDashboardData(String patientName, String patientCPI, String regNumber, String regAttDoctor, String regAttDoctorName, String queueNumber, String patientRoom, String regDate, Integer ewsScore, Boolean isImageSaved) {
        PatientName = patientName;
        PatientCPI = patientCPI;
        RegNumber = regNumber;
        RegAttDoctor = regAttDoctor;
        RegAttDoctorName = regAttDoctorName;
        QueueNumber = queueNumber;
        PatientRoom = patientRoom;
        RegDate = regDate;
        EWSScore = ewsScore;
        this.isImageSaved = isImageSaved;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientCPI() {
        return PatientCPI;
    }

    public void setPatientCPI(String patientCPI) {
        PatientCPI = patientCPI;
    }

    public String getRegNumber() {
        return RegNumber;
    }

    public void setRegNumber(String regNumber) {
        RegNumber = regNumber;
    }

    public String getQueueNumber() {
        return QueueNumber;
    }

    public void setQueueNumber(String queueNumber) {
        QueueNumber = queueNumber;
    }

    public String getPatientRoom() {
        return PatientRoom;
    }

    public void setPatientRoom(String patientRoom) {
        PatientRoom = patientRoom;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public Integer getEWSScore() {
        return EWSScore;
    }

    public void setEWSScore(Integer EWSScore) {
        this.EWSScore = EWSScore;
    }

    public Boolean getIsImageSaved() {
        return isImageSaved;
    }

    public void setIsImageSaved(Boolean isImageSaved) {
        this.isImageSaved = isImageSaved;
    }

    public String getRegAttDoctor() {
        return RegAttDoctor;
    }

    public void setRegAttDoctor(String regAttDoctor) {
        RegAttDoctor = regAttDoctor;
    }

    public String getRegAttDoctorName() {
        return RegAttDoctorName;
    }

    public void setRegAttDoctorName(String regAttDoctorName) {
        RegAttDoctorName = regAttDoctorName;
    }
}
