package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class ImageCount {
    @SerializedName("ImgCount")
    private String ImgCount;

    public ImageCount(String imgCount) {
        ImgCount = imgCount;
    }

    public String getImgCount() {
        return ImgCount;
    }

    public void setImgCount(String imgCount) {
        ImgCount = imgCount;
    }
}
