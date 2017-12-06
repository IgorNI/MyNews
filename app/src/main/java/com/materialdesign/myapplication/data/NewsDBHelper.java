package com.materialdesign.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.materialdesign.myapplication.config.Config;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/6
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    /**创建表，包括主键 id，
     * 标题：key
     * 是否已读： is_read
     * 内容：content
     * 图片：image
     *
     * */

    public static final String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists %s " +
            "(id integer  primary key autoincrement,key text unique,is_read integer)";
    public static final String CREATE_BOTH_TABLE_IF_NOT_EXISTS = "create table if not exists %s " +
            "(_id integer  primary key autoincrement,key text unique,is_read integer,content text,image text,type text)";

    public NewsDBHelper(Context context) {
        super(context, Config.DB__IS_READ_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format(CREATE_BOTH_TABLE_IF_NOT_EXISTS, Config.NEWS));
        db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS, Config.ZHIHU));
        db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS, Config.TOPNEWS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Config.NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + Config.ZHIHU);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TOPNEWS);
        onCreate(db);
    }

}
