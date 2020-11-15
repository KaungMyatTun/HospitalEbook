package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class DIProceduresModel {
    @SerializedName("IPrcCode")
    private String procedureCode;
    @SerializedName("IPrcDesc")
    private String procedureDesc;
    @SerializedName("IPGLGroupID")
    private int prcGroupId;
    @SerializedName("IPGCaption")
    private String prcGroupDesc;

    public DIProceduresModel(String procedureCode, String procedureDesc, int prcGroupId, String prcGroupDesc) {
        this.procedureCode = procedureCode;
        this.procedureDesc = procedureDesc;
        this.prcGroupId = prcGroupId;
        this.prcGroupDesc = prcGroupDesc;
    }


    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureDesc() {
        return procedureDesc;
    }

    public void setProcedureDesc(String procedureDesc) {
        this.procedureDesc = procedureDesc;
    }

    public int getPrcGroupId() {
        return prcGroupId;
    }

    public void setPrcGroupId(int prcGroupId) {
        this.prcGroupId = prcGroupId;
    }

    public String getPrcGroupDesc() {
        return prcGroupDesc;
    }

    public void setPrcGroupDesc(String prcGroupDesc) {
        this.prcGroupDesc = prcGroupDesc;
    }
}
