package com.heroku.java.bean;

public class User{
    private int userid;
    private String fullname;
    private String username;
    private String password;

    public User(){

    }


    public User(int userid, String fullname, String username, String password) {
        this.userid = userid;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }


    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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


}
