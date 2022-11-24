package com.example.feelings;

public class Users {

    public String fullName, phoneNumber, wphoneNumber, birthDate, anniversaryDate, address;

    public Users() {
    }

    public Users(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Users(String fullName, String phoneNumber, String wphoneNumber, String birthDate, String anniversaryDate, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.wphoneNumber = wphoneNumber;
        this.birthDate = birthDate;
        this.anniversaryDate = anniversaryDate;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWphoneNumber() {
        return wphoneNumber;
    }

    public void setWphoneNumber(String wphoneNumber) {
        this.wphoneNumber = wphoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
