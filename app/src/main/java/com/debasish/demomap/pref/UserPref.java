package com.debasish.demomap.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by root on 5/12/17.
 */

public class UserPref {

    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static final String ADMIN_TYPE = "ADMIN_TYPE";
    public static final String PROFILE_DOCUMENT_URL = "PROFILE_DOCUMENT_URL";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG = UserPref.class.getSimpleName();

    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String REMEMBER_ME = "REMEMBER_ME";

    public static final String ID  = "ID";
    public static final String EMAIL = "EMAIL";
    public static final String FULL_NAME = "FULL_NAME";

    public static final String COMPANY_FULL_NAME = "COMPANY_FULL_NAME";
    public static final String COMPANY_ADDRESS = "COMPANY_ADDRESS";
    public static final String COMPANY_LOGO = "COMPANY_LOGO";
    public static final String COMPANY_COVER = "COMPANY_COVER";

    public static final String ADDRESS = "ADDRESS";
    public static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    public static final String ZIGGEO_TOKEN = "ZIGGEO_TOKEN";
    public static final String DOCUMENT = "DOCUMENT";
    public static final String PASSWORD = "PASSWORD";
    public static final String CHANGE = "CHANGE";

    public static final String TRIAL_TAKEN = "TRIAL_TAKEN";
    public UserPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void put(String key, String value) {
        editor.putString(key,value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        editor.putBoolean(key,value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key,"");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key,false);
    }

    public String getId() {
        if (!TextUtils.isEmpty(sharedPreferences.getString(ID, "")))
            return sharedPreferences.getString(ID, "0");
        else return "0";
    }

    public void clear(){
        editor.putBoolean(LOGIN_STATUS,false);
        editor.commit();
        //sharedPreferences.edit().clear().commit();
    }
}
