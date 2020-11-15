package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class PHItem {
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("Drug")
    private String drug;
    @SerializedName("Route")
    private String route;
    @SerializedName("Dosage")
    private String dosage;
    @SerializedName("Units")
    private Double unit;
    @SerializedName("OIPharmFreq")
    private String frequency;
    @SerializedName("Duration")
    private Integer duration;
    @SerializedName("OrderedBy")
    private String orderBy;
    @SerializedName("Comment")
    private String comment;
    @SerializedName("OrderStatus")
    private String orderStatus;
    @SerializedName("EditedOrder")
    private String editOrder;
    @SerializedName("OrdNum")
    private String orderNum;
    @SerializedName("OriSeq")
    private Integer orderSeq;

    public PHItem(String startDate, String drug, String route, String dosage, Double unit, String frequency, Integer duration, String orderBy, String comment, String orderStatus, String editOrder, String orderNum, Integer orderSeq) {
        this.startDate = startDate;
        this.drug = drug;
        this.route = route;
        this.dosage = dosage;
        this.unit = unit;
        this.frequency = frequency;
        this.duration = duration;
        this.orderBy = orderBy;
        this.comment = comment;
        this.orderStatus = orderStatus;
        this.editOrder = editOrder;
        this.orderNum = orderNum;
        this.orderSeq = orderSeq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getEditOrder() {
        return editOrder;
    }

    public void setEditOrder(String editOrder) {
        this.editOrder = editOrder;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }
}
