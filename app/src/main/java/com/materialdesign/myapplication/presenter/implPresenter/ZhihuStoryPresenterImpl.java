package com.materialdesign.myapplication.presenter.implPresenter;

import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.zhihu.ZhihuStory;
import com.materialdesign.myapplication.presenter.ZhihuStoryPresenter;
import com.materialdesign.myapplication.presenter.implView.IZhihuStory;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ni on 2017/3/27.
 */

public class ZhihuStoryPresenterImpl extends BasePresenterImpl implements ZhihuStoryPresenter{

    private IZhihuStory izhihuStory;

    public ZhihuStoryPresenterImpl(IZhihuStory zhihuStory) {
        if (zhihuStory == null)
            throw new IllegalArgumentException("zhihuStory must not be null");
        this.izhihuStory = zhihuStory;
    }

    @Override
    public void getZhuhuStory(String id) {
        Subscription s = ApiManager.getInstance().getZhihuApiService().getZhihuStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        izhihuStory.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {
                        izhihuStory.showStory(zhihuStory);
                    }
                });
        addCompositeSubscription(s);
    }
}
