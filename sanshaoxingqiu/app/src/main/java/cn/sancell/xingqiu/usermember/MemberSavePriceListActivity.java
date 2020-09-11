package cn.sancell.xingqiu.usermember;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.usermember.adapter.MemberSavePriceListAdapter;
import cn.sancell.xingqiu.usermember.bean.MemberSavePriceListBean;
import cn.sancell.xingqiu.usermember.contract.MemberSavePriceListContract;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

public class MemberSavePriceListActivity extends BaseMVPActivity<MemberSavePriceListContract.MemberSavePriceListPresenter>
        implements MemberSavePriceListContract.MemberSavePriceListView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    @BindView(R.id.tv_save_total_price)
    RelativeSizeTextView tv_save_total_price;
    @BindView(R.id.rcv_save_price_list)
    RecyclerView rcv_save_price_list;
    private MemberSavePriceListAdapter memberSavePriceListAdapter;
    private List<MemberSavePriceListBean.MemberSavePriceBean> data_save_price;

    private int economyMoneyE2;


    @Override
    protected MemberSavePriceListContract.MemberSavePriceListPresenter createPresenter() {
        return new MemberSavePriceListContract.MemberSavePriceListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_save_price_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        economyMoneyE2 = getIntent().getIntExtra(Constants.Key.KEY_1, 0);
        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 440 / 375));
        btn_back.setOnClickListener(this);
        rcv_save_price_list.setLayoutManager(new LinearLayoutManager(this));
        tv_save_total_price.setTagText(StringUtils.getPrice(economyMoneyE2));
        tv_save_total_price.setEndText(StringUtils.getPriceDecimal(economyMoneyE2));
        mPresenter.GetMemberSavePriceList(this);
    }

    @Override
    protected BaseView getView() {
        return this;
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
        tv_refresh.setOnClickListener(view -> mPresenter.GetMemberSavePriceList(MemberSavePriceListActivity.this));
    }

    @Override
    public void getMemberSavePriceListSuccess(MemberSavePriceListBean memberSavePriceListBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        data_save_price = memberSavePriceListBean.getDataList();
        memberSavePriceListAdapter = new MemberSavePriceListAdapter(data_save_price);
        rcv_save_price_list.setAdapter(memberSavePriceListAdapter);
    }
}
