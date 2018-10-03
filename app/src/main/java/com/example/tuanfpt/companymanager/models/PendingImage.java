package com.example.tuanfpt.companymanager.models;

import android.net.Uri;

public class PendingImage {

    private String url;

    private Uri uri;
    private int position;
    private String type;
    private String state; // Constant: init, exits, uploadFail, upLoadSuccess

    public PendingImage(Uri uri, int position, String type, String state) {
        this.uri = uri;
        this.position = position;
        this.type = type;
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PendingImage{" +
                "url='" + url + '\'' +
                ", uri=" + uri +
                ", position=" + position +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
