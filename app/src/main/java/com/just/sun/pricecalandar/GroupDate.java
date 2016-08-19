package com.just.sun.pricecalandar;

/**
 * Created by pc-004 on 2015/9/18.
 */
public class GroupDate {
    private int year;
    private int month;

    public GroupDate() {
    }

    public GroupDate(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "GroupDate{" +
                "year=" + year +
                ", month=" + month +
                '}';
    }
}
