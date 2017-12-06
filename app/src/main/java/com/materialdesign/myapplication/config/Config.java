package com.materialdesign.myapplication.config;

import android.net.Uri;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/5
 */

public class Config {
    public static final String DB__IS_READ_NAME = "IsRead";
    public static final String NEWS = "news";
    public static final String ZHIHU = "zhihu";
    public static final String TOPNEWS= "topnews";
    public static final String KEY = "key";
    public static final String IS_READ = "is_read";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String TYPE = "type";

    public static final Uri CONTENT_URI  = Uri.parse("content://com.zhuanghongji.MyContentProvider");
}
