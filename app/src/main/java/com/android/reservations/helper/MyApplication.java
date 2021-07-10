package com.android.reservations.helper;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LocaleUtils.onCreate(this, "ar");
        Hawk.init(getBaseContext()).build();

    }
}
