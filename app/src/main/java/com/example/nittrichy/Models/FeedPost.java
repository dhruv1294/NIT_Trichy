package com.example.nittrichy.Models;

public class FeedPost {
    String Title,Desc,image,Key;
    String Time;
    public FeedPost(){

    }

    public FeedPost(String title, String desc, String image, String time,String Key) {
        Title = title;
        Desc = desc;
        this.image = image;
        this.Key = Key;
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public String getKey(){
        return Key;
    }
    public void setKey(String key){
        Key = key;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
