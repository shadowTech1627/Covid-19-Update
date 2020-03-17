package com.shadowtech.covid_update.model;

import com.google.gson.annotations.SerializedName;

public class CountryLatLngModel {

    /**
     * country : AD
     * latitude : 42.546245
     * longitude : 1.601554
     * name : Andorra
     */

    @SerializedName("country")
    private String country;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
