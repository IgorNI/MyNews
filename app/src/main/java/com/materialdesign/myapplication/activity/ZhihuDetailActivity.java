package com.materialdesign.myapplication.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.bean.Cheeses;
import com.materialdesign.myapplication.bean.zhihu.ZhihuStory;
import com.materialdesign.myapplication.config.Config;
import com.materialdesign.myapplication.data.NewsContract;
import com.materialdesign.myapplication.presenter.implPresenter.ZhihuStoryPresenterImpl;
import com.materialdesign.myapplication.presenter.implView.IZhihuStory;
import com.materialdesign.myapplication.utils.AnimUtils;
import com.materialdesign.myapplication.utils.WebUtils;

import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ni on 2017/3/22.
 */
public class ZhihuDetailActivity extends AppCompatActivity implements IZhihuStory{

    private ZhihuStoryPresenterImpl zhihuStoryPresenter;
    private String mId;
    private String mTitle;
    private String mImageUrl;
    private NestedScrollView.OnScrollChangeListener scrollListener;
    private Transition.TransitionListener zhihuReturnHomeListener;
    private String mShareUrl;
    private String mBody;
    private String[] mCss;
    boolean isEmpty;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.dt_toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop)
    ImageView imageView;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.wv_zhihu)
    WebView wvZhihu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_detail_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initListener();
        initData();
        initView();
        getData();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            getWindow().getSharedElementReturnTransition().addListener(zhihuReturnHomeListener);
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
        enterAnimation();
    }


    private void initListener() {
        zhihuReturnHomeListener =
                new AnimUtils.TransitionListenerAdapter() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        super.onTransitionStart(transition);
                        toolbar.animate()
                                .alpha(0f)
                                .setDuration(100)
                                .setInterpolator(new AccelerateInterpolator());
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                            toolbar.setElevation(0f);
                            imageView.setElevation(1f);
                        }
                        mNest.animate()
                                .alpha(0f)
                                .setDuration(50)
                                .setInterpolator(new AccelerateInterpolator());
                    }
                };

    }
    private void initView() {
        collapsingToolbarLayout.setTitle(mTitle);
        toolbar.setTitleMargin(20,20,0,10);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNest.smoothScrollBy(0,0);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings settings = wvZhihu.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //settings.setUseWideViewPort(true);造成文字太小
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvZhihu.setWebChromeClient(new WebChromeClient());
    }

    private void initData() {
        mId = getIntent().getStringExtra("id");
        mTitle = getIntent().getStringExtra("title");
        mImageUrl = getIntent().getStringExtra("image");
        zhihuStoryPresenter = new ZhihuStoryPresenterImpl(this);
    }

    private void getData() {
       zhihuStoryPresenter.getZhuhuStory(mId);
    }

    @Override
    public void showError(String s) {
        Snackbar.make(wvZhihu,"请检查网络",Snackbar.LENGTH_INDEFINITE).setAction("请重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showStory(ZhihuStory zhihuStory) {
        Glide.with(this)
                .load(zhihuStory.getImage()).centerCrop()
                .into(imageView);
        mShareUrl = zhihuStory.getShareUrl();
        isEmpty= TextUtils.isEmpty(zhihuStory.getBody());
        mBody = zhihuStory.getBody();
        mCss = zhihuStory.getCss();
        if (isEmpty) {
            wvZhihu.loadUrl(mShareUrl);
        } else {
            String data = WebUtils.buildHtmlWithCss(mBody,mCss,false);
            ContentValues cv = new ContentValues();
            cv.put(NewsContract.ZhihuNewsEntry.KEY,mTitle);
            cv.put(NewsContract.ZhihuNewsEntry.IS_READ,1);
            Uri uri2 = NewsContract.ZhihuNewsEntry.buildZhihuIsRead(mTitle);
            Cursor cursor = getContentResolver().query(uri2,new String[]{"key","is_read"},"key=?",new String[]{mTitle},null);
            if (cursor.moveToFirst()) {
                int isread;
                do {
                    // 获取字段的值
                    isread = cursor.getInt(cursor.getColumnIndex("is_read"));
                    if (isread != 1) {
                        // 保存到知乎表
                        Uri uri = NewsContract.ZhihuNewsEntry.buildZhihuIsRead(mTitle);
                        ContentValues values = new ContentValues();
                        values.put(NewsContract.ZhihuNewsEntry.KEY, mTitle);
                        values.put(NewsContract.ZhihuNewsEntry.IS_READ,1);
                        getContentResolver().insert(uri, values);

                        // 保存到news表
                        Uri uri1 = NewsContract.NewsEntry.CONTENT_URI;
                        ContentValues values1 = new ContentValues();
                        values1.put(NewsContract.NewsEntry.KEY,mTitle);
                        values1.put(NewsContract.NewsEntry.IS_READ,1);
                        values1.put(NewsContract.NewsEntry.CONTENT,data);
                        values1.put(NewsContract.NewsEntry.IMAGE,mImageUrl);
                        values1.put(NewsContract.NewsEntry.TYPE, Config.TOPNEWS);
                        getContentResolver().insert(uri1,values1);
                    }
                } while (cursor.moveToNext());
            }else {
                // 保存到知乎表
                Uri uri = NewsContract.ZhihuNewsEntry.buildZhihuIsRead(mTitle);
                ContentValues values = new ContentValues();
                values.put(NewsContract.ZhihuNewsEntry.KEY, mTitle);
                values.put(NewsContract.ZhihuNewsEntry.IS_READ,1);
                getContentResolver().insert(uri, values);

                // 保存到news表
                Uri uri1 = NewsContract.NewsEntry.CONTENT_URI;
                ContentValues values1 = new ContentValues();
                values1.put(NewsContract.NewsEntry.KEY,mTitle);
                values1.put(NewsContract.NewsEntry.IS_READ,1);
                values1.put(NewsContract.NewsEntry.CONTENT,data);
                values1.put(NewsContract.NewsEntry.IMAGE,mImageUrl);
                values1.put(NewsContract.NewsEntry.TYPE, Config.TOPNEWS);
                getContentResolver().insert(uri1,values1);
            }

            wvZhihu.loadDataWithBaseURL(WebUtils.BASE_URL,data,WebUtils.MIME_TYPE,WebUtils.ENCODING, WebUtils.FAIL_URL);
        }
    }

    private void enterAnimation() {
        float offSet = toolbar.getHeight();
        LinearInterpolator interpolator=new LinearInterpolator();
        viewEnterAnimationNest(mNest,0f,interpolator);
    }

    private void viewEnterAnimationNest(NestedScrollView mNest, float v, LinearInterpolator interpolator) {
        mNest.setTranslationY(v);
        mNest.setAlpha(0.3f);
        mNest.animate()
                .setInterpolator(interpolator)
                .setDuration(500L)
                .alpha(1.0f)
                .translationY(0f)
                .setListener(null)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            wvZhihu.getClass().getMethod("onResume").invoke(wvZhihu, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            wvZhihu.getClass().getMethod("onPause").invoke(wvZhihu,(Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {

            getWindow().getSharedElementReturnTransition().removeListener(zhihuReturnHomeListener);
        }
        //webview内存泄露
        if (wvZhihu != null) {
            ((ViewGroup) wvZhihu.getParent()).removeView(wvZhihu);
            wvZhihu.destroy();
            wvZhihu = null;
        }
        zhihuStoryPresenter.unsubscribe();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
