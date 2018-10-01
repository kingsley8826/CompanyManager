package com.example.tuanfpt.companymanager.models;

public class Period {
    private String period;
    private String year;

    public Period(String period, String year) {
        this.period = period;
        this.year = year;
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

    @Override
    public String toString() {
        return "Period{" +
                "period='" + period + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
