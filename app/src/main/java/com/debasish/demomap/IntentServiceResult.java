package com.debasish.demomap;

/**
 * Created by shayan on 08-04-2018.
 */

public class IntentServiceResult {
    public double latitude;
    public double longitude;

    public IntentServiceResult(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
