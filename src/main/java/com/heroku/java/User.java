package com.heroku.java;

public class User{
    public String fname;
    public String username;
    public String password;
    public String usertype;
    // public int userid;



    public User(String fname, String username, String password, String usertype) {
        this.fname = fname;
        this.username = username;
        this.password = password;
        this.usertype = usertype;
        // this.userid = userid;
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


    // public int getUserid() {
    //     return this.userid;
    // }

    // public void setUserid(int userid) {
    //     this.userid = userid;
    // }

}
