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
import cn.sancell.xingqiu.usermember.adapter.MemberOpenRecordsListAdapter;
import cn.sancell.xingqiu.usermember.bean.MemberOpenRecordsListBean;
import cn.sancell.xingqiu.usermember.contract.MemberOpenRecordsListContract;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class MemberOpenRecordsListActivity extends BaseMVPActivity<MemberOpenRecordsListContract.MemberOpenRecordsListPresenter>
        implements MemberOpenRecordsListContract.MemberOpenRecordsListView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;

    @BindView(R.id.rcv_records_list)
    RecyclerView rcv_records_list;
    private MemberOpenRecordsListAdapter memberOpenRecordsListAdapter;
    private List<MemberOpenRecordsListBean.MemberOpenRecordsBean> data_records;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    @Override
    protected MemberOpenRecordsListContract.MemberOpenRecordsListPresenter createPresenter() {
        return new MemberOpenRecordsListContract.MemberOpenRecordsListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_open_records_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(MemberOpenRecordsListActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }

        iv_bg.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 440 / 375));
        btn_back.setOnClickListener(this);
        rcv_records_list.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.GetMemberRecordsList(this);
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
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.GetMemberRecordsList(MemberOpenRecordsListActivity.this);
            }
        });
    }

    @Override
    public void getOpenRecordsListSuccess(List<MemberOpenRecordsListBean.MemberOpenRecordsBean> dataList) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        data_records = dataList;
        memberOpenRecordsListAdapter = new MemberOpenRecordsListAdapter(data_records);
        rcv_records_list.setAdapter(memberOpenRecordsListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
