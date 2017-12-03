package com.materialdesign.myapplication.api;

import com.materialdesign.myapplication.bean.city.CityInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/11/29
 */

public interface GoogleMapLocationApi {
    // 根据经纬度获取当前城市  json?latlng={locationStr}&language=zh-CN&sensor=true
    @GET("api/geocode/json")
    Observable<CityInfo> getCityInfo(@Query("latlng") String locationInfo,
                                     @Query("language") String language,
                                     @Query("sensor") String isSensor,
                                     @Query("ak") String key);
}
