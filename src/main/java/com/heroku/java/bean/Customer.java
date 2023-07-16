package com.heroku.java.bean;

import java.sql.Date;

public class Customer extends User {
    private Date licensedate;
    private String icnumber;
    private int phonenumC;

    public Customer(int userid, String fullname, String username, String password, Date licensedate, String icnumber,int phonenumC) {
        super(userid, fullname, username, password);
        this.licensedate = licensedate;
        this.icnumber = icnumber;
        this.phonenumC = phonenumC;
    }
    
    public Customer(){
        
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

    public int getPhonenumC() {
        return this.phonenumC;
    }

    public void setPhonenumC(int phonenumC) {
        this.phonenumC = phonenumC;
    }

    
}
