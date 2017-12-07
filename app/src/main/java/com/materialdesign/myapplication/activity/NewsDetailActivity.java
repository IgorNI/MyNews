package com.materialdesign.myapplication.activity;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.bean.news.NewsDetailBean;
import com.materialdesign.myapplication.config.Config;
import com.materialdesign.myapplication.data.NewsContract;
import com.materialdesign.myapplication.presenter.implPresenter.WangyiNewsDescPresenterImpl;
import com.materialdesign.myapplication.presenter.implView.IWangyiNewsDescFragment;
import com.materialdesign.myapplication.utils.AnimUtils;
import com.materialdesign.myapplication.utils.ColorUtils;
import com.materialdesign.myapplication.utils.GlideUtils;
import com.materialdesign.myapplication.utils.ViewUtils;
import com.materialdesign.myapplication.view.ParallaxScrimageView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */
public class NewsDetailActivity extends AppCompatActivity implements IWangyiNewsDescFragment {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.dt_toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop)
    ParallaxScrimageView imageView;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.html_content_tv)
    HtmlTextView content_htmlTv;
    @BindView(R.id.progress)
    ProgressBar mProgress;


    private String id;
    private String imgSrc;
    private String title;
    private WangyiNewsDescPresenterImpl newsDescPresenter;
    private NestedScrollView.OnScrollChangeListener scrollListener;
    private Transition.TransitionListener  mEnterTransitionListener;
    private Transition.TransitionListener mReturnHomeTransitionListener;
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
        getData();
        enterAnimation();
        getWindow().getSharedElementReturnTransition().addListener(mReturnHomeTransitionListener);
        getWindow().getSharedElementEnterTransition().addListener(mEnterTransitionListener);
    }

    private void enterAnimation() {
        float offset = toolbar.getHeight();
        LinearInterpolator linearInter = new LinearInterpolator();
        AccelerateInterpolator acceler = new AccelerateInterpolator();
        viewEnterAnimation(toolbar,offset,linearInter);
        viewEnterAnimationNest(mNest,0f,acceler);
    }

    private void viewEnterAnimationNest(NestedScrollView mNest, float offset, Interpolator acceler) {
        mNest.setTranslationY(-offset);
        mNest.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(50L)
                .setInterpolator(acceler)
                .setListener(null)
                .start();

    }

    private void viewEnterAnimation(Toolbar toolbar, float offset, Interpolator linearInter) {
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
                    imageView.setElevation(1f);
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
//                            imageView.setOf((Integer) valueAnimator.getAnimatedValue() * 10);
                        mNest.smoothScrollTo((Integer) valueAnimator.getAnimatedValue() / 10, 0);

                    }
                });
                valueAnimator.start();
            }
            @Override
            public void onTransitionResume(android.transition.Transition transition) {
                super.onTransitionResume(transition);
            }
        };
    }
    private void initView() {
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

    private void getData() {

        newsDescPresenter.getDescribe(id);

    }
    private void initData() {
        id = getIntent().getStringExtra("docid");
        imgSrc = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        collapsingToolbarLayout.setTitle(title);
        Glide.with(this)
                .load(imgSrc)
                .fitCenter()
                .listener(glideLoadListener)
                .into(imageView);
        newsDescPresenter = new WangyiNewsDescPresenterImpl(this);
    }

    private RequestListener glideLoadListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onResourceReady(GlideDrawable resource, String model,
                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
            final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24, NewsDetailActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            boolean isDark;
                            @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                            if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                                isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                            } else {
                                isDark = lightness == ColorUtils.IS_DARK;
                            }
                            // color the status bar. Set a complementary dark color on L,
                            // light or dark color on M (with matching status bar icons)
                            int statusBarColor = getWindow().getStatusBarColor();
                            final Palette.Swatch topColor =
                                    ColorUtils.getMostPopulousSwatch(palette);
                            if (topColor != null &&
                                    (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                                statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                        isDark, 0.075f);
                                // set a light status bar on M+
                                if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ViewUtils.setLightStatusBar(imageView);
                                }
                            }

                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {

                                if (statusBarColor != getWindow().getStatusBarColor()) {
                                    imageView.setScrimColor(statusBarColor);
                                    ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                                            getWindow().getStatusBarColor(), statusBarColor);
                                    statusBarColorAnim.addUpdateListener(new ValueAnimator
                                            .AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            getWindow().setStatusBarColor(
                                                    (int) animation.getAnimatedValue());
                                        }
                                    });
                                    statusBarColorAnim.setDuration(1000L);
                                    statusBarColorAnim.setInterpolator(
                                            new AccelerateInterpolator());
                                    statusBarColorAnim.start();
                                }
                            }
                        }
                    });

            Palette.from(bitmap)
                    .clearFilters()
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {

                            // slightly more opaque ripple on the pinned image to compensate
                            // for the scrim
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                                imageView.setForeground(ViewUtils.createRipple(palette, 0.3f, 0.6f,
                                        ContextCompat.getColor(NewsDetailActivity.this, R.color.mid_grey),
                                        true));
                            }

                        }
                    });

            // TODO should keep the background if the image contains transparency?!
            imageView.setBackground(null);
            return false;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            return false;
        }
    };

    @Override
    public void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void updateItem(NewsDetailBean newsDetailBean) {
        mProgress.setVisibility(View.INVISIBLE);

        ContentValues values = new ContentValues();
        values.put(NewsContract.TopNewsEntry.KEY,title);
        values.put(NewsContract.TopNewsEntry.IS_READ,1);
        Uri uri = NewsContract.TopNewsEntry.buildTopIsRead(title);
        Cursor cursor = getContentResolver().query(uri,new String[]{"key","is_read"},"key=?",new String[]{title},null);
        if (cursor.moveToFirst()) {
            int isread;
            do {
                // 获取字段的值
                isread = cursor.getInt(cursor.getColumnIndex("is_read"));
                if (isread != 1) {
                    // 保存到已读表
                    Uri uri1 = NewsContract.TopNewsEntry.buildTopIsRead(title);
                    ContentValues values1 = new ContentValues();
                    values1.put(NewsContract.TopNewsEntry.KEY, title);
                    values1.put(NewsContract.TopNewsEntry.IS_READ,1);
                    getContentResolver().insert(uri1, values1);
                    // 保存到news表
                    Uri uri2 = NewsContract.NewsEntry.CONTENT_URI;
                    ContentValues values2 = new ContentValues();
                    values2.put(NewsContract.NewsEntry.KEY,title);
                    values2.put(NewsContract.NewsEntry.IS_READ,1);
                    values2.put(NewsContract.NewsEntry.CONTENT,newsDetailBean.getBody());
                    values2.put(NewsContract.NewsEntry.IMAGE,imgSrc);
                    values2.put(NewsContract.NewsEntry.TYPE, Config.TOPNEWS);
                    getContentResolver().insert(uri2,values2);
                }
            } while (cursor.moveToNext());
        }else {
            // 保存到已读表
            Uri uri2 = NewsContract.TopNewsEntry.buildTopIsRead(title);
            ContentValues values2 = new ContentValues();
            values2.put(NewsContract.TopNewsEntry.KEY, title);
            values2.put(NewsContract.TopNewsEntry.IS_READ,1);
            getContentResolver().insert(uri2, values2);
            // 保存到news表
            Uri uri1 = NewsContract.NewsEntry.CONTENT_URI;
            ContentValues values1 = new ContentValues();
            values1.put(NewsContract.NewsEntry.KEY,title);
            values1.put(NewsContract.NewsEntry.IS_READ,1);
            values1.put(NewsContract.NewsEntry.CONTENT,newsDetailBean.getBody());
            values1.put(NewsContract.NewsEntry.IMAGE,imgSrc);
            values1.put(NewsContract.NewsEntry.TYPE, Config.TOPNEWS);
            getContentResolver().insert(uri1,values1);
        }
        content_htmlTv.setHtmlFromString(newsDetailBean.getBody(),new HtmlTextView.LocalImageGetter());
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementReturnTransition().removeListener(mReturnHomeTransitionListener);
            getWindow().getSharedElementEnterTransition().removeListener(mEnterTransitionListener);

        }
        super.onDestroy();

    }
}
