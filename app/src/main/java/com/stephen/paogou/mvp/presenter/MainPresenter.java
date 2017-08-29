package com.stephen.paogou.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.stephen.paogou.mvp.model.ImageModel;
import com.stephen.paogou.mvp.view.MainView;
import com.stephen.paogou.utils.CacheUtil;
import com.stephen.paogou.utils.HttpUtil;
import com.stephen.paogou.utils.ParseUtil;
import com.stephen.paogou.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StephenChen on 2017/8/29.
 */

public class MainPresenter extends BasePresenter {

    private final String CACHETAG = "IMAGEURLS";
    private final String homeUrl = "https://www-paogou.cc/";
    private int currentImgIndex;
    private List<String> mHtmlUrls;
    private List<String> mImageUrls;
    private CacheUtil cacheUtil;

    MainView mainView;
    Context context;

    public MainPresenter(@NonNull MainView mainView, @NonNull Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    public List<ImageModel> getImageList(){

        if (mImageUrls == null || mImageUrls.isEmpty()) return null;
        List<ImageModel> list = new ArrayList<>(mImageUrls.size());
        for (String url : mImageUrls){
            ImageModel model = new ImageModel();
            model.setImageUrl(url);
            list.add(model);
        }
        return list;
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    public void getList(){
        mainView.showLoading();

        getCacheImageUrls();
        if (mImageUrls != null && !mImageUrls.isEmpty()) mainView.updateView();

        getHtmlList();
    }

    private void getHtmlList(){

        HttpUtil httpUtil = new HttpUtil(new HttpUtil.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String htmlStr = object.toString();
                if (StringUtil.isEmpty(htmlStr)){
                    mainView.showToast("请求失败:homeHtml==null");
                    mainView.hidenLoading();
                }else {
                    List<String> htmlUrls = ParseUtil.paseHtmlUrls(htmlStr);
                    if (htmlUrls == null || htmlStr.isEmpty()){
                        mainView.showToast("请求失败:htmlUrls==null");
                        mainView.hidenLoading();
                    }else {
                        currentImgIndex = 0;
                        mHtmlUrls = htmlUrls;
                        mImageUrls = new ArrayList<>(mHtmlUrls.size());
                        getImageUrl();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                mainView.showToast(error);
                mainView.hidenLoading();
            }
        });
        httpUtil.sendGetHttp(homeUrl, null);
    }

    private void getImageUrl(){

        if (mHtmlUrls == null || mHtmlUrls.isEmpty() || currentImgIndex >= mHtmlUrls.size()){
            mainView.hidenLoading();
            if (mImageUrls != null && !mImageUrls.isEmpty()){
                mainView.updateView();
                cacheImageUrls();
            }else {
                mainView.showToast("请求失败:imageUrls==null");
            }

        }else {

            HttpUtil httpUtil = new HttpUtil(new HttpUtil.HttpResponse() {
                @Override
                public void onSuccess(Object object) {
                    String urlStr = object.toString();
                    if (!StringUtil.isEmpty(urlStr)){
                        String imageUrl = ParseUtil.paseImageUrl(urlStr);
                        if (!StringUtil.isEmpty(imageUrl)){
                            mImageUrls.add(imageUrl);
                        }
                    }
                    currentImgIndex++;
                    getImageUrl();
                }
                @Override
                public void onFail(String error) {
                    currentImgIndex++;
                    getImageUrl();
                }
            });
            httpUtil.sendGetHttp(mHtmlUrls.get(currentImgIndex), null);
        }
    }

    private void cacheImageUrls(){
        if (cacheUtil == null) cacheUtil = CacheUtil.get(context);
        if (mImageUrls != null && !mImageUrls.isEmpty()){
            String jsonUrls = JSON.toJSONString(mImageUrls);
            cacheUtil.put(CACHETAG, jsonUrls);
        }
    }

    private void getCacheImageUrls(){
        if (cacheUtil == null) cacheUtil = CacheUtil.get(context);
        String jsonUrls = cacheUtil.getAsString(CACHETAG);
        if (!StringUtil.isEmpty(jsonUrls)){
            mImageUrls = JSON.parseArray(jsonUrls, String.class);
        }
    }


}
