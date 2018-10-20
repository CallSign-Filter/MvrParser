package com.brandonhessler.java.mvr;

import java.util.Date;
import java.util.List;

/**
 * Created by Brandon on 1/8/2016.
 */
public class Client {

    private String name;
    private Date renewalDate;
    private double paidAtRenewal;
    private List<Charge> charges;

    public Client(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public double getPaidAtRenewal() {
        return paidAtRenewal;
    }

    public void setPaidAtRenewal(double paidAtRenewal) {
        this.paidAtRenewal = paidAtRenewal;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    @Override
    public String toString() {
        return getName();
    }
}
