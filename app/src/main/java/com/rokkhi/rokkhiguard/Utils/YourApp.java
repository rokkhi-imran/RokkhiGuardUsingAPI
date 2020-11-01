package com.rokkhi.rokkhiguard.Utils;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class YourApp extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }
}