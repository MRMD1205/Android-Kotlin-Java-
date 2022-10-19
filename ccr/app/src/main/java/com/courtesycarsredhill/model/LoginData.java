package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginData {
/*

    {
        "Data": {
        "UserID": 5,
                "UserType": "Driver",
                "FirstName": "Vaibhav",
                "LastName": "Parekh",
                "UserName": "Vaibhav",
                "Email": "vaibhav.p@shaligraminfotech.com",
                "ContactNo": "123456789",
                "IsActive": true,
                "ProfilePhoto": "http://106.201.238.169:7016//Image/Driver/icon _ without name1.png",
                "Photo": "/Image/Driver/icon _ without name1.png",
                "Password": null,
                "OldPassword": null,
                "AuthenticationKey": "2BcLZR3sb4lzkPh/hw2iWTjPQtJ8BZ/Z4km+ydgH+6veOUCSv24XGOtAzOM33VLY1MiRc50HgOppgFVK+ngjMrEKenYe0WU4gYXZOl3l1jW+WQVewh6zJOeA7STsdz+BcdBRp9IEiyhthIFYN1vluvpENceqIoHdSsnU6uK+OeVqQ2gp4AN8FHJrMk7ZKiParjZfOkOkANS5CF4ZF76Bf1CVSFwQD18xigFnqAYLaeQgxRS+bfURAcoSirI5Ak4ULDilDHK61Da/LOM8ccKuLYhNTzMT/bQbrzBTZkgVGypyJ0sBxbKJFPWDqvuTsKek"
    },
        "Success": true,
            "Message": "Login Success"
    }*/

    @Expose
    @SerializedName("AuthenticationKey")
    private String authenticationkey;
    @Expose
    @SerializedName("OldPassword")
    private String oldpassword;
    @Expose
    @SerializedName("Password")
    private String password;
    @Expose
    @SerializedName("Photo")
    private String photo;
    @Expose
    @SerializedName("ProfilePhoto")
    private String profilephoto;
    @Expose
    @SerializedName("IsActive")
    private boolean isactive;
    @Expose
    @SerializedName("ContactNo")
    private String contactno;
    @Expose
    @SerializedName("Email")
    private String email;
    @Expose
    @SerializedName("LastName")
    private String lastname;
    @Expose
    @SerializedName("FirstName")
    private String firstname;
    @Expose
    @SerializedName("UserType")
    private String usertype;
    @Expose
    @SerializedName("UserID")
    private int userid;

    public String getAuthenticationkey() {
        return authenticationkey;
    }

    public void setAuthenticationkey(String authenticationkey) {
        this.authenticationkey = authenticationkey;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
