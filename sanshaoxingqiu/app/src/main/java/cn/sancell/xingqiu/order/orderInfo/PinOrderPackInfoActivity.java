package cn.sancell.xingqiu.order.orderInfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.common.media.imagepicker.loader.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.glide.ImageLoaderUtils;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.OrderPackInfoActivity;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.interfaces.OnOrderItemFunListener;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.order.entity.req.PinCancelReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderPackageReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderReq;
import cn.sancell.xingqiu.order.entity.res.PinOrderPackRes;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.FontUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.PriceUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * 包裹详情页（支付成功后分包裹）
 */
public class PinOrderPackInfoActivity extends BaseMVPToobarActivity<PinOrderPackPresenter>
        implements PinOrderPackView, View.OnClickListener {


    @BindView(R.id.tv_pin_states)
    AppCompatTextView tv_pin_states;
    @BindView(R.id.tv_pin_states_desc)
    AppCompatTextView tv_pin_states_desc;
    @BindView(R.id.tv_name_phone)
    AppCompatTextView tv_name_phone;
    @BindView(R.id.tv_address)
    AppCompatTextView tv_address;
    @BindView(R.id.iv_order_goods)
    AppCompatImageView iv_order_goods;
    @BindView(R.id.tv_goods_name)
    AppCompatTextView tv_goods_name;
    @BindView(R.id.tv_goods_desc)
    AppCompatTextView tv_goods_desc;
    @BindView(R.id.tv_goods_price)
    AppCompatTextView tv_goods_price;
    @BindView(R.id.tv_goods_count)
    AppCompatTextView tv_goods_count;
    @BindView(R.id.tv_remark)
    AppCompatTextView tv_remark;
    @BindView(R.id.tv_remark_desc)
    AppCompatTextView tv_remark_desc;

    @BindView(R.id.vg_order_num)
    VgOrderItem vg_order_num;
    @BindView(R.id.vg_time_create)
    VgOrderItem vg_time_create;
    @BindView(R.id.vg_pay_type)
    VgOrderItem vg_pay_type;
    @BindView(R.id.vg_pay_time)
    VgOrderItem vg_pay_time;

    @BindView(R.id.ll_invoice)
    LinearLayout ll_invoice;
    @BindView(R.id.vg_invoice)
    VgOrderItem vg_invoice;
    @BindView(R.id.vg_invoice_type)
    VgOrderItem vg_invoice_type;
    @BindView(R.id.vg_invoice_content)
    VgOrderItem vg_invoice_content;

    @BindView(R.id.vg_total_price)
    VgOrderPriceItem vg_total_price;
    @BindView(R.id.vg_freight)
    VgOrderPriceItem vg_freight;
    @BindView(R.id.vg_rp)
    VgOrderPriceItem vg_rp;
    @BindView(R.id.tv_pay_price)
    AppCompatTextView tv_pay_price;

    @BindView(R.id.rl_cancel)
    RelativeLayout rl_cancel;
    @BindView(R.id.tv_pin_cancel)
    AppCompatTextView tv_pin_cancel;
    private PinOrderPackRes mPinOrderPackRes;
//
//    @BindView(R.id.rl_success_no_delivery)
//    RelativeLayout rl_success_no_delivery;
//    @BindView(R.id.tv_no_delivery_purchase)
//    AppCompatTextView tv_no_delivery_purchase;
//    @BindView(R.id.tv_no_delivery_cancel)
//    AppCompatTextView tv_no_delivery_cancel;
//
//    @BindView(R.id.rl_success_start_delivery)
//    RelativeLayout rl_success_start_delivery;
//    @BindView(R.id.tv_pin_start_delivery_receive)
//    AppCompatTextView tv_pin_start_delivery_receive;
//    @BindView(R.id.tv_pin_look_delivery)
//    AppCompatTextView tv_pin_look_delivery;
//    @BindView(R.id.tv_pin_start_delivery_purchase)
//    AppCompatTextView tv_pin_start_delivery_purchase;
//
//    @BindView(R.id.rl_success_receive)
//    RelativeLayout rl_success_receive;
//    @BindView(R.id.tv_pin_already_receive)
//    AppCompatTextView tv_pin_already_receive;
//    @BindView(R.id.tv_pin_evaluate)
//    AppCompatTextView tv_pin_evaluate;
//    @BindView(R.id.tv_delete_order)
//    AppCompatTextView tv_delete_order;


    private String mOrderId;
    private String mBuyOrderId;

    public static void start(Context context, String orderId) {
        Intent intent = new Intent(context, PinOrderPackInfoActivity.class);
        intent.putExtra(IntentKey.PIN_DETAIL_ORDER_ID, orderId);
        context.startActivity(intent);
    }


    @Override
    protected PinOrderPackPresenter createPresenter() {
        return new PinOrderPackPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pin_order_pack;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("订单详情", R.mipmap.icon_goods_service, v -> showCallPhone());

        mOrderId = getIntent().getStringExtra(IntentKey.PIN_DETAIL_ORDER_ID);
        getPackOrderInfo();
        tv_pin_cancel.setOnClickListener(this);
        iv_order_goods.setOnClickListener(this);

    }

//    public void initListener() {
//        tv_pin_cancel.setOnClickListener(this);
//        tv_pin_cancel.setOnClickListener(this);
//        tv_no_delivery_cancel.setOnClickListener(this);
//        tv_no_delivery_purchase.setOnClickListener(this);
//        tv_pin_start_delivery_purchase.setOnClickListener(this);
//        tv_pin_start_delivery_receive.setOnClickListener(this);
//        tv_pin_look_delivery.setOnClickListener(this);
//        tv_pin_already_receive.setOnClickListener(this);
//        tv_pin_evaluate.setOnClickListener(this);
//        tv_delete_order.setOnClickListener(this);
//    }

    public void getPackOrderInfo() {
        PinOrderPackageReq req = new PinOrderPackageReq();
        req.id = mOrderId;
        mPresenter.getPinOrderInfo(req);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void getPackInfoSuccess(PinOrderPackRes res) {

        mPinOrderPackRes = res;
        if (res.pageStatus == 1) {
            //已支付
            tv_pin_states.setText("已支付");
            tv_pin_states_desc.setText("直拼中，若直拼失败，钱款自动原路退回");
        } else if (res.pageStatus == 2) {
            tv_pin_states.setText("待发货");
            tv_pin_states_desc.setText("订单正在处理中，请耐心等待。");

        } else if (res.pageStatus == 3) {
            tv_pin_states.setText("已发货");
            tv_pin_states_desc.setText("订单已经在路上，请耐心等待。");

        } else if (res.pageStatus == 4) {
            tv_pin_states.setText("已完成");
            tv_pin_states_desc.setText("感谢您使用三少医美，期待再次为您服务。");
        } else if (res.pageStatus == 5) {
            tv_pin_states.setText("直拼失败，退款中");
            tv_pin_states_desc.setText("系统处理中，请耐心等待");
        } else if (res.pageStatus == 6) {
            tv_pin_states.setText("直拼失败，已退款");
            tv_pin_states_desc.setText("退款完成");
        } else if (res.pageStatus == 7) {
            tv_pin_states.setText("直拼取消，退款中");
            tv_pin_states_desc.setText("系统处理中，请耐心等待");
        } else {
            tv_pin_states.setText("直拼取消，已退款");
            tv_pin_states_desc.setText("退款完成");
        }
        showBottom(res.pageStatus);

        mBuyOrderId = res.buyOrderId;

        if (res.addressInfo != null) {

            tv_address.setText(res.addressInfo.codeString);
            tv_name_phone.setText(String.format(getString(R.string.name_phone), res.addressInfo.consignee, res.addressInfo.mobile));
        }
        if (res.goods != null) {
            tv_goods_name.setText(res.goods.title);
            Glide.with(this).load(res.goods.coverPic).into(iv_order_goods);
            tv_goods_desc.setText(res.goods.specification);
            tv_goods_count.setText(String.format(getString(R.string.support_count), res.goods.num));
            tv_goods_price.setText(PriceUtils.getInstance().getMainPrice(this, (int) res.goods.grouponPriceE2, 11));
        }
        //评论
        if (TextUtils.isEmpty(res.remark)) {
            tv_remark.setVisibility(View.GONE);
            tv_remark_desc.setVisibility(View.GONE);
        } else {
            tv_remark.setVisibility(View.VISIBLE);
            tv_remark_desc.setVisibility(View.VISIBLE);
            tv_remark_desc.setText(res.remark);
        }

        //订单信息
        vg_order_num.setData("团单编号", res.grouponNo, "复制");
        vg_order_num.setListener(() -> FontUtils.getInstance().copyText(PinOrderPackInfoActivity.this, res.grouponNo));
        vg_time_create.setData("创建时间", DateUtils.getTimeByStamp(res.orderCreateTime), "");
        vg_pay_type.setData("支付方式", res.payPlatform == 1 ? "微信" : res.payPlatform == 2 ? "支付宝" : "红包全额", "");
        vg_pay_time.setData("支付时间", DateUtils.getTimeByStamp(res.orderPayTime), "");

        //发票信息
        if (res.invoiceData != null) {
            vg_invoice_type.setVisibility(View.VISIBLE);
            vg_invoice_content.setVisibility(View.VISIBLE);
            vg_invoice.setData("发票类型", "普通发票", "查看发票");
            vg_invoice.setListener(() -> {
                if (!TextUtils.isEmpty(res.invoiceData.invoiceUrl)) {
                    UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
                    if (userBean != null) {
                        String userId = userBean.getUserId() + "";
                        String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
                        String token = "userId=" + userId + "&skey=" + skey;
                        try {
                            String url = res.invoiceData.invoiceUrl + "&token=" + URLEncoder.encode(RSAUtils.encryptByPublic_other(token), "UTF-8");
                            if (!StringUtils.isTextEmpty(url)) {
                                Intent intent5 = new Intent(PinOrderPackInfoActivity.this, UrlInfoActivity.class);
                                intent5.putExtra(Constants.Key.KEY_1, url);
                                intent5.putExtra(Constants.Key.KEY_2, "发票详情");
                                startActivity(intent5);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            vg_invoice_type.setData("发票抬头", res.invoiceData.invoiceLookedUp == 1 ? "个人" : "企业", "");
            vg_invoice_content.setData("发票内容", "商品明细", "");

        } else {
            vg_invoice.setData("发票类型", "不开发票", "");
            vg_invoice_type.setVisibility(View.GONE);
            vg_invoice_content.setVisibility(View.GONE);
        }
        //价格信息
        vg_total_price.setData("商品总额", PriceUtils.getInstance().getPriceWithSyp(res.payAmtE2));
        vg_freight.setData("运费", "+0.00");
        vg_rp.setData("红包", "-" + PriceUtils.getInstance().getPriceWithSyp(res.payPointAmtE2));
        tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(this, (int) res.payMoneyAmtE2, 14));
    }

    @Override
    public void getError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void pinCancelOrderSuccess() {
        getPackOrderInfo();
    }


    public void showBottom(int states) {

        rl_cancel.setVisibility(states == 1 ? View.VISIBLE : View.GONE);
//        rl_success_no_delivery.setVisibility(states == 2 ? View.VISIBLE : View.GONE);
//        rl_success_start_delivery.setVisibility(states == 3 ? View.VISIBLE : View.GONE);
//        rl_success_receive.setVisibility(states == 4 ? View.VISIBLE : View.GONE);
    }


    //拨打电话
    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(this, "", mOrderId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pin_cancel:
                PinCancelReq req = new PinCancelReq();
                req.buyOrderId = mBuyOrderId;
                mPresenter.pinCancelOrder(req);
                break;
            case R.id.iv_order_goods:
                if (mPinOrderPackRes != null) {
                    GoodsDetailActivity.start(this, mPinOrderPackRes.goods.goodsId);
                }
                break;
            default:
                break;
        }
    }
}
