package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentAppVersionModel implements Serializable {
    @SerializedName("CurrentAndroidVersion")
    @Expose
    private String currentAndroidVersion;
    @SerializedName("ForceAndroidUpdate")
    @Expose
    private Boolean forceAndroidUpdate;
    @SerializedName("CurrentIOSVersion")
    @Expose
    private String currentIOSVersion;
    @SerializedName("ForceIOSUpdate")
    @Expose
    private Boolean forceIOSUpdate;
    @SerializedName("UpdatedBy")
    @Expose
    private Integer updatedBy;

    public String getCurrentAndroidVersion() {
        return currentAndroidVersion;
    }

    public void setCurrentAndroidVersion(String currentAndroidVersion) {
        this.currentAndroidVersion = currentAndroidVersion;
    }

    public Boolean getForceAndroidUpdate() {
        return forceAndroidUpdate;
    }

    public void setForceAndroidUpdate(Boolean forceAndroidUpdate) {
        this.forceAndroidUpdate = forceAndroidUpdate;
    }

    public String getCurrentIOSVersion() {
        return currentIOSVersion;
    }

    public void setCurrentIOSVersion(String currentIOSVersion) {
        this.currentIOSVersion = currentIOSVersion;
    }

    public Boolean getForceIOSUpdate() {
        return forceIOSUpdate;
    }

    public void setForceIOSUpdate(Boolean forceIOSUpdate) {
        this.forceIOSUpdate = forceIOSUpdate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}
