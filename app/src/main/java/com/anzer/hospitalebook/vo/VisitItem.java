package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class VisitItem {
    @SerializedName("OrdVisit")
    private String visit;

    public VisitItem(String visit) {
        this.visit = visit;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }
}
