package com.materialdesign.myapplication.bean.image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ni on 2017/3/23.
 */

public class ImageResponse {
    @SerializedName("data")
    private ImageData data;

    public ImageData getData() {
        return data;
    }

    public void setData(ImageData data) {
        this.data = data;
    }
}
