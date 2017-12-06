package com.materialdesign.myapplication.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/6
 */

public class NewsContract {
    public static final String CONTENT_AUTHORITY = "com.materialdesign.myapplication";
    public static final String PATH_ZHIHU_NEWS = "zhihu";
    public static final String PATH_TOPNEWS = "topnews";
    public static final String PATH_NEWS = "news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // 知乎表
    public static final class ZhihuNewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "zhihu";
        public static final String KEY = "key";
        public static final String IS_READ = "is_read";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ZHIHU_NEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ZHIHU_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ZHIHU_NEWS;
        public static Uri buildZhihuUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildZhihuIsRead(String key) {
            return CONTENT_URI.buildUpon().appendPath(key).build();
        }
    }

    // 网易表
    public static final class TopNewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "topnews";
        public static final String KEY = "key";
        public static final String IS_READ = "is_read";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOPNEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPNEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOPNEWS;

        public static Uri buildTopUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTopIsRead(String key) {
            return CONTENT_URI.buildUpon().appendPath(key).build();
        }
    }

    public static final class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String KEY = "key";
        public static final String IS_READ = "is_read";
        public static final String CONTENT = "content";
        public static final String IMAGE = "image";
        public static final String TYPE = "type";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNewsIsRead(String key) {
            return CONTENT_URI.buildUpon().appendPath(key).build();
        }
    }
}
