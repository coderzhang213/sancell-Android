package cn.sancell.xingqiu.homecommunity.video.fgm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homecommunity.HomeCommunityFragment;
import cn.sancell.xingqiu.homecommunity.adapter.CommunityVideoListAdapter;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.homecommunity.video.VideoPlayListActivity;
import cn.sancell.xingqiu.im.entity.req.VideoListReq;

/**
 * @author Alan_Xiong
 * @desc: 首页视频
 * @time 2019-11-27 15:26
 */
public class HomeVideoFgm extends BaseLazyMVPFragment<HomeVideoPresenter> implements HomeVideoView, HomeCommunityFragment.OnRefLinsener {

    @BindView(R.id.rv_common)
    RecyclerView rv_common;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int mCurrentPage = 1;
    private int mPageSize = 10;
    private CommunityVideoListAdapter mAdapter;
    private List<CommunityVideoListBean.VideoBean> mDatas = new ArrayList<>();

    public static HomeVideoFgm newInstance() {

        Bundle args = new Bundle();

        HomeVideoFgm fragment = new HomeVideoFgm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected HomeVideoPresenter createPresenter() {
        return new HomeVideoPresenter(getContext());
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_recycleview, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initLinsener() {
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                getData();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        VideoListReq req = new VideoListReq();
        req.page = mCurrentPage + "";
        req.pageSize = mPageSize + "";
        req.type = "1";
        mPresenter.getVideoList(req);
    }

    @Override
    protected void initData() {
        initLinsener();
        refreshLayout.setEnableRefresh(false);
        rv_common.setLayoutManager(new LinearLayoutManager(getContext()));
        mCurrentPage = 1;
        getData();

    }


    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public void getVideoListSuccess(CommunityVideoListBean res) {
        refreshLayout.finishLoadMore();
        if (res == null || res.getDataList() == null || res.getDataList().size() <= 0) {//显示没有更多数据了
            refreshLayout.finishLoadMoreWithNoMoreData();
            return;
        }
        if (mCurrentPage == 1) {
            mDatas.clear();
        }
        mDatas.addAll(res.getDataList());
        setAdapter();


    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommunityVideoListAdapter(mDatas);
            mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                CommunityVideoListBean.VideoBean videoBean = mDatas.get(position);
                Intent intent = new Intent(getContext(), VideoPlayListActivity.class);
                intent.putExtra("playId", videoBean.getId());
                startActivity(intent);
                //VideoPlayActivity.start(getActivity(), videoBean.getId());
            });
        } else {
            mAdapter.setNewData(mDatas);
        }
        rv_common.setAdapter(mAdapter);
    }

    @Override
    public void getVideoListError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void onRefLinsener() {
        refreshLayout.resetNoMoreData();
        mCurrentPage = 1;
        getData();
    }
}
