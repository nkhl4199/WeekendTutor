package com.chetan.wt;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    String date;
    String name;
    String amount;
    String course_name;

    public Transaction() {}

    public Transaction(String date, String name, String amount, String course_name) {
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.course_name = course_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
