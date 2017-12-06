package com.materialdesign.myapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.materialdesign.myapplication.config.Config;

/**
 * @Description : 数据库存储工具
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/5
 */

public class DBUtils {
    /**创建表，包括主键 id，
     * 标题：key
     * 是否已读： is_read
     * 内容：content
     * 图片：image
     *
     * */

    public static final String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists %s " +
            "(id integer  primary key autoincrement,key text unique,is_read integer,content text,image text)";
    public static final String CREATE_BOTH_TABLE_IF_NOT_EXISTS = "create table if not exists %s " +
            "(id integer  primary key autoincrement,key text unique,is_read integer,content text,image text,type text)";
    private static DBUtils sDBUtis;
    private SQLiteDatabase mSQLiteDatabase;

    private DBUtils(Context context) {
        mSQLiteDatabase = new DBHelper(context, Config.DB__IS_READ_NAME + ".db").getWritableDatabase();
    }

    public static synchronized DBUtils getDB(Context context) {
        if (sDBUtis == null)
            sDBUtis = new DBUtils(context);
        return sDBUtis;
    }


    public void insertHasRead(String table, String key, int value,String text, String imageUrl) {
        Cursor cursor = mSQLiteDatabase.query(table, null, null, null, null, null, "id asc");
        if (cursor.getCount() > 200 && cursor.moveToNext()) {
            mSQLiteDatabase.delete(table, "id=?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id")))});
        }
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.KEY, key);
        contentValues.put(Config.IS_READ, value);
        contentValues.put(Config.CONTENT, text);
        contentValues.put(Config.IMAGE, imageUrl);
        mSQLiteDatabase.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean isRead(String table, String key, int value) {
        boolean isRead = false;
        Cursor cursor = mSQLiteDatabase.query(table, null, "key=?", new String[]{key}, null, null, null);
        if (cursor.moveToNext() && (cursor.getInt(cursor.getColumnIndex("is_read")) == value)) {
            isRead = true;
        }
        cursor.close();
        return isRead;
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name) {
            super(context, name, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(String.format(CREATE_BOTH_TABLE_IF_NOT_EXISTS,Config.NEWS));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS, Config.ZHIHU));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS, Config.TOPNEWS));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
