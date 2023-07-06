package com.heroku.java;

public class Employee extends User{
    
    public Integer managerid;
    public String emprole;


    public Employee(Integer userid, String fullname, String username, String password, String usertype, Integer managerid, String emprole) {
        super(userid, fullname, username, password, usertype);
        this.managerid=managerid; 
        this.emprole=emprole;
    }


    public String getRoles() {
        return this.emprole;
    }

    public void setRoles(String emprole) {
        this.emprole = emprole;
    }


    public Integer getManagerid() {
        return this.managerid;
    }

    public void setManagerid(Integer managerid) {
        this.managerid = managerid != null ? managerid : 0;
    }

}
