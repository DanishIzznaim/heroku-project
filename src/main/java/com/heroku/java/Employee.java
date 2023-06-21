package com.heroku.java;

public class Employee extends User{
    
    public String roles;

    public Employee(String fullname, String username, String password, String roles) {
        super(fullname, username, password, roles); // Call the superclass constructor with parameters
        this.roles=roles;
    }


    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

}
