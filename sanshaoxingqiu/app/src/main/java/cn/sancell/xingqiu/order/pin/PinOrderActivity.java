package cn.sancell.xingqiu.order.pin;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.business.session.constant.RequestCode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.address.select.SelectAddressActivity;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.OrderVoucherRes;
import cn.sancell.xingqiu.bean.VoucherInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.dialog.ChoseVoucherDialog;
import cn.sancell.xingqiu.dialog.OrderTipDialogFgm;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homeshoppingcar.ProductCreateOrderActivity;
import cn.sancell.xingqiu.homeshoppingcar.ProductPaymentActivity;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckPhoneActivity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.interfaces.OnPlayPasswordLinsenr;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.order.entity.PinIntentBean;
import cn.sancell.xingqiu.order.entity.req.GoodVoucherReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderNewReq;
import cn.sancell.xingqiu.order.entity.req.PinOrderReq;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.service.PayDataService;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.util.BigDecimalUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PlayDialogUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.PriceUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.viewGroup.NoSupportGoodsViewGroup;
import cn.sancell.xingqiu.viewGroup.manager.GoodsAreaManger;
import cn.sancell.xingqiu.widget.SwitchButton;
import cn.sancell.xingqiu.wxapi.WXPayEntryActivity;

/**
 * @author Alan_Xiong
 * @desc: 拼团订单详情
 * @time 2020-01-02 10:37
 */
public class PinOrderActivity extends BaseMVPToobarActivity<PinOrderPresenter> implements PinOrderView, View.OnClickListener {


    @BindView(R.id.tv_pin_states)
    AppCompatTextView tv_pin_states;
    @BindView(R.id.tv_pin_states_desc)
    AppCompatTextView tv_pin_states_desc;
    //地址
    @BindView(R.id.card_address)
    CardView card_address;
    @BindView(R.id.tv_add_province)
    AppCompatTextView tv_add_province;
    @BindView(R.id.tv_add_detail)
    AppCompatTextView tv_add_detail;
    @BindView(R.id.tv_receive_name)
    AppCompatTextView tv_receive_name;
    @BindView(R.id.tv_receive_phone)
    AppCompatTextView tv_receive_phone;
    //代金券地址
    @BindView(R.id.card_address_voucher)
    CardView card_address_voucher;
    @BindView(R.id.tv_voucher_receive_name)
    AppCompatTextView tv_voucher_receive_name;
    @BindView(R.id.tv_voucher_phone)
    AppCompatTextView tv_voucher_phone;
    @BindView(R.id.tv_voucher_adr)
    AppCompatTextView tv_voucher_adr;

    //商品
    @BindView(R.id.card_goods)
    CardView card_goods;
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
    @BindView(R.id.et_remark)
    AppCompatEditText et_remark;
    //不支持配送
    @BindView(R.id.vg_goods)
    NoSupportGoodsViewGroup vg_goods;
    //红包
    @BindView(R.id.tv_total_red_packet)
    AppCompatTextView tv_total_red_packet;
    @BindView(R.id.tv_deduct_red_packet)
    AppCompatTextView tv_deduct_red_packet;
    @BindView(R.id.tv_modify_red_packet)
    AppCompatTextView tv_modify_red_packet;
    @BindView(R.id.btn_red_packet_credit)
    SwitchButton btn_red_packet_credit;
    //发票
    @BindView(R.id.rl_invoice)
    RelativeLayout rl_invoice;
    @BindView(R.id.tv_invoice_info)
    AppCompatTextView tv_invoice_info;
    //金额
    @BindView(R.id.tv_total_price)
    AppCompatTextView tv_total_price;
    @BindView(R.id.tv_red_price_tip)
    AppCompatTextView tv_red_price_tip;
    @BindView(R.id.tv_red_price)
    AppCompatTextView tv_red_price;
    @BindView(R.id.tv_no_goods_pay)
    AppCompatTextView tv_no_goods_pay;
    //支付金额
    @BindView(R.id.tv_pay_price)
    AppCompatTextView tv_pay_price;
    @BindView(R.id.tv_pay)
    AppCompatTextView tv_pay;

    //代金券
    @BindView(R.id.tv_voucher_count)
    AppCompatTextView tv_voucher_count;
    @BindView(R.id.iv_voucher_more)
    AppCompatImageView iv_voucher_more;
    @BindView(R.id.tv_rp_can_use)
    AppCompatTextView tv_rp_can_use;
    @BindView(R.id.tv_rb_voucher)
    AppCompatTextView tv_rb_voucher;
    @BindView(R.id.tv_rb_rp)
    AppCompatTextView tv_rb_rp;
    @BindView(R.id.tv_reduce_tip)
    AppCompatTextView tv_reduce_tip;

    //结算
    @BindView(R.id.tv_goods_total)
    AppCompatTextView tv_goods_total;
    @BindView(R.id.tv_reduce_title)
    AppCompatTextView tv_reduce_title;
    @BindView(R.id.tv_reduce_money)
    AppCompatTextView tv_reduce_money;
    @BindView(R.id.tv_add_address)
    AppCompatTextView tv_add_address;
    @BindView(R.id.rl_address_info)
    RelativeLayout rl_address_info;
    //protocol
    @BindView(R.id.rl_protocol)
    RelativeLayout rl_protocol;
    @BindView(R.id.cb_check)
    CheckBox cb_check;
    @BindView(R.id.tv_link)
    AppCompatTextView tv_link;


    private int mProvinceId;
    private CreateOrderDefaultPreBean mPreData;
    private PinIntentBean mIntentData;

    private int mAddressId;
    //红包
    private long mMaxRpCanUse;
    //红包
    private long mCurrentUseRp; //当前可使用的红包
    private long mTotalRp; //总红包

    private long mTotalPrice;//总价
    private long mPayPrice; //支付金额
    private UserBean mUserBean;

    private int mReduceType = 0;//折扣类型1代金券。2红包
    private int mVoucherCount;//代金券数量
    private VoucherInfo mVoucher;//选中的代金券
    private List<VoucherInfo> mVoucherList = new ArrayList<>();
    private int mCanUserReduce = 0; //非会员不能使用代金券balabala

    public static void start(Context context, PinIntentBean bean) {
        Intent intent = new Intent(context, PinOrderActivity.class);
        intent.putExtra(IntentKey.PIN_DATA, bean);
        context.startActivity(intent);
    }

    @Override
    protected PinOrderPresenter createPresenter() {
        return new PinOrderPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pin_order;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle(R.string.sure_pin_order, R.mipmap.icon_goods_service, v -> {
            showCallPhone();
        });
        mIntentData = (PinIntentBean) getIntent().getSerializableExtra(IntentKey.PIN_DATA);
        if (mIntentData == null) {
            SCApp.getInstance().showSystemCenterToast("数据异常");
            finish();
            return;
        }
        //协议 6成品
        if (mIntentData.goodsFlag == 6) {
            rl_protocol.setVisibility(View.VISIBLE);
        } else {
            rl_protocol.setVisibility(View.GONE);
        }

        mUserBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);

        mTotalPrice = mIntentData.total_price * mIntentData.buyNum;
        mMaxRpCanUse = mIntentData.mMaxRpCanUse;
        initGoods();

        bindAddressView(mIntentData.mAddress);
        mPresenter.getDefaultAddress();
        mPresenter.getUserMember();
        getVoucherCount();
        initListener();

    }

    public void getVoucherCount() {
        GoodVoucherReq req = new GoodVoucherReq();
        req.goodsId = mIntentData.goodsId + "";
        req.onlyCount = "1"; //只要数量
        mPresenter.getVoucherInfo(req);
    }

    public void getVoucherList() {
        GoodVoucherReq req = new GoodVoucherReq();
        req.goodsId = mIntentData.goodsId + "";
        req.onlyCount = "0";
        mPresenter.getVoucherInfo(req);
    }


    //商品参数
    private void initGoods() {
        Glide.with(this).load(mIntentData.good_pic).into(iv_order_goods);
        tv_goods_name.setText(mIntentData.good_title);
        tv_goods_desc.setText(mIntentData.good_spec);
        tv_goods_count.setText(new StringBuffer("x" + mIntentData.buyNum));
        tv_goods_price.setText(PriceUtils.getInstance().getMainPrice(this, mIntentData.total_price, 14));
    }

    //绑定地址
    private void bindAddressView(AddressListDataBean.AddressItemBean item) {
        if (item != null && item.getId() > 0) {
            mAddressId = item.getId();
            mProvinceId = item.getProvinceId();
            /*
            旧地址UI，勿删
            tv_add_province.setText(new StringBuffer(item.getProvinceName() + item.getCityName() + item.getAreasName() + item.getStreetName()));
            tv_add_detail.setText(item.getAddress());
            tv_receive_name.setText(item.getConsignee());
            tv_receive_phone.setText(item.getMobile());
            */
            tv_voucher_adr.setText(new StringBuffer(item.getProvinceName() + item.getCityName() + item.getAreasName() + item.getStreetName() + item.getAddress()));
            tv_voucher_phone.setText(item.getMobile());
            tv_voucher_receive_name.setText(item.getConsignee());

            //  dealOverArea();
            rl_address_info.setVisibility(View.VISIBLE);
            tv_add_address.setVisibility(View.GONE);
        } else {
            tv_add_address.setVisibility(View.VISIBLE);
            rl_address_info.setVisibility(View.GONE);
        }
    }

    /**
     * 代金券与红包
     */
    private void bindVoucherRp() {


        //总红包数量
        mTotalRp = mPreData.getPointE2();
        if (mTotalRp <= 0 || mMaxRpCanUse <= 0) {
            //没红包
            tv_rb_rp.setEnabled(false);
            tv_rp_can_use.setText("无可用红包");
        } else {
            tv_rb_rp.setEnabled(true);
            long rpCanUse = Math.min(mTotalRp, mMaxRpCanUse);
            tv_rp_can_use.setText(new StringBuffer("可抵扣¥" + StringUtils.getAllPrice(Math.min(rpCanUse, mTotalPrice)) + "元"));
        }

        //代金券
        if (mVoucherCount == 0) {
            tv_voucher_count.setText("无可用代金券");
            tv_rp_can_use.setEnabled(false);
            tv_rb_voucher.setEnabled(false);
            iv_voucher_more.setEnabled(false);
        } else {
            tv_rp_can_use.setEnabled(true);
            tv_rb_voucher.setEnabled(true);
            iv_voucher_more.setEnabled(true);
            tv_voucher_count.setText(new StringBuffer(mVoucherCount + "张可用代金券"));
        }
        if (mCanUserReduce == 0) {
            tv_rb_rp.setEnabled(false);
            tv_rb_voucher.setEnabled(false);
        }


    }


    /**
     * 判断超区，并设置显示
     */
    public void dealOverArea() {

        boolean inArea = GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId + "", mIntentData.supportArea);

        if (inArea) {
            card_goods.setVisibility(View.VISIBLE);
            vg_goods.setVisibility(View.GONE);
            if (mAddressId > 0) {

                tv_no_goods_pay.setVisibility(View.GONE);
            } else {
                tv_no_goods_pay.setVisibility(View.GONE);
            }
        } else {
            card_goods.setVisibility(View.GONE);
            vg_goods.setVisibility(View.VISIBLE);
            vg_goods.setData(mIntentData.good_pic);
            tv_no_goods_pay.setVisibility(View.VISIBLE);
            if (mAddressId <= 0) {
                tv_no_goods_pay.setText("暂无收货地址");
            } else {
                tv_no_goods_pay.setText("当前订单没有可支付商品");
            }
        }
        setPrice(inArea);
        bindRp();
    }


    private void bindRp() {

        if (mPreData != null) {
            mTotalRp = mPreData.getPointE2();
            if (mMaxRpCanUse == 0) {
                mCurrentUseRp = 0;
            } else if (mMaxRpCanUse < mTotalRp) {
                mCurrentUseRp = mMaxRpCanUse;
            } else {
                mCurrentUseRp = mTotalRp;
            }

        } else {
            if (mUserBean != null) {
                mTotalRp = mUserBean.getPointE2();
                if (mMaxRpCanUse == 0) {
                    mCurrentUseRp = 0;
                } else if (mMaxRpCanUse < mTotalRp) {
                    mCurrentUseRp = mMaxRpCanUse;
                } else {
                    mCurrentUseRp = mTotalRp;
                }
            }
        }

        tv_total_red_packet.setText(new StringBuffer("共¥" + StringUtils.getAllPrice(mTotalRp) + "元红包，"));
        if (mCurrentUseRp > 0) {
            mPayPrice = mTotalPrice - mCurrentUseRp;
            tv_deduct_red_packet.setText(new StringBuffer("抵¥" + StringUtils.getAllPrice(mCurrentUseRp) + "元"));
            btn_red_packet_credit.setEnabled(true);
            btn_red_packet_credit.setChecked(true);
            tv_red_price_tip.setVisibility(View.VISIBLE);
            tv_red_price.setVisibility(View.VISIBLE);
            tv_red_price.setText(new StringBuffer("-" + PriceUtils.getInstance().getPrice(mCurrentUseRp)));
            tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(this, (int) mPayPrice, 20));
        } else {
            mPayPrice = mTotalPrice;
            tv_deduct_red_packet.setText("无可抵");
            btn_red_packet_credit.setEnabled(false);
            btn_red_packet_credit.setChecked(false);
            tv_red_price_tip.setVisibility(View.GONE);
            tv_red_price.setVisibility(View.GONE);
            tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(this, (int) mPayPrice, 20));
        }

    }

    public void setPrice(boolean inArea) {
        if (inArea) {
            mTotalPrice = mIntentData.total_price * mIntentData.buyNum;
        } else {
            mTotalPrice = 0;
        }
        tv_total_price.setText(PriceUtils.getInstance().getPriceWithSyp(mTotalPrice));

    }


    public void initListener() {

        tv_pay.setOnClickListener(this);
        rl_invoice.setOnClickListener(this);
        tv_modify_red_packet.setOnClickListener(this);
        card_address.setOnClickListener(this);
        card_address_voucher.setOnClickListener(this);
        tv_rb_voucher.setOnClickListener(this);
        tv_rb_rp.setOnClickListener(this);
        tv_voucher_count.setOnClickListener(this);
        iv_voucher_more.setOnClickListener(this);
        tv_link.setOnClickListener(this);

        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_check.setChecked(true);
                } else {
                    cb_check.setChecked(false);
                }
            }
        });

        et_remark.addTextChangedListener(new TextWatcher() {
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

        btn_red_packet_credit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mPayPrice = mTotalPrice - mCurrentUseRp;
                tv_red_price_tip.setVisibility(View.VISIBLE);
                tv_red_price.setVisibility(View.VISIBLE);
                tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(PinOrderActivity.this, (int) mPayPrice, 20));
                tv_modify_red_packet.setVisibility(View.VISIBLE);

            } else {
                mPayPrice = mTotalPrice;
                tv_red_price_tip.setVisibility(View.GONE);
                tv_red_price.setVisibility(View.GONE);
                tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(PinOrderActivity.this, (int) mPayPrice, 20));
                tv_modify_red_packet.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    //拨打电话
    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(this, "", "");
    }

    //默认地址
    @Override
    public void getDefaultAddressSuccess(CreateOrderDefaultPreBean res) {
        mPreData = res;
        //限制红包的使用金额
        mIntentData.mAddress = res.getAddressInfo();
        bindAddressView(res.getAddressInfo());
        bindVoucherRp();
        bindReduce();
    }


    @Override
    public void getError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void getGoodsAreaSuccess(List<String> res) {

    }

    @Override
    public void createPinOrderSuccess(OrderRes order) {
        //拿到服务端订单号
        PayDataService.mAddress = order.addressInfo;
        PayDataService.totalPrice = mPayPrice;
        if (mPayPrice == 0 && btn_red_packet_credit.isChecked()) {
            //红包抵扣了所有现金
            if (mPlayDialogUtils != null) {
                mPlayDialogUtils.dismiss();
            }
            PreferencesUtils.put(Constants.Key.KEY_orderId, order.grouponChildNo);
            PreferencesUtils.put(Constants.Key.KEY_orderTag, ProductCreateOrderActivity.mPageName);
            Intent intent = new Intent(this, WXPayEntryActivity.class);
            intent.putExtra(Constants.Key.KEY_1, false);
            intent.putExtra(Constants.Key.KEY_2, false);
            startActivity(intent);


        } else {
            if (mPlayDialogUtils != null) {
                mPlayDialogUtils.dismiss();
            }
            ProductPaymentActivity.start(this, order.grouponChildNo, (int) mPayPrice, UiHelper.PIN_PAY);

        }
    }

    @Override
    public void getVoucherInfoSuccess(OrderVoucherRes res, String type) {
        if (TextUtils.equals(type, "1")) {

            mVoucherCount = res.validCount;
            bindVoucherRp();
            bindReduce();
        } else {
            //代金券列表
            mVoucherList = res.canUsed.dataList;
            showPickVoucherDialog();
        }
    }


    @Override
    public void getVoucherInfoError(String msg) {
        SCApp.getInstance().showSystemCenterToast(msg);
    }

    @Override
    public void getUserMemberSuccess(UserMemberRes res) {
        mCanUserReduce = res.useCouponPoint;
        if (mCanUserReduce == 0) {
            tv_reduce_tip.setText("* 成为三少医美粉丝即可使用红包和代金券");
        } else {
            tv_reduce_tip.setText("* 代金券与红包不可同时使用");
        }
        PreferencesUtils.put(UiHelper.KEY_USER_MEMBER, res);
    }

    public void showPickVoucherDialog() {
        ChoseVoucherDialog dialog = ChoseVoucherDialog.Companion.newInstance();
        dialog.setData(mVoucherList, mVoucher != null ? mVoucher.couponReceiveId : "");
        dialog.setListener(item -> {
            mVoucher = item;
            mReduceType = 1;
            bindReduce();
        });
        dialog.show(getSupportFragmentManager(), "voucher");

    }

    public void bindReduce() {

        tv_goods_total.setText(String.format(getResources().getString(R.string.price_rmb), StringUtils.getAllPrice(mTotalPrice)));
        mPayPrice = mTotalPrice;

        if (mReduceType == 1) {
            tv_reduce_title.setVisibility(View.VISIBLE);
            tv_reduce_money.setVisibility(View.VISIBLE);
            tv_reduce_title.setText("代金券抵扣");
            //代金券
            tv_rb_rp.setSelected(false);
            tv_rb_voucher.setSelected(true);
            //处理代金券
            if (mVoucher != null) {
                tv_voucher_count.setText(new StringBuffer("抵扣¥" + StringUtils.getAllPrice(mVoucher.faceValueE2)));
                tv_reduce_money.setText(new StringBuffer("-¥" + StringUtils.getAllPrice(Math.min(mTotalPrice, mVoucher.faceValueE2))));
                mPayPrice = mTotalPrice - Math.min(mTotalPrice, mVoucher.faceValueE2);
            }
            //更新红包文案
            tv_rp_can_use.setText(new StringBuffer("可抵扣¥" + StringUtils.getAllPrice(Math.min(Math.min(mTotalRp, mMaxRpCanUse), mTotalPrice))));

        } else if (mReduceType == 2) {
            tv_reduce_title.setVisibility(View.VISIBLE);
            tv_reduce_money.setVisibility(View.VISIBLE);
            tv_rb_rp.setSelected(true);
            tv_rb_voucher.setSelected(false);
            tv_reduce_title.setText("红包抵扣");
            tv_rp_can_use.setText(new StringBuffer("抵扣¥" + StringUtils.getAllPrice(Math.min(Math.min(mTotalRp, mMaxRpCanUse), mTotalPrice))));
            //处理红包金额
            if (Math.min(mMaxRpCanUse, mTotalRp) > mTotalPrice) {
                //红包金额大与商品,商品金额可能为0
                tv_reduce_money.setText(new StringBuffer("-¥" + StringUtils.getAllPrice(Math.min(Math.min(mTotalRp, mMaxRpCanUse), mTotalPrice))));
                mPayPrice = 0;
            } else {
                tv_reduce_money.setText(new StringBuffer("-¥" + StringUtils.getAllPrice(Math.min(mMaxRpCanUse, mTotalRp))));
                mPayPrice = mTotalPrice - Math.min(mTotalRp, mMaxRpCanUse);
            }
            //如果选择了代金券
//            if (mVoucher != null) {
////                tv_voucher_count.setText(new StringBuffer("可抵扣" + StringUtils.getAllPrice(Math.min(mTotalPrice, mVoucher.faceValueE2))));
////            }
            if (mVoucherCount > 0) {
                tv_voucher_count.setText(new StringBuffer(mVoucherCount + "张可用代金券"));
            }
        } else {
            //最初状态
            tv_rb_rp.setSelected(false);
            tv_rb_voucher.setSelected(false);
            tv_reduce_title.setVisibility(View.GONE);
            tv_reduce_money.setVisibility(View.GONE);
            bindVoucherRp();
        }
        tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(PinOrderActivity.this, (int) mPayPrice, 20));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_address:
                //选择地址
            case R.id.card_address_voucher:
                if (mAddressId > 0) {
                    SelectAddressActivity.start(this, RequestCode.SELECT_ADDRESS_CALL_BACK);
                } else {
                    Intent intent = new Intent(this, AddressEditActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, true);
                    intent.putExtra(Constants.Key.KEY_2, true);
                    intent.putExtra(Constants.Key.KEY_4, false);
                    startActivityForResult(intent, RequestCode.ADD_ADDRESS);
                }
                break;
            case R.id.tv_modify_red_packet:
                showModifyRedPacketDialog();
                break;
            case R.id.rl_invoice:
                showSelectInvoiceDialog();
                break;
            case R.id.iv_voucher_more:
            case R.id.tv_voucher_count:
                if (mCanUserReduce == 0){
                    return;
                }
                //选取代金券
                if (mVoucherCount > 0) {
                    getVoucherList();
                }
                break;
            case R.id.tv_rb_voucher:
                //选中代金券
                if (mReduceType == 1) {
                    mReduceType = 0;
                    bindReduce();
                } else {
                    mReduceType = 1;
                    if (mVoucher != null) {
                        bindReduce();
                    } else {
                        getVoucherList();
                    }
                }
                break;
            case R.id.tv_rb_rp:
                if (mReduceType == 2) {
                    mReduceType = 0;
                    tv_rb_rp.setSelected(false);
                } else {
                    mReduceType = 2;
                }
                bindReduce();
                break;
            case R.id.tv_link:
                UrlInfoActivity.start(this, Constants.Url.URL_FANS_PROTOCOL, "会员协议");
                break;
            case R.id.tv_pay:
                if (mAddressId <= 0) {
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("请填写收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "");
                    return;
                }
                //未选取券
                if (mReduceType == 1 && mVoucher == null) {
                    SCApp.getInstance().showSystemCenterToast("请先选取代金券");
                    return;
                }

                //是否勾选协议
                if (mIntentData.goodsFlag == 6) {
                    if (!cb_check.isChecked()) {
                        SCApp.getInstance().showSystemCenterToast("请先勾选阅读芙蓉会粉丝协议");
                        return;
                    }
                }


                if (mReduceType == 2 || (mReduceType == 1 && mPayPrice == 0)) {
                    //使用红包，或者使用代金券抵扣为0时触发支付密码
                    UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
                    if (userBean != null && userBean.getAddPayPasswordStatus() == 1) {  //设置过支付密码
                        showCheckPayPwdDialog();
                    } else {
                        DialogUtil.showOperateDialog(PinOrderActivity.this,
                                getResources().getString(R.string.is_setting_pay_pwd),
                                getResources().getString(R.string.is_setting_pay_pwd_desc),
                                getResources().getString(R.string.dialog_set_paypwd_no),
                                getResources().getString(R.string.dialog_set_paypwd_yes), clickSureAction);
                    }
                } else {
                    createNewOrder("");
                }

                //判断是否在配送范围
//                if (GoodsAreaManger.getInstance().singleGoodsInArea(mProvinceId + "", mIntentData.supportArea)) {
//                    //可支付
//                    if (btn_red_packet_credit.isChecked()) {
//                        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
//                        if (userBean != null && userBean.getAddPayPasswordStatus() == 1) {  //设置过支付密码
//                            showCheckPayPwdDialog();
//                        } else {
//                            DialogUtil.showOperateDialog(PinOrderActivity.this,
//                                    getResources().getString(R.string.is_setting_pay_pwd),
//                                    getResources().getString(R.string.is_setting_pay_pwd_desc),
//                                    getResources().getString(R.string.dialog_set_paypwd_no),
//                                    getResources().getString(R.string.dialog_set_paypwd_yes), clickSureAction);
//                        }
//                    } else {
//                        createOrder("");
//                    }
//                } else {
//                    //提示不可支付
//                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("抱歉，当前地址没有可配送商品，请确认收货地址", "确定", "");
//                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
//                    dialogFgm.show(getSupportFragmentManager(), "all");
//                }
                break;
            default:
                break;
        }

    }

    DialogUtil.ClickSureAction clickSureAction = position -> startActivity(new Intent(this, ModifyPayPwdCheckPhoneActivity.class));

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.SELECT_ADDRESS_CALL_BACK) {
                AddressListDataBean.AddressItemBean addressItemBean = (AddressListDataBean.AddressItemBean) data.getSerializableExtra(IntentKey.ADDRESS_SELECT_BACK);
                bindAddressView(addressItemBean);
            } else if (requestCode == RequestCode.ADD_ADDRESS) {
                mPresenter.getDefaultAddress();
            }
        }
    }

    private PlayDialogUtils mPlayDialogUtils;

    public void showCheckPayPwdDialog() {
        mPlayDialogUtils = new PlayDialogUtils();
        mPlayDialogUtils.showCheckPayPwdDialog(this, new OnPlayPasswordLinsenr() {
            @Override
            public void onComrinPlayPaasord(@NotNull String pass) {
                createNewOrder(pass);
                //createOrder(pass);
            }
        });
//        View view = getLayoutInflater().inflate(R.layout.dialog_create_order_check_pay_pwd,
//                null);
//        dialog_check_pay_pwd = new Dialog(this, R.style.dialogPayPwdWindowStyle);
//        dialog_check_pay_pwd.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        Window window = dialog_check_pay_pwd.getWindow();
//        /**
//         * 位于底部
//         */
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(params);
//        /**
//         * 设置弹出动画
//         */
//        window.setWindowAnimations(R.style.ani_bottom_pay_pwd);
//        // 设置点击外围解散
//        dialog_check_pay_pwd.setCanceledOnTouchOutside(true);
//        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
//        VerificationCodeEditText dialog_ed_pay_pwd = view.findViewById(R.id.ed_pay_pwd);
//        dialog_ed_pay_pwd.requestFocus();
//        TextView tv_error_tip = view.findViewById(R.id.tv_error_tip);
//        iv_dialog_close.setOnClickListener(v -> {
//            ScreenUtils.hideKeyboard(dialog_ed_pay_pwd);
//            dialog_check_pay_pwd.dismiss();
//        });
//
//        dialog_ed_pay_pwd.setOnVerificationCodeChangedListener(new VerificationCodeEditText
//                .OnVerificationCodeChangedListener() {
//
//            @Override
//            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() > 0) {
//                    tv_error_tip.setText("");
//                }
//            }
//
//            @Overridef
//            public void onInputCompleted(CharSequence s) {
//                String pay_pwd = s.toString();
//
//                createOrder(pay_pwd);
//            }
//        });
//
//        dialog_check_pay_pwd.show();
    }

    /**
     * 发起订单确认请求
     *
     * @param pwd 红包支付密码
     */
    public void createOrder(@NonNull String pwd) {

        PinOrderReq req = new PinOrderReq();
        req.addressId = mAddressId + "";
        req.clientId = 3 + "";
        req.goodsId = mIntentData.goodsId + "";
        if (mIntentData.gruopId > 0) { //参团
            req.type = 2 + "";
            req.groupOrderId = mIntentData.gruopId + "";
        } else {
            req.type = 1 + "";
            req.groupOrderId = "0";
        }
        req.pointE2 = btn_red_packet_credit.isChecked() ? mCurrentUseRp + "" : "0";
        req.payPassword = RSAUtils.encryptByPublic(pwd);
        req.isNeedInvoice = isNeedInvoice + "";
        req.companyName = companyName == null ? "" : companyName;
        req.companyIdentifyNumber = companyIdentifyNumber == null ? "" : companyIdentifyNumber;
        req.mobile = mobile == null ? "" : mobile;
        req.email = eamil == null ? "" : eamil;
        req.invoiceLookedUp = invoiceLookedUp == null ? "" : invoiceLookedUp;
        req.remark = et_remark.getText().toString();
        req.desc = "";
        req.liveBatchId = mIntentData.liveBatchId == null ? "" : mIntentData.liveBatchId;
        mPresenter.createPinOrder(req);
    }

    public void createNewOrder(String pwd) {

        PinOrderNewReq req = new PinOrderNewReq();
        req.addressId = mAddressId + "";
        req.clientId = 3 + "";
        req.goodsId = mIntentData.goodsId + "";
        if (mIntentData.gruopId > 0) { //参团
            req.type = 2 + "";
            req.groupOrderId = mIntentData.gruopId + "";
        } else {
            req.type = 1 + "";
            req.groupOrderId = "0";
        }
        req.payPassword = RSAUtils.encryptByPublic(pwd);
        if (mReduceType == 1) {
            //使用代金券
            req.pointE2 = 0 + "";
            if (mVoucher != null) {
                req.couponReceiveId = mVoucher.couponReceiveId;
            }
        } else if (mReduceType == 2) {
            //使用红包
            req.pointE2 = Math.min(Math.min(mMaxRpCanUse, mTotalRp), mTotalPrice) + "";
            req.couponReceiveId = "";
        } else {
            //都不用
            req.pointE2 = 0 + "";
            req.couponReceiveId = "";
        }
        req.remark = et_remark.getText().toString();
        req.liveBatchId = mIntentData.liveBatchId == null ? "" : mIntentData.liveBatchId;
        req.inviteData = "";
        mPresenter.createNewPinOrder(req);
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
            if (mUserBean != null) {
                ed_taker_phone.setText(mUserBean.getMobile());
                ed_taker_phone.setSelection(ed_taker_phone.getText().length());
            }
            TextView tv_invoice_tip = view.findViewById(R.id.tv_invoice_tip);
            tv_invoice_tip.setOnClickListener(view13 -> DialogUtil.showInvoiceIntroduce(this));

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
                        tv_invoice_info.setText("个人发票");
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
                        tv_invoice_info.setText("个人发票");
                    }
                }
                dialog_invoice.dismiss();
            });

        }

        dialog_invoice.show();
    }

    /**
     * 修复红包抵扣金额对话框
     */

    public void showModifyRedPacketDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_modify_red_packet,
                null);
        Dialog dialog_modify_red_packet = new Dialog(this, R.style.dialogPayPwdWindowStyle);
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
        ImageView iv_dialog_jia = view.findViewById(R.id.iv_dialog_jia);
        ImageView iv_dialog_jian = view.findViewById(R.id.iv_dialog_jian);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        EditText dialog_ed_red_packet = view.findViewById(R.id.ed_red_packet);
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
                long min = mTotalRp > mMaxRpCanUse ? mMaxRpCanUse : mTotalRp;
                if (!StringUtils.isTextEmpty(s)) {
                    int red = (int) (Float.valueOf(s) * 100);
                    if (mTotalRp < 100) {
                        iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian_invalid);
                        iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia_invalid);
                        if (red > min) {
                            mCurrentUseRp = mTotalRp;
                            dialog_ed_red_packet.setText(StringUtils.getAllPrice(min));
                            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
                        } else {
                            mCurrentUseRp = red;
                        }
                    } else {
                        if (red < 100) {
                            iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian_invalid);
                        } else {
                            iv_dialog_jian.setImageResource(R.mipmap.icon_modify_red_packtet_jian);
                        }
                        if (mCurrentUseRp - red >= 100) {
                            iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia);
                        } else {
                            iv_dialog_jia.setImageResource(R.mipmap.icon_modify_red_packtet_jia_invalid);
                        }

                        if (red > min) {
                            mCurrentUseRp = mTotalRp;
                            dialog_ed_red_packet.setText(StringUtils.getAllPrice(min));
                            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
                        } else {
                            mCurrentUseRp = red;
                        }
                    }
                    if (red > mMaxRpCanUse) {
                        if (mMaxRpCanUse > mTotalRp) {
                            SCApp.getInstance().showSystemCenterToast("该商品最多只能抵用" + BigDecimalUtils.div(mTotalRp + "", "100", 2) + "元红包");

                        } else {
                            SCApp.getInstance().showSystemCenterToast("该商品最多只能抵用" + BigDecimalUtils.div(mMaxRpCanUse + "", "100", 2) + "元红包");

                        }
                    }
                } else {
                    mCurrentUseRp = 0;
                }
            }
        });
        iv_dialog_jia.setOnClickListener(view13 -> {
            mCurrentUseRp += 100;
            if (mCurrentUseRp > mTotalRp) {
                mCurrentUseRp -= 100;
            } else {
                dialog_ed_red_packet.setText(StringUtils.getAllPrice(mCurrentUseRp));
                dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
            }
        });
        iv_dialog_jian.setOnClickListener(view12 -> {
            if (mCurrentUseRp < 100) {
                return;
            }
            mCurrentUseRp -= 100;
            dialog_ed_red_packet.setText(StringUtils.getAllPrice(mCurrentUseRp));
            dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
        });
        dialog_ed_red_packet.setText(StringUtils.getAllPrice(mCurrentUseRp));
        dialog_ed_red_packet.setSelection(dialog_ed_red_packet.getText().length());
        iv_dialog_close.setOnClickListener(view1 -> {
            ScreenUtils.hideKeyboard(dialog_ed_red_packet);
            dialog_modify_red_packet.dismiss();
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_modify_red_packet.dismiss();
                if (mTotalPrice - mCurrentUseRp < 0) {
                    mCurrentUseRp = mTotalPrice;
                } else if (mTotalRp - mCurrentUseRp < 0) {
                    mCurrentUseRp = mTotalRp;
                }
                if (mCurrentUseRp > 0) {
                    btn_red_packet_credit.setChecked(true);
                } else {
                    btn_red_packet_credit.setChecked(false);
                }
                tv_deduct_red_packet.setText("抵¥" + StringUtils.getAllPrice(mCurrentUseRp) + "元");
                tv_red_price.setText("- ¥" + StringUtils.getAllPrice(mCurrentUseRp));
                mPayPrice = mTotalPrice - mCurrentUseRp;
                tv_pay_price.setText(PriceUtils.getInstance().getMainPrice(PinOrderActivity.this, (int) mPayPrice, 20));
            }
        });
        dialog_modify_red_packet.show();
    }

}
