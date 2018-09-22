package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

public class JSONDepartmentSendForm {
    @SerializedName("department")
    private String department;

    public JSONDepartmentSendForm(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
