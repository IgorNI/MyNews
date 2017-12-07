package com.materialdesign.myapplication.utils;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/7
 */

public class MathUtils {
    private MathUtils() { }

    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }
}
