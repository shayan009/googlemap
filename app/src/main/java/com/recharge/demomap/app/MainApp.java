package com.recharge.demomap.app;

import android.app.Application;
import android.content.Context;

import com.recharge.demomap.data.DataManager;
import com.recharge.demomap.data.IDataManager;
import com.recharge.demomap.data.firebase.Firebase;
import com.recharge.demomap.data.firebase.IFirebase;
import com.recharge.demomap.pref.UserPref;


public class MainApp extends Application {

    public static final String TAG = MainApp.class.getSimpleName();

    private static IDataManager dataManager;
    private static UserPref userPref;

    private static Context context;
    private static IFirebase firebase;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static IDataManager dataManager() {
        if (dataManager == null)
            dataManager = new DataManager();

        return dataManager;
    }

    public static IFirebase getFirebase(){
        if (firebase == null)
            firebase = new Firebase();
        return firebase;
    }
    public static UserPref getUserPref() {
        if (userPref == null)
            userPref = new UserPref(context);

        return userPref;
    }
}
