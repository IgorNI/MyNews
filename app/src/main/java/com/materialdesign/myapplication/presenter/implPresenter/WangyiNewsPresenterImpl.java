package com.materialdesign.myapplication.presenter.implPresenter;

import android.util.Log;

import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.news.NewsList;
import com.materialdesign.myapplication.presenter.IWangyiNewsPresenter;
import com.materialdesign.myapplication.presenter.implView.IWangyiNewsFragment;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description :
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */

public class WangyiNewsPresenterImpl extends BasePresenterImpl implements IWangyiNewsPresenter {
    private static final String TAG = WangyiNewsPresenterImpl.class.getSimpleName();
    IWangyiNewsFragment iWangyiNewsFragment;

    public WangyiNewsPresenterImpl(IWangyiNewsFragment fragment) {
        this.iWangyiNewsFragment = fragment;
    }

    @Override
    public void getList(int t) {
        iWangyiNewsFragment.showProgressDialog();
        Subscription subscription = ApiManager.getInstance().getNewsApiService().getNews(t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iWangyiNewsFragment.hideProgressDialog();
                        iWangyiNewsFragment.showError(e.toString());
                    }

                    @Override
                    public void onNext(NewsList newsList) {
                        iWangyiNewsFragment.hideProgressDialog();
                        iWangyiNewsFragment.upListItem(newsList);
                        Log.i(TAG, "onNext: " + newsList.getNewsList().size());
                    }
                });
        addCompositeSubscription(subscription);
    }
}
