package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeshoppingcar.ProductPaymentActivity;
import cn.sancell.xingqiu.homeuser.adapter.OrderListAdapter;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListNoPayListDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.contract.UserOrderListContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * Created by ai11 on 2019/6/20.
 * 未支付订单列表
 */

public class UserOrderNoPayListFragment extends BaseLazyMVPFragment<UserOrderListContract.OrderListFragmentPresenter>
        implements UserOrderListContract.UserOrderListView {
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    OrderListAdapter orderListAdapter;
    private List<OrderAllListNoPayListDataBean> data_order;


    @Override
    public void showLoading(boolean show) {

    }

    @Override
    protected UserOrderListContract.OrderListFragmentPresenter createPresenter() {
        return new UserOrderListContract.OrderListFragmentPresenter();
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCount = 0;
                if(orderListAdapter!=null){
                    orderListAdapter.resetCurrentPage();
                }
                mPresenter.GetOrderNoPayList(1, getActivity());
            }
        });
    }

    @Override
    public void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason, int pos) {
        DialogUtil.showCancelOrder(getActivity(), data_reason, pos, clickCommitAction);
    }


    @Override
    public void getOrderAllListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount) {

    }

    private int currentCount;

    @Override
    public void getOrderNoPayListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        this.currentCount += currentCount;
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_order = data;
            if (data_order != null && data_order.size() > 0) {
                mEmptyLayout.setVisibility(View.GONE);
                orderListAdapter = new OrderListAdapter(getActivity(), data_order);
                orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.tv_nopay_gopay:  //去支付
                                if (data_order.get(position).getOrderPayEndTime() != 0) {
                                    long current = System.currentTimeMillis() / 1000;
                                    long time = (data_order.get(position).getOrderPayEndTime() - current) * 1000;
                                    if (time > 0) {
                                        if(data_order.get(position)!=null&&data_order.get(position).getGoodsFlag()==3){  //一元购
                                            PreferencesUtils.put(Constants.Key.KEY_isOneYuanPurchase, 1); //保存是否是一元购商品生成订单在WXPayEntryActivity界面判断待支付状态显示5分钟和30分钟内可以支付（1：是 0：否）
                                        }
                                        Intent intent1 = new Intent(getActivity(), ProductPaymentActivity.class);
                                        intent1.putExtra(Constants.Key.KEY_1, data_order.get(position).getOrderId() + "");
                                        intent1.putExtra(Constants.Key.KEY_2, data_order.get(position).getPayAmtE2());
                                        startActivity(intent1);
                                    } else {
                                        SCApp.getInstance().showSystemCenterToast("您的支付时间已经超时");
                                    }
                                } else {
                                    SCApp.getInstance().showSystemCenterToast("您的支付时间已经超时");
                                }
                                break;
                            case R.id.tv_nopay_cancel:  //取消订单
                                mPresenter.GetCancelReasonInfo(getActivity(), position);
                                break;
                            case R.id.rl_item:
                                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                                intent.putExtra(Constants.Key.KEY_1, data_order.get(position).getOrderId() + "");
                                startActivity(intent);
                                break;

                        }
                    }
                });
                rcv_list.setAdapter(orderListAdapter);
                orderListAdapter.correctCurrentPage();
            } else {
                orderListAdapter = new OrderListAdapter(getActivity(), data_order);
                rcv_list.setAdapter(orderListAdapter);
                orderListAdapter.correctCurrentPage();
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            data_order.addAll(data);
            orderListAdapter.notifyDataSetChanged();
            orderListAdapter.correctCurrentPage();
        }
        if (totalCount > 0 && totalCount == this.currentCount) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void getOrderUndeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {

    }

    @Override
    public void getOrderDeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {

    }

    @Override
    public void getOrderFinishedListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {

    }

    @Override
    public void cancelOrderSuccess() {
        currentCount = 0;
        if(orderListAdapter!=null){
            orderListAdapter.resetCurrentPage();
        }
        mPresenter.GetOrderNoPayList(1, getActivity());
    }

    @Override
    public void confirmReceiptSuccess() {

    }

    @Override
    public void deleteOrderSuccess() {

    }

    @Override
    public void batchAddSuccess() {

    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_order_list, null);
        ButterKnife.bind(this, view);
        rcv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentCount = 0;
                if(orderListAdapter!=null){
                    orderListAdapter.resetCurrentPage();
                }
                mPresenter.GetOrderNoPayList(1, getActivity());
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (orderListAdapter != null) {
                    mPresenter.GetOrderNoPayList(orderListAdapter.getNextPage(), getActivity());
                } else {
                    currentCount = 0;
                    mPresenter.GetOrderNoPayList(1, getActivity());
                }
                refreshLayout.finishLoadMore();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void initData() {
        mPresenter.GetOrderNoPayList(1, getActivity());
    }

    DialogUtil.ClickSureAction clickSureAction = new DialogUtil.ClickSureAction() {
        @Override
        public void sureAction(int postion) {

        }
    };

    DialogUtil.ClickCommitAction clickCommitAction = new DialogUtil.ClickCommitAction() {
        @Override
        public void commitAction(String reason) {

        }

        @Override
        public void commitAction(String select_reason, int postion) {
            mPresenter.CancelOrder(data_order.get(postion).getOrderId() + "", select_reason, getActivity());
        }
    };

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }
}