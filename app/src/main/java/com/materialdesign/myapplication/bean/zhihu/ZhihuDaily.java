package com.materialdesign.myapplication.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ni on 2017/3/23.
 *
 * date : 日期
 * top_stories : 界面顶部 ViewPager 滚动显示的显示内容
 * storied : 当日新闻
 */

public class ZhihuDaily {
    @SerializedName("date")
    private String date;
    @SerializedName("top_stories")
    private ArrayList<ZhihuDailyItem> mZhihuDailyItems;
    @SerializedName("stories")
    private ArrayList<ZhihuDailyItem> mZhihuStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDailyItem> getmZhihuDailyItems() {
        return mZhihuDailyItems;
    }

    public void setmZhihuDailyItems(ArrayList<ZhihuDailyItem> mZhihuDailyItems) {
        this.mZhihuDailyItems = mZhihuDailyItems;
    }

    public ArrayList<ZhihuDailyItem> getmZhihuStories() {
        return mZhihuStories;
    }

    public void setmZhihuStories(ArrayList<ZhihuDailyItem> mZhihuStories) {
        this.mZhihuStories = mZhihuStories;
    }
}
