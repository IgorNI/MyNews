package com.materialdesign.myapplication.utils;

import android.graphics.Bitmap;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/7
 */

public class GlideUtils {
    private GlideUtils() {
    }

    public static Bitmap getBitmap(GlideDrawable glideDrawable) {
        if (glideDrawable instanceof GlideBitmapDrawable) {
            return ((GlideBitmapDrawable) glideDrawable).getBitmap();
        } else if (glideDrawable instanceof GifDrawable) {
            return ((GifDrawable) glideDrawable).getFirstFrame();
        }
        return null;
    }
}
