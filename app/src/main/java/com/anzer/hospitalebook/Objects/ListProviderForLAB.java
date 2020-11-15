package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 9/14/2018.
 */

public class ListProviderForLAB {

    private String id, date_time, ordering_md, procedures, urgent, exam_date_time, comment, order_status;
    private boolean isSelected = false;

    public ListProviderForLAB(String id, String date_time, String ordering_md, String procedures, String urgent, String exam_date_time, String comment, String order_status) {
        this.id = id;
        this.date_time = date_time;
        this.ordering_md = ordering_md;
        this.procedures = procedures;
        this.urgent = urgent;
        this.exam_date_time = exam_date_time;
        this.comment = comment;
        this.order_status = order_status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getOrdering_md() {
        return ordering_md;
    }

    public void setOrdering_md(String ordering_md) {
        this.ordering_md = ordering_md;
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

    public String getExam_date_time() {
        return exam_date_time;
    }

    public void setExam_date_time(String exam_date_time) {
        this.exam_date_time = exam_date_time;
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
