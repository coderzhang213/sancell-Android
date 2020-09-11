package cn.sancell.xingqiu.homepage;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homepage.adapter.SeckillSessionProductListAdapter;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionListBean;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionProductListBean;
import cn.sancell.xingqiu.homepage.contract.SeckillListContract;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.CountDownView;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

/**
 * 秒杀模块界面
 */
public class SeckillListActivity extends BaseMVPActivity<SeckillListContract.SeckillListPresenter>
        implements SeckillListContract.SeckillListView, View.OnClickListener {
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.iv_top_bg)
    ImageView iv_top_bg;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.mColumnHorizontalScrollView)
    HorizontalScrollView mColumnHorizontalScrollView;
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroup_content;

    TextView tv_seckill_status;
    CountDownView countdownView;

    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    private SeckillSessionProductListAdapter seckillSessionProductListAdapter;
    private List<SeckillSessionProductListBean.SeckillProductBean> data_productList = new ArrayList<>();


    private List<SeckillSessionListBean.SeckillSessionBean> data_sessionList;


    @Override
    protected SeckillListContract.SeckillListPresenter createPresenter() {
        return new SeckillListContract.SeckillListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seckill_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_top.getLayoutParams();
            lp.topMargin = statusHeight;
            rl_top.setLayoutParams(lp);
            iv_top_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(this, 102) + statusHeight));
        } else {
            iv_top_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(this, 102)));
        }
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        btn_back.setOnClickListener(this);
        mInflater = LayoutInflater.from(this);

        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (seckillSessionProductListAdapter != null) {
                seckillSessionProductListAdapter.resetCurrentPage();
            }
            mPresenter.GetSeckillSessionList(this);
            //mPresenter.GetSessionProductList(1, data_sessionList.get(columnSelectIndex).getId() + "", SeckillListActivity.this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (seckillSessionProductListAdapter != null) {
                mPresenter.GetSessionProductList(seckillSessionProductListAdapter.getNextPage(), data_sessionList.get(columnSelectIndex).getId() + "", SeckillListActivity.this);
            } else {
                if (seckillSessionProductListAdapter != null) {
                    seckillSessionProductListAdapter.resetCurrentPage();
                }
                mPresenter.GetSessionProductList(1, data_sessionList.get(columnSelectIndex).getId() + "", SeckillListActivity.this);
            }
        });
        rcv_list.setLayoutManager(new LinearLayoutManager(this));
        seckillSessionProductListAdapter = new SeckillSessionProductListAdapter(data_productList);
        seckillSessionProductListAdapter.addHeaderView(getHeaderView());
        seckillSessionProductListAdapter.openLoadAnimation();
        rcv_list.setAdapter(seckillSessionProductListAdapter);
        seckillSessionProductListAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            switch (view1.getId()) {
                case R.id.rl_item:
//                    Intent intent = new Intent(SeckillListActivity.this, ProductInfoActivity.class);
//                    intent.putExtra(Constants.Key.KEY_1, data_productList.get(position).getGoodsId());
//                    startActivity(intent);
                    GoodsDetailActivity.start(this,Integer.parseInt(data_productList.get(position).getGoodsId()));

                    break;
                case R.id.iv_seckill_remind:  //提醒
                    mPresenter.GetSeckillRemind(data_productList.get(position).getSeckillRoundId(),
                            data_productList.get(position).getId(),
                            data_productList.get(position).getGoodsId(), SeckillListActivity.this);
                    break;
            }
        });
        mPresenter.GetSeckillSessionList(this);

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_seckill_list_top, (ViewGroup) rcv_list.getParent(), false);
        tv_seckill_status=view.findViewById(R.id.tv_seckill_status);
        countdownView=view.findViewById(R.id.countdownView);
        countdownView.setHourTvBackgroundRes(R.color.colorTran)
                .setHourTvTextColorHex("#FFFFFF")
                .setHourTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setHourTvTextSize(15)

                .setHourColonTvBackgroundColorHex("#00FFFFFF")
                //.setHourColonTvSize(ScreenUtils.dip2px(this, 8), ScreenUtils.dip2px(this, 22))
                .setHourColonTvTextColorHex("#FFFFFF")
                .setHourColonTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setHourColonTvTextSize(15)

                .setMinuteTvBackgroundRes(R.color.colorTran)
                .setMinuteTvTextColorHex("#FFFFFF")
                .setMinuteTvTextSize(15)

                .setMinuteColonTvBackgroundColorHex("#00FFFFFF")
                //.setMinuteColonTvSize(ScreenUtils.dip2px(this, 8), ScreenUtils.dip2px(this, 22))
                .setMinuteColonTvTextColorHex("#FFFFFF")
                .setMinuteColonTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setMinuteColonTvTextSize(15)

                .setSecondTvBackgroundRes(R.color.colorTran)
                .setSecondTvTextColorHex("#FFFFFF")
                .setSecondTvTextSize(15)
                // 设置倒计时结束监听
                .setCountDownEndListener(() -> mPresenter.GetSeckillSessionList(SeckillListActivity.this));
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            if (seckillSessionProductListAdapter != null) {
                seckillSessionProductListAdapter.resetCurrentPage();
            }
            mPresenter.GetSeckillSessionList(this);
            //mPresenter.GetSessionProductList(1, data_sessionList.get(columnSelectIndex).getId() + "", SeckillListActivity.this);
        });
    }

    @Override
    public void getSeckillRemindSuccess() {
        SCApp.getInstance().showSystemCenterToast("设置提醒成功将会在开抢前3分钟短信提醒您");
    }

    @Override
    public void getSeckillSessionListSuccess(List<SeckillSessionListBean.SeckillSessionBean> dataList) {
        data_sessionList = dataList;
        int index1=1000;
        int index2=1000;
        for (int i = 0; i < data_sessionList.size(); i++) {
            if (data_sessionList.get(i).getSeckillRoundStatus() == 1) {
                index1 = i;
                break;
            }
            if (data_sessionList.get(i).getSeckillRoundStatus() == 3) {
                index2 = i;
                break;
            }
        }
        if(index1!=1000){
            columnSelectIndex=index1;
        }
        if(index2!=1000){
            columnSelectIndex=index2;
        }
        if(index1==1000&&index2==1000){
            columnSelectIndex=data_sessionList.size()-1;
        }
        initTabColumn();
        mColumnHorizontalScrollView.post(() -> selectTab(columnSelectIndex));
        mPresenter.GetSessionProductList(1, data_sessionList.get(columnSelectIndex).getId() + "", this);
    }

    @Override
    public void getSessionProductListSuccess(SeckillSessionProductListBean seckillSessionProductListBean, int page) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_productList.clear();
            if (seckillSessionProductListBean != null && seckillSessionProductListBean.getDataList() != null && seckillSessionProductListBean.getDataList().size() > 0) {
                data_productList.addAll(seckillSessionProductListBean.getDataList());
            }
            if (data_productList.size() > 0) {
                countdownView.pauseCountDown();
                switch (data_sessionList.get(columnSelectIndex).getSeckillRoundStatus()) {
                    case 1: //进行中
                        tv_seckill_status.setText("距结束");
                        countdownView.setVisibility(View.VISIBLE);
                        countdownView.setCountTime(data_productList.get(0).getGapTime());
                        countdownView.startCountDown();// 设置倒计时时间戳
                        break;
                    case 2:  //已结束
                        countdownView.setVisibility(View.GONE);
                        tv_seckill_status.setText("已结束");
                        break;
                    case 3: //即将开始
                        countdownView.setVisibility(View.VISIBLE);
                        tv_seckill_status.setText("距开始");
                        countdownView.setCountTime(data_productList.get(0).getGapTime());
                        countdownView.startCountDown();// 设置倒计时时间戳
                        break;
                }
            } else {
                countdownView.setVisibility(View.GONE);
                tv_seckill_status.setText("");
            }
            seckillSessionProductListAdapter.notifyDataSetChanged();
            seckillSessionProductListAdapter.correctCurrentPage();
        } else {
            if (seckillSessionProductListBean != null && seckillSessionProductListBean.getDataList() != null && seckillSessionProductListBean.getDataList().size() > 0) {
                data_productList.addAll(seckillSessionProductListBean.getDataList());
                seckillSessionProductListAdapter.notifyDataSetChanged();
                seckillSessionProductListAdapter.correctCurrentPage();
            } else {
                refreshLayout.setNoMoreData(true);
            }
        }

        if (data_productList.size() == seckillSessionProductListBean.getDataCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }

    /**
     * 初始化Column栏目项
     */
    View view;
    LayoutInflater mInflater;
    /**
     * 当前选中的栏目
     */
    private int columnSelectIndex = 0;

    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = data_sessionList.size();
        for (int i = 0; i < count; i++) {
            Log.e("i===", "" + i);

            view = mInflater.inflate(R.layout.item_seckill_session_list, null);
            RelativeLayout.LayoutParams params;
            if (count < 5) {
                params = new RelativeLayout.LayoutParams(
                        ScreenUtils.getScreenWidth(SeckillListActivity.this) / data_sessionList.size(), ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                params = new RelativeLayout.LayoutParams(
                        ScreenUtils.getScreenWidth(SeckillListActivity.this) / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            TextView tv = view.findViewById(R.id.tv_session_time);
            TextView tv_session_status = view.findViewById(R.id.tv_session_status);
            tv.setId(i);
            tv.setText(DateUtils.getStrTime2(data_sessionList.get(i).getActBeginTime()));
            tv.setTextColor(getResources().getColor(R.color.colorWhite_tran80));
            tv_session_status.setId(i);
            tv_session_status.setText(data_sessionList.get(i).getSeckillRoundStatusStr());
            tv_session_status.setTextColor(getResources().getColor(R.color.colorWhite_tran80));
            if (columnSelectIndex == i) {
                Log.e("columnSelectIndex===", "" + columnSelectIndex);
                tv.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_session_status.setTextColor(getResources().getColor(R.color.colorWhite));
                view.setSelected(true);
            }
            view.setOnClickListener(v -> {
                for (int i1 = 0; i1 < mRadioGroup_content.getChildCount(); i1++) {
                    View localView = mRadioGroup_content.getChildAt(i1);
                    if (localView == v) {
                        selectTab(i1);
                        initTabColumn();
                        break;
                    }
                }
                if (seckillSessionProductListAdapter != null) {
                    seckillSessionProductListAdapter.resetCurrentPage();
                }
                mPresenter.GetSessionProductList(1, data_sessionList.get(columnSelectIndex).getId() + "", SeckillListActivity.this);
            });
            mRadioGroup_content.addView(view, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - ScreenUtils.getScreenWidth(this) / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        // 判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownView != null) {
            countdownView.destoryCountDownView();
        }
    }
}
