package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DocumentTypeData implements Serializable {

        @SerializedName("DocumentTypeId")
        @Expose
        private Integer documentTypeId;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("IsReminder")
        @Expose
        private Boolean isReminder;
        @SerializedName("ReminderDays")
        @Expose
        private String reminderDays;

        public Integer getDocumentTypeId() {
            return documentTypeId;
        }

        public void setDocumentTypeId(Integer documentTypeId) {
            this.documentTypeId = documentTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getIsReminder() {
            return isReminder;
        }

        public void setIsReminder(Boolean isReminder) {
            this.isReminder = isReminder;
        }

        public String getReminderDays() {
            return reminderDays;
        }

        public void setReminderDays(String reminderDays) {
            this.reminderDays = reminderDays;
        }

}
