package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class LabJson {
    @SerializedName("IPrcCode")
    private String labCode;
    @SerializedName("IPrcDesc")
    private String labTestName;

    public LabJson(String labCode, String labTestName) {
        this.labCode = labCode;
        this.labTestName = labTestName;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getLabTestName() {
        return labTestName;
    }

    public void setLabTestName(String labTestName) {
        this.labTestName = labTestName;
    }
}