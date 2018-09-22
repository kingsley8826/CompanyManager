package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("_id")
    private String id;
    @SerializedName("department")
    private String department;
    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String password;

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Account(String id, String department, String userName, String password) {
        this.id = id;
        this.department = department;
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", department='" + department + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
