package cn.sancell.xingqiu.homeuser;

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
import cn.sancell.xingqiu.homeuser.adapter.MessageRedPacketListAdapter;
import cn.sancell.xingqiu.homeuser.bean.MessageRedPacketListBean;
import cn.sancell.xingqiu.homeuser.contract.MessageRedPacketListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

public class MessageRedPacketListActivity extends BaseMVPToobarActivity<MessageRedPacketListContract.MessageRedPacketListPresenter>
        implements MessageRedPacketListContract.MessageRedPacketListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rcv_message_red_packet_list)
    RecyclerView rcv_message_red_packet_list;
    MessageRedPacketListAdapter messageRedPacketListAdapter;
    private List<MessageRedPacketListBean.MessageRedPacketBean> data_red_packetList = new ArrayList<>();
    private int totalcount;

    @Override
    protected MessageRedPacketListContract.MessageRedPacketListPresenter createPresenter() {
        return new MessageRedPacketListContract.MessageRedPacketListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_red_packet_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.message_red_packet);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rcv_message_red_packet_list.setLayoutManager(new LinearLayoutManager(this));
        messageRedPacketListAdapter = new MessageRedPacketListAdapter(data_red_packetList);
        rcv_message_red_packet_list.setAdapter(messageRedPacketListAdapter);
        refreshLayout.setRefreshHeader(new SancellClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (messageRedPacketListAdapter != null) {
                messageRedPacketListAdapter.resetCurrentPage();
            }
            mPresenter.GetMessageRedPacketList(1, MessageRedPacketListActivity.this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (messageRedPacketListAdapter != null) {
                if (data_red_packetList.size() < totalcount) {
                    mPresenter.GetMessageRedPacketList(messageRedPacketListAdapter.getNextPage(), MessageRedPacketListActivity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetMessageRedPacketList(1, MessageRedPacketListActivity.this);
            }
        });
        mPresenter.GetMessageRedPacketList(1,this);
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
            if (messageRedPacketListAdapter != null) {
                messageRedPacketListAdapter.resetCurrentPage();
            }
            mPresenter.GetMessageRedPacketList( 1, MessageRedPacketListActivity.this);
        });
    }

    @Override
    public void getMessageRedPacketListSuccess(MessageRedPacketListBean messageRedPacketListBean, int page) {
        totalcount = messageRedPacketListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            if (messageRedPacketListBean != null && messageRedPacketListBean.getDataList() != null && messageRedPacketListBean.getDataList().size() > 0) {
                mEmptyLayout.setVisibility(View.GONE);
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_red_packetList.clear();
                data_red_packetList.addAll(messageRedPacketListBean.getDataList());
                messageRedPacketListAdapter.notifyDataSetChanged();
                messageRedPacketListAdapter.correctCurrentPage();
            } else {
                data_red_packetList.clear();
                messageRedPacketListAdapter.notifyDataSetChanged();
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (messageRedPacketListBean != null && messageRedPacketListBean.getDataList() != null) {
                data_red_packetList.addAll(messageRedPacketListBean.getDataList());
            }
            messageRedPacketListAdapter.notifyDataSetChanged();
            messageRedPacketListAdapter.correctCurrentPage();
        }
        if (data_red_packetList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }

}
