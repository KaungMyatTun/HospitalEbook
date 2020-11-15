package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 9/14/2018.
 */

public class ListProviderForConsumable {

    private String id,material, orderedBy, date, units, comment, order_status;
    private boolean isSelected = false;

    public ListProviderForConsumable(String id, String material, String orderedBy, String date, String units, String comment, String order_status){
        this.id = id;
        this.material = material;
        this.orderedBy = orderedBy;
        this.date = date;
        this.units = units;
        this.comment = comment;
        this.order_status = order_status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
