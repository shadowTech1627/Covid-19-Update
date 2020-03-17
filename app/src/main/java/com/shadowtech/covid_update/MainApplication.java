package com.shadowtech.covid_update;

import android.app.Application;


public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private synchronized static void createAppInstance(MainApplication mainApplication) {
        sInstance = mainApplication;
    }

    public static MainApplication getInstance() {
        return sInstance;
    }


}
