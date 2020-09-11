package cn.sancell.xingqiu.im.ui.red.RpDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.homeuser.UserRedPacketListActivity;
import cn.sancell.xingqiu.im.entity.req.RpDetailReq;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.util.BigDecimalUtils;
import cn.sancell.xingqiu.util.FontUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 红包详情
 * @time 2019-11-18 20:37
 */
public class RpDetailActivity extends BaseMVPActivity<RpDetailPresenter> implements RpDetailView, View.OnClickListener {


    @BindView(R.id.rv_rp)
    RecyclerView rv_rp;
    @BindView(R.id.tv_empty)
    AppCompatTextView tv_empty;
    @BindView(R.id.tv_rp_page)
    TextView tv_rp_page;
    @BindView(R.id.tv_grab_money)
    TextView tv_grab_money;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_send_name)
    TextView tv_send_name;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_garb_count)
    AppCompatTextView tv_garb_count;
    @BindView(R.id.common_fresh)
    SmartRefreshLayout common_fresh;

    private String rpId;
    private RpDetailAdapter mAdapter;
    private String balance;
    private List<RpDetailRes.ReceiveDetail> mDatas = new ArrayList<>();
    private int mPage = 1;

    public static void start(Context context, String rpId) {
        Intent intent = new Intent(context, RpDetailActivity.class);
        intent.putExtra(IntentKey.rp_id, rpId);
        context.startActivity(intent);
    }

    @Override
    protected RpDetailPresenter createPresenter() {
        return new RpDetailPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_rp_detail;
    }

    public void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.white_back_icon);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initToolBar();
        rpId = getIntent().getStringExtra(IntentKey.rp_id);
        rv_rp.setLayoutManager(new LinearLayoutManager(this));
        tv_rp_page.setOnClickListener(this);
        getRpInfo();
        mPresenter.getRpYue();
        common_fresh.setEnableRefresh(false);
        common_fresh.setOnLoadMoreListener(refreshLayout -> {
            getRpInfo();
            common_fresh.finishLoadMore();
        });

    }

    public void getRpInfo(){
        RpDetailReq req = new RpDetailReq();
        req.redId = rpId;
        req.page = mPage+"";
        mPresenter.getDetail(req);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void getDetailSuccess(RpDetailRes res) {
        if (res != null) {
            if (res.redUserName.length() >10){
                res.redUserName = res.redUserName.substring(0,9) +"...";
            }
            tv_send_name.setText(String.format(getResources().getString(R.string.who_rp), res.redUserName));
            if (res.redType == UiHelper.PIN_RP_TYPE) {
                Drawable nav_right = getResources().getDrawable(R.mipmap.icon_rp_pin);
                nav_right.setBounds(0, 0, nav_right.getMinimumWidth(), nav_right.getMinimumHeight());
                tv_send_name.setCompoundDrawables(null, null, nav_right, null);
            } else {
                tv_send_name.setCompoundDrawables(null, null, null, null);
            }
            if (res.getGrabMoney() <= 0) {
                tv_grab_money.setText("红包已抢完！");
                tv_grab_money.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            } else {
                tv_grab_money.setText(FontUtils.getInstance().changeTextSize(this, 14,
                        String.format(getResources().getString(R.string.price_rmb), res.grabMoney), 0, 1));
            }
            tv_desc.setText(res.showInfo);
            tv_garb_count.setText(String.format(getResources().getString(R.string.garb_count_total), res.redNum, res.redReceviceSumNum, res.redNum));

            if (res.receiveDetail != null) {
                mDatas.addAll(res.receiveDetail);
            }


            if (mDatas != null && mDatas.size() > 0) {
                //展示列表
                if (res.redType == 2 && res.redNum == res.redReceviceSumNum) {
                    //拼手气已抢完
                    mAdapter = new RpDetailAdapter(getPinSkyChild(mDatas));
                } else {
                    mAdapter = new RpDetailAdapter(mDatas);
                }

                rv_rp.setAdapter(mAdapter);
                tv_empty.setVisibility(View.GONE);
                mPage++;

            } else {
                //empty
                tv_empty.setVisibility(View.VISIBLE);
                rv_rp.setVisibility(View.GONE);
            }

            if (res.receiveDetailCount > mDatas.size()){
                common_fresh.setEnableLoadMore(true);
            }else{
                common_fresh.setEnableLoadMore(false);
            }

        }
    }

    @Override
    public void getDetailError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void getYueSuccess(YueRes res) {
        balance = res.balance;
    }

    @Override
    public void getYueError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    public List<RpDetailRes.ReceiveDetail> getPinSkyChild(List<RpDetailRes.ReceiveDetail> datas) {

        double max = 0;
        int index = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getGetMoenyDouble() > max) {
                max = datas.get(i).getGetMoenyDouble();
                index = i;
            }
        }
        datas.get(index).isSkyChild = true;
        return datas;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_rp_page) {
            UserRedPacketListActivity.start(this, Long.parseLong(BigDecimalUtils.mul(balance, "100", 0)));
        }
    }
}
