package com.courtesycarsredhill.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.model.LoginData;
import com.courtesycarsredhill.ui.signin.LoginActivity;
import com.google.gson.Gson;


public class SessionManager {

    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_ACCOUNT_STATUS = "account_status";
    public static final String IS_LOGIN = "is_login";
    public static final String KEY_CURRENCY = "user_currency";
    private static final String KEY_USER_MODEL = "user_model";
    public static final String KEY_USER_COUNTRY_FLAG = "user_country_flag";
    private static final String KEY_PREF = "KEY_PREF";
    public static final String KEY_LAST_EQIPMENT_DATE = "key_last_eqipment_date";
    public  static final String KEY_IS_SAVED_EQUIPMENT_DETAIL = "key_is_saved_equipment_detail";
    private SecurePreferences pref;
    private Context mContext;

    public SessionManager(Context context) {
        String PREF_NAME = context.getResources().getString(R.string.app_name);
        pref = SecurePreferences.getInstance(context, PREF_NAME);
        mContext = context;
    }

    public void storeUserDetail(LoginData userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel); // myObject - instance of UserModel
        pref.putString(KEY_USER_MODEL, json);
        pref.putBoolean(IS_LOGIN, true);

        pref.commit();
    }

    public LoginData getUserDetail() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER_MODEL, null);
        return gson.fromJson(json, LoginData.class);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getDataByKey(String Key) {
        return getDataByKey(Key, "");
    }

    public String getDataByKey(String Key, String DefaultValue) {
        String returnValue;
        if (pref.containsKey(Key)) {
            returnValue = pref.getString(Key, DefaultValue);
        } else {
            returnValue = DefaultValue;
        }
        return returnValue;
    }

    public Boolean getDataByKey(String Key, boolean DefaultValue) {
        if (pref.containsKey(Key)) {
            return pref.getBoolean(Key, DefaultValue);
        } else {
            return DefaultValue;
        }
    }

    public int getDataByKey(String Key, int DefaultValue) {
        if (pref.containsKey(Key)) {
            return pref.getInt(Key, DefaultValue);
        } else {
            return DefaultValue;
        }
    }

    public void storeDataByKey(String key, int Value) {
        pref.putInt(key, Value);
        pref.commit();
    }

    public void storeDataByKey(String key, String Value) {
        pref.putString(key, Value);
        pref.commit();
    }

    public void storeDataByKey(String key, boolean Value) {
        pref.putBoolean(key, Value);
        pref.commit();
    }

    public void clearDataByKey(String key) {
        pref.removeValue(key);
    }

    // region Helper Methods
    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
    }

    // Reset Challenge
    public void logoutUser(Context context) {
        storeDataByKey(SessionManager.IS_LOGIN, false);
//        storeDataByKey(SessionManager.KEY_USER_MODEL, null);
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(KEY_USER_MODEL)
                .apply();

        // Redirect to Welcome Screen
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("BlankIntent", "BlankIntent");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
