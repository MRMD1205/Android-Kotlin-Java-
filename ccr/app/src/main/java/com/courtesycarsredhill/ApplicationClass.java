package com.courtesycarsredhill;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class ApplicationClass extends MultiDexApplication {

    private static ApplicationClass context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }


    public static Context getAppContext() {
        return context;
    }
}
