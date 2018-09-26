package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("mother")
    private String mother;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("lattitude")
    private String lattitude;

    public Company(String id, String name, String type, String mother, String address, String phone, String longitude, String lattitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.mother = mother;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", mother='" + mother + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", longitude='" + longitude + '\'' +
                ", lattitude='" + lattitude + '\'' +
                '}';
    }
}
