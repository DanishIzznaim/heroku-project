package com.heroku.java;

public class User{
    public String fname;
    public String username;
    public String password;
    public String usertype;
    // public int managerid;
    public Integer userid;



    public User(Integer userid, String fname, String username, String password, String usertype) {
        this.userid = userid;
        this.fname = fname;
        this.username = username;
        this.password = password;
        // this.managerid=managerid;
        this.usertype = usertype;
        
    }


    public String getFname() {
        return this.fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public String getName() {
        return this.fname;
    }

    public void setName(String fname) {
        this.fname = fname;
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


    public String getUsertype() {
        return this.usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

   

}
