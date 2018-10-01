package com.example.tuanfpt.companymanager.models;

public class ImageRequestCode {
    private int requestCode;
    private String type;
    private String photoPath;

    public ImageRequestCode(int requestCode, String type, String photoPath) {
        this.requestCode = requestCode;
        this.type = type;
        this.photoPath = photoPath;
    }
    public ImageRequestCode(int requestCode, String type) {
        this.requestCode = requestCode;
        this.type = type;
    }
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }



    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
