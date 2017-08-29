package com.stephen.paogou.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by StephenChen on 2017/8/28.
 */

public class HttpUtil {

    private String mUrl;
    private Map<String, String> mParam;
    private HttpResponse mHttpResponse;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private final OkHttpClient client = new OkHttpClient();

    public interface HttpResponse{
        void onSuccess(Object object);
        void onFail(String error);
    }

    public HttpUtil(HttpResponse response) {
        mHttpResponse = response;
    }

    public void sendPostHttp(String url, Map<String, String> param){
        sendHttp(url, param, true);
    }

    public void sendGetHttp(String url, Map<String, String> param){
        sendHttp(url, param, false);
    }

    private void sendHttp(String url, Map<String, String> prarm, boolean isPost){
        mUrl = url;
        mParam = prarm;
        run(isPost);
    }

    /**
     * 发送请求
     * @param isPost
     */
    private void run(boolean isPost) {

        final Request request = createReqsuet(isPost);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpResponse != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mHttpResponse != null){

                    try {
                        final String result = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!response.isSuccessful()){
                                    mHttpResponse.onFail("请求失败:code"+response);
                                }else {
                                    mHttpResponse.onSuccess(result);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mHttpResponse.onFail("结果转换失败");
                            }
                        });
                    }

//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!response.isSuccessful()) {
//                                mHttpResponse.onFail("请求失败:code"+response);
//                            }else {
//                                try {
//                                    mHttpResponse.onSuccess(response.body().string());
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                    mHttpResponse.onFail("结果转换失败");
//                                }
//                            }
//                        }
//                    });
                }
            }
        });
    }

    /**
     * 创建请求
     * @param isPost
     * @return
     */
    private Request createReqsuet(boolean isPost){

        Request request;

        if (isPost){

            if (mParam == null || mParam.isEmpty()){

                request = new Request.Builder().url(mUrl).build();

            }else {

                MultipartBody.Builder requsetBodyBuilder = new MultipartBody.Builder();
                requsetBodyBuilder.setType(MultipartBody.FORM);

                // 遍历参数
                Iterator<Map.Entry<String, String>> iterator = mParam.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, String> entry = iterator.next();
                    requsetBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                request = new Request.Builder().url(mUrl).post(requsetBodyBuilder.build()).build();
            }


        }else {

            String urlStr = mParam == null || mParam.isEmpty() ? mUrl : mUrl + "?" + mapParamToString(mParam);
            request = new Request.Builder()
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json").url(urlStr).build();
        }

        return request;
    }

    /**
     * 组合参数
     * @param param
     * @return
     */
    private  String mapParamToString(Map<String, String> param){

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str = stringBuilder.toString().substring(0, stringBuilder.length()-1);
        return str;

    }

}
