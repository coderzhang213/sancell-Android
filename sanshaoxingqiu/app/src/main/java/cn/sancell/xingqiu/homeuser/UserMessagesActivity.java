package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.bean.MessageNoticeListBean;
import cn.sancell.xingqiu.homeuser.bean.MessageRedPacketListBean;
import cn.sancell.xingqiu.homeuser.bean.MessageTransactionLogisticsListBean;
import cn.sancell.xingqiu.homeuser.bean.MessagesBean;
import cn.sancell.xingqiu.homeuser.contract.UserMessagesContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class UserMessagesActivity extends BaseMVPToobarActivity<UserMessagesContract.UserMessagesPresenter>
        implements UserMessagesContract.UserMessagesView, View.OnClickListener {
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rl_transaction_logistics_message)
    RelativeLayout rl_transaction_logistics_message;
    @BindView(R.id.iv_red_point_transaction_logistics)
    ImageView iv_red_point_transaction_logistics;
    @BindView(R.id.tv_transaction_logistics)
    TextView tv_transaction_logistics;
    @BindView(R.id.tv_time_transaction_logistics)
    TextView tv_time_transaction_logistics;

    @BindView(R.id.rl_red_packet_message)
    RelativeLayout rl_red_packet_message;
    @BindView(R.id.iv_red_point_red_packet)
    ImageView iv_red_point_red_packet;
    @BindView(R.id.tv_red_packet)
    TextView tv_red_packet;
    @BindView(R.id.tv_time_red_packet)
    TextView tv_time_red_packet;

    @BindView(R.id.rl_notice_message)
    RelativeLayout rl_notice_message;
    @BindView(R.id.iv_red_point_notice)
    ImageView iv_red_point_notice;
    @BindView(R.id.tv_notice)
    TextView tv_notice;
    @BindView(R.id.tv_time_notice)
    TextView tv_time_notice;

    @Override
    protected UserMessagesContract.UserMessagesPresenter createPresenter() {
        return new UserMessagesContract.UserMessagesPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_messages;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.message_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rl_transaction_logistics_message.setOnClickListener(this);
        rl_red_packet_message.setOnClickListener(this);
        rl_notice_message.setOnClickListener(this);
        mPresenter.GetNewestMessages(this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_transaction_logistics_message:
                startActivity(new Intent(UserMessagesActivity.this, MessageTransactionLogisticsListActvity.class));
                break;
            case R.id.rl_red_packet_message:
                startActivity(new Intent(UserMessagesActivity.this, MessageRedPacketListActivity.class));
                break;
            case R.id.rl_notice_message:
                startActivity(new Intent(UserMessagesActivity.this, MessageNoticeListActivity.class));
                break;
        }
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(view -> {
            mPresenter.GetNewestMessages(UserMessagesActivity.this);
        });
    }

    @Override
    public void getNewestMessagesSuccess(MessagesBean messagesBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        MessageTransactionLogisticsListBean.TransactionLogisticsBean logisticsNoticeInfo = messagesBean.getLogisticsNoticeInfo();
        if (logisticsNoticeInfo != null && !StringUtils.isTextEmpty(logisticsNoticeInfo.getPublicTimeStr())) {
            mEmptyLayout.setVisibility(View.GONE);
            rl_transaction_logistics_message.setVisibility(View.VISIBLE);
            if (logisticsNoticeInfo.getReadStatus() == 1) { //已读
                iv_red_point_transaction_logistics.setVisibility(View.GONE);
            } else {
                iv_red_point_transaction_logistics.setVisibility(View.VISIBLE);
            }
            tv_time_transaction_logistics.setText(logisticsNoticeInfo.getPublicTimeStr());
            if (logisticsNoticeInfo.getLogisticsNoticeType() == 3) {  //已收货
                tv_transaction_logistics.setText("您的包裹已完成");
            } else if (logisticsNoticeInfo.getLogisticsNoticeType() == 1) {
                tv_transaction_logistics.setText("您的包裹已发货");
            }
        } else {
            rl_transaction_logistics_message.setVisibility(View.GONE);
        }
        MessageRedPacketListBean.MessageRedPacketBean userPointNoticeInfo = messagesBean.getUserPointNoticeInfo();
        if (userPointNoticeInfo != null && !StringUtils.isTextEmpty(userPointNoticeInfo.getPublicTimeStr())) {
            rl_red_packet_message.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            if (userPointNoticeInfo.getReadStatus() == 1) { //已读
                iv_red_point_red_packet.setVisibility(View.GONE);
            } else {
                iv_red_point_red_packet.setVisibility(View.VISIBLE);
            }
            tv_time_red_packet.setText(userPointNoticeInfo.getPublicTimeStr());
            tv_red_packet.setText(userPointNoticeInfo.getNoticeTitle());
        } else {
            rl_red_packet_message.setVisibility(View.GONE);
        }

        MessageNoticeListBean.MessageNoticeBean noticeInfo = messagesBean.getNoticeInfo();
        if (noticeInfo != null && !StringUtils.isTextEmpty(noticeInfo.getPublicTimeStr())) {
            rl_notice_message.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            if (noticeInfo.getReadStatus() == 1) { //已读
                iv_red_point_notice.setVisibility(View.GONE);
            } else {
                iv_red_point_notice.setVisibility(View.VISIBLE);
            }
            tv_time_notice.setText(noticeInfo.getPublicTimeStr());
            tv_notice.setText(noticeInfo.getTitle());
        } else {
            rl_notice_message.setVisibility(View.GONE);
        }
    }
}
