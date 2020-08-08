package com.example.nittrichy.Models;

public class Places {
    private String shopNo, shopName , shopTime , shopAddress , shopContact;

    public Places(String shopNo, String shopName, String shopTime, String shopAddress, String shopContact) {
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopAddress = shopAddress;
        this.shopContact = shopContact;
    }
    public Places(){

    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopTime() {
        return shopTime;
    }

    public void setShopTime(String shopTime) {
        this.shopTime = shopTime;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopContact() {
        return shopContact;
    }

    public void setShopContact(String shopContact) {
        this.shopContact = shopContact;
    }
}
