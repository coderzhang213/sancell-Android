package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.adapter.UserRedPacketSettledListAdapter;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.homeuser.contract.UserRedPacketSettledListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

public class UserRedPacketSettledListActivity extends BaseMVPToobarActivity<UserRedPacketSettledListContract.UserRedPacketSettledListPresenter>
        implements UserRedPacketSettledListContract.UserRedPacketSettledListView, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_red_packet_settled_list)
    RecyclerView rcv_red_packet_settled_list;
    UserRedPacketSettledListAdapter userRedPacketSettledListAdapter;
    private List<UserRedPacketListBean.RedPacketBean> data_red_packet_settledList = new ArrayList<>();
    private int totalcount;

    private int settled_red_packet_price;

    @Override
    protected UserRedPacketSettledListContract.UserRedPacketSettledListPresenter createPresenter() {
        return new UserRedPacketSettledListContract.UserRedPacketSettledListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_red_packet_settled_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.red_packet_settled_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        settled_red_packet_price = getIntent().getIntExtra(Constants.Key.KEY_1, 0);
        rcv_red_packet_settled_list.setLayoutManager(new LinearLayoutManager(this));
        userRedPacketSettledListAdapter = new UserRedPacketSettledListAdapter(data_red_packet_settledList);
        userRedPacketSettledListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.tv_order_look:
                    Intent intent = new Intent(UserRedPacketSettledListActivity.this, OrderNewPackInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, data_red_packet_settledList.get(i).getOrderParcelId());
                    intent.putExtra(Constants.Key.KEY_2, data_red_packet_settledList.get(i).getOrderId());
                    startActivity(intent);
                    break;
            }
        });
        userRedPacketSettledListAdapter.addHeaderView(getHeaderView());
        rcv_red_packet_settled_list.setAdapter(userRedPacketSettledListAdapter);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (userRedPacketSettledListAdapter != null) {
                if (data_red_packet_settledList.size() < totalcount) {
                    mPresenter.GetUserRedPacketSettledList(userRedPacketSettledListAdapter.getNextPage(), UserRedPacketSettledListActivity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetUserRedPacketSettledList(1, UserRedPacketSettledListActivity.this);
            }
        });
        mPresenter.GetUserRedPacketSettledList(1, UserRedPacketSettledListActivity.this);
    }

    private ImageView iv_reward_price_introduce;
    private RelativeSizeTextView tv_remain_red_packet_settled;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_user_red_packet_settledlist_top, (ViewGroup) rcv_red_packet_settled_list.getParent(), false);
        iv_reward_price_introduce = view.findViewById(R.id.iv_reward_price_introduce);
        tv_remain_red_packet_settled = view.findViewById(R.id.tv_remain_red_packet_settled);
        tv_remain_red_packet_settled.setStartProportion(0.73f);
        tv_remain_red_packet_settled.setEndProportion(0.73f);
        tv_remain_red_packet_settled.setTagText(StringUtils.getPrice(settled_red_packet_price));
        tv_remain_red_packet_settled.setEndText(StringUtils.getPriceDecimal(settled_red_packet_price));
        iv_reward_price_introduce.setOnClickListener(this);
        return view;
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
            if (userRedPacketSettledListAdapter != null) {
                userRedPacketSettledListAdapter.resetCurrentPage();
            }
            mPresenter.GetUserRedPacketSettledList(1, UserRedPacketSettledListActivity.this);
        });
    }

    @Override
    public void getUserRedPacketSettledListSuccess(UserRedPacketListBean userRedPacketSettledListBean, int page) {
        totalcount = userRedPacketSettledListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            if (userRedPacketSettledListBean != null && userRedPacketSettledListBean.getDataList() != null && userRedPacketSettledListBean.getDataList().size() > 0) {
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_red_packet_settledList.clear();
                data_red_packet_settledList.addAll(userRedPacketSettledListBean.getDataList());
                userRedPacketSettledListAdapter.notifyDataSetChanged();
                userRedPacketSettledListAdapter.correctCurrentPage();
            } else {
                data_red_packet_settledList.clear();
                userRedPacketSettledListAdapter.notifyDataSetChanged();
            }
        } else {
            if (userRedPacketSettledListBean != null && userRedPacketSettledListBean.getDataList() != null) {
                data_red_packet_settledList.addAll(userRedPacketSettledListBean.getDataList());
            }
            userRedPacketSettledListAdapter.notifyDataSetChanged();
            userRedPacketSettledListAdapter.correctCurrentPage();
        }
        if (data_red_packet_settledList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reward_price_introduce:
                UrlInfoActivity.start(UserRedPacketSettledListActivity.this, UrlConstants.INSTANCE.getREAD_GZ(), getResources().getString(R.string.red_packet_agreement));

                break;
        }
    }
}
