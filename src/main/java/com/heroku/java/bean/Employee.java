package com.heroku.java.bean;

public class Employee extends User{
    
    private String emprole;
    private int phonenumE;
    private int managerid;


    public Employee() {
    }

    public Employee(int userid, String fullname, String username, String password, String emprole, int phonenumE, int managerid) {
        super(userid, fullname, username, password);
        this.managerid=managerid; 
        this.emprole=emprole;
        this.phonenumE = phonenumE;
    }


    public String getEmprole() {
        return this.emprole;
    }

    public void setEmprole(String emprole) {
        this.emprole = emprole;
    }

    public int getPhonenumE() {
        return this.phonenumE;
    }

    public void setPhonenumE(int phonenumE) {
        this.phonenumE = phonenumE;
    }

    public int getManagerid() {
        return this.managerid;
    }

    public void setManagerid(int managerid) {
        this.managerid = managerid;
    }


}
