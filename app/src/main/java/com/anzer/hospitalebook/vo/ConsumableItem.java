package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class ConsumableItem {
    @SerializedName("Material")
    private String material;
    @SerializedName("OrderedBy")
    private String orderedBy;
    @SerializedName("Date")
    private String orderDate;
    @SerializedName("Units")
    private String units;
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


    public ConsumableItem(String material, String orderedBy, String orderDate, String units, String comment, String orderStatus, String editedOrder, String orderNum, String orderSeq) {
        this.material = material;
        this.orderedBy = orderedBy;
        this.orderDate = orderDate;
        this.units = units;
        this.comment = comment;
        this.orderStatus = orderStatus;
        this.editedOrder = editedOrder;
        this.orderNum = orderNum;
        this.orderSeq = orderSeq;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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
