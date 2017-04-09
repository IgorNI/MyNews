package com.materialdesign.myapplication.presenter.implView;

import com.materialdesign.myapplication.bean.zhihu.ZhihuStory;

/**
 * Created by ni on 2017/3/27.
 */

public interface IZhihuStory{
    void showError(String s);

    void showStory(ZhihuStory zhihuStory);
}
