package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class DIGroupModel {
    @SerializedName("IPGId")
    private int groupId;
    @SerializedName("IPGCaption")
    private String groupCaption;

    public DIGroupModel(int groupId, String groupCaption) {
        this.groupId = groupId;
        this.groupCaption = groupCaption;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupCaption() {
        return groupCaption;
    }

    public void setGroupCaption(String groupCaption) {
        this.groupCaption = groupCaption;
    }
}
