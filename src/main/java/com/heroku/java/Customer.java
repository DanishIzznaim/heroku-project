package com.heroku.java;

import java.sql.Date;

public class Customer extends User {
    public Date licensedate;

    public String icnumber;

    public String phonenum;

    // Constructors
    
    public Customer(String fullname, String username, String password, Date licensedate, String icnumber, String phonenum) {
        super(fullname, username, password, phonenum); // Call the superclass constructor with parameters
        this.licensedate = licensedate;
        this.icnumber = icnumber;
        this.phonenum = phonenum;
    }

    public Date getLicensedate() {
        return this.licensedate;
    }

    public void setLicensedate(Date licensedate) {
        this.licensedate = licensedate;
    }

    public String getIcnumber() {
        return this.icnumber;
    }

    public void setIcnumber(String icnumber) {
        this.icnumber = icnumber;
    }

    public String getPhonenum() {
        return this.phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getFullname() {
        return null;
    }
    
}
