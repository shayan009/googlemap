package com.debasish.demomap;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by shayan on 10-04-2018.
 */

public class ReverseGeoCoding {
    private String address1, address2, city, state, country, county, PIN;
    private static final String LOG_TAG = ReverseGeoCoding.class.getSimpleName();
private Activity activity;
    public ReverseGeoCoding( Activity activity,double latitude, double longitude) {
        init();
        this.activity=activity;
        retrieveData(latitude, longitude);
    }

    private void retrieveData(double latitude, double longitude) {
        try {
            String responseFromHttpUrl = getResponseFromHttpUrl(buildUrl(latitude, longitude));
            JSONObject jsonResponse = new JSONObject(responseFromHttpUrl);
            String status = jsonResponse.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                JSONArray results = jsonResponse.getJSONArray("results");
                JSONObject zero = results.getJSONObject(0);
                JSONArray addressComponents = zero.getJSONArray("address_components");
              String formatadd= zero.getString("formatted_address");
                ArrayList<String> strings=new ArrayList<>();
                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject zero2 = addressComponents.getJSONObject(i);
                    String longName = zero2.getString("long_name");
                    JSONArray types = zero2.getJSONArray("types");
                    String type = types.getString(0);


                    if (!TextUtils.isEmpty(longName)) {
                        if (type.equalsIgnoreCase("street_number")) {
                            address1 = longName + " ";

                        } else if (type.equalsIgnoreCase("route")) {
                            address1 = address1 + longName;
                        } else if (type.equalsIgnoreCase("sublocality")) {
                            address2 = longName;
                        } else if (type.equalsIgnoreCase("locality")) {
                            // address2 = address2 + longName + ", ";
                            city = longName;
                        } else if (type.equalsIgnoreCase("administrative_area_level_2")) {
                            county = longName;
                        } else if (type.equalsIgnoreCase("administrative_area_level_1")) {
                            state = longName;
                        } else if (type.equalsIgnoreCase("country")) {
                            country = longName;
                        } else if (type.equalsIgnoreCase("postal_code")) {
                            PIN = longName;
                        }
                    }
                }
                strings.add(formatadd);
                strings.add(address1);
                strings.add(address2);
                strings.add(city);
                strings.add(county);
                strings.add(state);
                strings.add(country);

                strings.add(PIN);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        address1 = "";
        address2 = "";
        city = "";
        state = "";
        country = "";
        county = "";
        PIN = "";
    }
    private String createUrl(double latitude, double longitude) throws UnsupportedEncodingException {
      return "https://maps.googleapis.com/maps/api/geocode/json?" + "latlng=" + latitude + "," + longitude + "&key=" + activity.getResources().getString(R.string.map_apiid);
    }
    private URL buildUrl(double latitude, double longitude) {

        try {
            Log.w(TAG, "buildUrl: "+createUrl(latitude,longitude));
            return new URL(createUrl(latitude,longitude));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "can't construct location object");
            return null;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public String getAddress1() { return address1; }

    public String getAddress2() { return address2; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public String getCountry() { return country; }

    public String getCounty() { return county; }

    public String getPIN() { return PIN; }

}
