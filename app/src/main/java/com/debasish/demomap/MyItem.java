package com.debasish.demomap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shayan on 08-04-2018.
 */

public class MyItem {

    private  LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }
}
