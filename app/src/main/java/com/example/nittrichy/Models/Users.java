package com.example.nittrichy.Models;

public class Users {
    private String name;
    private String userid;
    public Users(){

    }

    public Users(String name,String userid) {
        this.name = name;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }
}
