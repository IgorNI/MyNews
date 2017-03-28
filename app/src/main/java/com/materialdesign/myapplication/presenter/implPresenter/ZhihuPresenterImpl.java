package com.materialdesign.myapplication.presenter.implPresenter;

import android.content.Context;
import android.util.Log;

import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.ZhihuDaily;
import com.materialdesign.myapplication.bean.ZhihuDailyItem;
import com.materialdesign.myapplication.fragment.ZhihuFragment;
import com.materialdesign.myapplication.presenter.ZhihuPresrenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ni on 2017/3/23.
 */

public class ZhihuPresenterImpl extends BasePresenterImpl implements ZhihuPresrenter {

    private static final String TAG = ZhihuPresenterImpl.class.getSimpleName();
    private Context mContext;
    private ZhihuFragment fragment;


    public ZhihuPresenterImpl(Context context, ZhihuFragment zhihuFragment) {
        this.mContext = context;
        this.fragment = zhihuFragment;
    }


    /**
     * 获取知乎头条的新闻
     * */
    @Override
    public List<String> getCheeseList(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>();
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    @Override
    public void getLastZhihuNews() {
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
                        fragment.hideProgressDialog();
                        fragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        fragment.hideProgressDialog();
                        fragment.updateList(zhihuDaily);
                        Log.i(TAG, "onNext: " + zhihuDaily.getDate());
                    }
                });
        addCompositeSubscription(subscription);
    }

    @Override
    public void getTheDaily(String date) {
        Subscription subscription = ApiManager.getInstance().getZhihuApiService().getDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getmZhihuStories()) {
                            zhihuDailyItem.setDate(date);
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
                        fragment.hideProgressDialog();
                        fragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        fragment.hideProgressDialog();
                        fragment.updateList(zhihuDaily);
                    }
                });
        addCompositeSubscription(subscription);
    }
}
