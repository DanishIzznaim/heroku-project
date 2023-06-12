package com.heroku.java;
import java.sql.Date;

public class Customer {
    public String fullname;
    public String address;
    public String phonenum;
    public String icnumber;
    public Date licensecard;
    public String username;
    public String password;


    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenum() {
        return this.phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getIcnumber() {
        return this.icnumber;
    }

    public void setIcnumber(String icnumber) {
        this.icnumber = icnumber;
    }

    public Date getLicensecard() {
        return this.licensecard;
    }

    public void setLicensecard(Date licensecard) {
        this.licensecard = licensecard;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
