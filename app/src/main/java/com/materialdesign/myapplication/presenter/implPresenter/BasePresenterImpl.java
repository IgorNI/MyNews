package com.materialdesign.myapplication.presenter.implPresenter;

import com.materialdesign.myapplication.presenter.BasePresrenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ni on 2017/3/23.
 */

public class BasePresenterImpl implements BasePresrenter {
    private CompositeSubscription compositeSubscription; // 用于存储所有代办的subscription

    protected void addCompositeSubscription(Subscription s) {
        if (this.compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(s);
    }
    @Override
    public void unsubscribe() {
        if (this.compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
