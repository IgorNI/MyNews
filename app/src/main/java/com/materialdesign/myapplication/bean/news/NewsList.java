package com.materialdesign.myapplication.bean.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Description : 新闻列表
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */

public class NewsList {
    @SerializedName("T1348647909107")
    ArrayList<NewsBean> newsList;
    public ArrayList<NewsBean> getNewsList() {
        return newsList;
    }
    public void setNewsList(ArrayList<NewsBean> newsList) {
        this.newsList = newsList;
    }
}
