package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import com.google.gson.annotations.SerializedName;

public class Station {
    @SerializedName("sname")
    private String stationName;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    public Station(String stationName, double latitude, double longitude) {
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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
}
