package cn.sancell.xingqiu.homeuser;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homeshoppingcar.ProductPaymentActivity;
import cn.sancell.xingqiu.homeuser.adapter.OrderInfoPackListAdapter;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.contract.OrderInfoContract;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * 订单详情页（未支付订单、未支付订单用户取消或系统自动取消变成完成订单）
 */
public class OrderInfoActivity extends BaseMVPActivity<OrderInfoContract.OrderInfoPresenter>
        implements OrderInfoContract.OrderInfoView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_contact)
    ImageView btn_contact;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_black)
    TextView tv_title_black;
    @BindView(R.id.rcv_product_pack)
    RecyclerView rcv_product_pack;
    private OrderInfoPackListAdapter orderInfoPackListAdapter;
    private List<OderInfoDataBean.PackListDataBean.PackBean> data_packlist;

    private OderInfoDataBean oderInfoDataBean;

    @BindView(R.id.ll_nopay)
    LinearLayout ll_nopay;
    @BindView(R.id.tv_nopay_cancel)
    TextView tv_nopay_cancel;
    @BindView(R.id.tv_nopay_gopay)
    TextView tv_nopay_gopay;

    @BindView(R.id.ll_undelivered)
    LinearLayout ll_undelivered;
    @BindView(R.id.tv_undelivered_cancel)
    TextView tv_undelivered_cancel;
    @BindView(R.id.tv_undelivered_againpay)
    TextView tv_undelivered_againpay;

    @BindView(R.id.ll_close)
    LinearLayout ll_close;
    @BindView(R.id.tv_close_delete)
    TextView tv_close_delete;
    @BindView(R.id.tv_close_againpay)
    TextView tv_close_againpay;

    /**
     * 头部布局
     */
    private RelativeLayout rl_ordertype;
    private View top_background;
    private TextView tv_ordertype_name;
    private TextView tv_ordertype_desc;
    private TextView tv_top_gopay;
    private ImageView iv_ordertype_mark;
    private TextView tv_address_name;
    private TextView tv_address_phone;
    private TextView tv_address;
    /**
     * 底部布局
     */
    private TextView tv_order_num;
    private TextView tv_ordernum_copy;
    private TextView tv_order_time;
    private RelativeLayout rl_paytype;
    private TextView tv_pay_type;
    private TextView tv_pay_time;
    private RelativeSizeTextView tv_totalprice;
    private RelativeSizeTextView tv_actual_price;
    private TextView tv_red_price_tip;
    private RelativeSizeTextView tv_red_price;
    private RelativeLayout rl_reward;
    private ImageView iv_reward_price_introduce;
    private RelativeSizeTextView tv_reward_price;
    private TextView tv_invoice_type;  //发票类型
    private TextView tv_invoice_open;//补开发票
    private TextView tv_invoice_look;  //查看发票
    private TextView tv_invoice_title_tip; //发票抬头文本
    private TextView tv_invoice_title;  //发票抬头
    private TextView tv_invoice_content_tip, tv_invoice_content;
    private TextView tv_invioce_company_id_tip;  //纳税人识别号文本
    private TextView tv_invoice_company_id;  //纳税人识别号


    private String orderId;

    @Override
    protected OrderInfoContract.OrderInfoPresenter createPresenter() {
        return new OrderInfoContract.OrderInfoPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_info;
    }

    int scrollviewH;

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(OrderInfoActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        btn_back.setOnClickListener(this);
        rcv_product_pack.setLayoutManager(new LinearLayoutManager(this));
        scrollviewH = ScreenUtils.dip2px(this, 80);
        rcv_product_pack.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int h = getUpDistance();
                if (h <= 0) {
                    rl_top.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                    btn_back.setImageResource(R.mipmap.icon_white_back);
                    btn_contact.setImageResource(R.mipmap.icon_white_contact);
                } else if (h > 0 && h <= scrollviewH) {
                    float scale = (float) h / scrollviewH;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    rl_top.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    tv_title.setTextColor(Color.argb((255 - (int) alpha), 255, 255, 255));
                    tv_title_black.setTextColor(Color.argb((int) alpha, 25, 26, 25));
                    btn_back.setImageResource(R.mipmap.icon_white_back);
                    btn_contact.setImageResource(R.mipmap.icon_white_contact);
                } else {
                    rl_top.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    btn_back.setImageResource(R.mipmap.icon_black_back);
                    btn_contact.setImageResource(R.mipmap.icon_black_contact);
                }
            }
        });
        btn_contact.setOnClickListener(this);
        tv_nopay_cancel.setOnClickListener(this);
        tv_nopay_gopay.setOnClickListener(this);
        tv_undelivered_cancel.setOnClickListener(this);
        tv_undelivered_againpay.setOnClickListener(this);
        tv_close_againpay.setOnClickListener(this);
        tv_close_delete.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetOrderInfo(orderId, this);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_orderinfo_top, (ViewGroup) rcv_product_pack.getParent(), false);
        rl_ordertype = view.findViewById(R.id.rl_ordertype);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rl_ordertype.getLayoutParams();
            lp.topMargin = statusHeight + ScreenUtils.dip2px(this, 66);
            rl_ordertype.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rl_ordertype.getLayoutParams();
            lp.topMargin = ScreenUtils.dip2px(this, 66);
            rl_ordertype.setLayoutParams(lp);
        }
        top_background = view.findViewById(R.id.top_background);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) top_background.getLayoutParams();
        lp1.height = statusHeight + ScreenUtils.dip2px(this, 164);
        top_background.setLayoutParams(lp1);
        tv_ordertype_name = view.findViewById(R.id.tv_ordertype_name);
        tv_ordertype_desc = view.findViewById(R.id.tv_ordertype_desc);
        tv_top_gopay = view.findViewById(R.id.tv_top_gopay);
        tv_address_name = view.findViewById(R.id.tv_address_name);
        tv_address_phone = view.findViewById(R.id.tv_address_phone);
        tv_address = view.findViewById(R.id.tv_address);
        iv_ordertype_mark = view.findViewById(R.id.iv_ordertype_mark);
        return view;
    }

    CountDownTimer countDownTimer;

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.layout_orderinfo_foot, (ViewGroup) rcv_product_pack.getParent(), false);
        tv_order_num = view.findViewById(R.id.tv_order_num);
        rl_paytype = view.findViewById(R.id.rl_paytype);
        tv_ordernum_copy = view.findViewById(R.id.tv_ordernum_copy);
        tv_ordernum_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", tv_order_num.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                SCApp.getInstance().showSystemCenterToast(R.string.copy_success);
            }
        });
        tv_order_time = view.findViewById(R.id.tv_order_time);
        tv_pay_type = view.findViewById(R.id.tv_pay_type);
        tv_pay_time = view.findViewById(R.id.tv_pay_time);
        tv_totalprice = view.findViewById(R.id.tv_totalprice);
        tv_actual_price = view.findViewById(R.id.tv_actual_price);
        tv_red_price_tip = view.findViewById(R.id.tv_red_price_tip);
        tv_red_price = view.findViewById(R.id.tv_red_price);
        rl_reward = view.findViewById(R.id.rl_reward);
        iv_reward_price_introduce = view.findViewById(R.id.iv_reward_price_introduce);
        iv_reward_price_introduce.setOnClickListener(this);
        tv_reward_price = view.findViewById(R.id.tv_reward_price);
        tv_invoice_type = view.findViewById(R.id.tv_invoice_type);
        tv_invoice_look = view.findViewById(R.id.tv_invoice_look);
        tv_invoice_open = view.findViewById(R.id.tv_invoice_open);
        tv_invoice_title_tip = view.findViewById(R.id.tv_invoice_title_tip);
        tv_invoice_title = view.findViewById(R.id.tv_invoice_title);
        tv_invoice_content_tip = view.findViewById(R.id.tv_invoice_content_tip);
        tv_invoice_content = view.findViewById(R.id.tv_invoice_content);

        tv_invioce_company_id_tip = view.findViewById(R.id.tv_invioce_company_id_tip);
        tv_invoice_company_id = view.findViewById(R.id.tv_invoice_company_id);
        tv_invoice_look.setOnClickListener(this);
        tv_invoice_open.setOnClickListener(this);
        return view;
    }


    /**
     * 已滑动的距离
     */
    private int getUpDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rcv_product_pack.getLayoutManager();
        View firstVisibItem = rcv_product_pack.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
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
                mPresenter.GetOrderInfo(orderId, OrderInfoActivity.this);
            }
        });
    }

    @Override
    public void cancelOrderSuccess() {
        mPresenter.GetOrderInfo(orderId, OrderInfoActivity.this);
    }

    @Override
    public void deleteOrderSuccess() {
        finish();
    }

    @Override
    public void batchAddSuccess() {
        SCApp.getInstance().showSystemCenterToast(R.string.again_buy_success);
    }

    @Override
    public void openInvoiceSuccess() {
        dialog_invoice.dismiss();
        mPresenter.GetOrderInfo(orderId, this);
    }

    @Override
    public void getOrderInfoSuccess(OderInfoDataBean oderInfoDataBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        this.oderInfoDataBean = oderInfoDataBean;
        data_packlist = oderInfoDataBean.getParcelInfo().getDataList();
        orderInfoPackListAdapter = new OrderInfoPackListAdapter(OrderInfoActivity.this, data_packlist, new OrderInfoPackListAdapter.ProductClickAction() {
            @Override
            public void productClick(String id) {
//                Intent intent = new Intent(OrderInfoActivity.this, ProductInfoActivity.class);
//                intent.putExtra(Constants.Key.KEY_1, id);
//                startActivity(intent);
                GoodsDetailActivity.start(OrderInfoActivity.this, Integer.parseInt(id));

            }
        });
        orderInfoPackListAdapter.addHeaderView(getHeaderView());
        orderInfoPackListAdapter.addFooterView(getFooterView());
        tv_order_num.setText(oderInfoDataBean.getOrderNumber());
        tv_order_time.setText(DateUtils.getStrTime1(oderInfoDataBean.getOrderTime() + ""));
        tv_address.setText(oderInfoDataBean.getAddressInfo().getCodeString().replaceAll("-", "") + oderInfoDataBean.getAddressInfo().getAddress());
        tv_address_name.setText(oderInfoDataBean.getAddressInfo().getConsignee());
        tv_address_phone.setText(oderInfoDataBean.getAddressInfo().getMobile());
        tv_reward_price.setTagText("+ ¥" + StringUtils.getAllPrice(oderInfoDataBean.getRewardPointE2()));
        if (oderInfoDataBean.getTotalPointAmtE2() == 0) {
            tv_red_price_tip.setVisibility(View.GONE);
            tv_red_price.setVisibility(View.GONE);
        } else {
            tv_red_price_tip.setVisibility(View.VISIBLE);
            tv_red_price.setVisibility(View.VISIBLE);
            tv_red_price.setTagText("- ¥" + StringUtils.getAllPrice(oderInfoDataBean.getTotalPointAmtE2()));
        }

        if (oderInfoDataBean.getOrderState() == 1) {
            if (oderInfoDataBean.getPayState() == 1) {  //未支付
                rl_paytype.setVisibility(View.GONE);
                ll_nopay.setVisibility(View.VISIBLE);
                ll_undelivered.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.VISIBLE);
                tv_top_gopay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (oderInfoDataBean != null && oderInfoDataBean.getGoodsFlag() == 3) {  //一元购
                            PreferencesUtils.put(Constants.Key.KEY_isOneYuanPurchase, 1); //保存是否是一元购商品生成订单在WXPayEntryActivity界面判断待支付状态显示5分钟和30分钟内可以支付（1：是 0：否）
                        }
                        Intent intent1 = new Intent(OrderInfoActivity.this, ProductPaymentActivity.class);
                        intent1.putExtra(Constants.Key.KEY_1, orderId);
                        intent1.putExtra(Constants.Key.KEY_2, oderInfoDataBean.getTotalMoneyAmtE2());
                        startActivity(intent1);
                    }
                });
                iv_ordertype_mark.setVisibility(View.GONE);
                tv_ordertype_name.setText(R.string.orderinfo_status_waitpay);
                if (oderInfoDataBean.getOrderPayEndTime() != 0) {
                    long current = System.currentTimeMillis() / 1000;
                    long time = (oderInfoDataBean.getOrderPayEndTime() - current) * 1000;
                    if (time > 0) {
                        //判断倒计时是否结束
                        countDownTimer = new CountDownTimer(time, 60000) {
                            public void onTick(long millisUntilFinished) {
                                tv_ordertype_desc.setText("剩余时间：" + DateUtils.getMinute(millisUntilFinished));
                            }

                            public void onFinish() {
                                if (mPresenter != null && !StringUtils.isTextEmpty(orderId)) {
                                    mPresenter.GetOrderInfo(orderId, OrderInfoActivity.this);
                                } else {
                                    finish();
                                }
                            }
                        }.start();
                    } else {
                        ll_close.setVisibility(View.VISIBLE);
                        ll_nopay.setVisibility(View.GONE);
                        tv_top_gopay.setVisibility(View.GONE);
                        iv_ordertype_mark.setVisibility(View.VISIBLE);
                        iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_close);
                        tv_ordertype_name.setText(R.string.orderinfo_status_tradeclose);
                        tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                    }
                } else {
                    ll_close.setVisibility(View.VISIBLE);
                    ll_nopay.setVisibility(View.GONE);
                    tv_top_gopay.setVisibility(View.GONE);
                    iv_ordertype_mark.setVisibility(View.VISIBLE);
                    iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_close);
                    tv_ordertype_name.setText(R.string.orderinfo_status_tradeclose);
                    tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                }
                tv_actual_price.setTagText(StringUtils.getPrice(oderInfoDataBean.getTotalMoneyAmtE2()));
                tv_actual_price.setStartText("¥");
                tv_actual_price.setStartProportion(0.75f);
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(oderInfoDataBean.getTotalMoneyAmtE2()));
                tv_actual_price.setEndProportion(0.75f);
                if (oderInfoDataBean.getIsNeedInvoice() == 2) {  //没有发票
                    tv_invoice_open.setVisibility(View.VISIBLE);
                } else {
                    tv_invoice_open.setVisibility(View.GONE);
                }
            } else {
                if (oderInfoDataBean.getTotalMoneyAmtE2() == 0) {
                    rl_paytype.setVisibility(View.GONE);
                } else {
                    rl_paytype.setVisibility(View.VISIBLE);
                    tv_pay_type.setText(oderInfoDataBean.getPayClientTypeId() == 1 ? "微信" : "支付宝");
                    tv_pay_time.setText(DateUtils.getStrTime1(oderInfoDataBean.getPayEndTime()));
                }
                ll_nopay.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                ll_undelivered.setVisibility(View.VISIBLE);
                //暂时无包裹取消功能
                tv_undelivered_cancel.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_undelivered);
                tv_ordertype_name.setText(R.string.order_undelivered);
                tv_ordertype_desc.setText(R.string.orderinfo_status_undelivered_desc);
                tv_actual_price.setTagText(StringUtils.getPrice(oderInfoDataBean.getTotalMoneyAmtE2()));
                tv_actual_price.setStartText("¥");
                tv_actual_price.setStartProportion(0.75f);
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(oderInfoDataBean.getTotalMoneyAmtE2()));
                tv_actual_price.setEndProportion(0.75f);
            }
        } else if (oderInfoDataBean.getOrderState() == 2 || oderInfoDataBean.getOrderState() == 3 ||
                oderInfoDataBean.getOrderState() == 7 || oderInfoDataBean.getOrderState() == 8) {
            rl_paytype.setVisibility(View.GONE);
            ll_undelivered.setVisibility(View.GONE);
            ll_close.setVisibility(View.VISIBLE);
            ll_nopay.setVisibility(View.GONE);
            tv_top_gopay.setVisibility(View.GONE);
            iv_ordertype_mark.setVisibility(View.VISIBLE);
            iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_close);
            tv_ordertype_name.setText(R.string.orderinfo_status_tradeclose);
            tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
            tv_actual_price.setTagText(StringUtils.getPrice(oderInfoDataBean.getTotalMoneyAmtE2()));
            tv_actual_price.setStartText("¥");
            tv_actual_price.setStartProportion(0.75f);
            tv_actual_price.setEndText(StringUtils.getPriceDecimal(oderInfoDataBean.getTotalMoneyAmtE2()));
            tv_actual_price.setEndProportion(0.75f);
        }
        tv_totalprice.setTagText("¥" + StringUtils.getAllPrice(oderInfoDataBean.getTotalAmtE2()));
        if (oderInfoDataBean.getIsNeedInvoice() == 1 && oderInfoDataBean.getOrderInvoiceInfo() != null) {  //开发票
            tv_invoice_title_tip.setVisibility(View.VISIBLE);
            tv_invoice_title.setVisibility(View.VISIBLE);
            tv_invoice_content_tip.setVisibility(View.VISIBLE);
            tv_invoice_content.setVisibility(View.VISIBLE);
            tv_invoice_type.setText(oderInfoDataBean.getOrderInvoiceInfo().getInvoiceTypeStr());
            if (oderInfoDataBean.getOrderInvoiceInfo().getInvoiceLookedUp() == 2) { //企业
                tv_invoice_company_id.setVisibility(View.VISIBLE);
                tv_invioce_company_id_tip.setVisibility(View.VISIBLE);
                tv_invoice_title.setText(oderInfoDataBean.getOrderInvoiceInfo().getCompanyName());
                tv_invoice_company_id.setText(oderInfoDataBean.getOrderInvoiceInfo().getCompanyIdentifyNumber());
            } else {
                tv_invoice_company_id.setVisibility(View.GONE);
                tv_invioce_company_id_tip.setVisibility(View.GONE);
                tv_invoice_title.setText("个人");
            }
            if (oderInfoDataBean.getConfirmState() == 1) {  //已经开具发票
                tv_invoice_look.setVisibility(View.VISIBLE);
            } else {
                tv_invoice_look.setVisibility(View.GONE);
            }
        } else {
            tv_invoice_title_tip.setVisibility(View.GONE);
            tv_invoice_title.setVisibility(View.GONE);
            tv_invoice_content_tip.setVisibility(View.GONE);
            tv_invoice_content.setVisibility(View.GONE);
            tv_invoice_company_id.setVisibility(View.GONE);
            tv_invioce_company_id_tip.setVisibility(View.GONE);
            tv_invoice_look.setVisibility(View.GONE);
            tv_invoice_type.setText("不开发票");
        }
        rcv_product_pack.setAdapter(orderInfoPackListAdapter);
    }

    @Override
    public void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason) {
        DialogUtil.showCancelOrder(OrderInfoActivity.this, data_reason, clickCommitAction);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                OrderInfoActivity.this.finish();
                break;
            case R.id.btn_contact:
                DialogUtil.showCallPhone(OrderInfoActivity.this, "", orderId);
                break;
            case R.id.tv_nopay_gopay:  //去支付
                if (oderInfoDataBean != null && oderInfoDataBean.getGoodsFlag() == 3) {  //一元购
                    PreferencesUtils.put(Constants.Key.KEY_isOneYuanPurchase, 1); //保存是否是一元购商品生成订单在WXPayEntryActivity界面判断待支付状态显示5分钟和30分钟内可以支付（1：是 0：否）
                }
                Intent intent1 = new Intent(OrderInfoActivity.this, ProductPaymentActivity.class);
                intent1.putExtra(Constants.Key.KEY_1, orderId);
                intent1.putExtra(Constants.Key.KEY_2, oderInfoDataBean.getTotalMoneyAmtE2());
                startActivity(intent1);
                break;
            case R.id.tv_nopay_cancel:  //取消订单
                mPresenter.GetCancelReasonInfo(OrderInfoActivity.this);
                break;
            case R.id.tv_undelivered_cancel: //暂时无包裹取消功能

                break;
            case R.id.tv_undelivered_againpay:  //再次购买
            case R.id.tv_close_againpay:
                String productid = "";
                for (OderInfoDataBean.PackListDataBean.PackBean temp : data_packlist
                ) {
                    List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data_product = temp.getOrderDetail().getDataList();
                    for (OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean product : data_product
                    ) {
                        productid += product.getGoodsId() + "-";
                    }
                }
                if (StringUtils.isTextEmpty(productid)) {
                    return;
                }
                productid = productid.substring(0, productid.lastIndexOf("-"));
                mPresenter.BatchAddCar(productid, OrderInfoActivity.this);
                break;
            case R.id.tv_close_delete://删除订单
                mPresenter.DeleteOrder(orderId, OrderInfoActivity.this);
                break;
            case R.id.iv_reward_price_introduce:  //购物送红包说明
                DialogUtil.showRedPacket(OrderInfoActivity.this);
                break;
            case R.id.tv_invoice_look:  //查看发票

                break;
            case R.id.tv_invoice_open:  //补开发票
                showSelectInvoiceDialog();
                break;
        }
    }

    /**
     * 取消订单提交
     */
    DialogUtil.ClickCommitAction clickCommitAction = new DialogUtil.ClickCommitAction() {
        @Override
        public void commitAction(String reason) {
            mPresenter.CancelOrder(orderId, reason, OrderInfoActivity.this);
        }

        @Override
        public void commitAction(String select_reason, int postion) {

        }
    };

    /**
     * 选择填写发票类型
     */
    Dialog dialog_invoice;
    String invoiceLookedUp;
    String companyName, companyIdentifyNumber, mobile, eamil;

    public void showSelectInvoiceDialog() {
        if (dialog_invoice == null) {
            View view = getLayoutInflater().inflate(R.layout.dialog_select_edit_invoice,
                    null);
            dialog_invoice = new Dialog(this, R.style.transparentFrameWindowStyle);
            dialog_invoice.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ScreenUtils.dip2px(this, 548)));
            Window window = dialog_invoice.getWindow();
            /**
             * 位于底部
             */
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            /**
             * 设置弹出动画
             */
            window.setWindowAnimations(R.style.ani_bottom);
            // 设置点击外围解散
            dialog_invoice.setCanceledOnTouchOutside(true);
            ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
            iv_dialog_close.setOnClickListener(view1 -> dialog_invoice.dismiss());
            RelativeLayout rl_normal = view.findViewById(R.id.rl_normal);
            RadioGroup rg_invoice_type = view.findViewById(R.id.rg_invoice_type);
            rg_invoice_type.setOnCheckedChangeListener((radioGroup, checkedId) -> {
                switch (checkedId) {
                    case R.id.rb_type_no:
                        rl_normal.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_type_normal:
                        rl_normal.setVisibility(View.VISIBLE);
                        break;
                }
            });
            RelativeLayout rl_company_info = view.findViewById(R.id.rl_company_info);
            RadioGroup rg_invoice_title = view.findViewById(R.id.rg_invoice_title);
            RadioButton rb_type_normal = view.findViewById(R.id.rb_type_normal);
            rb_type_normal.setChecked(true);
            rg_invoice_title.setOnCheckedChangeListener((radioGroup, checkedId) -> {
                switch (checkedId) {
                    case R.id.rb_title_personal:
                        rl_company_info.setVisibility(View.GONE);
                        break;
                    case R.id.rb_title_company:
                        rl_company_info.setVisibility(View.VISIBLE);
                        break;
                }
            });
            EditText ed_company_name = view.findViewById(R.id.ed_company_name);
            EditText ed_company_id = view.findViewById(R.id.ed_company_id);
            EditText ed_taker_phone = view.findViewById(R.id.ed_taker_phone);
            EditText ed_taker_email = view.findViewById(R.id.ed_taker_email);
            UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
            if (userBean != null) {
                ed_taker_phone.setText(userBean.getMobile());
                ed_taker_phone.setSelection(ed_taker_phone.getText().length());
            }
            TextView tv_invoice_tip = view.findViewById(R.id.tv_invoice_tip);
            tv_invoice_tip.setOnClickListener(view13 -> DialogUtil.showInvoiceIntroduce(OrderInfoActivity.this));

            TextView tv_sure = view.findViewById(R.id.tv_sure);
            tv_sure.setOnClickListener(view12 -> {
                if (rg_invoice_type.getCheckedRadioButtonId() == R.id.rb_type_normal) {
                    if (rg_invoice_title.getCheckedRadioButtonId() == R.id.rb_title_personal) {
                        invoiceLookedUp = "1";
                        if (StringUtils.isTextEmpty(ed_taker_phone.getText().toString())) {
                            SCApp.getInstance().showSystemCenterToast("请填写手机号");
                            return;
                        }
                        if (ed_taker_phone.getText().toString().length() < 11) {
                            SCApp.getInstance().showSystemCenterToast("请填写11位的手机号");
                            return;
                        }
                        if (!StringUtils.isTextEmpty(ed_taker_email.getText().toString()) && !StringUtils.isEmail(ed_taker_email.getText().toString())) {
                            SCApp.getInstance().showSystemCenterToast("请填写正确的邮箱");
                            return;
                        }
                        mobile = ed_taker_phone.getText().toString();
                        eamil = ed_taker_email.getText().toString();
                    } else {
                        invoiceLookedUp = "2";
                        if (StringUtils.isTextEmpty(ed_company_name.getText().toString())) {
                            SCApp.getInstance().showSystemCenterToast("请填写单位名称");
                            return;
                        }
                        if (StringUtils.isTextEmpty(ed_company_id.getText().toString())) {
                            SCApp.getInstance().showSystemCenterToast("请填写纳税人识别号");
                            return;
                        }
                        if (StringUtils.isTextEmpty(ed_taker_phone.getText().toString())) {
                            SCApp.getInstance().showSystemCenterToast("请填写手机号");
                            return;
                        }
                        if (ed_taker_phone.getText().toString().length() < 11) {
                            SCApp.getInstance().showSystemCenterToast("请填写11位的手机号");
                            return;
                        }
                        mobile = ed_taker_phone.getText().toString();
                        eamil = ed_taker_email.getText().toString();
                        companyName = ed_company_name.getText().toString();
                        companyIdentifyNumber = ed_company_id.getText().toString();
                        //tv_invoice_info.setText("普票(商品明细-" + ed_company_name.getText().toString() + ")");
                    }
                    mPresenter.InvoiceOpen(orderId, invoiceLookedUp, companyName, companyIdentifyNumber, mobile, eamil, OrderInfoActivity.this);
                } else {
                    dialog_invoice.dismiss();
                }
            });

        }

        dialog_invoice.show();
    }
}
