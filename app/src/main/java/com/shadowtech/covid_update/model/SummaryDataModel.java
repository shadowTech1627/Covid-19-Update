package com.shadowtech.covid_update.model;

import com.google.gson.annotations.SerializedName;

public class SummaryDataModel {

    /**
     * cases : 169907
     * deaths : 6520
     * recovered : 77776
     */

    @SerializedName("cases")
    private int cases;
    @SerializedName("deaths")
    private int deaths;
    @SerializedName("recovered")
    private int recovered;

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    @Override
    public String toString() {
        return "SummaryDataModel{" +
                "cases=" + cases +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                '}';
    }
}
