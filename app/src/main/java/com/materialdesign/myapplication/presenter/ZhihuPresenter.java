package com.materialdesign.myapplication.presenter;

/**
 * Created by ni on 2017/3/23.
 */

public interface ZhihuPresenter extends BasePresenter {
//    List<String> (String[] array, int amount);
    void getLastZhihuNews();
    void getTheDaily(String date);
}
