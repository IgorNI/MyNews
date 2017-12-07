package com.materialdesign.myapplication.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.activity.MainActivity;
import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.zhihu.ZhihuDaily;
import com.materialdesign.myapplication.bean.zhihu.ZhihuDailyItem;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/3
 */

public class UpdateWidgetService extends IntentService{

    private String id;
    private String title;
    private String imageUrl;
    private Bitmap[] bitmap = new Bitmap[1];
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        postNewsAtHalfAnHourPeriod();
    }

    private void postNewsAtHalfAnHourPeriod() {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                NewsWidgetProvider.class));
        // Get today's data from the NetWork

        Subscription subscription = ApiManager.getInstance().getZhihuApiService().getLastDay()
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {

                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuItem : zhihuDaily.getmZhihuStories()) {
                            zhihuItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        ZhihuDailyItem item = zhihuDaily.getmZhihuDailyItems().get(0);
                        id = item.getId();
                        title = item.getTitle();
                    }
                });

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_layout;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setImageViewResource(R.id.iv_widget, R.drawable.zhihu);
            Log.i("1234", "postNewsAtHalfAnHourPeriod: " + title);
            if ("".equals(title) || title == null) {
                views.setTextViewText(R.id.tv_widget,getString(R.string.tips));
            }else {
                views.setTextViewText(R.id.tv_widget,title);
            }

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, getResources().getString(R.string.zhihu));
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.iv_widget, description);
    }
}
