package com.example.nittrichy.Models;

public class Contacts {
    private String desig , name , phoneNo;
    public Contacts(){

    }

    public Contacts(String desig, String name, String phoneNo) {
        this.desig = desig;
        this.name = name;
        this.phoneNo = phoneNo;

    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


}
