package com.materialdesign.myapplication.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.materialdesign.myapplication.view.GridItemDividerDecoration;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.widget.WrapContentLinearLayoutManager;
import com.materialdesign.myapplication.adapter.ItemAdapter;
import com.materialdesign.myapplication.bean.Cheeses;
import com.materialdesign.myapplication.bean.ZhihuDaily;
import com.materialdesign.myapplication.presenter.ZhihuPresrenter;
import com.materialdesign.myapplication.presenter.implPresenter.ZhihuPresenterImpl;
import com.materialdesign.myapplication.presenter.implView.IZhihuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ni on 2017/3/22.
 */

public class ZhihuFragment extends Fragment implements IZhihuFragment {

    private ZhihuPresrenter zhihuPresenter;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView.OnScrollListener loadingMoreListener;
    private String mCurrentDate;  // 当前天
    boolean loading;
    private ItemAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_cheese_list,container,false);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLinearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        }else {
            mLinearLayoutManager = new LinearLayoutManager(getContext());
        }
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridItemDividerDecoration(getContext(),R.dimen.grid_item_margin,R.color.diver));
        recyclerView.setAdapter(adapter);
        loadDate();
    }

    private void loadDate() {
        if (adapter.getItemCount() > 0) {
            adapter.clearData();
        }
        mCurrentDate = "0";
        zhihuPresenter.getLastZhihuNews();
    }

    private void initListener() {
        loadingMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // 向下滑动
                    int visiableCount = mLinearLayoutManager.getChildCount();
                    int totalCount = mLinearLayoutManager.getItemCount();
                    int pastVisiableCount = mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (!loading && (visiableCount + pastVisiableCount) >= totalCount) {
                        loading = true;
                        loadMoreDate();
                    }
                }
            }
        };

    }

    private void loadMoreDate() {
        adapter.loadStart();
        zhihuPresenter.getTheDaily(mCurrentDate);
    }

    private void initDate() {
        zhihuPresenter = new ZhihuPresenterImpl(getContext(),ZhihuFragment.this);
        adapter = new ItemAdapter(getContext(),zhihuPresenter.getCheeseList(Cheeses.sCheeseStrings,30));
        zhihuPresenter.getLastZhihuNews();
    }

    @Override
    public void updateList(ZhihuDaily zhihuDaily) {
        if (loading) {
            loading = false;
            adapter.loadFinish();
        }
        mCurrentDate = zhihuDaily.getDate();
        adapter.addItem(zhihuDaily.getmZhihuStories());
        if (!recyclerView.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM)) {
            loadMoreDate();
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String s) {

    }
}
