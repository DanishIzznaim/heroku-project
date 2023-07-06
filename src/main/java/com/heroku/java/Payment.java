package com.heroku.java;

public class Payment {
    public int paymentid;
    public String paystatus;
    public Double payamount;


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

}
