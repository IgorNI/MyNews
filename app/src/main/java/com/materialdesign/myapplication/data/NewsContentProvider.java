package com.materialdesign.myapplication.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/6
 */

public class NewsContentProvider extends ContentProvider {
    
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int NEWS = 100;
    private static final int NEWS_WITH_TITLE = 102;
    private static final int ZHIHU_NEWS_TITLE_AND_IS_READ  = 101;
    private static final int TOPNEWS_TITLE_AND_IS_READ = 201;
    private NewsDBHelper mOpenHelper;
    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDBHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, NewsContract.PATH_NEWS, NEWS);
        matcher.addURI(authority, NewsContract.PATH_TOPNEWS + "/*", ZHIHU_NEWS_TITLE_AND_IS_READ);// "/*"表示后面跟的是字符串和数字
        matcher.addURI(authority, NewsContract.PATH_ZHIHU_NEWS + "/*", TOPNEWS_TITLE_AND_IS_READ);// "/*"表示后面跟的是字符串和数字

        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            // "zhihu/*"
            // 以下这些是传递给数据库的参数
            case ZHIHU_NEWS_TITLE_AND_IS_READ:
            {
                retCursor = db.query(NewsContract.ZhihuNewsEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
                break;
            }
            // "top/*"
            case TOPNEWS_TITLE_AND_IS_READ: {
                retCursor = db.query(NewsContract.TopNewsEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
                break;
            }
            // "news"
            case NEWS: {
                retCursor = db.query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection, selection, selectionArgs, sortOrder, null, null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            // Student: Uncomment and fill out these two cases
            case ZHIHU_NEWS_TITLE_AND_IS_READ:
               // 单条数据
                return NewsContract.ZhihuNewsEntry.CONTENT_ITEM_TYPE;
            case TOPNEWS_TITLE_AND_IS_READ:
                // 单条数据
                return NewsContract.TopNewsEntry.CONTENT_ITEM_TYPE;
            case NEWS:
                // 返回多条数据
                return NewsContract.NewsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id;
        switch ( buildUriMatcher().match(uri)) {
            case ZHIHU_NEWS_TITLE_AND_IS_READ:
                _id = db.insert(NewsContract.ZhihuNewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.ZhihuNewsEntry.buildZhihuUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case TOPNEWS_TITLE_AND_IS_READ:
                _id = db.insert(NewsContract.TopNewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.TopNewsEntry.buildTopUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case NEWS:
                _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.NewsEntry.buildNewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new android.database.SQLException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
