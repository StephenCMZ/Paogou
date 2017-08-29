package com.stephen.paogou.mvp.presenter;

import android.content.Context;

/**
 * Created by StephenChen on 2017/8/29.
 */

public class BasePresenter {

    Context context;
    public void attach(Context context){
        this.context = context;
    }

    public void onPause(){}
    public void onResume(){}

    public void onDestroy(){
        context= null;
    }

}
