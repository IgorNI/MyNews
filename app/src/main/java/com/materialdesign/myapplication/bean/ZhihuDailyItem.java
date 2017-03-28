package com.materialdesign.myapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ni on 2017/3/23.
 *
 * title : 新闻标题
 * images : 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
 * type : 作用未知
 * id : url 与 share_url 中最后的数字（应为内容的 id）
 */

public class ZhihuDailyItem {
    @SerializedName("images")
    private String[] images;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    private String date;
    public boolean hasFadedIn = false;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getImages() {
        return images;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
