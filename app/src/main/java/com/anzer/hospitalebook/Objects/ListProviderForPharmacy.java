package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 9/13/2018.
 */

public class ListProviderForPharmacy {
//    private int id;
    private String id;
    private String startDate;
    private String drug;
    private String route;
    private String dosage;
    private String units;
    private String frequency;
    private String duration;
    private String orderedby;
    private String comment;
    private String order_status;
    private boolean isSelected = false;


    public ListProviderForPharmacy(String id, String startDate, String drug, String route, String dosage, String units, String frequency, String duration, String orderedby, String comment, String order_status) {
        this.id = id;
        this.startDate = startDate;
        this.drug = drug;
        this.route = route;
        this.dosage = dosage;
        this.units = units;
        this.frequency = frequency;
        this.duration = duration;
        this.orderedby = orderedby;
        this.comment = comment;
        this.order_status = order_status;
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

    public String getDossage() {
        return dosage;
    }

    public void setDossage(String dossage) {
        this.dosage = dossage;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOrderedby() {
        return orderedby;
    }

    public void setOrderedby(String orderedby) {
        this.orderedby = orderedby;
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
