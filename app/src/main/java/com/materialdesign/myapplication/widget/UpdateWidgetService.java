package com.materialdesign.myapplication.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.zhihu.ZhihuDaily;
import com.materialdesign.myapplication.bean.zhihu.ZhihuDailyItem;
import com.materialdesign.myapplication.utils.KeyUtils;

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
        postNewsAtHalfHourPeriod();
    }

    private void postNewsAtHalfHourPeriod() {
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
                        String id = item.getId();
                        String title = item.getTitle();
                        Intent intent = new Intent(KeyUtils.KEY_UPDATE_WIDGET_STR);
                        intent.putExtra(KeyUtils.KEY_NEWS_ID_STR,id);
                        intent.putExtra(KeyUtils.KEY_NEWS_TITLE_STR,title);
                        sendBroadcast(intent);
                    }
                });
    }
}
