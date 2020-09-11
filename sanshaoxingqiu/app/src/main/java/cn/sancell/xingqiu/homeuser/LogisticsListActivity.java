package cn.sancell.xingqiu.homeuser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.adapter.LogisticsInfoAdapter;
import cn.sancell.xingqiu.homeuser.adapter.LogisticsListAdapter;
import cn.sancell.xingqiu.homeuser.bean.LogisticsInfoBean;
import cn.sancell.xingqiu.homeuser.bean.LogisticsListBean;
import cn.sancell.xingqiu.homeuser.contract.LogisticsListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;

public class LogisticsListActivity extends BaseMVPToobarActivity<LogisticsListContract.LogisticsListPresenter>
        implements LogisticsListContract.LogisticsListView {
    /**
     * 多个物流列表布局
     */
    @BindView(R.id.rcv_logistics)
    RecyclerView rcv_logistics;
    private LogisticsListAdapter logisticsListAdapter;
    private List<LogisticsListBean.OrderReceiveInfo.LogisticsBean> data_logistics;
    private LogisticsListBean logisticsListBean;
    /**
     * 单个物流布局
     */
    @BindView(R.id.rl_logisticsinfo)
    RelativeLayout rl_logisticsinfo;
    @BindView(R.id.tv_order_num)
    TextView tv_order_num;
    @BindView(R.id.tv_logistics_name)
    TextView tv_logistics_name;
    @BindView(R.id.tv_logistics_num)
    TextView tv_logistics_num;
    @BindView(R.id.tv_logisticsnum_copy)
    TextView tv_logisticsnum_copy;
    @BindView(R.id.rcv_logistics_info)
    RecyclerView rcv_logistics_info;
    private List<LogisticsInfoBean.InfoBean> data_info;


    private String orderId;   //订单id
    private String parcelId; //包裹id



    @Override
    protected LogisticsListContract.LogisticsListPresenter createPresenter() {
        return new LogisticsListContract.LogisticsListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logistics_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("物流");
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        parcelId = getIntent().getStringExtra(Constants.Key.KEY_2);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rcv_logistics.setLayoutManager(new LinearLayoutManager(this));


        rcv_logistics_info.setLayoutManager(new LinearLayoutManager(this));
        tv_logisticsnum_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_logistics_num.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast("复制成功");
            }
        });

        mPresenter.GetLogisticsListList(orderId,parcelId,this);
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
            mPresenter.GetLogisticsListList(orderId, parcelId, this);
        });
    }

    @Override
    public void getLogisticsListSuccess(LogisticsListBean logisticsListBean) {
        this.logisticsListBean = logisticsListBean;
        if (logisticsListBean.getOrderReceiveInfo() != null) {
            data_logistics = logisticsListBean.getOrderReceiveInfo().getDataList();
            if(data_logistics!=null&&data_logistics.size()==1){
                rl_logisticsinfo.setVisibility(View.VISIBLE);
                rcv_logistics.setVisibility(View.GONE);
                mPresenter.GetLogisticsInfo(orderId,parcelId,data_logistics.get(0).getCourierNumber(),data_logistics.get(0).getCourierTag(),LogisticsListActivity.this);
            }else {
                rl_logisticsinfo.setVisibility(View.GONE);
                rcv_logistics.setVisibility(View.VISIBLE);
                logisticsListAdapter = new LogisticsListAdapter(data_logistics);
                logisticsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        switch (view.getId()) {
                            case R.id.ll_item:
                                Intent intent = new Intent(LogisticsListActivity.this, LogisticsInfoActivity.class);
                                intent.putExtra(Constants.Key.KEY_1, orderId);
                                intent.putExtra(Constants.Key.KEY_2, parcelId);
                                intent.putExtra(Constants.Key.KEY_3, data_logistics.get(i).getCourierNumber());
                                intent.putExtra(Constants.Key.KEY_4, data_logistics.get(i).getCourierTag());
                                startActivity(intent);
                                break;
                        }
                    }
                });
                rcv_logistics.setAdapter(logisticsListAdapter);
            }
        }
    }

    @Override
    public void getLogisticsInfoSuccess(LogisticsInfoBean logisticsInfoBean) {
        tv_logistics_num.setText("物流单号："+logisticsInfoBean.getNu());
        tv_logistics_name.setText("物流公司："+logisticsInfoBean.getCom());
        tv_order_num.setText("订单编号："+logisticsInfoBean.getOrderNum());
        data_info = logisticsInfoBean.getData();
        LogisticsInfoAdapter logisticsInfoAdapter = new LogisticsInfoAdapter(data_info);
        rcv_logistics_info.setAdapter(logisticsInfoAdapter);
    }
}
