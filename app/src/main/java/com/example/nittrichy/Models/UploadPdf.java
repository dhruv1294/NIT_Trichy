package com.example.nittrichy.Models;

public class UploadPdf {
    private String name;
    private String url;
    private String key;

    public UploadPdf(String name, String url, String key) {
        this.name = name;
        this.url = url;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UploadPdf() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
