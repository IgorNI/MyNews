package com.materialdesign.myapplication.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ni on 2017/3/23.
 */

public class ApiManager {
    public static ApiManager apiManager;
    public ZhihuApi zhihuApi;
    public NewsApi newsApi;
    public GoogleMapLocationApi googleMapLocationApi;
    private Object zhihuMonitor = new Object();

    // 单例模式中的懒汉模式，双重检查枷锁，保证线程安全
    public static ApiManager getInstance() {
    if (apiManager == null) {
        synchronized (ApiManager.class) {
            if (apiManager == null) {
                apiManager = new ApiManager();
            }
        }
    }
    return apiManager;
}

    public ZhihuApi getZhihuApiService() {
        if (zhihuApi == null) {
            synchronized (zhihuMonitor) {
                if (zhihuApi == null) {
                    zhihuApi = new Retrofit.Builder()
                            .baseUrl("http://news-at.zhihu.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ZhihuApi.class);
                }
            }
        }
        return zhihuApi;
    }

    public NewsApi getNewsApiService() {
        if (newsApi == null) {
            synchronized (zhihuMonitor) {
                if (newsApi == null) {
                    newsApi = new Retrofit.Builder()
                            .baseUrl("http://c.m.163.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(NewsApi.class);
                }
            }
        }
        return newsApi;
    }

    public GoogleMapLocationApi getLocationService() {
        if (googleMapLocationApi == null) {
            synchronized (zhihuMonitor) {
                if (googleMapLocationApi == null) {
                    googleMapLocationApi = new Retrofit.Builder()
                            .baseUrl("http://maps.google.com/maps/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(GoogleMapLocationApi.class);
                }
            }
        }
        return googleMapLocationApi;
    }
}
