package cn.sancell.xingqiu.homeuser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.adapter.LogisticsInfoAdapter;
import cn.sancell.xingqiu.homeuser.bean.LogisticsInfoBean;
import cn.sancell.xingqiu.homeuser.contract.LogisticsInfoContract;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * 单个物流详情界面
 */
public class LogisticsInfoActivity extends BaseMVPToobarActivity<LogisticsInfoContract.LogisticsInfoPresenter>
        implements LogisticsInfoContract.LogisticsInfoView {
    @BindView(R.id.tv_order_num)
    TextView tv_order_num;
    @BindView(R.id.tv_logistics_name)
    TextView tv_logistics_name;
    @BindView(R.id.tv_logistics_num)
    TextView tv_logistics_num;
    @BindView(R.id.tv_logisticsnum_copy)
    TextView tv_logisticsnum_copy;
    @BindView(R.id.rcv_logistics_info)
    RecyclerView rcv_logistics;
    private List<LogisticsInfoBean.InfoBean> data_info;


    private String orderId;   //订单id
    private String parcelId; //包裹id
    private String courierNumber;
    private String courierTag;


    @Override
    protected LogisticsInfoContract.LogisticsInfoPresenter createPresenter() {
        return new LogisticsInfoContract.LogisticsInfoPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logistics_info;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        parcelId = getIntent().getStringExtra(Constants.Key.KEY_2);
        courierNumber=getIntent().getStringExtra(Constants.Key.KEY_3);
        courierTag=getIntent().getStringExtra(Constants.Key.KEY_4);
        initActivityTitle(R.string.logisticsInfo_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rcv_logistics.setLayoutManager(new LinearLayoutManager(this));
        tv_logisticsnum_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_logistics_num.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast("复制成功");
            }
        });
        mPresenter.GetLogisticsInfo(orderId, parcelId,courierNumber,courierTag, LogisticsInfoActivity.this);
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
                mPresenter.GetLogisticsInfo(orderId, parcelId,courierNumber,courierTag, LogisticsInfoActivity.this);
            }
        });
    }

    @Override
    public void getLogisticsInfoSuccess(LogisticsInfoBean logisticsInfoBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        tv_logistics_num.setText("物流单号："+logisticsInfoBean.getNu());
        tv_logistics_name.setText("物流公司："+logisticsInfoBean.getCom());
        tv_order_num.setText("订单编号："+logisticsInfoBean.getOrderNum());
        data_info = logisticsInfoBean.getData();
        LogisticsInfoAdapter logisticsInfoAdapter = new LogisticsInfoAdapter(data_info);
        rcv_logistics.setAdapter(logisticsInfoAdapter);
    }
}
