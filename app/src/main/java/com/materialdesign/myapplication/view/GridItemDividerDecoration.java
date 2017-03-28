package com.materialdesign.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import uk.co.senab.photoview.Compat;

/**
 * Created by ni on 2017/3/24.
 *
 *  修饰recycleView中的item
 */

public class GridItemDividerDecoration extends RecyclerView.ItemDecoration{

    private final float dividerSize;
    private final Paint paint;
    public GridItemDividerDecoration(float dividerSize, @ColorInt int dividerColor) {
        this.dividerSize = dividerSize;
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public GridItemDividerDecoration(@NonNull Context context,
                                     @DimenRes int dividerSizeResId,
                                     @ColorRes int dividerColorResId) {
        this(
                context.getResources().getDimensionPixelSize(dividerSizeResId),
                ContextCompat.getColor(context, dividerColorResId));
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.isAnimating()) return;
        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        for (int i = 0;i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);
            if (requiresDivider(viewHolder)) {
                int right = lm.getDecoratedRight(child);
                int bottom = lm.getDecoratedBottom(child);
                // draw the bottom divider
                c.drawRect(lm.getDecoratedLeft(child),
                        bottom - dividerSize,
                        right,
                        bottom,
                        paint);
                // draw the right edge divider
                c.drawRect(right - dividerSize,
                        lm.getDecoratedTop(child),
                        right,
                        bottom - dividerSize,
                        paint);
            }
        }

    }

    private boolean requiresDivider(RecyclerView.ViewHolder viewHolder) {
        return true;
    }
}
