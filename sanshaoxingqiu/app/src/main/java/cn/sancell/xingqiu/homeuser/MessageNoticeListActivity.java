package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.adapter.MessageNoticeListAdapter;
import cn.sancell.xingqiu.homeuser.bean.MessageNoticeListBean;
import cn.sancell.xingqiu.homeuser.contract.MessageNoticeListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

public class MessageNoticeListActivity extends BaseMVPToobarActivity<MessageNoticeListContract.MessageNoticeListPresenter>
        implements MessageNoticeListContract.MessageNoticeListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rcv_notice_list)
    RecyclerView rcv_notice_list;
    MessageNoticeListAdapter messageNoticeListAdapter;
    private List<MessageNoticeListBean.MessageNoticeBean> data_noticeList = new ArrayList<>();
    private int totalcount;

    @Override
    protected MessageNoticeListContract.MessageNoticeListPresenter createPresenter() {
        return new MessageNoticeListContract.MessageNoticeListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_notice_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.message_notice);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rcv_notice_list.setLayoutManager(new LinearLayoutManager(this));
        messageNoticeListAdapter = new MessageNoticeListAdapter(data_noticeList);
        messageNoticeListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()){
                case R.id.rl_item:
                    if(!StringUtils.isTextEmpty(data_noticeList.get(i).getUrl())){
                        Intent intent = new Intent(MessageNoticeListActivity.this, UrlInfoActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, data_noticeList.get(i).getUrl());
                        intent.putExtra(Constants.Key.KEY_2, "");
                        intent.putExtra(Constants.Key.KEY_3, data_noticeList.get(i).getIsNeedLoginData());
                        startActivity(intent);
                    }
                    break;
            }
        });
        rcv_notice_list.setAdapter(messageNoticeListAdapter);
        refreshLayout.setRefreshHeader(new SancellClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (messageNoticeListAdapter != null) {
                messageNoticeListAdapter.resetCurrentPage();
            }
            mPresenter.GetMessageNoticeList(1, MessageNoticeListActivity.this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (messageNoticeListAdapter != null) {
                if (data_noticeList.size() < totalcount) {
                    mPresenter.GetMessageNoticeList(messageNoticeListAdapter.getNextPage(), MessageNoticeListActivity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetMessageNoticeList(1, MessageNoticeListActivity.this);
            }
        });
        mPresenter.GetMessageNoticeList(1,this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(view -> {
            if (messageNoticeListAdapter != null) {
                messageNoticeListAdapter.resetCurrentPage();
            }
            mPresenter.GetMessageNoticeList( 1, MessageNoticeListActivity.this);
        });
    }

    @Override
    public void getMessageNoticeListSuccess(MessageNoticeListBean messageNoticeListBean, int page) {
        totalcount = messageNoticeListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            if (messageNoticeListBean != null && messageNoticeListBean.getDataList() != null && messageNoticeListBean.getDataList().size() > 0) {
                mEmptyLayout.setVisibility(View.GONE);
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_noticeList.clear();
                data_noticeList.addAll(messageNoticeListBean.getDataList());
                messageNoticeListAdapter.notifyDataSetChanged();
                messageNoticeListAdapter.correctCurrentPage();
            } else {
                data_noticeList.clear();
                messageNoticeListAdapter.notifyDataSetChanged();
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (messageNoticeListBean != null && messageNoticeListBean.getDataList() != null) {
                data_noticeList.addAll(messageNoticeListBean.getDataList());
            }
            messageNoticeListAdapter.notifyDataSetChanged();
            messageNoticeListAdapter.correctCurrentPage();
        }
        if (data_noticeList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
