package com.example.tuanfpt.companymanager.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Maintain {

    @SerializedName("mother")
    private String mother;
    @SerializedName("child")
    private String child;
    @SerializedName("period")
    private String period;
    @SerializedName("year")
    private String year;
    @SerializedName("date")
    private String date;
    @SerializedName("employeeName")
    private String employeeName;
    @SerializedName("trait")
    private String trait;
    @SerializedName("problem")
    private String problem;
    @SerializedName("note")
    private String note;
    @SerializedName("imageBefore")
    private ArrayList<String> imageBefore;
    @SerializedName("imageAfter")
    private ArrayList<String> imageAfter;

    public Maintain(String mother, String child, String period, String year, String date, String employeeName,
                    String trait, String problem, String note, ArrayList<String> imageBefore, ArrayList<String> imageAfter) {
        this.mother = mother;
        this.child = child;
        this.period = period;
        this.year = year;
        this.date = date;
        this.employeeName = employeeName;
        this.trait = trait;
        this.problem = problem;
        this.note = note;
        this.imageBefore = imageBefore;
        this.imageAfter = imageAfter;
    }

    public Maintain(String period, String year, String date, String employeeName, String trait,
                    String problem, String note, ArrayList<String> imageBefore, ArrayList<String> imageAfter) {
        this.period = period;
        this.year = year;
        this.date = date;
        this.employeeName = employeeName;
        this.trait = trait;
        this.problem = problem;
        this.note = note;
        this.imageBefore = imageBefore;
        this.imageAfter = imageAfter;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    @Override
    public String toString() {
        return "Maintain{" +
                "period='" + period + '\'' +
                ", year='" + year + '\'' +
                ", date='" + date + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", trait='" + trait + '\'' +
                ", problem='" + problem + '\'' +
                ", note='" + note + '\'' +
                ", imageBefore=" + imageBefore +
                ", imageAfter=" + imageAfter +
                '}';
    }
}
