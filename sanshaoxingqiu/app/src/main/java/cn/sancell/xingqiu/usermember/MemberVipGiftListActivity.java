package cn.sancell.xingqiu.usermember;

import android.content.Intent;
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
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.usermember.adapter.MemberLimitTimeGiftListAdapter;
import cn.sancell.xingqiu.usermember.adapter.MemberVipAllGiftListAdapter;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.usermember.contract.MemberVipGiftListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

public class MemberVipGiftListActivity extends BaseMVPToobarActivity<MemberVipGiftListContract.MemberVipGiftListPresenter>
        implements MemberVipGiftListContract.MemberVipGiftListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_gift_list)
    RecyclerView rcv_gift_list;
    private MemberVipAllGiftListAdapter memberVipAllGiftListAdapter;
    private List<LikeBean> data_gift = new ArrayList<>();

    MemberLimitTimeGiftListAdapter memberLimitTimeGiftListAdapter;
    private List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data_limitList=new ArrayList<>();

    private String goodsGiftLevel;
    private boolean isSeckill;
    private int totalcount;

    @Override
    protected MemberVipGiftListContract.MemberVipGiftListPresenter createPresenter() {
        return new MemberVipGiftListContract.MemberVipGiftListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_vip_gift_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        goodsGiftLevel = getIntent().getStringExtra(Constants.Key.KEY_1);
        isSeckill=getIntent().getBooleanExtra(Constants.Key.KEY_2,false);
        switch (goodsGiftLevel){
            case "2":
                if(isSeckill){
                    initActivityTitle("银猩限量购礼包");
                }else {
                    initActivityTitle("银猩会员礼包");
                }
                break;
            case "3":
                if(isSeckill){
                    initActivityTitle("金猩限量购礼包");
                }else {
                    initActivityTitle("金猩会员礼包");
                }
                break;
            case "4":
                if(isSeckill){
                    initActivityTitle("红猩限量购礼包");
                }else {
                    initActivityTitle("红猩会员礼包");
                }
                break;
        }
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }

        rcv_gift_list.setLayoutManager(new LinearLayoutManager(this));
        if(isSeckill){
            memberLimitTimeGiftListAdapter = new MemberLimitTimeGiftListAdapter(data_limitList);
            rcv_gift_list.setAdapter(memberLimitTimeGiftListAdapter);
            memberLimitTimeGiftListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
                Intent intent = new Intent(MemberVipGiftListActivity.this, MemberVipGiftInfoActivity.class);
                intent.putExtra(Constants.Key.KEY_1, data_limitList.get(i).getGoodsInfo().getId() + "");
                startActivity(intent);
            });
        }else {
            memberVipAllGiftListAdapter = new MemberVipAllGiftListAdapter(data_gift);
            rcv_gift_list.setAdapter(memberVipAllGiftListAdapter);
            memberVipAllGiftListAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
                switch (view.getId()) {
                    case R.id.rl_item:
                        Intent intent = new Intent(MemberVipGiftListActivity.this, MemberVipGiftInfoActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, data_gift.get(i).getId() + "");
                        startActivity(intent);
                        break;
                }
            });
        }
        refreshLayout.setRefreshHeader(new SancellClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if(isSeckill){
                if (memberLimitTimeGiftListAdapter != null) {
                    memberLimitTimeGiftListAdapter.resetCurrentPage();
                }
                mPresenter.GetMemberLimitTimeGiftList(1,goodsGiftLevel, this);
            }else {
                if (memberVipAllGiftListAdapter != null) {
                    memberVipAllGiftListAdapter.resetCurrentPage();
                }
                mPresenter.GetMemberVipGiftList(1, goodsGiftLevel, MemberVipGiftListActivity.this);
            }
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if(isSeckill){
                if (memberLimitTimeGiftListAdapter != null) {
                    if (data_limitList.size() < totalcount) {
                        mPresenter.GetMemberLimitTimeGiftList(memberLimitTimeGiftListAdapter.getNextPage(), goodsGiftLevel, MemberVipGiftListActivity.this);
                    } else {
                        refreshLayout.setNoMoreData(true);
                    }
                } else {
                    mPresenter.GetMemberLimitTimeGiftList(1,goodsGiftLevel, this);
                }
            }else {
                if (memberVipAllGiftListAdapter != null) {
                    if (data_gift.size() < totalcount) {
                        mPresenter.GetMemberVipGiftList(memberVipAllGiftListAdapter.getNextPage(), goodsGiftLevel, MemberVipGiftListActivity.this);
                    } else {
                        refreshLayout.setNoMoreData(true);
                    }
                } else {
                    mPresenter.GetMemberVipGiftList(1, goodsGiftLevel, MemberVipGiftListActivity.this);
                }
            }
        });
        if(isSeckill) {
            mPresenter.GetMemberLimitTimeGiftList(1,goodsGiftLevel, this);
        }else {
            mPresenter.GetMemberVipGiftList(1, goodsGiftLevel, this);
        }
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
            if(isSeckill) {
                mPresenter.GetMemberLimitTimeGiftList(1,goodsGiftLevel, this);
            }else {
                mPresenter.GetMemberVipGiftList(1, goodsGiftLevel, MemberVipGiftListActivity.this);
            }
        });
    }

    @Override
    public void getLimitTimeGiftListSuccess(int page, MemberLimitTimeGiftListBean memberLimitTimeGiftListBean) {
        totalcount = memberLimitTimeGiftListBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_limitList.clear();
            if (memberLimitTimeGiftListBean!=null&&memberLimitTimeGiftListBean.getDataList()!=null&&memberLimitTimeGiftListBean.getDataList().size() > 0) {
                data_limitList.addAll(memberLimitTimeGiftListBean.getDataList());
            }
            memberLimitTimeGiftListAdapter.notifyDataSetChanged();
            memberLimitTimeGiftListAdapter.correctCurrentPage();
        } else {
            if (memberLimitTimeGiftListBean!=null&&memberLimitTimeGiftListBean.getDataList()!=null&&memberLimitTimeGiftListBean.getDataList().size() > 0) {
                data_limitList.addAll(memberLimitTimeGiftListBean.getDataList());
            }
            memberLimitTimeGiftListAdapter.notifyDataSetChanged();
            memberLimitTimeGiftListAdapter.correctCurrentPage();
        }
        if (data_limitList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void getMemberVipGiftListSuccess(int page, HomePageLikeListDataBean.LikeListDataBean listDataBean) {
        totalcount = listDataBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_gift.clear();
            if (listDataBean!=null&&listDataBean.getDataList()!=null&&listDataBean.getDataList().size() > 0) {
                data_gift.addAll(listDataBean.getDataList());
            }
            memberVipAllGiftListAdapter.notifyDataSetChanged();
            memberVipAllGiftListAdapter.correctCurrentPage();
        } else {
            if (listDataBean!=null&&listDataBean.getDataList()!=null&&listDataBean.getDataList().size() > 0) {
                data_gift.addAll(listDataBean.getDataList());
            }
            memberVipAllGiftListAdapter.notifyDataSetChanged();
            memberVipAllGiftListAdapter.correctCurrentPage();
        }
        if (data_gift.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
