package com.heroku.java.bean;

import java.sql.Date;

public class Customer extends User {
    private Date licensedate;
    private String icnumber;
    private int phonenumC;
    public String fname;

    public Customer(int userid, String fullname, String username, String password, Date licensedate, String icnumber,int phonenumC) {
        super(userid, fullname, username, password);
        this.licensedate = licensedate;
        this.icnumber = icnumber;
        this.phonenumC = phonenumC;
    }
    
    public Customer(){
        
    }


    public Customer(int userid, String fname, String usernamecust, String password, String usertype, Date licensedate2,
            String icnumber2, String phonenum) {
        //TODO Auto-generated constructor stub
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
