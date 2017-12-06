package com.materialdesign.myapplication.activity;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.utils.AnimUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/6
 */

public class NewsHistoryDetailActivity extends AppCompatActivity {
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.dt_toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop)
    ImageView imageView;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.html_content_tv)
    HtmlTextView content_htmlTv;
    @BindView(R.id.progress)
    ProgressBar mProgress;


    private String id;
    private String imgSrc;
    private String title;
    private String content;
    private NestedScrollView.OnScrollChangeListener scrollListener;
    private android.transition.Transition.TransitionListener mEnterTransitionListener;
    private android.transition.Transition.TransitionListener mReturnHomeTransitionListener;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initListener();
        initData();
        initView();
        showData();
        enterAnimation();
        getWindow().getSharedElementReturnTransition().addListener(mReturnHomeTransitionListener);
        getWindow().getSharedElementEnterTransition().addListener((android.transition.Transition.TransitionListener) mEnterTransitionListener);
    }

    private void enterAnimation() {
        float offset = toolbar.getHeight();
        LinearInterpolator linearInter = new LinearInterpolator();
        AccelerateInterpolator acceler = new AccelerateInterpolator();
        viewEnterAnimation(toolbar,offset,linearInter);
        viewEnterAnimationNest(mNest,offset,acceler);
    }

    private void viewEnterAnimationNest(NestedScrollView mNest, float offset, AccelerateInterpolator acceler) {
        mNest.setTranslationY(-offset);
        mNest.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(50L)
                .setInterpolator(acceler)
                .setListener(null)
                .start();

    }

    private void viewEnterAnimation(Toolbar toolbar, float offset, LinearInterpolator linearInter) {
        toolbar.setTranslationY(-offset);
        toolbar.setAlpha(0.6f);
        toolbar.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(600L)
                .setInterpolator(linearInter)
                .setListener(null)
                .start();
    }

    private void initListener() {
        mReturnHomeTransitionListener = new AnimUtils.TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(android.transition.Transition transition) {
                super.onTransitionStart(transition);
                toolbar.animate()
                        .alpha(0f)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(100);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.setElevation(0f);
                }
                mNest.animate()
                        .alpha(0f)
                        .setDuration(50)
                        .setInterpolator(new AccelerateInterpolator());

            }
        };
        mEnterTransitionListener = new AnimUtils.TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(android.transition.Transition transition) {
                super.onTransitionEnd(transition);
                //                    解决5.0 shara element bug
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100).setDuration(100);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            mShot.setOffset((Integer) valueAnimator.getAnimatedValue() * 10);
                        mNest.smoothScrollTo((Integer) valueAnimator.getAnimatedValue() / 10, 0);

                    }
                });
                valueAnimator.start();
//                    mShot.setAlpha(0.5f);
//                    mShot.animate().alpha(1f).setDuration(800L).start();
            }
            @Override
            public void onTransitionResume(android.transition.Transition transition) {
                super.onTransitionResume(transition);
            }
        };
    }
    private void initView() {
        mProgress.setVisibility(View.GONE);
        mNest.setAlpha(0.5f);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNest.scrollTo(0,0);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showData() {
        content_htmlTv.setHtmlFromString(content,new HtmlTextView.LocalImageGetter());
    }
    private void initData() {
        title = getIntent().getStringExtra("title");
        imgSrc = getIntent().getStringExtra("image");
        content = getIntent().getStringExtra("content");
        collapsingToolbarLayout.setTitle(title);
        Glide.with(this)
                .load(imgSrc)
                .fitCenter()
                .into(imageView);
    }

}
