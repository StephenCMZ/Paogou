package com.stephen.paogou.config;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by StephenChen on 2017/8/28.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // imageLoader
        Fresco.initialize(this);
    }
}
