package com.materialdesign.myapplication.presenter.implPresenter;

import com.materialdesign.myapplication.bean.news.NewsDetailBean;
import com.materialdesign.myapplication.presenter.IWangyiNewsDescPresenter;
import com.materialdesign.myapplication.presenter.implView.IWangyiNewsDescFragment;
import com.materialdesign.myapplication.utils.DBUtils;
import com.materialdesign.myapplication.utils.NewsJsonUtils;
import com.materialdesign.myapplication.utils.OkHttpUtils;
import com.materialdesign.myapplication.utils.Urls;

/**
 * Description :
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/9
 */

public class WangyiNewsDescPresenterImpl extends BasePresenterImpl implements IWangyiNewsDescPresenter {
    private IWangyiNewsDescFragment iWangyiNewsDescFragment;

    public WangyiNewsDescPresenterImpl(IWangyiNewsDescFragment fragment) {
        if (fragment == null)
        throw new IllegalArgumentException(" must not be null");
        this.iWangyiNewsDescFragment = fragment;
    }


    @Override
    public void getDescribe(final String id) {
        iWangyiNewsDescFragment.showProgressDialog();
        String url = getDetailUrl(id);
        OkHttpUtils.ResultCallback<String> loadNewsCallBack = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, id);
                iWangyiNewsDescFragment.updateItem(newsDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                iWangyiNewsDescFragment.showError(e.getMessage());
            }
        };
        OkHttpUtils.get(url, loadNewsCallBack);
    }

    private String getDetailUrl(String id) {
        StringBuilder sb = new StringBuilder(Urls.NEW_DETAIL);
        sb.append(id).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }
}
