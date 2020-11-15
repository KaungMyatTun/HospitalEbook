package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 5/15/2018.
 */

public class Pt_Info_Data {
    private String SurName;
    private String GivenName;
    private String Salutation;
    private String Sex;
    private String MaritalStatus;
    private String DOB;
    private String TOB;
    private String Age;
    private String Alive;
    private String DOD;
    private String Language;
    private String Address;
    private String Township;
    private String City;
    private String Province;
    private String PostalCode;
    private String HomePh;
    private String CellPh;
    private String FamilyDoctor;
    private String RefDoctor;
    private String Allergy;

    public Pt_Info_Data(String surName, String givenName, String salutation, String sex, String maritalStatus, String dob, String tob, String age, String alive, String dod, String language, String address, String township, String city, String province, String postalCode, String homePh, String cellPh, String FamilyDoctor, String refDoctor, String allergy){
        this.SurName = surName;
        this.GivenName = givenName;
        this.Salutation = salutation;
        this.Sex = sex;
        this.MaritalStatus = maritalStatus;
        this.DOB = dob;
        this.TOB = tob;
        this.Age = age;
        this.Alive = alive;
        this.DOD = dod;
        this.Language = language;
        this.Address = address;
        this.Township = township;
        this.City = city;
        this.Province = province;
        this.PostalCode = postalCode;
        this.HomePh = homePh;
        this.CellPh = cellPh;
        this.FamilyDoctor = FamilyDoctor;
        this.RefDoctor = refDoctor;
        this.Allergy = allergy;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public String getGivenName() {
        return GivenName;
    }

    public void setGivenName(String givenName) {
        GivenName = givenName;
    }

    public String getSalutation() {
        return Salutation;
    }

    public void setSalutation(String salutation) {
        Salutation = salutation;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getTOB() {
        return TOB;
    }

    public void setTOB(String TOB) {
        this.TOB = TOB;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAlive() {
        return Alive;
    }

    public void setAlive(String alive) {
        Alive = alive;
    }

    public String getDOD() {
        return DOD;
    }

    public void setDOD(String DOD) {
        this.DOD = DOD;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTownship() {
        return Township;
    }

    public void setTownship(String township) {
        Township = township;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getHomePh() {
        return HomePh;
    }

    public void setHomePh(String homePh) {
        HomePh = homePh;
    }

    public String getCellPh() {
        return CellPh;
    }

    public void setCellPh(String cellPh) {
        CellPh = cellPh;
    }

    public String getFamilyDoctor() {
        return FamilyDoctor;
    }

    public void setAttDoctor(String attDoctor) {
        FamilyDoctor = attDoctor;
    }

    public String getRefDoctor() {
        return RefDoctor;
    }

    public void setRefDoctor(String refDoctor) {
        RefDoctor = refDoctor;
    }

    public String getAllergy() {
        return Allergy;
    }

    public void setAllergy(String allergy) {
        Allergy = allergy;
    }
}
