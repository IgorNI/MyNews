package com.materialdesign.myapplication.presenter;

import java.util.List;

/**
 * Created by ni on 2017/3/23.
 */

public interface ZhihuPresrenter extends BasePresrenter{
    List<String> getCheeseList(String[] array, int amount);
    void getLastZhihuNews();
    void getTheDaily(String date);
}
