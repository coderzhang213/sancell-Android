package cn.sancell.xingqiu.usermember;

import android.app.Dialog;
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
import cn.sancell.xingqiu.homeshoppingcar.bean.req.GoodsAreaReq;
import cn.sancell.xingqiu.homeshoppingcar.contract.ProductCreateOrderContract;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.MemberPaymentActivity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.viewGroup.NoSupportGoodsViewGroup;
import cn.sancell.xingqiu.viewGroup.manager.GoodsAreaManger;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SwitchButton;

/**
 * 礼包购买生成订单界面
 */
public class MemberGiftCreateOrderActivity extends BaseMVPToobarActivity<ProductCreateOrderContract.CreateOrderPresenter>
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
    @BindView(R.id.ed_remarks)
    EditText ed_remarks;
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
    @BindView(R.id.vg_goods)
    NoSupportGoodsViewGroup vg_goods;
    @BindView(R.id.rl_giftinfo)
    RelativeLayout rl_giftinfo;

    @BindView(R.id.tv_no_goods_pay)
    AppCompatTextView tv_no_goods_pay;

    private int requestCode_addressAdd = 101;

    private CreateOrderDefaultPreBean mPreBean;

    private String addressId;

    private String mProvinceId;

    private List<String> mProvinceList;

    /**
     * 上个界面传来的商品总价
     */
    int total_price;
    int mPayPrice;
    private String good_id, good_title, good_spec, good_pic;
    private int goodMemberLevel;

    private long actual_price, deduct_red_packet;

    private UserBean userBean;


    @Override
    protected ProductCreateOrderContract.CreateOrderPresenter createPresenter() {
        return new ProductCreateOrderContract.CreateOrderPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_gift_create_order;
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
        good_id = getIntent().getStringExtra(Constants.Key.KEY_1);
        good_title = getIntent().getStringExtra(Constants.Key.KEY_2);
        good_spec = getIntent().getStringExtra(Constants.Key.KEY_3);
        good_pic = getIntent().getStringExtra(Constants.Key.KEY_4);
        total_price = getIntent().getIntExtra(Constants.Key.KEY_5, 0);
        goodMemberLevel = getIntent().getIntExtra(Constants.Key.KEY_6, 0);

        mPayPrice = total_price;
        tv_gopay.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_noaddress.setOnClickListener(this);

        sdv_pic.setImageURI(Uri.parse(good_pic));
        tv_product_name.setText(good_title);
        tv_product_specs.setText(good_spec);
        tv_modify_red_packet.setOnClickListener(this);
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
                tv_actual_price.setTagText(StringUtils.getPrice(mPayPrice));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(mPayPrice));
                tv_modify_red_packet.setVisibility(View.GONE);
            }
        });
        tv_modify_red_packet.setOnClickListener(this);
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
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.GetDefualtOrderPre(MemberGiftCreateOrderActivity.this);
            }
        });
    }

    public void getGoodArea() {
        GoodsAreaReq req = new GoodsAreaReq();
        req.goodsId = good_id;
        mPresenter.getGoodsArea(this, req);
    }


    @Override
    public void getDefaultOrderPreSuccess(CreateOrderDefaultPreBean createOrderDefaultPreBean) {
        mPreBean = createOrderDefaultPreBean;
        mProvinceId = createOrderDefaultPreBean.getAddressInfo().getProvinceId()+"";
        mNetworkErrorLayout.setVisibility(View.GONE);
        getGoodArea();

    }

    public void setRp(){
        if (userBean != null) {
            if (userBean.getIsOldUser() == 1) { //老会员
                rl_red_packet.setVisibility(View.VISIBLE);
                if (mPreBean != null) {
                    total_red_packet = mPreBean.getPointE2();
                    if (mPayPrice > total_red_packet) {
                        current_red_packet = mPreBean.getPointE2();
                    } else {
                        current_red_packet = mPayPrice;
                    }
                } else {
                    total_red_packet = userBean.getPointE2();
                    if (mPayPrice > total_red_packet) {
                        current_red_packet = userBean.getPointE2();
                    } else {
                        current_red_packet = mPayPrice;
                    }
                }
                tv_total_red_packet.setText("共¥" + StringUtils.getAllPrice(total_red_packet) + "元平移金额，");
                if (current_red_packet > 0) {
                    tv_deduct_red_packet.setText("抵¥" + StringUtils.getAllPrice(current_red_packet) + "元");
                    sb_red_packet_credit.setChecked(true);
                    sb_red_packet_credit.setEnabled(true);
                    tv_red_price_tip.setVisibility(View.VISIBLE);
                    tv_red_price.setVisibility(View.VISIBLE);
                    deduct_red_packet = current_red_packet;
                    tv_red_price.setText("-" + StringUtils.getAllPrice(current_red_packet));
                    tv_actual_price.setTagText(StringUtils.getPrice(mPayPrice - current_red_packet));
                    tv_actual_price.setEndText(StringUtils.getPriceDecimal(mPayPrice - current_red_packet));
                    actual_price = mPayPrice - current_red_packet;
                } else {
                    sb_red_packet_credit.setChecked(false);
                    sb_red_packet_credit.setEnabled(false);
                    tv_deduct_red_packet.setText("无可抵");
                    deduct_red_packet = 0;
                    tv_red_price_tip.setVisibility(View.GONE);
                    tv_red_price.setVisibility(View.GONE);
                    tv_actual_price.setTagText(StringUtils.getPrice(mPayPrice));
                    tv_actual_price.setEndText(StringUtils.getPriceDecimal(mPayPrice));
                    actual_price = mPayPrice;
                }
            } else {
                rl_red_packet.setVisibility(View.GONE);
                sb_red_packet_credit.setChecked(false);
                actual_price = mPayPrice;
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
    public void createOrderSuccess(String orderId, String orderParcelId) {
        if (sb_red_packet_credit.isChecked() && actual_price == 0) {
            PreferencesUtils.put(Constants.Key.KEY_orderId, orderId);
            PreferencesUtils.put(Constants.Key.KEY_orderTag, MemberPaymentActivity.mPageName);
            PreferencesUtils.put(Constants.Key.KEY_orderParceId, orderParcelId);
            PreferencesUtils.put(Constants.Key.KEY_orderMemberLevel, goodMemberLevel + "");
            startActivity(new Intent(MemberGiftCreateOrderActivity.this, MemberBuyGiftSuccessActivity.class));
        } else {
            Intent intent = new Intent(MemberGiftCreateOrderActivity.this, MemberPaymentActivity.class);
            intent.putExtra(Constants.Key.KEY_1, orderId);
            if (sb_red_packet_credit.isChecked()) {
                intent.putExtra(Constants.Key.KEY_2, (int) actual_price);
            } else {
                intent.putExtra(Constants.Key.KEY_2, mPayPrice);
            }
            intent.putExtra(Constants.Key.KEY_3, orderParcelId);
            intent.putExtra(Constants.Key.KEY_4, goodMemberLevel + "");
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void createOrderFail(String errorTip) {
        //订单创建失败，判断是否存在超区


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
        setRp();
        getGoodArea();

    }

    public void setTotalPrice(boolean inArea){
        if (inArea){
            mPayPrice = total_price;
        }else{
            mPayPrice = 0;
        }
        tv_totalprice.setText("¥" + StringUtils.getAllPrice(mPayPrice));
        tv_actual_price.setStartText("¥");
        tv_actual_price.setStartProportion(0.75f);
        tv_actual_price.setEndProportion(0.75f);
        tv_product_price.setTagText(StringUtils.getPrice(mPayPrice));
        tv_product_price.setStartText("¥");
        tv_product_price.setStartProportion(0.78f);
        tv_product_price.setEndText(StringUtils.getPriceDecimal(mPayPrice));
        tv_product_price.setEndProportion(0.78f);
    }

    /**
     * 判断超区，并设置显示
     */
    public void dealOverArea() {
        boolean inArea = GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId, mProvinceList);

        setTotalPrice(inArea);

        if (inArea) {
            rl_giftinfo.setVisibility(View.VISIBLE);
            vg_goods.setVisibility(View.GONE);
            if (TextUtils.isEmpty(addressId) || TextUtils.equals(addressId,"0")){
                tv_no_goods_pay.setVisibility(View.VISIBLE);
                tv_no_goods_pay.setText("暂无收货地址");
            }else{
                tv_no_goods_pay.setVisibility(View.GONE);
            }
        } else {
            rl_giftinfo.setVisibility(View.GONE);
            vg_goods.setVisibility(View.VISIBLE);
            tv_no_goods_pay.setVisibility(View.VISIBLE);
            vg_goods.setData(good_pic);
            if (TextUtils.isEmpty(addressId) || TextUtils.equals(addressId,"0")){
                tv_no_goods_pay.setText("暂无收货地址");
            }else{
                tv_no_goods_pay.setText("当前订单没有可支付商品");

            }
        }
        setRp();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
//                Intent intent1 = new Intent(MemberGiftCreateOrderActivity.this, UserAddressListActivity.class);
//                intent1.putExtra(Constants.Key.KEY_1, true);
//                intent1.putExtra(Constants.Key.KEY_2, addressId);
//                startActivityForResult(intent1, requestCode_addressSelect);
                SelectAddressActivity.start(this, RequestCode.SELECT_ADDRESS_CALL_BACK);
                break;
            case R.id.rl_noaddress:
                Intent intent = new Intent(MemberGiftCreateOrderActivity.this, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                intent.putExtra(Constants.Key.KEY_2, true);
                intent.putExtra(Constants.Key.KEY_4, false);
                startActivityForResult(intent, requestCode_addressAdd);
                break;
            case R.id.tv_gopay:
                if (StringUtils.isTextEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("请填写收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "address");
                    return;
                }

                if (GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId, mProvinceList)) {
                    if (sb_red_packet_credit.isChecked()) {
                        mPresenter.CreateMemberOrder(addressId, good_id, deduct_red_packet + "", ed_remarks.getText().toString(), MemberGiftCreateOrderActivity.this);
                    } else {
                        mPresenter.CreateMemberOrder(addressId, good_id, "", ed_remarks.getText().toString(), MemberGiftCreateOrderActivity.this);
                    }
                }else{
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("抱歉，当前地址没有可配送商品，请确认收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "all");
                }


                break;
            case R.id.tv_modify_red_packet:
                showModifyRedPacketDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_addressAdd) {
                mPresenter.GetDefualtOrderPre(MemberGiftCreateOrderActivity.this);
            } else if (requestCode == RequestCode.SELECT_ADDRESS_CALL_BACK) {
                AddressListDataBean.AddressItemBean addressItemBean = (AddressListDataBean.AddressItemBean) data.getSerializableExtra(IntentKey.ADDRESS_SELECT_BACK);
                addressId = addressItemBean.getId() + "";
                mProvinceId = addressItemBean.getProvinceId()+"";
                tv_address_city.setText(addressItemBean.getProvinceName() + addressItemBean.getCityName() + addressItemBean.getAreasName() + addressItemBean.getStreetName());
                tv_address_detail.setText(addressItemBean.getAddress());
                tv_address_name.setText(addressItemBean.getConsignee());
                tv_address_phone.setText(addressItemBean.getMobile());
                dealOverArea();
            }
        }
    }

    /**
     * 修改平移金额抵扣金额对话框
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
        tv_sure.setOnClickListener(view14 -> {
            dialog_modify_red_packet.dismiss();
            if (mPayPrice - current_red_packet < 0) {
                current_red_packet = mPayPrice;
            } else if (total_red_packet - current_red_packet < 0) {
                current_red_packet = total_red_packet;
            }
            /*if (current_red_packet >= total_red_packet) {
                if (total_price > total_red_packet) {
                    current_red_packet = total_red_packet;
                } else {
                    current_red_packet = total_price;
                }
            } else {
                if (current_red_packet > total_price) {
                    current_red_packet = total_price;
                }
            }*/
            if (current_red_packet > 0) {
                sb_red_packet_credit.setChecked(true);
            } else {
                sb_red_packet_credit.setChecked(false);
            }
            tv_deduct_red_packet.setText("抵¥" + StringUtils.getAllPrice(current_red_packet) + "元");
            tv_red_price.setText("-" + StringUtils.getAllPrice(current_red_packet));
            deduct_red_packet = current_red_packet;
            actual_price = mPayPrice - current_red_packet;
            tv_actual_price.setTagText(StringUtils.getPrice(actual_price));
            tv_actual_price.setEndText(StringUtils.getPriceDecimal(actual_price));
        });


        dialog_modify_red_packet.show();
    }
}
