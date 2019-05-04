package com.rokkhi.rokkhiguard;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;


public class RokkhiGuard extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());


    }
}
