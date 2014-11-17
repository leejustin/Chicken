package com.sinkwater.chicken;

/**
 * Created by justin on 11/16/14.
 */
public class GPSData {

    private Double longitude;
    private Double latitude;

    public GPSData(Double longitude, Double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Mutators
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    //Accessors
    public Double getLongitude() {
        return this.longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

}
