package com.anzer.hospitalebook.vo;

import com.google.gson.annotations.SerializedName;

public class PtInfoDetail {
    @SerializedName("CPerSurname")
    private String surName;
    @SerializedName("CPerGiven")
    private String givenName;
    @SerializedName("CPerSalut")
    private String salutName;
    @SerializedName("Sex")
    private String sex;
    @SerializedName("MaritalStatus")
    private String maritalStatus;
    @SerializedName("DOB")
    private String dob;
    @SerializedName("TOB")
    private String tob;
    @SerializedName("CPerAge")
    private String age;
    @SerializedName("CPerAlive")
    private String alive;
    @SerializedName("CPerDoD")
    private String dod;
    @SerializedName("Lang")
    private String lang;
    @SerializedName("CPerAddr1")
    private String address1;
    @SerializedName("Township")
    private String township;
    @SerializedName("CPerCity")
    private String city;
    @SerializedName("Province")
    private String province;
    @SerializedName("CPerPCode")
    private String pcode;
    @SerializedName("CPerHPhone")
    private String homePhone;
    @SerializedName("CPerCPhone")
    private String cellPhone;
    @SerializedName("AttDoc")
    private String attDoctor;
    @SerializedName("RefDoc")
    private String refDoctor;
    @SerializedName("CPerAllergies")
    private String allergies;

    public PtInfoDetail(String surName, String givenName, String salutName, String sex, String maritalStatus, String dob, String tob, String age, String alive, String dod, String lang, String address1, String township, String city, String province, String pcode, String homePhone, String cellPhone, String attDoctor, String refDoctor, String allergies) {
        this.surName = surName;
        this.givenName = givenName;
        this.salutName = salutName;
        this.sex = sex;
        this.maritalStatus = maritalStatus;
        this.dob = dob;
        this.tob = tob;
        this.age = age;
        this.alive = alive;
        this.dod = dod;
        this.lang = lang;
        this.address1 = address1;
        this.township = township;
        this.city = city;
        this.province = province;
        this.pcode = pcode;
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
        this.attDoctor = attDoctor;
        this.refDoctor = refDoctor;
        this.allergies = allergies;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSalutName() {
        return salutName;
    }

    public void setSalutName(String salutName) {
        this.salutName = salutName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTob() {
        return tob;
    }

    public void setTob(String tob) {
        this.tob = tob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAttDoctor() {
        return attDoctor;
    }

    public void setAttDoctor(String attDoctor) {
        this.attDoctor = attDoctor;
    }

    public String getRefDoctor() {
        return refDoctor;
    }

    public void setRefDoctor(String refDoctor) {
        this.refDoctor = refDoctor;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
