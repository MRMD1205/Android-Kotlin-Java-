package com.courtesycarsredhill.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckListData implements Serializable {

    @SerializedName("CheckListID")
    private Integer checkListID;
    @SerializedName("CheckListItem")
    private String checkListItem;
    @SerializedName("isChecked")
    private Boolean isChecked;
    @SerializedName("value")
    private String value ="";

    public Integer getCheckListID() {
        return checkListID;
    }

    public void setCheckListID(Integer checkListID) {
        this.checkListID = checkListID;
    }

    public String getCheckListItem() {
        return checkListItem;
    }

    public void setCheckListItem(String checkListItem) {
        this.checkListItem = checkListItem;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
