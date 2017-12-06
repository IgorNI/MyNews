package com.materialdesign.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;

import com.materialdesign.myapplication.R;

/**
 * Created by ni on 2017/3/24.
 */

public class ForegroundImageView extends AppCompatImageView {
    private Drawable foreground;

    public ForegroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView);
        final Drawable d = a.getDrawable(R.styleable.ForegroundView_android_foreground);
        if (d != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setForeground(d);
            }
        }
        a.recycle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    public ForegroundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView);
        final Drawable d = a.getDrawable(R.styleable.ForegroundView_android_foreground);
        if (d != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setForeground(d);
            }
        }
        a.recycle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (foreground != null) {
            foreground.setBounds(0,0,w,h);
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return super.hasOverlappingRendering();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        return super.verifyDrawable(dr) || (dr == foreground);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foreground != null) foreground.jumpToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foreground != null && foreground.isStateful()) {
            foreground.setState(foreground.getState());
        }
    }

    @Override
    public Drawable getForeground() {
        return foreground;
    }

    @Override
    public void setForeground(Drawable foreground) {
        if (this.foreground != foreground) {
            if (foreground != null) {
                foreground.setCallback(null);
                unscheduleDrawable(foreground);
            }

            this.foreground = foreground;

            if (foreground != null) {
                foreground.setBounds(0, 0, getWidth(), getHeight());
                setWillNotDraw(false);
                foreground.setCallback(this);
                if (foreground.isStateful()) {
                    foreground.setState(getDrawableState());
                }
            } else {
                setWillNotDraw(true);
            }
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (foreground != null) {
            foreground.draw(canvas);
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (foreground != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                foreground.setHotspot(x, y);
            }
        }
    }
}
