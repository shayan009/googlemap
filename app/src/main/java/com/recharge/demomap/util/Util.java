package com.recharge.demomap.util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.recharge.demomap.app.MainApp;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

public class Util {

    public static DateFormat formatSimpleDate = new SimpleDateFormat("MM/dd/yyyy");

    public static DateFormat formatFullDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM, yyyy");
    public static final DecimalFormat RATING_FORMAT = new DecimalFormat("#.#");

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
                    }
        return "";
    }


 
    public static String filterError(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof NetworkErrorException)
            return "Please check your network connection and try again";

        return e.getCause().getMessage();
    }

    public static String getFormattedDate(String secondValue) {
        if (TextUtils.isEmpty(secondValue) && !TextUtils.isDigitsOnly(secondValue))
            return "NA";

        return DATE_FORMAT.format(Long.parseLong(secondValue) * 1000);
    }

    public static String getFormattedDate2(String secondValue) {
        if (TextUtils.isEmpty(secondValue) && !TextUtils.isDigitsOnly(secondValue))
            return "NA";

        return DATE_FORMAT.format(Long.parseLong(secondValue));
    }

    // slide the view from below itself to the current position
    public static void slideUp(View view){
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
    // slide the view from its current position to below itself
    public static void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public static int generateId(){
        Random rnd = new Random();
        return rnd.nextInt(999999)+ Integer.parseInt(MainApp.getUserPref().getId());
    }
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static String getDate(){

        try {
            return formatSimpleDate.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
