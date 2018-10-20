package com.brandonhessler.java.mvr;

import java.util.Date;

/**
 * Created by Brandon on 1/8/2016.
 */
public class Charge {

    private Date date;
    private double amount;

    public Charge() {
        this.date = new Date();
        this.amount = 0;
    }

    public Charge(double amount) {
        this.amount = amount;
    }

    public Charge(Date date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
