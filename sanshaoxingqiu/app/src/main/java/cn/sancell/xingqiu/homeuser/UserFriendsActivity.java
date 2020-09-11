package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.adapter.UserInviteFriendsListAdapter;
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean;
import cn.sancell.xingqiu.homeuser.contract.InviteFriendsContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.MaxRecyclerView;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * 我的好友界面
 */
public class UserFriendsActivity extends BaseMVPActivity<InviteFriendsContract.InviteFriendsPresenter>
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

    /**
     * 邀请我的人信息
     */
    @BindView(R.id.rl_inviter_info)
    RelativeLayout rl_inviter_info;
    @BindView(R.id.riv_inviter_photo)
    SimpleDraweeView riv_inviter_photo;
    @BindView(R.id.tv_inviter_name)
    TextView tv_inviter_name;
    @BindView(R.id.iv_inviter_level)
    ImageView iv_inviter_level;
    @BindView(R.id.tv_inviter_id)
    TextView tv_inviter_id;

    /**
     * 邀请好友记录列表
     */
    @BindView(R.id.tv_my_invite)
    TextView tv_my_invite;
    @BindView(R.id.rcv_invite_friend_list)
    MaxRecyclerView rcv_invite_friend_list;
    private List<InviteFriendsListBean.MyInviteInfo> data_invite_list = new ArrayList<>();
    private UserInviteFriendsListAdapter inviteFriendsListAdapter;
    /**
     * 暂无记录布局
     */
    @BindView(R.id.ll_empty_invite)
    LinearLayout ll_empty_invite;
    @BindView(R.id.btn_invite)
    TextView btn_invite;


    @Override
    protected InviteFriendsContract.InviteFriendsPresenter createPresenter() {
        return new InviteFriendsContract.InviteFriendsPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_friends;
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
        btn_back.setOnClickListener(this);
        btn_invite.setOnClickListener(this);
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
        inviteFriendsListAdapter = new UserInviteFriendsListAdapter(data_invite_list);
        rcv_invite_friend_list.setAdapter(inviteFriendsListAdapter);
        mPresenter.GetInviteFriendsList(1, this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_invite:  //邀请
                startActivity(new Intent(UserFriendsActivity.this, InviteFriendActivity.class));
                break;
            case R.id.btn_back:
                finish();
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
        if (inviteFriendsListBean != null && inviteFriendsListBean.getInviteFromInfo() != null && !StringUtils.isTextEmpty(inviteFriendsListBean.getInviteFromInfo().getUserId())) {
            rl_inviter_info.setVisibility(View.VISIBLE);
            riv_inviter_photo.setImageURI(Uri.parse(inviteFriendsListBean.getInviteFromInfo().getGravatar()));
            tv_inviter_name.setText(inviteFriendsListBean.getInviteFromInfo().getNickName());
            tv_inviter_id.setText("ID: " + inviteFriendsListBean.getInviteFromInfo().getUserId());
            switch (inviteFriendsListBean.getInviteFromInfo().getMemberLevel()) {
                case 1:
                    iv_inviter_level.setImageResource(R.mipmap.icon_home_user_name0);
                    break;
                case 2:
                    iv_inviter_level.setImageResource(R.mipmap.icon_home_user_name1);
                    break;
                case 3:
                    iv_inviter_level.setImageResource(R.mipmap.icon_home_user_name2);
                    break;
                case 4:
                    iv_inviter_level.setImageResource(R.mipmap.icon_home_user_name3);
                    break;
            }
        } else {
            rl_inviter_info.setVisibility(View.GONE);
        }
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            tv_my_invite.setText("我邀请的 " + inviteFriendsListBean.getInviteCount());
            if (inviteFriendsListBean.getInviteCount() > 0) {
                ll_empty_invite.setVisibility(View.GONE);
                rcv_invite_friend_list.setVisibility(View.VISIBLE);
                data_invite_list.clear();
                if (inviteFriendsListBean != null && inviteFriendsListBean.getMyInviteInfo() != null) {
                    data_invite_list.addAll(inviteFriendsListBean.getMyInviteInfo());
                }
                inviteFriendsListAdapter.notifyDataSetChanged();
                inviteFriendsListAdapter.correctCurrentPage();
            } else {
                ll_empty_invite.setVisibility(View.VISIBLE);
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
