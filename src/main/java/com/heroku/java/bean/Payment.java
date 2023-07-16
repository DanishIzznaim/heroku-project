package com.heroku.java.bean;

import org.springframework.web.multipart.MultipartFile;
import java.sql.*;
public class Payment {
    private int rentid;
    private int paymentid;
    private String paystatus;
    private Double payamount;
    private String paymentmethod;
    private byte[] paymentbyte;
    private MultipartFile paymentreceipt;
    private String imageSrc;
    private Date cashreceivedate;


    public Payment() {
    }


    public Payment(int rentid, int paymentid, String paystatus, Double payamount, String paymentmethod, byte[] paymentbyte, MultipartFile paymentreceipt, String imageSrc, Date cashreceivedate) {
        this.rentid = rentid;
        this.paymentid = paymentid;
        this.paystatus = paystatus;
        this.payamount = payamount;
        this.paymentmethod = paymentmethod;
        this.paymentbyte = paymentbyte;
        this.paymentreceipt = paymentreceipt;
        this.imageSrc = imageSrc;
        this.cashreceivedate = cashreceivedate;
    }


    public int getRentid() {
        return this.rentid;
    }

    public void setRentid(int rentid) {
        this.rentid = rentid;
    }


    
    

    public int getPaymentid() {
        return this.paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    public String getPaystatus() {
        return this.paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public Double getPayamount() {
        return this.payamount;
    }

    public void setPayamount(Double payamount) {
        this.payamount = payamount;
    }

    public String getPaymentmethod() {
        return this.paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public byte[] getPaymentbyte() {
        return this.paymentbyte;
    }

    public void setPaymentbyte(byte[] paymentbyte) {
        this.paymentbyte = paymentbyte;
    }

    public MultipartFile getPaymentreceipt() {
        return this.paymentreceipt;
    }

    public void setPaymentreceipt(MultipartFile paymentreceipt) {
        this.paymentreceipt = paymentreceipt;
    }

    public String getImageSrc() {
        return this.imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public Date getCashreceivedate() {
        return this.cashreceivedate;
    }

    public void setCashreceivedate(Date cashreceivedate) {
        this.cashreceivedate = cashreceivedate;
    }
    

}
