package com.example.nittrichy.Models;

public class FeedPost {
    String Title,Desc,image,Key;
    String Time,deadlineDate,deadlineTime;
    public FeedPost(){

    }

    public FeedPost(String title, String desc, String image, String time,String Key,String deadlineDate,String deadlineTime) {
        Title = title;
        Desc = desc;
        this.image = image;
        this.Key = Key;
        Time = time;
        this.deadlineDate=deadlineDate;
        this.deadlineTime = deadlineTime;
    }
    public FeedPost(String title, String desc, String image, String time,String Key) {
        Title = title;
        Desc = desc;
        this.image = image;
        this.Key = Key;
        Time = time;

    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
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
