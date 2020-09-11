package cn.sancell.xingqiu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.AppManager;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.dialog.MakeFansDialogFgm;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeshoppingcar.ProductCreateOrderActivity;
import cn.sancell.xingqiu.homeshoppingcar.ProductPaymentActivity;
import cn.sancell.xingqiu.homeshoppingcar.contract.PayResultContract;
import cn.sancell.xingqiu.homeuser.MemberPaymentActivity;
import cn.sancell.xingqiu.homeuser.OrderInfoActivity;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.order.detail.PinDetailActivity;
import cn.sancell.xingqiu.order.entity.req.PinOrderPackageReq;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.order.orderInfo.PinOrderPackInfoActivity;
import cn.sancell.xingqiu.order.pin.PinOrderActivity;
import cn.sancell.xingqiu.service.PayDataService;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * 支付结果展示界面
 */
public class WXPayEntryActivity extends BaseMVPActivity<PayResultContract.PayResultPresenter>
        implements PayResultContract.PayResultView, View.OnClickListener, IWXAPIEventHandler {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.top_background)
    View top_background;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_actual_price_tip)
    TextView tv_actual_price_tip;
    @BindView(R.id.tv_actual_price)
    RelativeSizeTextView tv_actual_price;
    @BindView(R.id.iv_success_mark)
    ImageView iv_success_mark;
    @BindView(R.id.tv_address_name)
    TextView tv_address_name;
    @BindView(R.id.tv_address_phone)
    TextView tv_address_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_order_detail)
    TextView tv_order_detail;
    @BindView(R.id.tv_continue)
    TextView tv_continue;
    @BindView(R.id.tv_pay_title)
    AppCompatTextView tv_pay_title;
    @BindView(R.id.view_bg)
    View view_bg;

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private int code;

    private boolean isNeedConfirmPayResult;//是否需要调用确认支付接口告知后台服务器（客户端支付成功需要调用确认支付接口）
    private boolean isWXBack;//是否是微信支付回调
    private String orderId;
    private String Tag; //区分普通商品和会员礼包，普通商品需要当前界面展示支付结果，会员礼包不在这个界面处理，需要回到上个界面调转到另外界面（MemberBuyGiftSuccessActivity）

    @Override
    protected PayResultContract.PayResultPresenter createPresenter() {
        return new PayResultContract.PayResultPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pay_result;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(WXPayEntryActivity.this, false);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) top_background.getLayoutParams();
        lp1.height = statusHeight + ScreenUtils.dip2px(this, 280);
        top_background.setLayoutParams(lp1);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_top.getLayoutParams();
            lp.topMargin = statusHeight;
            rl_top.setLayoutParams(lp);
        }
        orderId = PreferencesUtils.getString(Constants.Key.KEY_orderId, "");
        Tag = PreferencesUtils.getString(Constants.Key.KEY_orderTag, "");


        isNeedConfirmPayResult = getIntent().getBooleanExtra(Constants.Key.KEY_1, false);
        isWXBack = getIntent().getBooleanExtra(Constants.Key.KEY_2, true);
        if (Tag.equals(ProductCreateOrderActivity.mPageName)) {
            paySuccess();
            setAddress();
        } else {
            if (!isWXBack) {
                if (isNeedConfirmPayResult) {
                    paySuccess();
                    setAddress();
//                    mPresenter.ConfirmPayResult(orderId, WXPayEntryActivity.this);

                } else {
                    payFail();
                    //mPresenter.GetOrderInfo(orderId,WXPayEntryActivity.this);
                }
            }
        }
        btn_back.setOnClickListener(this);
        tv_order_detail.setOnClickListener(this);
        tv_continue.setOnClickListener(this);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            code = resp.errCode;
            if (Tag.equals(MemberPaymentActivity.mPageName)) {
                PreferencesUtils.put(Constants.Key.KEY_weixinCode, code + "");
                finish();
            } else if (Tag.equals(ProductPaymentActivity.mPageName)) {
                if (code == 0) {  //成功
                    paySuccess();
                    mNetworkErrorLayout.setVisibility(View.GONE);
                    tv_title.setText(R.string.pay_success);
                    //  mPresenter.ConfirmPayResult(orderId, WXPayEntryActivity.this);
                    setAddress();

                    //支付成功关闭上个页面
                    Activity activity = AppManager.getInstance().getActivity(ProductPaymentActivity.class);
                    if (activity != null) {
                        activity.finish();
                    }

                } else {
//                    payFail();
                    finish();
                }
            }
        }
    }

    public void payFail() {
        iv_success_mark.setImageResource(R.mipmap.icon_payresult_fail);
        tv_pay_title.setText(R.string.pay_fail);
        tv_title.setText(R.string.pay_fail);
        if (PreferencesUtils.getInt(Constants.Key.KEY_isOneYuanPurchase, 0) == 1) {  //是一元购订单显示5分钟内可以支付
            tv_actual_price_tip.setText(R.string.pay_fail_tip_five);
            PreferencesUtils.put(Constants.Key.KEY_isOneYuanPurchase, 0);
        } else { //显示30分钟内可以支付
            tv_actual_price_tip.setText(R.string.pay_fail_tip);
            tv_actual_price.setText("");
        }
    }

    public void paySuccess() {
        iv_success_mark.setImageResource(R.mipmap.icon_payresult_success);
        tv_pay_title.setText(R.string.pay_success);
        tv_title.setText(R.string.pay_success);
        tv_actual_price_tip.setText(R.string.pay_actual_price);
        view_bg.setVisibility(View.VISIBLE);
        //检查会员信息
        showLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getPayUserMember(WXPayEntryActivity.this);
            }
        },1100);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                AppManager.getInstance().finishActivity(ProductPaymentActivity.class);
                finish();
                break;
            case R.id.tv_order_detail:
                Intent intent = new Intent(WXPayEntryActivity.this, OrderInfoActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                startActivity(intent);
                AppManager.getInstance().finishActivity(ProductPaymentActivity.class);
                finish();
                break;
            case R.id.tv_continue:
                // AppManager.getInstance().finishActivity(PinOrderActivity.class, PinDetailActivity.class, GoodsDetailActivity.class);
                HomeTabsActivity.Companion.start(this, 0);
                finish();
                break;
        }
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
        showLoading(false);
        view_bg.setVisibility(View.GONE);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isWXBack) {
                    if (isNeedConfirmPayResult) {
                        paySuccess();
                        mNetworkErrorLayout.setVisibility(View.GONE);
                        tv_title.setText(R.string.pay_success);
                        //  mPresenter.ConfirmPayResult(orderId, WXPayEntryActivity.this);
                        setAddress();
                    }
//                    else {
//                        mPresenter.GetOrderInfo(orderId,WXPayEntryActivity.this);
//                        payFail();
//                    }
                } else {
                    if (code == 0) {  //成功
                        paySuccess();
                        mNetworkErrorLayout.setVisibility(View.GONE);
                        tv_title.setText(R.string.pay_success);
                        setAddress();
                        //  mPresenter.ConfirmPayResult(orderId, WXPayEntryActivity.this);
                    }
//                    else {
//                        payFail();
//                    }
                }
            }
        });
    }

    public void setAddress() {

        OrderRes.AddressInfoBean info = PayDataService.mAddress;
        if (info != null) {

            tv_address_name.setText(info.consignee);
            tv_address_phone.setText(info.mobile);
            tv_address.setText(new StringBuffer(info.codeString.replaceAll("-", "") + info.address));
            tv_actual_price.setTagText(StringUtils.getPrice(PayDataService.totalPrice));
            tv_actual_price.setStartText("¥");
            tv_actual_price.setStartProportion(0.66f);
            tv_actual_price.setEndText(StringUtils.getPriceDecimal(PayDataService.totalPrice));
            tv_actual_price.setEndProportion(0.66f);
        }


    }

    @Override
    public void getOrderInfoSuccess(OderInfoDataBean oderInfoDataBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        tv_address_name.setText(oderInfoDataBean.getAddressInfo().getConsignee());
        tv_address_phone.setText(oderInfoDataBean.getAddressInfo().getMobile());
        tv_address.setText(oderInfoDataBean.getAddressInfo().getCodeString().replaceAll("-", "") + oderInfoDataBean.getAddressInfo().getAddress());
        tv_actual_price.setTagText(StringUtils.getPrice(oderInfoDataBean.getTotalMoneyAmtE2()));
        tv_actual_price.setStartText("¥");
        tv_actual_price.setStartProportion(0.66f);
        tv_actual_price.setEndText(StringUtils.getPriceDecimal(oderInfoDataBean.getTotalMoneyAmtE2()));
        tv_actual_price.setEndProportion(0.66f);
        /*if (oderInfoDataBean.getPayState() == 3) {
            paySuccess();
        } else {
            payFail();
        }*/
    }

    @Override
    public void confirmSuccess() {

        //mPresenter.GetOrderInfo(orderId, WXPayEntryActivity.this);
    }

    @Override
    public void getUserMemberSuccess(UserMemberRes res) {
        showLoading(false);
        view_bg.setVisibility(View.GONE);
        int history = AppUtils.getUserMemberLevel();
        //大于当前等级提示
        PreferencesUtils.put(UiHelper.KEY_USER_MEMBER,res);
        if (res.realMemberLevel <= history){
            return;
        }
        String level = "";
        if (res.realMemberLevel == 6) {
            level = "成为三少医美一星粉丝";
        } else if (res.realMemberLevel == 7) {
            level = "成为三少医美二星粉丝";
        } else if (res.realMemberLevel == 8) {
            level = "成为三少医美三星粉丝";
        } else {
            return;
        }
        //update
        MakeFansDialogFgm dialog = MakeFansDialogFgm.Companion.newInstance(level);
        dialog.show(getSupportFragmentManager(), "vip");
    }
}