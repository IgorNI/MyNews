package com.materialdesign.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.activity.MainActivity;
import com.materialdesign.myapplication.activity.ZhihuDetailActivity;
import com.materialdesign.myapplication.bean.zhihu.ZhihuDailyItem;
import com.materialdesign.myapplication.utils.NetWorkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ni on 2017/3/23.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements MainActivity.loadMore{

    private TypedValue typeValue = new TypedValue();
    private boolean showLoadMore;
    private int background;
    private ArrayList<ZhihuDailyItem> zhihuDailyItems = new ArrayList<>();
    private Context mContext;
    private String imageUrl;

    public ItemAdapter(Context context) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground,typeValue,false);
        background = typeValue.resourceId;
        mContext = context;
    }

    @Override
    public void loadStart() {
        if (showLoadMore) return;
        showLoadMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    private int getLoadingMoreItemPosition() {
        return showLoadMore ? getItemCount() -1 : RecyclerView.NO_POSITION;
    }

    @Override
    public void loadFinish() {
        if (!showLoadMore) return;
        showLoadMore = false;
        notifyItemRemoved(getLoadingMoreItemPosition());
    }

    public void addItem(ArrayList<ZhihuDailyItem> zhihuDailyItems) {
        this.zhihuDailyItems.addAll(zhihuDailyItems);
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.item_text_id)
        TextView itemText;
        @BindView(R.id.item_image_id)
        ImageView itemImage;
        @BindView(R.id.zhihu_item_layout)
        LinearLayout mLinearLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,view);
        }
    }
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cheese_item_layout,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihu_item_layout,parent,false);
//        view.setBackgroundResource(background);
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ZhihuDailyItem zhihuDailyItem = zhihuDailyItems.get(position);
        holder.itemText.setText(zhihuDailyItem.getTitle());
        Glide.with(mContext)
                .load(zhihuDailyItems.get(position).getImages()[0])
                .centerCrop()
                .into(holder.itemImage);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToZhihuDetailActivity(holder,zhihuDailyItem);
            }
        });
    }

    private void goToZhihuDetailActivity(ItemViewHolder holder, ZhihuDailyItem zhihuDailyItem) {
        Intent intent = new Intent();
        intent.setClass(mContext, ZhihuDetailActivity.class);
        intent.putExtra("id",zhihuDailyItem.getId());
        intent.putExtra("title",zhihuDailyItem.getTitle());
        intent.putExtra("image",imageUrl);
        if (NetWorkUtils.isNetworkConnected(mContext)) {
            mContext.startActivity(intent);
        }else {
            Toast.makeText(mContext,mContext.getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return zhihuDailyItems.size();
    }

    public void clearData() {
        zhihuDailyItems.clear();
        notifyDataSetChanged();
    }
}
