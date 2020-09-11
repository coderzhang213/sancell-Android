package cn.sancell.xingqiu.homeuser;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeuser.adapter.OrderPackInfoProductListAdapter;
import cn.sancell.xingqiu.homeuser.afterSale.ApplyModifyAddressActivity;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.bean.PackInfoDataBean;
import cn.sancell.xingqiu.homeuser.contract.OrderPackInfoContract;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.usermember.MemberVipGiftInfoActivity;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.FontUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * 包裹详情页（支付成功后分包裹）
 */
public class OrderNewPackInfoActivity extends BaseMVPActivity<OrderPackInfoContract.OrderPackInfoPresenter>
        implements OrderPackInfoContract.OrderPackInfoView, View.OnClickListener {
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
    private OrderPackInfoProductListAdapter orderPackInfoProductListAdapter;
    private List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data_productlist;

    @BindView(R.id.ll_undelivered)
    LinearLayout ll_undelivered;
    @BindView(R.id.tv_undelivered_cancel)
    TextView tv_undelivered_cancel;
    @BindView(R.id.tv_undelivered_againpay)
    TextView tv_undelivered_againpay;

    @BindView(R.id.ll_delivered)
    LinearLayout ll_delivered;
    @BindView(R.id.tv_delivered_againpay)
    TextView tv_delivered_againpay;
    @BindView(R.id.tv_delivered_logistics)
    TextView tv_delivered_logistics;
    @BindView(R.id.tv_delivered_sure)
    TextView tv_delivered_sure;

    @BindView(R.id.ll_finish)
    LinearLayout ll_finish;
    @BindView(R.id.tv_finish_delete)
    TextView tv_finish_delete;
    @BindView(R.id.tv_finish_againpay)
    TextView tv_finish_againpay;
    @BindView(R.id.tv_finish_evaluate)
    TextView tv_finish_evaluate;


    @BindView(R.id.ll_processing)
    LinearLayout ll_processing;
    @BindView(R.id.tv_processing_progress)
    TextView tv_processing_progress;
    @BindView(R.id.tv_processing_againpay)
    TextView tv_processing_againpay;

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
    private RelativeLayout rl_logisticsinfo;
    private ImageView iv_logisticsinfo_mark;
    private TextView tv_logistics_time;
    private TextView tv_logistics_desc;
    private TextView tv_address_name;
    private TextView tv_address_phone;
    private TextView tv_address;
    private AppCompatTextView tv_modify_adr;

    /**
     * 底部布局
     */
    private TextView tv_order_num;
    private TextView tv_ordernum_copy;
    private TextView tv_order_time;
    private RelativeLayout rl_paytype;
    private TextView tv_pay_type;
    private TextView tv_pay_time;
    private AppCompatTextView tv_totalprice;
    private TextView tv_red_price_tip;
    private AppCompatTextView tv_red_price;
    private ImageView iv_reward_price_introduce;
    AppCompatTextView tv_new_pay_price;
    private LinearLayout ll_phone;
    private RelativeLayout rl_hospital;
    private AppCompatTextView tv_hospital_name;
    private AppCompatTextView tv_hospital_adr;
    private AppCompatTextView tv_look_code;
    private AppCompatTextView tv_use_state;

    private static int CALLBACK_ADDRESS_MODIFY = 100;

    private String parcelId, orderId;

    private StartupDataBean.HospitalInfo hospitalData;

    public static void start(Context context, String parcelId, String orderId) {
        Intent intent = new Intent(context, OrderNewPackInfoActivity.class);
        intent.putExtra(Constants.Key.KEY_1, parcelId);
        intent.putExtra(Constants.Key.KEY_2, orderId);
        context.startActivity(intent);

    }

    @Override
    protected OrderPackInfoContract.OrderPackInfoPresenter createPresenter() {
        return new OrderPackInfoContract.OrderPackInfoPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_pack_info;
    }

    int scrollviewH;

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(OrderNewPackInfoActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
        }
        parcelId = getIntent().getStringExtra(Constants.Key.KEY_1);
        orderId = getIntent().getStringExtra(Constants.Key.KEY_2);
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
        tv_undelivered_cancel.setOnClickListener(this);
        tv_undelivered_againpay.setOnClickListener(this);
        tv_delivered_againpay.setOnClickListener(this);
        tv_delivered_logistics.setOnClickListener(this);
        tv_delivered_sure.setOnClickListener(this);
        tv_processing_againpay.setOnClickListener(this);
        tv_processing_progress.setOnClickListener(this);
        tv_finish_againpay.setOnClickListener(this);
        tv_finish_evaluate.setOnClickListener(this);
        tv_finish_delete.setOnClickListener(this);
        mPresenter.GetPackInfo(parcelId, this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_order_packinfo_top, (ViewGroup) rcv_product_pack.getParent(), false);
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
        tv_top_gopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        iv_ordertype_mark = view.findViewById(R.id.iv_ordertype_mark);
        rl_logisticsinfo = view.findViewById(R.id.rl_logisticsinfo);
        iv_logisticsinfo_mark = view.findViewById(R.id.iv_logisticsinfo_mark);
        tv_logistics_time = view.findViewById(R.id.tv_logistics_time);
        tv_logistics_desc = view.findViewById(R.id.tv_logistics_desc);
        tv_address_name = view.findViewById(R.id.tv_address_name);
        tv_address_phone = view.findViewById(R.id.tv_address_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tv_modify_adr = view.findViewById(R.id.tv_modify_adr);
        tv_modify_adr.setOnClickListener(this);

        return view;
    }


    private View getNewFooterView() {
        View view = getLayoutInflater().inflate(R.layout.layout_orderinfo_foot_new, (ViewGroup) rcv_product_pack.getParent(), false);
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
        //
        tv_totalprice = view.findViewById(R.id.tv_totalprice);
        tv_red_price_tip = view.findViewById(R.id.tv_red_price_tip);
        tv_red_price = view.findViewById(R.id.tv_red_price);

        tv_new_pay_price = view.findViewById(R.id.tv_new_pay_price);
        ll_phone = view.findViewById(R.id.ll_phone);
        rl_hospital = view.findViewById(R.id.rl_hospital);

        tv_hospital_name = view.findViewById(R.id.tv_hospital_name);
        tv_hospital_adr = view.findViewById(R.id.tv_hospital_adr);

        tv_use_state = view.findViewById(R.id.tv_use_state);
        tv_look_code = view.findViewById(R.id.tv_look_code);
        tv_look_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlInfoActivity.start(OrderNewPackInfoActivity.this,
                        packInfoDataBean.getParcelInfo().QRCodeH5Link != null ? packInfoDataBean.getParcelInfo().QRCodeH5Link : "", "核销码");
            }
        });
        ll_phone.setOnClickListener(v -> callPhone(hospitalData != null ? hospitalData.phone : ""));
        rl_hospital.setOnClickListener(v -> {
            UrlInfoActivity.start(this, hospitalData != null ? hospitalData.link : "", "机构介绍");
        });

        return view;
    }

    private void getHospitalInfo() {
        hospitalData = PreferencesUtils.readObject(Constants.Key.HOSPITAL_INFO, StartupDataBean.HospitalInfo.class);
        if (hospitalData != null) {
            tv_hospital_adr.setText(hospitalData.address);
            tv_hospital_name.setText(hospitalData.title);
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                OrderNewPackInfoActivity.this.finish();
                break;
            case R.id.btn_contact:
                DialogUtil.showCallPhone(OrderNewPackInfoActivity.this, "", orderId);
                break;
            case R.id.tv_undelivered_cancel://取消包裹(暂无此功能)
                mPresenter.GetCancelReasonInfo(OrderNewPackInfoActivity.this);
                break;
            case R.id.tv_undelivered_againpay:  //再次购买
            case R.id.tv_delivered_againpay:
            case R.id.tv_close_againpay:
            case R.id.tv_finish_againpay:
            case R.id.tv_processing_againpay:
//                String productid = "";
//                for (OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean product : data_productlist
//                ) {
//                    productid += product.getGoodsId() + "-";
//                }
//                if (StringUtils.isTextEmpty(productid)) {
//                    return;
//                }
//                productid = productid.substring(0, productid.lastIndexOf("-"));
//                mPresenter.BatchAddCar(productid, OrderNewPackInfoActivity.this);
                GoodsDetailActivity.start(this, data_productlist.get(0).getGoodsId());

                break;
            case R.id.tv_processing_progress:  //查看订单处理进度(暂无此功能)
                break;
            case R.id.tv_delivered_logistics:  //查看物流
                Intent intent = new Intent(OrderNewPackInfoActivity.this, LogisticsListActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                intent.putExtra(Constants.Key.KEY_2, parcelId);
                startActivity(intent);
                break;
            case R.id.tv_delivered_sure:  //确认收货
                DialogUtil.showOperateDialog(OrderNewPackInfoActivity.this, getResources().getString(R.string.dialog_conform_receipt_title),
                        getResources().getString(R.string.dialog_conform_receipt_desc),
                        getResources().getString(R.string.dialog_conform_receipt_no),
                        getResources().getString(R.string.dialog_conform_receipt_yes), clickSureAction);
                break;
            case R.id.tv_close_delete://删除订单
            case R.id.tv_finish_delete:
                mPresenter.DeleteOrder(orderId, OrderNewPackInfoActivity.this);
                break;
            case R.id.tv_finish_evaluate:  //评价晒单
                Intent intent4 = new Intent(OrderNewPackInfoActivity.this, EvaluatedProductListActivity.class);
                intent4.putExtra(Constants.Key.KEY_1, orderId);
                intent4.putExtra(Constants.Key.KEY_2, packInfoDataBean.getParcelInfo().getWarehouseId() + "");
                startActivity(intent4);
                break;
            case R.id.iv_reward_price_introduce:
                DialogUtil.showRedPacket(OrderNewPackInfoActivity.this);
                break;
            case R.id.tv_invoice_look:  //查看发票
                if (packInfoDataBean.getOrderInvoiceInfo() != null && !StringUtils.isTextEmpty(packInfoDataBean.getOrderInvoiceInfo().getInvoiceUrl())) {
                    UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
                    if (userBean != null) {
                        String userId = userBean.getUserId() + "";
                        String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
                        String token = "userId=" + userId + "&skey=" + skey;
                        try {
                            String url = packInfoDataBean.getOrderInvoiceInfo().getInvoiceUrl() + "&token=" + URLEncoder.encode(RSAUtils.encryptByPublic_other(token), "UTF-8");
                            if (!StringUtils.isTextEmpty(url)) {
                                Intent intent5 = new Intent(OrderNewPackInfoActivity.this, UrlInfoActivity.class);
                                intent5.putExtra(Constants.Key.KEY_1, url);
                                intent5.putExtra(Constants.Key.KEY_2, "发票详情");
                                startActivity(intent5);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
            case R.id.tv_invoice_open:  //补开发票

                break;
            case R.id.tv_modify_adr:
                //修改待发货状态的订单地址
                if (packInfoDataBean.getAddressInfo().refundStatus.equals("1")) {
                    ApplyModifyAddressActivity.start(this, parcelId, packInfoDataBean.getAddressInfo(), CALLBACK_ADDRESS_MODIFY);
                } else {
                    //h5
                    Intent urlIntent = new Intent(this, UrlInfoActivity.class);
                    urlIntent.putExtra(Constants.Key.KEY_2, getResources().getString(R.string.apply_progress));
                    urlIntent.putExtra(Constants.Key.KEY_1, packInfoDataBean.getAddressInfo().url + "?parcelId=" + parcelId);
                    startActivity(urlIntent);
                }

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
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.GetPackInfo(parcelId, OrderNewPackInfoActivity.this);
            }
        });
    }

    @Override
    public void batchAddSuccess() {

    }

    @Override
    public void deleteOrderSuccess() {
        finish();
    }

    @Override
    public void confirmReceiptSuccess() {
        mPresenter.GetPackInfo(parcelId, OrderNewPackInfoActivity.this);
    }

    PackInfoDataBean packInfoDataBean;

    @Override
    public void getPackInfoSuccess(PackInfoDataBean packInfoDataBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        this.packInfoDataBean = packInfoDataBean;
        data_productlist = packInfoDataBean.getParcelInfo().getOrderDetail().getDataList();
        orderPackInfoProductListAdapter = new OrderPackInfoProductListAdapter(data_productlist);

        orderPackInfoProductListAdapter.addHeaderView(getHeaderView());
        orderPackInfoProductListAdapter.addFooterView(getNewFooterView());
        orderPackInfoProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.rl_item:
                    if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) {  //会员礼包
                        Intent intent = new Intent(OrderNewPackInfoActivity.this, MemberVipGiftInfoActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, data_productlist.get(position).getGoodsId() + "");
                        startActivity(intent);
                    } else {  //普通商品
//                        Intent intent = new Intent(OrderPackInfoActivity.this, ProductInfoActivity.class);
//                        intent.putExtra(Constants.Key.KEY_1, data_productlist.get(position).getGoodsId() + "");
//                        startActivity(intent);
                        GoodsDetailActivity.start(OrderNewPackInfoActivity.this, data_productlist.get(position).getGoodsId());

                    }
                    break;
            }
        });

        if (packInfoDataBean.getParcelInfo().writeOffStatus == 1) {
            //未使用
            tv_use_state.setSelected(true);
            tv_look_code.setSelected(true);
        } else {
            tv_use_state.setSelected(false);
            tv_look_code.setSelected(false);
        }


        tv_order_num.setText(packInfoDataBean.getOrderInfo().getOrderNumber());
        tv_order_time.setText(DateUtils.getStrTime1(packInfoDataBean.getOrderInfo().getOrderTime() + ""));
        //1 微信，2支付宝 4红包,6代金券
        if (packInfoDataBean.getOrderInfo().getPayClientTypeId() == 1) {
            tv_pay_type.setText("微信");
        } else if (packInfoDataBean.getOrderInfo().getPayClientTypeId() == 2) {
            tv_pay_type.setText("支付宝");
        } else if (packInfoDataBean.getOrderInfo().getPayClientTypeId() == 4) {
            tv_pay_type.setText("红包");
        } else {
            tv_pay_type.setText("代金券");
        }
        tv_pay_time.setText(DateUtils.getStrTime1(packInfoDataBean.getOrderInfo().getPayEndTime()));

//        if (packInfoDataBean.getParcelInfo().getTotalMoneyAmtE2() == 0) {  //实际支付价格为0
//            rl_paytype.setVisibility(View.GONE);
//        } else {
//            rl_paytype.setVisibility(View.VISIBLE);
//            tv_pay_type.setText(packInfoDataBean.getOrderInfo().getPayClientTypeId() == 1 ? "微信" : "支付宝");
//            tv_pay_time.setText(DateUtils.getStrTime1(packInfoDataBean.getOrderInfo().getPayEndTime()));
//        }
        tv_totalprice.setText("¥" + StringUtils.getAllPrice(packInfoDataBean.getParcelInfo().getTotalAmtE2()));

        tv_new_pay_price.setText(FontUtils.getInstance().changeTextSize(this, 12, "¥" + StringUtils.getAllPrice(packInfoDataBean.getParcelInfo().getTotalMoneyAmtE2()), "¥"));

        tv_address.setText(packInfoDataBean.getAddressInfo().getCodeString().replaceAll("-", "") + packInfoDataBean.getAddressInfo().getAddress());
        tv_address_name.setText(packInfoDataBean.getAddressInfo().getConsignee());
        tv_address_phone.setText(packInfoDataBean.getAddressInfo().getMobile());

//        if (packInfoDataBean.getExpressInfo() != null && packInfoDataBean.getExpressInfo().getData() != null) {
//            rl_logisticsinfo.setVisibility(View.VISIBLE);
//            rl_logisticsinfo.setOnClickListener(view -> {  //物流信息
//                Intent intent = new Intent(OrderNewPackInfoActivity.this, LogisticsListActivity.class);
//                intent.putExtra(Constants.Key.KEY_1, orderId);
//                intent.putExtra(Constants.Key.KEY_2, parcelId);
//                startActivity(intent);
//            });
//            tv_logistics_desc.setText(packInfoDataBean.getExpressInfo().getData().getContext());
//            tv_logistics_time.setText(packInfoDataBean.getExpressInfo().getData().getTime());
//            iv_logisticsinfo_mark.setImageResource(R.mipmap.icon_order_package);
//        } else {
//            rl_logisticsinfo.setVisibility(View.GONE);
//        }

        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);

        if (packInfoDataBean.getParcelInfo().getTotalPointAmtE2() > 0 || packInfoDataBean.getParcelInfo().deductPriceE2 > 0) {
            if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) { //会员礼包
                if (userBean != null && userBean.getIsOldUser() == 1) {  //老用户
                    tv_red_price_tip.setVisibility(View.VISIBLE);
                    tv_red_price.setVisibility(View.VISIBLE);
                    tv_red_price_tip.setText("平移金额");
                } else {
                    tv_red_price_tip.setVisibility(View.GONE);
                    tv_red_price.setVisibility(View.GONE);
                }

            } else {//普通商品
                if (packInfoDataBean.getParcelInfo().deductPriceE2 > 0) {
                    tv_red_price.setText("- ¥" + StringUtils.getAllPrice(packInfoDataBean.getParcelInfo().deductPriceE2));
                    tv_red_price_tip.setText("代金券抵扣");
                } else {

                    tv_red_price_tip.setText("红包抵扣");
                    tv_red_price.setText("- ¥" + StringUtils.getAllPrice(packInfoDataBean.getParcelInfo().getTotalPointAmtE2()));
                }
                tv_red_price_tip.setVisibility(View.VISIBLE);
                tv_red_price.setVisibility(View.VISIBLE);
            }
        } else {
            tv_red_price_tip.setVisibility(View.GONE);
            tv_red_price.setVisibility(View.GONE);
        }
        if (data_productlist != null) {
            data_productlist.get(0).mRemark = packInfoDataBean.getParcelInfo().getRemark();
        }
        rcv_product_pack.setAdapter(orderPackInfoProductListAdapter);

        getHospitalInfo();

        switch (StringUtils.getShowStatusInOrder(packInfoDataBean.getParcelInfo().getOrderState(), packInfoDataBean.getParcelInfo().getPayState(), packInfoDataBean.getParcelInfo().getIsEvaluation(), packInfoDataBean.getParcelInfo().getDeliveryState())) {
            case Constants.OrderShowStatus.KEY_undelivered:  //待发货
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) { //礼包商品
                    ll_undelivered.setVisibility(View.GONE);
                } else {  //普遍商品
                    ll_undelivered.setVisibility(View.VISIBLE);
                }
                //暂时无包裹取消功能
                tv_undelivered_cancel.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.GONE);
                ll_finish.setVisibility(View.GONE);
                ll_processing.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_undelivered);

                tv_ordertype_name.setText("待使用");
                tv_ordertype_desc.setText("医美商品需预约，欢迎您尽快消费使用");
                break;
            case Constants.OrderShowStatus.KEY_delivered:  //已发货
                tv_modify_adr.setVisibility(View.GONE);
                ll_undelivered.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.VISIBLE);
                ll_finish.setVisibility(View.GONE);
                ll_processing.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_delivered);
                tv_ordertype_name.setText(R.string.order_delivered);
                tv_ordertype_desc.setText(R.string.orderinfo_status_delivered_desc);
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) { //礼包商品
                    tv_delivered_againpay.setVisibility(View.GONE);
                } else {  //普遍商品
                    tv_delivered_againpay.setVisibility(View.VISIBLE);
                }
                break;
            case Constants.OrderShowStatus.KEY_finished:  //已完成但未全部评价完
                tv_modify_adr.setVisibility(View.GONE);
                ll_undelivered.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.GONE);
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) {  //礼包商品
                    ll_finish.setVisibility(View.GONE);
                } else { //普遍商品
                    ll_finish.setVisibility(View.VISIBLE);
                }
                //暂无包裹删除功能
                tv_finish_delete.setVisibility(View.GONE);
                ll_processing.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_finished);
//                tv_ordertype_name.setText(R.string.order_finished);
//                tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                tv_ordertype_name.setText(R.string.order_finished);
                tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                iv_logisticsinfo_mark.setImageResource(R.mipmap.icon_order_complete);
                tv_finish_evaluate.setBackground(getResources().getDrawable(R.drawable.shape_eva_red_bg));
                tv_finish_evaluate.setTextColor(Color.parseColor("#FA1905"));
                tv_finish_evaluate.setVisibility(View.VISIBLE);
                break;
            case Constants.OrderShowStatus.KEY_evaluated:  //已完成且全部评价完
                ll_undelivered.setVisibility(View.GONE);
                tv_modify_adr.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.GONE);
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) {//礼包商品
                    ll_finish.setVisibility(View.GONE);
                } else {   //普遍商品
                    ll_finish.setVisibility(View.VISIBLE);
                }
                //暂无包裹删除功能
                tv_finish_delete.setVisibility(View.GONE);
                ll_processing.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_finished);
                tv_ordertype_name.setText(R.string.order_finished);
                tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                iv_logisticsinfo_mark.setImageResource(R.mipmap.icon_order_complete);
                tv_finish_evaluate.setVisibility(View.GONE);

                break;
            case Constants.OrderShowStatus.KEY_processing:  //订单处理中
                ll_undelivered.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.GONE);
                ll_finish.setVisibility(View.GONE);
                tv_modify_adr.setVisibility(View.GONE);
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) { //礼包商品
                    ll_processing.setVisibility(View.GONE);
                } else {  //普遍商品
                    ll_processing.setVisibility(View.VISIBLE);
                }
                //暂时无包裹取消功能即无查看进度此功能
                tv_processing_progress.setVisibility(View.GONE);
                ll_close.setVisibility(View.GONE);
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_undelivered);
                tv_ordertype_name.setText(R.string.order_undelivered);
                tv_ordertype_desc.setText(R.string.orderinfo_status_undelivered_desc);
                break;
            case Constants.OrderShowStatus.KEY_closed:  //订单已关闭
                ll_undelivered.setVisibility(View.GONE);
                ll_delivered.setVisibility(View.GONE);
                ll_finish.setVisibility(View.GONE);
                tv_modify_adr.setVisibility(View.GONE);
                ll_processing.setVisibility(View.GONE);
                if (packInfoDataBean.getParcelInfo().getGoodsFlag() == 2) { //礼包商品
                    ll_close.setVisibility(View.GONE);
                } else {  //普遍商品
                    ll_close.setVisibility(View.VISIBLE);
                }
                tv_top_gopay.setVisibility(View.GONE);
                iv_ordertype_mark.setVisibility(View.VISIBLE);
                iv_ordertype_mark.setImageResource(R.mipmap.icon_orderinfo_close);
                tv_ordertype_name.setText(R.string.orderinfo_status_tradeclose);
                tv_ordertype_desc.setText(R.string.orderinfo_status_tradeclose_desc);
                break;
        }


    }

    @Override
    public void getOrderReasonSuccess(List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason) {
        DialogUtil.showCancelOrder(OrderNewPackInfoActivity.this, data_reason, clickCommitAction);
    }

    /**
     * 确认收货
     */
    DialogUtil.ClickSureAction clickSureAction = new DialogUtil.ClickSureAction() {
        @Override
        public void sureAction(int postion) {
            mPresenter.ConfirmReceipt(orderId, parcelId, OrderNewPackInfoActivity.this);
        }
    };
    /**
     * 取消订单提交
     */
    DialogUtil.ClickCommitAction clickCommitAction = new DialogUtil.ClickCommitAction() {
        @Override
        public void commitAction(String reason) {

        }

        @Override
        public void commitAction(String select_reason, int postion) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CALLBACK_ADDRESS_MODIFY) {
                tv_modify_adr.setText(getResources().getString(R.string.address_modify_is_checking));
                packInfoDataBean.getAddressInfo().refundStatus = "2";
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
