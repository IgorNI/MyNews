package com.materialdesign.myapplication.api;

import com.materialdesign.myapplication.bean.ZhihuDaily;
import com.materialdesign.myapplication.bean.ZhihuStory;
import com.materialdesign.myapplication.bean.image.ImageData;
import com.materialdesign.myapplication.bean.image.ImageResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ni on 2017/3/23.
 */

public interface ZhihuApi {
    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDay();
    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getDaily(@Path("date") String date);
    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);
    @GET("http://lab.zuimeia.com/wallpaper/category/1/?page_size=1")
    Observable<ImageResponse> getImage();
}
