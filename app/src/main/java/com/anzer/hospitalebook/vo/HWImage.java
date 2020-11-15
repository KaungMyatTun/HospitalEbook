package com.anzer.hospitalebook.vo;

public class HWImage {
    private Integer ptID;
    private String encodedImage;
    private String imgDesc;

    public HWImage(Integer ptID, String encodedImage, String imgDesc) {
        this.ptID = ptID;
        this.encodedImage = encodedImage;
        this.imgDesc = imgDesc;
    }

    public Integer getPtID() {
        return ptID;
    }

    public void setPtID(Integer ptID) {
        this.ptID = ptID;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }
}
