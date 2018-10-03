package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JSONAddMaintainSendForm {

    @SerializedName("companyId")
    private String companyId;

    @SerializedName("period")
    private String period;
    @SerializedName("imageBefore")
    private ArrayList<String> imageBefore;

    @SerializedName("imageAfter")
    private ArrayList<String> imageAfter;

    @SerializedName("trait")
    private String trait;

    @SerializedName("problem")
    private String problem;

    @SerializedName("year")
    private String year;

    @SerializedName("date")
    private String date;

    @SerializedName("employeeName")
    private String employeeName;

    @SerializedName("note")
    private String note;


    public JSONAddMaintainSendForm(String companyId, String period, ArrayList<String> imageBefore,
                                   ArrayList<String> imageAfter, String trait, String problem,
                                   String year, String date, String employeeName, String note) {
        this.companyId = companyId;
        this.period = period;
        this.imageBefore = imageBefore;
        this.imageAfter = imageAfter;
        this.trait = trait;
        this.problem = problem;
        this.year = year;
        this.date = date;
        this.employeeName = employeeName;
        this.note = note;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public ArrayList<String> getImageBefore() {
        return imageBefore;
    }

    public void setImageBefore(ArrayList<String> imageBefore) {
        this.imageBefore = imageBefore;
    }

    public ArrayList<String> getImageAfter() {
        return imageAfter;
    }

    public void setImageAfter(ArrayList<String> imageAfter) {
        this.imageAfter = imageAfter;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
