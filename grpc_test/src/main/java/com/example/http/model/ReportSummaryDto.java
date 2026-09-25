package com.example.http.model;

public class ReportSummaryDto {

    private int count;
    private double average;

    public ReportSummaryDto() {
    }

    public ReportSummaryDto(int count, double average) {
        this.count = count;
        this.average = average;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
