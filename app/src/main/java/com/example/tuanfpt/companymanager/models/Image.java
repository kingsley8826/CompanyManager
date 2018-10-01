package com.example.tuanfpt.companymanager.models;

public class Image {
    private String pathInDevice;
    private String url;
    private String type;

    public Image(String pathInDevice, String url, String type) {
        this.pathInDevice = pathInDevice;
        this.url = url;
        this.type = type;
    }

    public String getPathInDevice() {
        return pathInDevice;
    }

    public void setPathInDevice(String pathInDevice) {
        this.pathInDevice = pathInDevice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
