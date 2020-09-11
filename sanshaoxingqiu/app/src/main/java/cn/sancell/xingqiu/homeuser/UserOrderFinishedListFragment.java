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
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeuser.adapter.OrderListAdapter;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListNoPayListDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.bean.req.OrderListReq;
import cn.sancell.xingqiu.homeuser.contract.UserOrderListContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * Created by ai11 on 2019/7/3.
 * 已完成订单列表
 */

public class UserOrderFinishedListFragment extends BaseLazyMVPFragment<UserOrderListContract.OrderListFragmentPresenter>
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
                if (orderListAdapter != null) {
                    orderListAdapter.resetCurrentPage();
                }
                //  mPresenter.GetOrderFinishedList(1, getActivity());
                mPresenter.getOrderListType(getContext(), getOrderListReq(1));
            }
        });
    }


    private OrderListReq getOrderListReq(int page) {
        OrderListReq req = new OrderListReq();
        req.orderStatus = 5 + "";
        req.goodsFlag = "1";
        req.page = page + "";
        req.pageSize = 10 + "";
        return req;
    }

    @Override
    public void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason, int pos) {
        DialogUtil.showCancelOrder(getActivity(), data_reason, pos, clickCommitAction);
    }

    @Override
    public void getOrderAllListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount) {

    }

    @Override
    public void getOrderNoPayListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount, int currentCount) {

    }

    @Override
    public void getOrderUndeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {

    }

    @Override
    public void getOrderDeliveredListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {

    }

    @Override
    public void getOrderFinishedListSuccess(List<OrderAllListNoPayListDataBean> data, int page, int totalCount) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
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
                            case R.id.tv_finish_againpay: //再次购买
//                                List<String> data_productid = data_order.get(position).getProductIds();
//                                String productid = "";
//                                for (int i = 0; i < data_productid.size(); i++) {
//                                    if (i == data_productid.size() - 1) {
//                                        productid += data_productid.get(i);
//                                    } else {
//                                        productid += data_productid.get(i) + "-";
//                                    }
//                                }
//                                mPresenter.BatchAddCar(productid, getActivity());
                                GoodsDetailActivity.start(getContext(), Integer.parseInt(data_order.get(position).getProductIds().get(0)));
                                break;
                            case R.id.tv_finish_delete:  //删除订单
                                mPresenter.DeleteOrder(data_order.get(position).getOrderId() + "", getActivity());
                                break;
                            case R.id.tv_finish_evaluate:  //评价晒单
                                Intent intent4 = new Intent(getActivity(), EvaluatedProductListActivity.class);
                                intent4.putExtra(Constants.Key.KEY_1, data_order.get(position).getOrderId() + "");
                                intent4.putExtra(Constants.Key.KEY_2, data_order.get(position).getWarehouseId() + "");
                                startActivity(intent4);
                                break;
                            case R.id.rl_item:
                                Intent intent = new Intent(getActivity(), OrderNewPackInfoActivity.class);
                                intent.putExtra(Constants.Key.KEY_1, data_order.get(position).getPackId() + "");
                                intent.putExtra(Constants.Key.KEY_2, data_order.get(position).getOrderId() + "");
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
        if (data_order.size() > 0 && data_order.size() == totalCount) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void cancelOrderSuccess() {

    }

    @Override
    public void confirmReceiptSuccess() {

    }

    @Override
    public void deleteOrderSuccess() {
        if (orderListAdapter != null) {
            orderListAdapter.resetCurrentPage();
        }
        //  mPresenter.GetOrderFinishedList(1, getActivity());
        mPresenter.getOrderListType(getContext(), getOrderListReq(1));

    }

    @Override
    public void batchAddSuccess() {
    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_order_list, null);
        ButterKnife.bind(this, view);
        rcv_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void initData() {
        if (orderListAdapter != null) {
            orderListAdapter.resetCurrentPage();
        }
        //  mPresenter.GetOrderFinishedList(1, getActivity());
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(getActivity()));

        mPresenter.getOrderListType(getContext(), getOrderListReq(1));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (orderListAdapter != null) {
                orderListAdapter.resetCurrentPage();
            }
            mPresenter.getOrderListType(getContext(), getOrderListReq(1));
            refreshLayout.finishRefresh();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (orderListAdapter != null) {
                mPresenter.getOrderListType(getContext(), getOrderListReq(orderListAdapter.getNextPage()));
            } else {
                mPresenter.getOrderListType(getContext(), getOrderListReq(1));
            }
            refreshLayout.finishLoadMore();
        });

    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

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

        }
    };
}
