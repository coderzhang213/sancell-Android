package cn.sancell.xingqiu.homeshoppingcar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.AppManager;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.homeshoppingcar.bean.AlipayPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.WeiXinPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.contract.ProductPaymentContract;
import cn.sancell.xingqiu.order.entity.req.SCPayReq;
import cn.sancell.xingqiu.service.PayDataService;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.utilPay.PayResult;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.wxapi.WXPayEntryActivity;

/**
 * 商品选择支付方式界面（支付结果统一跳转到WXPayEntryActivity界面处理）
 */
public class ProductPaymentActivity extends BaseMVPActivity<ProductPaymentContract.ProductPaymentPresenter>
        implements ProductPaymentContract.ProductPaymentView, View.OnClickListener {
    public final static String mPageName = "ProductPaymentActivity";
    @BindView(R.id.top_background)
    View top_background;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.tv_pay_price)
    RelativeSizeTextView tv_pay_price;
    @BindView(R.id.rl_alipay)
    RelativeLayout rl_alipay;
    @BindView(R.id.iv_alipay)
    ImageView iv_alipay;
    @BindView(R.id.rl_weixin)
    RelativeLayout rl_weixin;
    @BindView(R.id.iv_weixin)
    ImageView iv_weixin;
    @BindView(R.id.tv_pay)
    TextView tv_pay;

    private int pay_type; //1:支付宝支付  2：微信支付

    /**
     * 上个界面传来的商品总价
     */
    long total_price;
    /**
     * 上个界面传来的订单id
     */
    String orderId;

    /**
     * 微信支付
     */
    PayReq req;
    IWXAPI msgApi;
    private int mPayFrom;


    /**
     *
     * @param context
     * @param orderNo
     * @param price
     * @param payFrom 普通支付 0 ，拼团支付 1
     */
    public static void start(Context context,String orderNo,int price,int payFrom){
        Intent intent = new Intent(context,ProductPaymentActivity.class);
        intent.putExtra(Constants.Key.KEY_1,orderNo);
        intent.putExtra(Constants.Key.KEY_2,price);
        intent.putExtra(IntentKey.PAY_FROM,payFrom);
        context.startActivity(intent);
    }

    @Override
    protected ProductPaymentContract.ProductPaymentPresenter createPresenter() {
        return new ProductPaymentContract.ProductPaymentPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_payment;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        StatusBarUtil.setStatusBarDarkTheme(ProductPaymentActivity.this, false);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) top_background.getLayoutParams();
        lp1.height = statusHeight + ScreenUtils.dip2px(this, 212);
        top_background.setLayoutParams(lp1);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_top.getLayoutParams();
            lp.topMargin = statusHeight;
            rl_top.setLayoutParams(lp);
        }
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        mPayFrom = getIntent().getIntExtra(IntentKey.PAY_FROM,0);
        PreferencesUtils.put(Constants.Key.KEY_orderId, orderId);
        PreferencesUtils.put(Constants.Key.KEY_orderTag, mPageName);
        total_price = getIntent().getIntExtra(Constants.Key.KEY_2, 0);
        tv_pay_price.setTagText(StringUtils.getPrice(total_price));
        tv_pay_price.setStartText("¥");
        tv_pay_price.setStartProportion(0.66f);
        tv_pay_price.setEndText(StringUtils.getPriceDecimal(total_price));
        tv_pay_price.setEndProportion(0.66f);
        tv_pay.setEnabled(false);
        btn_back.setOnClickListener(this);
        rl_alipay.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
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
    public void getWeiXinInfoSuccess(WeiXinPayInfoBean weiXinPayInfoBean) {
        sendWeixinPayReq(weiXinPayInfoBean);
    }

    @Override
    public void getAlipyInfoSuccess(AlipayPayInfoBean alipayPayInfoBean) {
        sendAlipay(alipayPayInfoBean.getAlipaySign());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
//                Intent intent = new Intent(ProductPaymentActivity.this, WXPayEntryActivity.class);
//                intent.putExtra(Constants.Key.KEY_1, false);
//                intent.putExtra(Constants.Key.KEY_2, false);
//                startActivity(intent);
                break;
            case R.id.rl_alipay:
                tv_pay.setEnabled(true);
                pay_type = 1;
                iv_alipay.setImageResource(R.mipmap.icon_car_select_yes);
                iv_weixin.setImageResource(R.mipmap.icon_car_select_no);
                break;
            case R.id.rl_weixin:
                tv_pay.setEnabled(true);
                pay_type = 2;
                iv_alipay.setImageResource(R.mipmap.icon_car_select_no);
                iv_weixin.setImageResource(R.mipmap.icon_car_select_yes);
                break;
            case R.id.tv_pay:

                if (mPayFrom == UiHelper.PIN_PAY){
                    SCPayReq req = new SCPayReq();
                    req.orderNo = orderId;
                    if (pay_type == 1){
                        mPresenter.getPinAliPay(this,req);
                    }else{
                        mPresenter.getWxpay(this,req);
                    }
                }else{
                    if (pay_type == 1) {  //支付宝支付
                        mPresenter.GetAlipayInfo(orderId, ProductPaymentActivity.this);
                    } else if (pay_type == 2) {  //微信支付
                        mPresenter.GetWeiXinInfo(orderId, ProductPaymentActivity.this);
                    }
                }
                break;
        }
    }


    public void sendWeixinPayReq(WeiXinPayInfoBean weiXinPayInfoBean) {

        if (loading_view != null) {
            loading_view.show();
        }
        req = new PayReq();
        req.appId = weiXinPayInfoBean.getAppid();
        req.partnerId = weiXinPayInfoBean.getPartnerid();
        req.prepayId = weiXinPayInfoBean.getPrepayid();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = weiXinPayInfoBean.getNoncestr();
        req.timeStamp = weiXinPayInfoBean.getTimestamp();
        req.sign = RSAUtils.decryptByPublic(weiXinPayInfoBean.getSign());
        msgApi.registerApp(req.appId);
        msgApi.sendReq(req);
        if (loading_view != null) {
            loading_view.dismiss();
        }
//        finish();
    }

    private static final int SDK_PAY_FLAG = 1;

    public void sendAlipay(String sign) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ProductPaymentActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(sign, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                pay_mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler pay_mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SDK_PAY_FLAG:

                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    //            返回码    含义
                    //            9000    订单支付成功
                    //            8000    正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    //            4000    订单支付失败
                    //            5000    重复请求
                    //            6001    用户中途取消
                    //            6002    网络连接出错
                    //            6004    支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    //            其它    其它支付错误
                    switch (payResult.getResultStatus()) {
                        case "9000":
                            Intent intent = new Intent(ProductPaymentActivity.this, WXPayEntryActivity.class);
                            intent.putExtra(Constants.Key.KEY_1, true);
                            intent.putExtra(Constants.Key.KEY_2, false);
                            startActivity(intent);
                            finish();
                            break;
                        default:
//                            Intent intent1 = new Intent(ProductPaymentActivity.this, WXPayEntryActivity.class);
//                            intent1.putExtra(Constants.Key.KEY_1, false);
//                            intent1.putExtra(Constants.Key.KEY_2, false);
//                            startActivity(intent1);
//                            finish();
                            break;
                    }
                    break;
            }
        }
    };

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//            Intent intent = new Intent(ProductPaymentActivity.this, WXPayEntryActivity.class);
//            intent.putExtra(Constants.Key.KEY_1, false);
//            intent.putExtra(Constants.Key.KEY_2, false);
//            startActivity(intent);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
