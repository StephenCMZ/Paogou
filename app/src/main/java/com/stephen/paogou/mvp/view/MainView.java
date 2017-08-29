package com.stephen.paogou.mvp.view;

/**
 * Created by StephenChen on 2017/8/29.
 */

public interface MainView extends LoadingView {

    void showToast(String msg);
    void updateView();

}
