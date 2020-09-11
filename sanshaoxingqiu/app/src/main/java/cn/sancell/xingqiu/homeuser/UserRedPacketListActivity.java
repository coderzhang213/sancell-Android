package cn.sancell.xingqiu.homeuser;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
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
import cn.sancell.xingqiu.homeuser.adapter.UserRedPacketListAdapter;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.homeuser.contract.UserRedPacketListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

public class UserRedPacketListActivity extends BaseMVPToobarActivity<UserRedPacketListContract.UserRedPacketListPresenter>
        implements UserRedPacketListContract.UserRedPacketListView, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_red_packet_list)
    RecyclerView rcv_red_packet_list;
    AppCompatTextView tv_total;

    UserRedPacketListAdapter userRedPacketListAdapter;
    private List<UserRedPacketListBean.RedPacketBean> data_red_packetList = new ArrayList<>();
    private int totalcount;

    private long remain_red_packet_price;

    public static void start(Context context,long money){
        Intent intent = new Intent(context,UserRedPacketListActivity.class);
        intent.putExtra(Constants.Key.KEY_1,money);
        context.startActivity(intent);
    }

    @Override
    protected UserRedPacketListContract.UserRedPacketListPresenter createPresenter() {
        return new UserRedPacketListContract.UserRedPacketListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_red_packet_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.red_packet_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        remain_red_packet_price = getIntent().getLongExtra(Constants.Key.KEY_1, 0);
        rcv_red_packet_list.setLayoutManager(new LinearLayoutManager(this));
        userRedPacketListAdapter = new UserRedPacketListAdapter(data_red_packetList);
        userRedPacketListAdapter.addHeaderView(getHeaderView());
        rcv_red_packet_list.setAdapter(userRedPacketListAdapter);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (userRedPacketListAdapter != null) {
                if (data_red_packetList.size() < totalcount) {
                    mPresenter.GetUserRedPacketList(userRedPacketListAdapter.getNextPage(), UserRedPacketListActivity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetUserRedPacketList(1, UserRedPacketListActivity.this);
            }
        });

        mPresenter.GetUserRedPacketList(1, UserRedPacketListActivity.this);
    }

    private ImageView iv_reward_price_introduce;
    private RelativeSizeTextView tv_remain_red_packet;
    private TextView tv_disbursement_price, tv_income_price;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_user_red_packet_list_top, (ViewGroup) rcv_red_packet_list.getParent(), false);
        iv_reward_price_introduce = view.findViewById(R.id.iv_reward_price_introduce);
        tv_remain_red_packet = view.findViewById(R.id.tv_remain_red_packet);
        tv_income_price = view.findViewById(R.id.tv_income_price);
        tv_disbursement_price = view.findViewById(R.id.tv_disbursement_price);
        tv_total = view.findViewById(R.id.tv_total);
        tv_remain_red_packet.setStartProportion(0.73f);
        tv_remain_red_packet.setEndProportion(0.73f);
        tv_remain_red_packet.setTagText(StringUtils.getPrice(remain_red_packet_price));
        tv_remain_red_packet.setEndText(StringUtils.getPriceDecimal(remain_red_packet_price));
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
            if (userRedPacketListAdapter != null) {
                userRedPacketListAdapter.resetCurrentPage();
            }
            mPresenter.GetUserRedPacketList(1, UserRedPacketListActivity.this);
        });
    }

    @Override
    public void getUserRedPacketListSuccess(UserRedPacketListBean userRedPacketListBean, int page) {
        totalcount = userRedPacketListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        tv_total.setText(String.format(getResources().getString(R.string.rp_income_total), StringUtils.getAllPrice(userRedPacketListBean.getAllIncomeE2())
                , StringUtils.getAllPrice(userRedPacketListBean.getAllSpendingE2())));
        /*tv_disbursement_price.setText("支出¥" + StringUtils.getAllPrice(userRedPacketListBean.getAllSpendingE2()));
        tv_income_price.setText("收入¥" + StringUtils.getAllPrice(userRedPacketListBean.getAllIncomeE2()));*/
        if (page == 1) {
            if (userRedPacketListBean != null && userRedPacketListBean.getDataList() != null && userRedPacketListBean.getDataList().size() > 0) {
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_red_packetList.clear();
                data_red_packetList.addAll(userRedPacketListBean.getDataList());
                userRedPacketListAdapter.notifyDataSetChanged();
                userRedPacketListAdapter.correctCurrentPage();
            } else {
                data_red_packetList.clear();
                userRedPacketListAdapter.notifyDataSetChanged();
            }
        } else {
            if (userRedPacketListBean != null && userRedPacketListBean.getDataList() != null) {
                data_red_packetList.addAll(userRedPacketListBean.getDataList());
            }
            userRedPacketListAdapter.notifyDataSetChanged();
            userRedPacketListAdapter.correctCurrentPage();
        }
        if (data_red_packetList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reward_price_introduce:
                UrlInfoActivity.start(UserRedPacketListActivity.this, UrlConstants.INSTANCE.getREAD_GZ(), getResources().getString(R.string.red_packet_agreement));

                break;
        }
    }
}
