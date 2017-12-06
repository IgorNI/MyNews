package com.materialdesign.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.activity.NewsHistoryDetailActivity;
import com.materialdesign.myapplication.activity.ZhihuHistoryDetailActivity;
import com.materialdesign.myapplication.config.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/7
 */

public class NewsHistoryAdapter extends CursorAdapter {
    public NewsHistoryAdapter(Context context, Cursor c,int flags) {
        super(context, c,flags);
    }

    public static class MyHolder{
        @BindView(R.id.item_text_id)
        TextView mTitleTv;
        @BindView(R.id.item_image_id)
        ImageView mImageIv;
        @BindView(R.id.zhihu_item_layout)
        LinearLayout mLinearLayout;
        public MyHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.zhihu_item_layout,parent,false);
        MyHolder viewHolder = new MyHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String title = cursor.getString(1);
        final String ImageUrl = cursor.getString(4);
        final String content = cursor.getString(3);
        final String type = cursor.getString(5);
        MyHolder viewHolder = (MyHolder) view.getTag();
        viewHolder.mTitleTv.setText(title);
        Glide.with(context)
                .load(ImageUrl)
                .centerCrop()
                .into(viewHolder.mImageIv);
        viewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",title);
                intent.putExtra("image",ImageUrl);
                intent.putExtra("content",content);
                if (Config.TOPNEWS.equals(type)) {
                    // 跳转到网易详情
                    intent.setClass(context, NewsHistoryDetailActivity.class);
                }else if (Config.ZHIHU.equals(type)) {
                    // 跳转到知乎详情
                    intent.setClass(context, ZhihuHistoryDetailActivity.class);
                }
                context.startActivity(intent);
            }
        });

    }
}
