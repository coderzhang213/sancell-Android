package cn.sancell.xingqiu.homecommunity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.nimlib.sdk.RequestCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homecommunity.adapter.RecommendGroupListAdapter;
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean;
import cn.sancell.xingqiu.homecommunity.contract.RecommendGroupListContract;
import cn.sancell.xingqiu.homepage.SeckillListActivity;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm;
import cn.sancell.xingqiu.im.entity.req.AddListReq;
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.usermember.MemberVipGiftBuyActivity;
import cn.sancell.xingqiu.usermember.MemberVipGiftListActivity;
import cn.sancell.xingqiu.util.BannerJumpUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * 群组推荐更多列表
 */
public class RecommendGroupListActivity extends BaseMVPActivity<RecommendGroupListContract.RecommendGroupListPresenter>
        implements RecommendGroupListContract.RecommendGroupListView, View.OnClickListener {

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
     * 头部广告
     */
    @BindView(R.id.sdv_top_pic)
    SimpleDraweeView sdv_top_pic;

    /**
     * 列表
     */
    @BindView(R.id.rcv_recommend_group_list)
    RecyclerView rcv_recommend_group_list;

    private List<HomeBannerDataBean.BannerBean> data_banner = new ArrayList<>();

    private List<RecommendGroupListBean.RecommGroupBean> data_recomm_list = new ArrayList<>();
    private RecommendGroupListAdapter recommendGroupListAdapter;


    @Override
    protected RecommendGroupListContract.RecommendGroupListPresenter createPresenter() {
        return new RecommendGroupListContract.RecommendGroupListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_group_list;
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
        sdv_top_pic.setOnClickListener(this);
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (recommendGroupListAdapter != null) {
                recommendGroupListAdapter.resetCurrentPage();
            }
            mPresenter.GetRecommGroupListList(1, this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (recommendGroupListAdapter != null) {
                mPresenter.GetRecommGroupListList(recommendGroupListAdapter.getNextPage(), this);
            } else {
                mPresenter.GetRecommGroupListList(1, this);
            }
        });

        rcv_recommend_group_list.setLayoutManager(new LinearLayoutManager(this));
        rcv_recommend_group_list.setHasFixedSize(true);
        rcv_recommend_group_list.setNestedScrollingEnabled(false);
        recommendGroupListAdapter = new RecommendGroupListAdapter(data_recomm_list);
        rcv_recommend_group_list.setAdapter(recommendGroupListAdapter);
        recommendGroupListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_apply_join) {

                ScClient.applyJoinWithVer(this, data_recomm_list.get(position).getTid(), PreferencesUtils.getString(Constants.Key.key_im_accid, ""), new TeamApplyListener() {
                    @Override
                    public void onSuccess(Object o) {
                        data_recomm_list.get(position).setInGroup(1);
                        recommendGroupListAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }

                    @Override
                    public void showInputDialog() {
                        ApplyJoinTeamInputDialogFgm dialogFgm = ApplyJoinTeamInputDialogFgm.newInstance();
                        dialogFgm.setOnApplyListener(str -> {
                            ScClient.applyJoinTeam(RecommendGroupListActivity.this, data_recomm_list.get(position).getTid()
                                    , null, PreferencesUtils.getString(Constants.Key.key_im_accid, ""), new RequestCallback() {
                                        @Override
                                        public void onSuccess(Object o) {

                                            data_recomm_list.get(position).setInGroup(1);
                                            recommendGroupListAdapter.notifyItemChanged(position);
                                        }

                                        @Override
                                        public void onFailed(int i) {

                                        }

                                        @Override
                                        public void onException(Throwable throwable) {

                                        }
                                    });
                        });
                        dialogFgm.show(getSupportFragmentManager(), "apply");
                    }
                });

            } else if (view.getId() == R.id.iv_join_group_chat) {
                ScClient.enterTeamChat(this, data_recomm_list.get(position).getTid());
            }
        });
        mPresenter.GetRecommGroupListList(1, this);
        AddListReq req = new AddListReq();
        req.bannerAdType = "3";
        mPresenter.getAdList(this, req);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_top_pic:  //点击头部广告
                BannerJumpUtils.bannerJump(this, data_banner, 0);
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
            if (recommendGroupListAdapter != null) {
                recommendGroupListAdapter.resetCurrentPage();
            }
            mPresenter.GetRecommGroupListList(1, this);
        });
    }

    @Override
    public void getRecommGroupListSuccess(RecommendGroupListBean recommGroupBeanList, int page) {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_recomm_list.clear();
            if (recommGroupBeanList != null) {
                data_recomm_list.addAll(recommGroupBeanList.getDataList());
            }
            recommendGroupListAdapter.notifyDataSetChanged();
            recommendGroupListAdapter.correctCurrentPage();

        } else {
            if (recommGroupBeanList != null) {
                data_recomm_list.addAll(recommGroupBeanList.getDataList());
            }
            recommendGroupListAdapter.notifyDataSetChanged();
            recommendGroupListAdapter.correctCurrentPage();
        }
        if (data_recomm_list.size() == recommGroupBeanList.getDataCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void getAdList(HomeBannerDataBean data) {
        if (data != null && data.getDataList() != null && data.getDataList().size() > 0) {
            data_banner = data.getDataList();
            sdv_top_pic.setVisibility(View.VISIBLE);
            sdv_top_pic.setImageURI(data.getDataList().get(0).getCoverPic());
        }
    }
}
