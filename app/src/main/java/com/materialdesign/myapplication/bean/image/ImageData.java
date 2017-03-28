package com.materialdesign.myapplication.bean.image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ni on 2017/3/23.
 */

public class ImageData {
    @SerializedName("images")
    private ArrayList<ImageItem> images;

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }
}

