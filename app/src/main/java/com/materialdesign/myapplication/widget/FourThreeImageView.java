package com.materialdesign.myapplication.widget;

import android.content.Context;
import android.icu.util.Measure;
import android.util.AttributeSet;

/**
 * Created by ni on 2017/3/24.
 */

public class FourThreeImageView extends ForegroundImageView{
    public FourThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FourThreeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 4
                ,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, fourThreeHeight);

    }
}
