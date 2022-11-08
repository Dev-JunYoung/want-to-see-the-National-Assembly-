package com.example.team_project.Adapter;

public class FavData {
    private String bill_name;
    private String step;

    public FavData(String bill_name, String step) {
        this.bill_name = bill_name;
        this.step = step;
    }

    public String getBill_name() {
        return bill_name;
    }

    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "FavData{" +
                "bill_name='" + bill_name + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
