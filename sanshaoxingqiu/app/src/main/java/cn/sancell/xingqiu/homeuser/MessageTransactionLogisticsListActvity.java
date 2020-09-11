package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.adapter.MessageTransactionLogisticsListAdapter;
import cn.sancell.xingqiu.homeuser.bean.MessageTransactionLogisticsListBean;
import cn.sancell.xingqiu.homeuser.contract.MessageTransactionLogisticsListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

public class MessageTransactionLogisticsListActvity extends BaseMVPToobarActivity<MessageTransactionLogisticsListContract.MessageTransactionLogisticsListPresenter>
        implements MessageTransactionLogisticsListContract.MessageTransactionLogisticsListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rcv_transaction_logistics_list)
    RecyclerView rcv_transaction_logistics_list;
    MessageTransactionLogisticsListAdapter messageTransactionLogisticsListAdapter;
    private List<MessageTransactionLogisticsListBean.TransactionLogisticsBean> data_transaction_logisticsList = new ArrayList<>();
    private int totalcount;

    @Override
    protected MessageTransactionLogisticsListContract.MessageTransactionLogisticsListPresenter createPresenter() {
        return new MessageTransactionLogisticsListContract.MessageTransactionLogisticsListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_transaction_logistics_list_actvity;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.message_transaction_logistics);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rcv_transaction_logistics_list.setLayoutManager(new LinearLayoutManager(this));
        messageTransactionLogisticsListAdapter = new MessageTransactionLogisticsListAdapter(data_transaction_logisticsList);
        messageTransactionLogisticsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.rl_item:
                        Intent intent = new Intent(MessageTransactionLogisticsListActvity.this, OrderNewPackInfoActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, data_transaction_logisticsList.get(i).getOrderParcelId());
                        intent.putExtra(Constants.Key.KEY_2, data_transaction_logisticsList.get(i).getOrderId());
                        startActivity(intent);
                        break;
                    case R.id.tv_evaluate:
                        Intent intent4 = new Intent(MessageTransactionLogisticsListActvity.this, EvaluatedProductListActivity.class);
                        intent4.putExtra(Constants.Key.KEY_1, data_transaction_logisticsList.get(i).getOrderId());
                        intent4.putExtra(Constants.Key.KEY_2, data_transaction_logisticsList.get(i).getWarehouseId());
                        startActivity(intent4);
                        break;
                }
            }
        });
        rcv_transaction_logistics_list.setAdapter(messageTransactionLogisticsListAdapter);
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (messageTransactionLogisticsListAdapter != null) {
                    messageTransactionLogisticsListAdapter.resetCurrentPage();
                }
                mPresenter.GetMessageTransactionLogisticsList(1, MessageTransactionLogisticsListActvity.this);
            }
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (messageTransactionLogisticsListAdapter != null) {
                if (data_transaction_logisticsList.size() < totalcount) {
                    mPresenter.GetMessageTransactionLogisticsList(messageTransactionLogisticsListAdapter.getNextPage(), MessageTransactionLogisticsListActvity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetMessageTransactionLogisticsList(1, MessageTransactionLogisticsListActvity.this);
            }
        });
        mPresenter.GetMessageTransactionLogisticsList(1, this);
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
            if (messageTransactionLogisticsListAdapter != null) {
                messageTransactionLogisticsListAdapter.resetCurrentPage();
            }
            mPresenter.GetMessageTransactionLogisticsList(1, MessageTransactionLogisticsListActvity.this);
        });
    }

    @Override
    public void getMessageTransactionLogisticsListSuccess(MessageTransactionLogisticsListBean messageTransactionLogisticsListBean, int page) {
        totalcount = messageTransactionLogisticsListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            if (messageTransactionLogisticsListBean != null && messageTransactionLogisticsListBean.getDataList() != null && messageTransactionLogisticsListBean.getDataList().size() > 0) {
                mNetworkErrorLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.GONE);
                data_transaction_logisticsList.clear();
                data_transaction_logisticsList.addAll(messageTransactionLogisticsListBean.getDataList());
                messageTransactionLogisticsListAdapter.notifyDataSetChanged();
                messageTransactionLogisticsListAdapter.correctCurrentPage();
            } else {
                mEmptyLayout.setVisibility(View.VISIBLE);
                data_transaction_logisticsList.clear();
                messageTransactionLogisticsListAdapter.notifyDataSetChanged();
            }
        } else {
            if (messageTransactionLogisticsListBean != null && messageTransactionLogisticsListBean.getDataList() != null) {
                data_transaction_logisticsList.addAll(messageTransactionLogisticsListBean.getDataList());
            }
            messageTransactionLogisticsListAdapter.notifyDataSetChanged();
            messageTransactionLogisticsListAdapter.correctCurrentPage();
        }
        if (data_transaction_logisticsList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
