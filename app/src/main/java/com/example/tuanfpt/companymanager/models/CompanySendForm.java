package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

public class CompanySendForm {
    @SerializedName("companyId")
    private String companyId;

    public CompanySendForm(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "CompanySendForm{" +
                "companyId='" + companyId + '\'' +
                '}';
    }
}
