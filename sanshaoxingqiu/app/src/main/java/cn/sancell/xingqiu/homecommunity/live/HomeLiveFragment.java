package cn.sancell.xingqiu.homecommunity.live;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.common.ToastHelper;
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
import cn.sancell.xingqiu.homecommunity.entity.LiveListReq;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveListRes;
import cn.sancell.xingqiu.homecommunity.live.actviity.LiveAttenListActivity;
import cn.sancell.xingqiu.homecommunity.live.adapter.LivIngAdapter;

/**
 * @author Alan_Xiong
 * @desc:
 * @time 2019-11-27 10:33
 */
public class HomeLiveFragment extends BaseLazyMVPFragment<HomeLivePresenter> implements HomeLiveView, HomeCommunityFragment.OnRefLinsener {

    private static final String TAG = "HomeLiveFragment";
    @BindView(R.id.rv_common)
    RecyclerView rv_common;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPageSize = 10;
    private int mCurrentPage = 1;
    private LivIngAdapter mAdapter;
    private List<LiveListRes> mDatas = new ArrayList<>();

    public static HomeLiveFragment newInstance() {

        Bundle args = new Bundle();

        HomeLiveFragment fragment = new HomeLiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected HomeLivePresenter createPresenter() {
        return new HomeLivePresenter(getContext());
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_recycleview, container, false);
    }

    /**
     * 获取数据
     */
    private void getData() {
        LiveListReq req = new LiveListReq();
        req.page = mCurrentPage + "";
        req.pageSize = mPageSize + "";
        mPresenter.getLiveList(req);

    }

    @Override
    protected void initData() {
        rv_common.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout.setEnableRefresh(false);
        // refreshLayout.setEnableNestedScroll(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                getData();
            }
        });
        mCurrentPage = 1;
        getData();
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    private void showNoteData() {
        if (mCurrentPage == 1) {//如果暂无数据，这样写是为了适配滑动
            mDatas.clear();
            refreshLayout.setEnableLoadMore(false);
            LiveListRes mLiveListRes = new LiveListRes();
            mLiveListRes.itemType = 1;
            mDatas.add(mLiveListRes);
            setAdapter();
        }
    }

    @Override
    public void getLiveListSuccess(LiveData res) {
        refreshLayout.finishLoadMore();
        if (res == null || res.getDataList() == null || res.getDataList().size() <= 0) {//显示没有更多数据了
            refreshLayout.finishLoadMoreWithNoMoreData();
            //去判断是否暂无数据
            showNoteData();
            return;
        }
        if (mCurrentPage == 1) {
            mDatas.clear();
        }
        mDatas.addAll(res.getDataList());

        setAdapter();

    }


    @Override
    public void getLiveListError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LivIngAdapter(mDatas);
            rv_common.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    LiveListRes liveListRes = mDatas.get(position);
                    switch (liveListRes.liveStatus) {//1 未开始， 2直播中， 3 已结束
                        case "1":
                            ToastHelper.showToast(getContext(), "亲爱的用户，本场直播尚未开始，请您耐心等待");
                            break;
                        case "2":
                        case "3":
                            toLiveRoomInfo(liveListRes);

                            break;
                    }

                }
            });
        } else {
            mAdapter.setNewData(mDatas);
        }
        rv_common.setAdapter(mAdapter);

    }

    /**
     * 跳转到直播
     *
     * @param liveListRes
     */
    public void toLiveRoomInfo(LiveListRes liveListRes) {
        startActivity(new Intent(getContext(), LiveAttenListActivity.class));
//        if (liveListRes.liveStatus.equals("2")) {//正在直播
//            LiveRoomActivity.startAudience(getContext(), liveListRes.roomId, liveListRes.pullUrl, true, liveListRes.batchId, liveListRes.onlineUser);
//
//        } else {//回放
//            Intent intent = new Intent(getActivity(), LiveActivity.class);
//            intent.putExtra("media_type", "livestream");
//            intent.putExtra("decode_type", "software");
//            intent.putExtra("yxId", liveListRes.batchId);
//            intent.putExtra("videoPath", liveListRes.replayUrl);
//            startActivity(intent);
//        }


    }

    @Override
    public void onRefLinsener() {
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.resetNoMoreData();
        mCurrentPage = 1;
        getData();
    }
}
