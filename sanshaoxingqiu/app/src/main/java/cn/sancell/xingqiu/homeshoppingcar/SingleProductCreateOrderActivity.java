package cn.sancell.xingqiu.homeshoppingcar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.nim.uikit.business.session.constant.RequestCode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.address.select.SelectAddressActivity;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.dialog.OrderTipDialogFgm;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.SingleOrderIntent;
import cn.sancell.xingqiu.homeshoppingcar.bean.req.GoodsAreaReq;
import cn.sancell.xingqiu.homeshoppingcar.contract.ProductCreateOrderContract;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckPhoneActivity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.viewGroup.NoSupportGoodsViewGroup;
import cn.sancell.xingqiu.viewGroup.manager.GoodsAreaManger;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SwitchButton;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;
import cn.sancell.xingqiu.wxapi.WXPayEntryActivity;

/**
 * 商品详情页跳转的单个商品生成订单（秒杀商品和一元购商品）
 */
public class SingleProductCreateOrderActivity extends BaseMVPToobarActivity<ProductCreateOrderContract.CreateOrderPresenter>
        implements ProductCreateOrderContract.CreateOrderView, View.OnClickListener {
    @BindView(R.id.tv_actual_price)
    RelativeSizeTextView tv_actual_price;
    @BindView(R.id.tv_gopay)
    TextView tv_gopay;
    @BindView(R.id.rl_noaddress)
    RelativeLayout rl_noaddress;
    @BindView(R.id.rl_address)
    RelativeLayout rl_address;
    @BindView(R.id.tv_address_city)
    TextView tv_address_city;
    @BindView(R.id.tv_address_detail)
    TextView tv_address_detail;
    @BindView(R.id.tv_address_name)
    TextView tv_address_name;
    @BindView(R.id.tv_address_phone)
    TextView tv_address_phone;
    @BindView(R.id.tv_totalprice)
    TextView tv_totalprice;

    @BindView(R.id.sdv_pic)
    SimpleDraweeView sdv_pic;
    @BindView(R.id.tv_product_name)
    TextView tv_product_name;
    @BindView(R.id.tv_product_specs)
    TextView tv_product_specs;
    @BindView(R.id.tv_product_price)
    RelativeSizeTextView tv_product_price;
    @BindView(R.id.rl_red_packet)
    RelativeLayout rl_red_packet;
    @BindView(R.id.tv_total_red_packet)
    TextView tv_total_red_packet;
    @BindView(R.id.tv_deduct_red_packet)
    TextView tv_deduct_red_packet;
    @BindView(R.id.tv_modify_red_packet)
    TextView tv_modify_red_packet;
    @BindView(R.id.sb_red_packet_credit)
    SwitchButton sb_red_packet_credit;
    @BindView(R.id.tv_red_price_tip)
    TextView tv_red_price_tip;
    @BindView(R.id.tv_red_price)
    TextView tv_red_price;
    @BindView(R.id.rl_invoice)
    RelativeLayout rl_invoice;
    @BindView(R.id.tv_invoice_info)
    TextView tv_invoice_info;

    @BindView(R.id.ed_remarks)
    EditText ed_remarks;

    @BindView(R.id.rl_giftinfo)
    RelativeLayout rl_giftinfo;
    @BindView(R.id.vg_goods)
    NoSupportGoodsViewGroup vg_goods;
    @BindView(R.id.tv_no_goods_pay)
    AppCompatTextView tv_no_goods_pay;

    private int requestCode_addressAdd = 101;

    private String addressId;
    private String mProvinceId;
    private CreateOrderDefaultPreBean mPreBean;

    private long actual_price, deduct_red_packet;
    private UserBean userBean;
    private SingleOrderIntent mIntentData;
    private List<String> mProvinceList;
    private int mTotalPrice;


    public static void start(Context context, SingleOrderIntent data) {
        Intent intent = new Intent(context, SingleProductCreateOrderActivity.class);
        intent.putExtra(IntentKey.SINGLE_ORDER_INFO, data);
        context.startActivity(intent);
    }

    @Override
    protected ProductCreateOrderContract.CreateOrderPresenter createPresenter() {
        return new ProductCreateOrderContract.CreateOrderPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_product_create_order;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.create_order_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        mIntentData = (SingleOrderIntent) getIntent().getSerializableExtra(IntentKey.SINGLE_ORDER_INFO);
        mTotalPrice = mIntentData.total_price;
        tv_gopay.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_noaddress.setOnClickListener(this);

        sdv_pic.setImageURI(Uri.parse(mIntentData.good_pic));
        tv_product_name.setText(mIntentData.good_title);
        tv_product_specs.setText(mIntentData.good_spec);

        sb_red_packet_credit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tv_red_price_tip.setVisibility(View.VISIBLE);
                tv_red_price.setVisibility(View.VISIBLE);
                tv_actual_price.setTagText(StringUtils.getPrice(actual_price));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(actual_price));
                tv_modify_red_packet.setVisibility(View.VISIBLE);
            } else {
                tv_red_price_tip.setVisibility(View.GONE);
                tv_red_price.setVisibility(View.GONE);
                tv_actual_price.setTagText(StringUtils.getPrice(mTotalPrice));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(mTotalPrice));
                tv_modify_red_packet.setVisibility(View.GONE);
            }
        });
        tv_modify_red_packet.setOnClickListener(this);

        rl_invoice.setOnClickListener(this);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);

        ed_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() >= 50) {
                    SCApp.getInstance().showSystemCenterToast(R.string.remark_input_limit);
                }
            }
        });
        mPresenter.GetDefualtOrderPre(this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address://有默认地址
//                Intent intent1 = new Intent(SingleProductCreateOrderActivity.this, UserAddressListActivity.class);
//                intent1.putExtra(Constants.Key.KEY_1, true);
//                intent1.putExtra(Constants.Key.KEY_2, addressId);
//                startActivityForResult(intent1, requestCode_addressSelect);
                SelectAddressActivity.start(this, RequestCode.SELECT_ADDRESS_CALL_BACK);
                break;
            case R.id.rl_noaddress://未设置过地址
                Intent intent = new Intent(SingleProductCreateOrderActivity.this, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                intent.putExtra(Constants.Key.KEY_2, true);
                intent.putExtra(Constants.Key.KEY_4, false);
                startActivityForResult(intent, requestCode_addressAdd);
                break;
            case R.id.tv_gopay://支付
                if (StringUtils.isTextEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("请填写收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "");
                    return;
                }

                //判断是否在配送范围
                if (GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId, mProvinceList)) {
                    //可支付
                    if (sb_red_packet_credit.isChecked()) {
                        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
                        if (userBean != null && userBean.getAddPayPasswordStatus() == 1) {  //设置过支付密码
                            showCheckPayPwdDialog();
                        } else {
                            DialogUtil.showOperateDialog(SingleProductCreateOrderActivity.this,
                                    getResources().getString(R.string.is_setting_pay_pwd),
                                    getResources().getString(R.string.is_setting_pay_pwd_desc),
                                    getResources().getString(R.string.dialog_set_paypwd_no),
                                    getResources().getString(R.string.dialog_set_paypwd_yes), clickSureAction);
                        }
                    } else {
                        if (mIntentData.isOneYuan) {
                            mPresenter.SingleCreateOrder(addressId, mIntentData.good_id, "", "",
                                    isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber
                                    , mobile, eamil, ed_remarks.getText().toString(), "1", SingleProductCreateOrderActivity.this, mIntentData.mActivityId);
                        } else {
                            mPresenter.SingleCreateOrder(addressId, mIntentData.good_id, "", "",
                                    isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber, mobile,
                                    eamil, ed_remarks.getText().toString(), "", SingleProductCreateOrderActivity.this, mIntentData.mActivityId);
                        }
                    }
                } else {
                    //提示不可支付
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("抱歉，当前地址没有可配送商品，请确认收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "all");
                }


                break;
            case R.id.iv_reward_price_introduce:  //购物送红包说明
                DialogUtil.showRedPacket(SingleProductCreateOrderActivity.this);
                break;
            case R.id.tv_modify_red_packet://修改红包对话框
                showModifyRedPacketDialog();
                break;
            case R.id.rl_invoice:  //填写发票
                showSelectInvoiceDialog();
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
                mPresenter.GetDefualtOrderPre(SingleProductCreateOrderActivity.this);
            }
        });
    }

    public void setPrice(boolean inArea) {
        if (inArea) {
            mTotalPrice = mIntentData.total_price;
        } else {
            mTotalPrice = 0;
        }
        tv_totalprice.setText("¥" + StringUtils.getAllPrice(mTotalPrice));
        tv_actual_price.setStartText("¥");
        tv_actual_price.setStartProportion(0.75f);
        tv_actual_price.setEndProportion(0.75f);
        tv_product_price.setTagText(StringUtils.getPrice(mTotalPrice));
        tv_product_price.setStartText("¥");
        tv_product_price.setStartProportion(0.78f);
        tv_product_price.setEndText(StringUtils.getPriceDecimal(mTotalPrice));
        tv_product_price.setEndProportion(0.78f);
    }

    public void getSupportArea() {
        GoodsAreaReq req = new GoodsAreaReq();
        req.goodsId = mIntentData.good_id;
        mPresenter.getGoodsArea(this, req);
    }

    public void setRp() {
        if (mPreBean != null || userBean != null) {
            if (mPreBean != null) {
                total_red_packet = mPreBean.getPointE2();
                if (mTotalPrice > total_red_packet) {
                    current_red_packet = mPreBean.getPointE2();
                } else {
                    current_red_packet = mTotalPrice;
                }
            } else {
                total_red_packet = userBean.getPointE2();
                if (mTotalPrice > total_red_packet) {
                    current_red_packet = userBean.getPointE2();
                } else {
                    current_red_packet = mTotalPrice;
                }
            }
            tv_total_red_packet.setText("共¥" + StringUtils.getAllPrice(total_red_packet) + "元红包，");
            if (current_red_packet > 0) {
                tv_deduct_red_packet.setText("抵¥" + StringUtils.getAllPrice(current_red_packet) + "元");
                sb_red_packet_credit.setChecked(true);
                sb_red_packet_credit.setEnabled(true);
                tv_red_price_tip.setVisibility(View.VISIBLE);
                tv_red_price.setVisibility(View.VISIBLE);
                deduct_red_packet = current_red_packet;
                tv_red_price.setText("- ¥" + StringUtils.getAllPrice(current_red_packet));
                tv_actual_price.setTagText(StringUtils.getPrice(mTotalPrice - current_red_packet));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(mTotalPrice - current_red_packet));
                actual_price = mTotalPrice - current_red_packet;
            } else {
                sb_red_packet_credit.setChecked(false);
                sb_red_packet_credit.setEnabled(false);
                tv_deduct_red_packet.setText("无可抵");
                deduct_red_packet = 0;
                tv_red_price_tip.setVisibility(View.GONE);
                tv_red_price.setVisibility(View.GONE);
                tv_actual_price.setTagText(StringUtils.getPrice(mTotalPrice));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(mTotalPrice));
                actual_price = mTotalPrice;
            }

        }
    }

    public void setData() {

        if (mPreBean != null && mPreBean.getAddressInfo() != null && !StringUtils.isTextEmpty(mPreBean.getAddressInfo().getProvinceName())) {
            addressId = mPreBean.getAddressInfo().getId() + "";
            rl_noaddress.setVisibility(View.GONE);
            rl_address.setVisibility(View.VISIBLE);
            tv_address_city.setText(mPreBean.getAddressInfo().getProvinceName() + mPreBean.getAddressInfo().getCityName() + mPreBean.getAddressInfo().getAreasName() + mPreBean.getAddressInfo().getStreetName());
            tv_address_detail.setText(mPreBean.getAddressInfo().getAddress());
            tv_address_name.setText(mPreBean.getAddressInfo().getConsignee());
            tv_address_phone.setText(mPreBean.getAddressInfo().getMobile());
        } else {
            rl_noaddress.setVisibility(View.VISIBLE);
            rl_address.setVisibility(View.GONE);
        }
    }


    @Override
    public void getDefaultOrderPreSuccess(CreateOrderDefaultPreBean createOrderDefaultPreBean) {

        mNetworkErrorLayout.setVisibility(View.GONE);
        mPreBean = createOrderDefaultPreBean;
        mProvinceId = mPreBean.getAddressInfo().getProvinceId() + "";

        getSupportArea();


    }

    @Override
    public void createOrderSuccess(String orderId, String orderParcelId) {
        if (sb_red_packet_credit.isChecked() && actual_price == 0) {
            PreferencesUtils.put(Constants.Key.KEY_orderId, orderId);
            PreferencesUtils.put(Constants.Key.KEY_orderTag, ProductCreateOrderActivity.mPageName);
            Intent intent = new Intent(SingleProductCreateOrderActivity.this, WXPayEntryActivity.class);
            intent.putExtra(Constants.Key.KEY_1, false);
            intent.putExtra(Constants.Key.KEY_2, false);
            startActivity(intent);
        } else {
            if (mIntentData.isOneYuan) {
                PreferencesUtils.put(Constants.Key.KEY_isOneYuanPurchase, 1); //保存是否是一元购商品生成订单在WXPayEntryActivity界面判断待支付状态显示5分钟和30分钟内可以支付（1：是 0：否）
            }
            if (sb_red_packet_credit.isChecked()) {
                Intent intent = new Intent(SingleProductCreateOrderActivity.this, ProductPaymentActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                intent.putExtra(Constants.Key.KEY_2, (int) actual_price);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SingleProductCreateOrderActivity.this, ProductPaymentActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                intent.putExtra(Constants.Key.KEY_2, mTotalPrice);
                startActivity(intent);
            }

        }
        finish();
    }

    @Override
    public void createOrderFail(String errorTip) {
        tv_error_tip.setText(errorTip);
        dialog_ed_pay_pwd.setText("");
        dialog_ed_pay_pwd.requestFocus();

        getSupportArea();
    }

    @Override
    public void getGoodsAreaSuccess(List<String> res) {
        mProvinceList = res;
        setData();
        dealOverArea();
    }

    @Override
    public void getGoodsSupportAreaError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
        setData();
        setPrice(true);
        setRp();

    }

    /**
     * 判断超区，并设置显示
     */
    public void dealOverArea() {

        boolean inArea = GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId, mProvinceList);

        if (inArea) {
            rl_giftinfo.setVisibility(View.VISIBLE);
            vg_goods.setVisibility(View.GONE);
            if (TextUtils.isEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                tv_no_goods_pay.setVisibility(View.VISIBLE);
                tv_no_goods_pay.setText("暂无收货地址");
            } else {
                tv_no_goods_pay.setVisibility(View.GONE);
            }
        } else {
            rl_giftinfo.setVisibility(View.GONE);
            vg_goods.setVisibility(View.VISIBLE);
            vg_goods.setData(mIntentData.good_pic);
            tv_no_goods_pay.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                tv_no_goods_pay.setText("暂无收货地址");
            } else {
                tv_no_goods_pay.setText("当前订单没有可支付商品");
            }
        }
        setPrice(inArea);
        setRp();

    }

    /**
     * 选择地址返回处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_addressAdd) {
                mPresenter.GetDefualtOrderPre(SingleProductCreateOrderActivity.this);
            } else if (requestCode == RequestCode.SELECT_ADDRESS_CALL_BACK) {
                AddressListDataBean.AddressItemBean addressItemBean = (AddressListDataBean.AddressItemBean) data.getSerializableExtra(IntentKey.ADDRESS_SELECT_BACK);
                addressId = addressItemBean.getId() + "";
                tv_address_city.setText(addressItemBean.getProvinceName() + addressItemBean.getCityName() + addressItemBean.getAreasName() + addressItemBean.getStreetName());
                tv_address_detail.setText(addressItemBean.getAddress());
                tv_address_name.setText(addressItemBean.getConsignee());
                tv_address_phone.setText(addressItemBean.getMobile());
                //绑定数据，判断是否超区
                mProvinceId = addressItemBean.getProvinceId() + "";
                dealOverArea();

            }
        }
    }


    /**
     * 输入支付密码对话框
     */
    private Dialog dialog_check_pay_pwd;
    private VerificationCodeEditText dialog_ed_pay_pwd;
    private TextView tv_error_tip;

    public void showCheckPayPwdDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_create_order_check_pay_pwd,
                null);
        dialog_check_pay_pwd = new Dialog(this, R.style.dialogPayPwdWindowStyle);
        dialog_check_pay_pwd.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog_check_pay_pwd.getWindow();
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
        window.setWindowAnimations(R.style.ani_bottom_pay_pwd);
        // 设置点击外围解散
        dialog_check_pay_pwd.setCanceledOnTouchOutside(true);
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        dialog_ed_pay_pwd = view.findViewById(R.id.ed_pay_pwd);
        dialog_ed_pay_pwd.requestFocus();
        tv_error_tip = view.findViewById(R.id.tv_error_tip);
        iv_dialog_close.setOnClickListener(v -> {
            ScreenUtils.hideKeyboard(dialog_ed_pay_pwd);
            dialog_check_pay_pwd.dismiss();
        });

        dialog_ed_pay_pwd.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tv_error_tip.setText("");
                }
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                String pay_pwd = s.toString();
                if (mIntentData.isOneYuan) {
                    mPresenter.SingleCreateOrder(addressId, mIntentData.good_id, deduct_red_packet + "", pay_pwd,
                            isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber,
                            mobile, eamil, ed_remarks.getText().toString(), "1", SingleProductCreateOrderActivity.this, mIntentData.mActivityId);
                } else {
                    mPresenter.SingleCreateOrder(addressId, mIntentData.good_id, deduct_red_packet + "", pay_pwd,
                            isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber, mobile,
                            eamil, ed_remarks.getText().toString(), "", SingleProductCreateOrderActivity.this, mIntentData.mActivityId);
                }
            }
        });

        dialog_check_pay_pwd.show();
    }

    DialogUtil.ClickSureAction clickSureAction = postion -> startActivity(new Intent(SingleProductCreateOrderActivity.this, ModifyPayPwdCheckPhoneActivity.class));

    /**
     * 修复红包抵扣金额对话框
     */
    private Dialog dialog_modify_red_packet;
    private EditText dialog_ed_red_packet;
    private ImageView iv_dialog_jia, iv_dialog_jian;
    private TextView tv_sure;
    private long current_red_packet;
    private long total_red_packet;

    public void showModifyRedPacketDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_modify_red_packet,
                null);
        dialog_modify_red_packet = new Dialog(this, R.style.dialogPayPwdWindowStyle);
        dialog_modify_red_packet.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog_modify_red_packet.getWindow();
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
        window.setWindowAnimations(R.style.ani_bottom_pay_pwd);
        // 设置点击外围解散
        dialog_modify_red_packet.setCanceledOnTouchOutside(true);
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        iv_dialog_jia = view.findViewById(R.id.iv_dialog_jia);
        iv_dialog_jian = view.findViewById(R.id.iv_dialog_jian);
        tv_sure = view.findViewById(R.id.tv_sure);
        dialog_ed_red_packet = view.findViewById(R.id.ed_red_packet);
        dialog_ed_red_packet.requestFocus();
        dialog_ed_red_packet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (!StringUtils.isTextEmpty(s)) {
                    int red = (int) (Float.valueOf(s) * 100);
                    if (total_red_packet < 100) {
                        iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian_invalid);
                        iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia_invalid);
                        if (red > total_red_packet) {
                            current_red_packet = total_red_packet;
                            dialog_ed_red_packet.setText(StringUtils.getAllPrice(total_red_packet));
                            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
                        } else {
                            current_red_packet = red;
                        }
                    } else if (total_red_packet >= 100) {
                        if (red < 100) {
                            iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian_invalid);
                        } else {
                            iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian);
                        }
                        if (total_red_packet - red >= 100) {
                            iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia);
                        } else {
                            iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia_invalid);
                        }
                        if (red > total_red_packet) {
                            current_red_packet = total_red_packet;
                            dialog_ed_red_packet.setText(StringUtils.getAllPrice(total_red_packet));
                            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
                        } else {
                            current_red_packet = red;
                        }
                    }
                } else {
                    current_red_packet = 0;
                }
            }
        });
        iv_dialog_jia.setOnClickListener(view13 -> {
            current_red_packet += 100;
            if (current_red_packet > total_red_packet) {
                current_red_packet -= 100;
            } else {
                dialog_ed_red_packet.setText(StringUtils.getAllPrice(current_red_packet));
                dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
            }
        });
        iv_dialog_jian.setOnClickListener(view12 -> {
            if (current_red_packet < 100) {
                return;
            }
            current_red_packet -= 100;
            dialog_ed_red_packet.setText(StringUtils.getAllPrice(current_red_packet));
            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
        });
        dialog_ed_red_packet.setText(StringUtils.getAllPrice(current_red_packet));
        dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
        iv_dialog_close.setOnClickListener(view1 -> {
            ScreenUtils.hideKeyboard(dialog_ed_red_packet);
            dialog_modify_red_packet.dismiss();
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_modify_red_packet.dismiss();
                if (mTotalPrice - current_red_packet < 0) {
                    current_red_packet =mTotalPrice;
                } else if (total_red_packet - current_red_packet < 0) {
                    current_red_packet = total_red_packet;
                }
                if (current_red_packet > 0) {
                    sb_red_packet_credit.setChecked(true);
                } else {
                    sb_red_packet_credit.setChecked(false);
                }
                tv_deduct_red_packet.setText("抵¥" + StringUtils.getAllPrice(current_red_packet) + "元");
                tv_red_price.setText("- ¥" + StringUtils.getAllPrice(current_red_packet));
                deduct_red_packet = current_red_packet;
                actual_price =mTotalPrice - current_red_packet;
                tv_actual_price.setTagText(StringUtils.getPrice(actual_price));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(actual_price));
            }
        });


        dialog_modify_red_packet.show();
    }

    /**
     * 选择填写发票类型
     */
    Dialog dialog_invoice;
    int isNeedInvoice = 2;
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
            if (userBean != null) {
                ed_taker_phone.setText(userBean.getMobile());
                ed_taker_phone.setSelection(ed_taker_phone.getText().length());
            }
            TextView tv_invoice_tip = view.findViewById(R.id.tv_invoice_tip);
            tv_invoice_tip.setOnClickListener(view13 -> DialogUtil.showInvoiceIntroduce(SingleProductCreateOrderActivity.this));

            TextView tv_sure = view.findViewById(R.id.tv_sure);
            tv_sure.setOnClickListener(view12 -> {
                if (rg_invoice_type.getCheckedRadioButtonId() == R.id.rb_type_no) {
                    tv_invoice_info.setText("不开发票");
                    isNeedInvoice = 2;
                    invoiceLookedUp = "";
                    mobile = "";
                    eamil = "";
                    companyName = "";
                    companyIdentifyNumber = "";
                } else {
                    isNeedInvoice = 1;
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
                        companyName = "";
                        companyIdentifyNumber = "";
                        tv_invoice_info.setText("普票(商品明细-个人)");
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
                        tv_invoice_info.setText("普票(商品明细-" + ed_company_name.getText().toString() + ")");
                    }
                }
                dialog_invoice.dismiss();
            });

        }

        dialog_invoice.show();
    }

}
