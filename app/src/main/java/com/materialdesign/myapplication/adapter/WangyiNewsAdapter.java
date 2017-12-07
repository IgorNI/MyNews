package com.materialdesign.myapplication.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.activity.MainActivity;
import com.materialdesign.myapplication.activity.NewsDetailActivity;
import com.materialdesign.myapplication.bean.news.NewsBean;
import com.materialdesign.myapplication.data.NewsContract;
import com.materialdesign.myapplication.utils.NetWorkUtils;
import com.materialdesign.myapplication.widget.BadgedFourThreeImageView;

import java.util.ArrayList;

/**
 * Description :
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */

public class WangyiNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MainActivity.loadMore{

    private Context mContext;
    private static final int TYPE_LOADING_MORE = -1;
    private static final int TYPE_NORMAL_ITEM = 1;
    private ArrayList<NewsBean> newsItems = new ArrayList<>();
    private boolean showLoadingMore = false;
    private Activity mActivity;



    public WangyiNewsAdapter(Context context, FragmentActivity activity) {
        this.mContext = context;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL_ITEM:
                return new NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.news_item_layout,parent,false));
            case TYPE_LOADING_MORE:
                return new LoadingMoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.loading_more_lauout,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_NORMAL_ITEM:
                bindItemViewHolder((NewsViewHolder)holder,position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingMoreViewHolder((LoadingMoreViewHolder)holder,position);
                break;
        }
    }

    private void bindLoadingMoreViewHolder(LoadingMoreViewHolder holder, int position) {
        holder.progressBar.setVisibility(showLoadingMore? View.VISIBLE : View.INVISIBLE);
    }

    private void bindItemViewHolder(final NewsViewHolder holder, int position) {
        final NewsBean newsBean = newsItems.get(holder.getAdapterPosition());
//        if (DBUtils.getDB(mContext).isRead(Config.TOPNEWS, newsBean.getTitle(), 1)){
            ContentValues values = new ContentValues();
            values.put(NewsContract.TopNewsEntry.KEY,newsBean.getTitle());
            values.put(NewsContract.TopNewsEntry.IS_READ,1);
            Uri uri = NewsContract.TopNewsEntry.buildTopIsRead(newsBean.getTitle());
            Cursor cursor = mContext.getContentResolver().query(uri,new String[]{"key","is_read"},"key=?",new String[]{newsBean.getTitle()},null);
            if (cursor.moveToFirst()) {
                int isread;
                do {
                    // 获取字段的值
                    isread = cursor.getInt(cursor.getColumnIndex("is_read"));
                    if (isread == 1) {
                        holder.textView.setTextColor(Color.GRAY);
                        holder.sourceTextview.setTextColor(Color.GRAY);
                    }else {
                        holder.textView.setTextColor(Color.BLACK);
                        holder.sourceTextview.setTextColor(Color.BLACK);
                    }
                } while (cursor.moveToNext());
            }else {
                holder.textView.setTextColor(Color.BLACK);
                holder.sourceTextview.setTextColor(Color.BLACK);
            }
//        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textView.setTextColor(Color.GRAY);
                holder.sourceTextview.setTextColor(Color.GRAY);
                newsDetailActivity(newsBean,holder);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textView.setTextColor(Color.GRAY);
                holder.sourceTextview.setTextColor(Color.GRAY);
                newsDetailActivity(newsBean,holder);
            }
        });

        holder.textView.setText(newsBean.getTitle());
        holder.sourceTextview.setText(newsBean.getSource());
        Glide.with(mContext)
                .load(newsBean.getImgsrc())
                .centerCrop()
                .into(holder.imageView);
    }

    private void newsDetailActivity(NewsBean newsBean, NewsViewHolder holder) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("docid", newsBean.getDocid());
        intent.putExtra("title", newsBean.getTitle());
        intent.putExtra("image", newsBean.getImgsrc());
        if (NetWorkUtils.isNetworkConnected(mContext)) {
            mContext.startActivity(intent);
        }else {
            Toast.makeText(mContext,mContext.getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public void clearData() {
        newsItems.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<NewsBean> newsList) {
        newsList.remove(0);// 移除顶端的数据
        newsItems.addAll(newsList);
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        final TextView textView;
        final LinearLayout linearLayout;
        final TextView sourceTextview;
        BadgedFourThreeImageView imageView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            imageView = (BadgedFourThreeImageView) itemView.findViewById(R.id.item_image_id);
            textView = (TextView) itemView.findViewById(R.id.item_text_id);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.news_item_layout);
            sourceTextview= (TextView) itemView.findViewById(R.id.item_text_source_id);
        }
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }
    @Override
    public void loadStart() {
        if (showLoadingMore) return;
        showLoadingMore = true;
        notifyItemChanged(getLoadingMoreItemPosition());
    }

    @Override
    public void loadFinish() {
        if (!showLoadingMore) return;
        final int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }

    private class LoadingMoreViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingMoreViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {

            return TYPE_NORMAL_ITEM;
        }
        return TYPE_LOADING_MORE;
    }
    private int getDataItemCount() {

        return newsItems.size();
    }
}
