package com.materialdesign.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.adapter.WangyiNewsAdapter;
import com.materialdesign.myapplication.bean.news.NewsList;
import com.materialdesign.myapplication.presenter.implPresenter.WangyiNewsPresenterImpl;
import com.materialdesign.myapplication.presenter.implView.IWangyiNewsFragment;
import com.materialdesign.myapplication.view.GridItemDividerDecoration;
import com.materialdesign.myapplication.widget.WrapContentLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :
 * Author : ni
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */

public class WangyiNewsFragment extends Fragment implements IWangyiNewsFragment {

    boolean isloading;
    boolean isConnected = true;
    private WangyiNewsAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;
    int currentIndex;

    @BindView(R.id.news_recyclerview)
    RecyclerView newsRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    RecyclerView.OnScrollListener mListener;

    WangyiNewsPresenterImpl presenterImpl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        initListener();
        mLinearLayout = new WrapContentLinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(mLinearLayout);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.addItemDecoration(new GridItemDividerDecoration(getContext(),R.dimen.divider_height, R.color.divider));
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerView.setAdapter(mAdapter);
        newsRecyclerView.addOnScrollListener(mListener);
        if (isConnected) {
            loadDate();
        }
    }

    private void initDate() {
        presenterImpl = new WangyiNewsPresenterImpl(this);
        mAdapter = new WangyiNewsAdapter(getContext());
    }

    @Override
    public void upListItem(NewsList newsList) {
        isloading = false;
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.addItem(newsList.getNewsList());
    }

    /**
     * 第一次获取数据
     * */
    private void loadDate() {
        if (mAdapter.getItemCount() > 0) {
            mAdapter.clearData();
        }
        currentIndex = 0;
        presenterImpl.getList(currentIndex);
    }

    private void initListener() {
        mListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visiableItemCount = mLinearLayout.getChildCount();
                    int totalItemCount = mLinearLayout.getItemCount();
                    int pastVisiblesItems = mLinearLayout.findFirstVisibleItemPosition();

                    if (!isloading && (visiableItemCount + pastVisiblesItems) >= totalItemCount) {
                        isloading = true;
                        loadMore();
                    }
                }
            }
        };
    }

    private void loadMore() {
        mAdapter.loadStart();
        currentIndex+=20;
        presenterImpl.getList(currentIndex);
    }

    @Override
    public void showProgressDialog() {
        if (currentIndex == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressDialog() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String s) {
        if (newsRecyclerView == null) {
            Snackbar.make(newsRecyclerView,"请检查网络",Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenterImpl.getList(currentIndex);
                }
            }).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterImpl.unsubscribe();
    }
}
