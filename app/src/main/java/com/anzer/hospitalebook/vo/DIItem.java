package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class DIItem {
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("OrderingMD")
    private String orderingMD;
    @SerializedName("Procedures")
    private String procedures;
    @SerializedName("Urgent")
    private String urgent;
    @SerializedName("Department")
    private String department;
    @SerializedName("ExamDateTime")
    private String examDateTime;
    @SerializedName("Comment")
    private String comment;
    @SerializedName("OrderStatus")
    private String orderStatus;
    @SerializedName("EditedOrder")
    private String editedOrder;
    @SerializedName("OrdNum")
    private String orderNum;
    @SerializedName("OriSeq")
    private String orderSeq;

    public DIItem(String dateTime, String orderingMD, String procedures, String urgent, String department, String examDateTime, String comment, String orderStatus, String editedOrder, String orderNum, String orderSeq) {
        this.dateTime = dateTime;
        this.orderingMD = orderingMD;
        this.procedures = procedures;
        this.urgent = urgent;
        this.department = department;
        this.examDateTime = examDateTime;
        this.comment = comment;
        this.orderStatus = orderStatus;
        this.editedOrder = editedOrder;
        this.orderNum = orderNum;
        this.orderSeq = orderSeq;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrderingMD() {
        return orderingMD;
    }

    public void setOrderingMD(String orderingMD) {
        this.orderingMD = orderingMD;
    }

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExamDateTime() {
        return examDateTime;
    }

    public void setExamDateTime(String examDateTime) {
        this.examDateTime = examDateTime;
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

    public String getEditedOrder() {
        return editedOrder;
    }

    public void setEditedOrder(String editedOrder) {
        this.editedOrder = editedOrder;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }
}
