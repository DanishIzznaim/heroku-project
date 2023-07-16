package com.heroku.java.bean;

import java.sql.Date;

public class Rental {
    public Integer rentid;
    public int day;
    public Date datestart;
    public Date dateend;
    public Date returndate;
    public String statusrent;
    public Double totalrentprice;
    public int customerid;
    public int carid;

    public Rental() {
    }


    public Rental(Integer rentid, int day, Date datestart, Date dateend, Date returndate, String statusrent, Double totalrentprice) {
        this.rentid = rentid;
        this.day = day;
        this.datestart = datestart;
        this.dateend = dateend;
        this.returndate = returndate;
        this.statusrent = statusrent;
        this.totalrentprice = totalrentprice;
        this.statusrent = statusrent;
    }


    public Rental(int day, int rentid, int carid, int customerid, Date datestart, Date dateend, String statusrent,
            double totalrentprice) {
        this.day = day;
        this.rentid = rentid;
        this.carid = carid;
        this.customerid = customerid;
        this.datestart = datestart;
        this.dateend = dateend;
        this.statusrent = statusrent;
        this.totalrentprice = totalrentprice;
    }


    public Integer getRentid() {
        return this.rentid;
    }

    public void setRentid(Integer rentid) {
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

    public Date getReturndate() {
        return this.returndate;
    }

    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }

    public String getStatusrent() {
        return this.statusrent;
    }

    public void setStatusrent(String statusrent) {
        this.statusrent = statusrent;
    }

    public Double getTotalrentprice() {
        return this.totalrentprice;
    }

    public void setTotalrentprice(Double totalrentprice) {
        this.totalrentprice = totalrentprice;
    }

    public int getCustomerid() {
        return this.customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }


    public int getCarid() {
        return this.carid;
    }

    public void setCarid(int carid) {
        this.carid = carid;
    }

}
