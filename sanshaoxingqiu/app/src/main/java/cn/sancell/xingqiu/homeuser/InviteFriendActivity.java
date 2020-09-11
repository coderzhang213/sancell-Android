package cn.sancell.xingqiu.homeuser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.activity.MyGoodFriendListActivity;
import cn.sancell.xingqiu.homeuser.adapter.InviteFriendsListAdapter;
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean;
import cn.sancell.xingqiu.homeuser.contract.InviteFriendsContract;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.MaxRecyclerView;
import cn.sancell.xingqiu.widget.NumberDinMediumTextView;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * 邀请好友界面
 */
public class InviteFriendActivity extends BaseMVPActivity<InviteFriendsContract.InviteFriendsPresenter>
        implements InviteFriendsContract.InviteFriendsView, View.OnClickListener {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_user_id)
    NumberDinMediumTextView tv_user_id;
    @BindView(R.id.tv_copy_id)
    TextView tv_copy_id;
    @BindView(R.id.tv_share)
    TextView tv_share;
    /**
     * 邀请好友总数
     */
    @BindView(R.id.tv_invite_total_count)
    TextView tv_invite_total_count;

    /**
     * 邀请好友记录列表
     */
    @BindView(R.id.rcv_invite_friend_list)
    MaxRecyclerView rcv_invite_friend_list;
    @BindView(R.id.tv_name_tip)
    TextView tv_name_tip;
    @BindView(R.id.tv_time_tip)
    TextView tv_time_tip;
    @BindView(R.id.tv_level_tip)
    TextView tv_level_tip;
    @BindView(R.id.tv_query_all)
    TextView tv_query_all;
    /**
     * 暂无记录布局
     */
    @BindView(R.id.tv_no_friends)
    TextView tv_no_friends;
    private List<InviteFriendsListBean.MyInviteInfo> data_invite_list = new ArrayList<>();
    private InviteFriendsListAdapter inviteFriendsListAdapter;

    /**
     * 分享
     */
    private String title = "";
    private String description = "";
    private String linkurl = "";
    private String codeUrl = "";
    private UMImage image;

    @Override
    protected InviteFriendsContract.InviteFriendsPresenter createPresenter() {
        return new InviteFriendsContract.InviteFriendsPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_top.getLayoutParams();
            lp.topMargin = statusHeight;
            rl_top.setLayoutParams(lp);
        }
        tv_copy_id.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        tv_user_id.setText(userBean.getUserId() + "");
        if (userBean.getmActivityInviteData() != null && userBean.getmActivityInviteData().getIsShow() == 1) {
            title = userBean.getmActivityInviteData().getTitle();
            description = userBean.getmActivityInviteData().getDesc();
            linkurl = userBean.getmActivityInviteData().getLink();
            codeUrl = userBean.getmActivityInviteData().getCodeUrl();
        }
        image = new UMImage(this, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));
        btn_back.setOnClickListener(this);
        tv_query_all.setOnClickListener(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (inviteFriendsListAdapter != null) {
                inviteFriendsListAdapter.resetCurrentPage();
            }
            mPresenter.GetInviteFriendsList(1, this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (inviteFriendsListAdapter != null) {
                mPresenter.GetInviteFriendsList(inviteFriendsListAdapter.getNextPage(), this);
            } else {
                mPresenter.GetInviteFriendsList(1, this);
            }
        });
        rcv_invite_friend_list.setLayoutManager(new LinearLayoutManager(this));
        rcv_invite_friend_list.setHasFixedSize(true);
        rcv_invite_friend_list.setNestedScrollingEnabled(false);
        inviteFriendsListAdapter = new InviteFriendsListAdapter(data_invite_list);
        rcv_invite_friend_list.setAdapter(inviteFriendsListAdapter);
        mPresenter.GetInviteFriendsList(1, this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享成功"), 1000);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享失败"), 1000);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            /*if (platform != SHARE_MEDIA.QQ) {
                SCApp.getInstance().showSystemCenterToast("分享取消");
            }*/
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(InviteFriendActivity.this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_id:
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_user_id.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast(R.string.copy_success);
                break;
            case R.id.tv_share:
                DialogUtil.getInviteFriendShareDialog(this, image, linkurl, title, description, codeUrl, umShareListener);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_query_all:
                startActivity(new Intent(InviteFriendActivity.this, MyGoodFriendListActivity.class));
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
            if (inviteFriendsListAdapter != null) {
                inviteFriendsListAdapter.resetCurrentPage();
            }
            mPresenter.GetInviteFriendsList(1, this);
        });
    }

    @Override
    public void getInviteFriendsListSuccess(InviteFriendsListBean inviteFriendsListBean, int page) {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            tv_invite_total_count.setText("邀请记录 " + inviteFriendsListBean.getInviteCount());
            if (inviteFriendsListBean.getInviteCount() > 0) {
                tv_name_tip.setVisibility(View.VISIBLE);
                tv_time_tip.setVisibility(View.VISIBLE);
                tv_level_tip.setVisibility(View.VISIBLE);
                tv_no_friends.setVisibility(View.GONE);
                rcv_invite_friend_list.setVisibility(View.VISIBLE);
                data_invite_list.clear();
                if (inviteFriendsListBean != null && inviteFriendsListBean.getMyInviteInfo() != null) {
                    data_invite_list.addAll(inviteFriendsListBean.getMyInviteInfo());
                }
                inviteFriendsListAdapter.notifyDataSetChanged();
                inviteFriendsListAdapter.correctCurrentPage();
            } else {
                tv_name_tip.setVisibility(View.GONE);
                tv_time_tip.setVisibility(View.GONE);
                tv_level_tip.setVisibility(View.GONE);
                tv_no_friends.setVisibility(View.VISIBLE);
                rcv_invite_friend_list.setVisibility(View.GONE);
                refreshLayout.setNoMoreData(true);
            }

        } else {
            if (inviteFriendsListBean != null && inviteFriendsListBean.getMyInviteInfo() != null) {
                data_invite_list.addAll(inviteFriendsListBean.getMyInviteInfo());
            }
            inviteFriendsListAdapter.notifyDataSetChanged();
            inviteFriendsListAdapter.correctCurrentPage();
        }
        if (data_invite_list.size() == inviteFriendsListBean.getInviteCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
