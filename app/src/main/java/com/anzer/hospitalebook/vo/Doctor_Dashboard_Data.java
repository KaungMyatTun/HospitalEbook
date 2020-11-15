package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 5/15/2018.
 */

public class Doctor_Dashboard_Data {

    @SerializedName("ORegCPI")
    private String patient_cpi;
    @SerializedName("PatientName")
    private String patient_name;
    @SerializedName("ORegRegNum")
    private String patient_visit_number;
    @SerializedName("ORegQueueNumber")
    private String patient_queue_number;
    @SerializedName("Room")
    private String patient_room_number;
    @SerializedName("ORegDate")
    private String patient_reg_date_time;
    @SerializedName("EWS")
    private String patient_ews_score;
    @SerializedName("IsSaved")
    private String img_isSaved;

    public Doctor_Dashboard_Data(String patient_cpi, String patient_name, String patient_visit_number, String patient_queue_number, String patient_room_number, String patient_reg_date_time, String patient_ews_score, String img_isSaved) {
        this.patient_cpi = patient_cpi;
        this.patient_name = patient_name;
        this.patient_visit_number = patient_visit_number;
        this.patient_queue_number = patient_queue_number;
        this.patient_room_number = patient_room_number;
        this.patient_reg_date_time = patient_reg_date_time;
        this.patient_ews_score = patient_ews_score;
        this.img_isSaved = img_isSaved;
    }

    public String getPatient_cpi() {
        return patient_cpi;
    }

    public void setPatient_cpi(String patient_cpi) {
        this.patient_cpi = patient_cpi;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_visit_number() {
        return patient_visit_number;
    }

    public void setPatient_visit_number(String patient_visit_number) {
        this.patient_visit_number = patient_visit_number;
    }

    public String getPatient_queue_number() {
        return patient_queue_number;
    }

    public void setPatient_queue_number(String patient_queue_number) {
        this.patient_queue_number = patient_queue_number;
    }

    public String getPatient_room_number() {
        return patient_room_number;
    }

    public void setPatient_room_number(String patient_room_number) {
        this.patient_room_number = patient_room_number;
    }

    public String getPatient_reg_date_time() {
        return patient_reg_date_time;
    }

    public void setPatient_reg_date_time(String patient_reg_date_time) {
        this.patient_reg_date_time = patient_reg_date_time;
    }

    public String getPatient_ews_score() {
        return patient_ews_score;
    }

    public void setPatient_ews_score(String patient_ews_score) {
        this.patient_ews_score = patient_ews_score;
    }

    public String getImg_isSaved() {
        return img_isSaved;
    }

    public void setImg_isSaved(String img_isSaved) {
        this.img_isSaved = img_isSaved;
    }
}
