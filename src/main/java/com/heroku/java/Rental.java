package com.heroku.java;

import java.sql.Date;

public class Rental {
    public int rentid;
    public int day;
    public Date datestart;
    public Date dateend;
    public Date datereturn;
    public String statusrent;


    public Rental() {
    }

    public Rental(int rentid, int day, Date datestart, Date dateend, Date datereturn, String statusrent) {
        this.rentid = rentid;
        this.day = day;
        this.datestart = datestart;
        this.dateend = dateend;
        this.datereturn = datereturn;
        this.statusrent = statusrent;
    }


    public int getRentid() {
        return this.rentid;
    }

    public void setRentid(int rentid) {
        this.rentid = rentid;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getDatestart() {
        return this.datestart;
    }

    public void setDatestart(Date datestart) {
        this.datestart = datestart;
    }

    public Date getDateend() {
        return this.dateend;
    }

    public void setDateend(Date dateend) {
        this.dateend = dateend;
    }

    public Date getDatereturn() {
        return this.datereturn;
    }

    public void setDatereturn(Date datereturn) {
        this.datereturn = datereturn;
    }

    public String getStatusrent() {
        return this.statusrent;
    }

    public void setStatusrent(String statusrent) {
        this.statusrent = statusrent;
    }

}
