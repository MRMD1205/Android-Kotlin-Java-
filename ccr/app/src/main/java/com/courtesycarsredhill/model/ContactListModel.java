package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContactListModel implements Serializable {

    @SerializedName("ContactId")
    @Expose
    private Integer contactId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LoggedinUserId")
    @Expose
    private Integer loggedinUserId;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

}