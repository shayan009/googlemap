package com.debasish.demomap.app;

import android.app.Application;
import android.content.Context;

import com.debasish.demomap.data.DataManager;
import com.debasish.demomap.data.IDataManager;


import com.debasish.demomap.firebase.FirebaseManager;
import com.debasish.demomap.firebase.IFirebaseManager;
import com.debasish.demomap.google_map.IMapManager;
import com.debasish.demomap.google_map.MapManager;
import com.debasish.demomap.pref.UserPref;


public class MainApp extends Application {

    public static final String TAG = MainApp.class.getSimpleName();

    private static IDataManager dataManager;
    private static UserPref userPref;

    private static Context context;
    private static IFirebaseManager firebase;
    private static IMapManager mapManager;

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

    public static IMapManager getMapManger(){
        if (mapManager == null)
            mapManager = new MapManager(context);
        return mapManager;
    }

    public static IFirebaseManager getFirebase(){
        if (firebase == null)
            firebase = new FirebaseManager();
        return firebase;
    }
    public static UserPref getUserPref() {
        if (userPref == null)
            userPref = new UserPref(context);

        return userPref;
    }
}
