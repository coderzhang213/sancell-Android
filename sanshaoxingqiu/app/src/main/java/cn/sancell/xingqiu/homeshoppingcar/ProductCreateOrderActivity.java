package cn.sancell.xingqiu.homeshoppingcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

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
import cn.sancell.xingqiu.homeshoppingcar.adapter.CreateOrderPackAdapter;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
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
import cn.sancell.xingqiu.viewGroup.entity.AreaSupportModel;
import cn.sancell.xingqiu.viewGroup.manager.GoodsAreaManger;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SwitchButton;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;
import cn.sancell.xingqiu.wxapi.WXPayEntryActivity;

/**
 * 购物车界面跳转到的商品生成订单界面
 */
public class ProductCreateOrderActivity extends BaseMVPToobarActivity<ProductCreateOrderContract.CreateOrderPresenter>
        implements ProductCreateOrderContract.CreateOrderView, View.OnClickListener {
    public final static String mPageName = "ProductCreateOrderActivity";
    @BindView(R.id.rcv_product_pack)
    RecyclerView rcv_product_pack;
    private CreateOrderPackAdapter createOrderPackAdapter;


    @BindView(R.id.tv_actual_price)
    RelativeSizeTextView tv_actual_price;
    @BindView(R.id.tv_gopay)
    TextView tv_gopay;
    @BindView(R.id.tv_no_goods_pay)
    AppCompatTextView tv_no_goods_pay;

    /**
     * 头部地址布局
     */
    RelativeLayout rl_noaddress;
    RelativeLayout rl_address;
    TextView tv_address_city;
    TextView tv_address_detail;
    TextView tv_address_name;
    TextView tv_address_phone;
    /**
     * 尾部布局
     */
    RelativeSizeTextView tv_totalprice;
    TextView tv_total_red_packet, tv_deduct_red_packet, tv_modify_red_packet;
    SwitchButton sb_red_packet_credit;
    TextView tv_red_price_tip, tv_red_price;
    RelativeLayout rl_red_packet_reward;
    ImageView iv_reward_price_introduce;
    RelativeSizeTextView tv_reward_price;
    RelativeLayout rl_invoice;
    TextView tv_invoice_info;

    //不支持配送区域
    NoSupportGoodsViewGroup mVgGoods;


    private int requestCode_addressSelect = 100;
    private int requestCode_addressAdd = 101;

    private String addressId; //地址id，判断是否选择了地址
    private String mProvinceId; //省份id，用于判断商品是否在配送范围

    /**
     * 上个界面传来的商品数组
     */
    List<HomeShoppingCarDataBean.ShopingCarProductBean> data_shoppingcar_product;
    /**
     * 上个界面传来的商品总价
     */
    int total_price, total_red_packet_reward;

    private long actual_price, deduct_red_packet;

    private UserBean userBean;

    private AreaSupportModel mData; //配送区域支持

    private CreateOrderDefaultPreBean mPreBean;


    @Override
    protected ProductCreateOrderContract.CreateOrderPresenter createPresenter() {
        return new ProductCreateOrderContract.CreateOrderPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_create_order;
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
        data_shoppingcar_product = (List<HomeShoppingCarDataBean.ShopingCarProductBean>) getIntent().getSerializableExtra(
                Constants.Key.KEY_1);


        total_price = getIntent().getIntExtra(Constants.Key.KEY_2, 0);
        total_red_packet_reward = getIntent().getIntExtra(Constants.Key.KEY_3, 0);
        tv_gopay.setOnClickListener(this);
        rcv_product_pack.setLayoutManager(new LinearLayoutManager(this));

        tv_actual_price.setStartText("¥");
        tv_actual_price.setStartProportion(0.75f);
        tv_actual_price.setEndProportion(0.75f);

        mPresenter.GetDefualtOrderPre(this);

    }

    /**
     * 计算商品区域问题
     */
    public void setAdapter() {
        if (createOrderPackAdapter == null) {
            createOrderPackAdapter = new CreateOrderPackAdapter(mData.vailList);
            createOrderPackAdapter.setListener(() -> SCApp.getInstance().showSystemCenterToast(R.string.remark_input_limit));
            createOrderPackAdapter.addHeaderView(getHeaderView());
            createOrderPackAdapter.addFooterView(getFooterView());
            rcv_product_pack.setAdapter(createOrderPackAdapter);
        } else {
            createOrderPackAdapter.setNewData(mData.vailList);
        }
    }

    public void changePrice(){
        tv_reward_price.setTagText("+ ¥" + StringUtils.getAllPrice(total_red_packet_reward));
        tv_totalprice.setTagText(StringUtils.getAllPrice(total_price));
    }


    /**
     * 设置商品adapter
     */
    public void setGoodsAdapter() {
        //获取商品配送区域的有效性
        mData = GoodsAreaManger.getInstance().getSortGoodsList(mProvinceId, data_shoppingcar_product);
        //计算金额
        int[] allPrice = GoodsAreaManger.getInstance().getTotalPrice(mData);
        total_price = allPrice[0];
        total_red_packet_reward = allPrice[1];

        setAdapter();
        //设置失效商品
        if (mData.inVailList != null && mData.inVailList.size() > 0) {
            mVgGoods.setVisibility(View.VISIBLE);
            if (mData.inVailList.size() == mData.totalCount) {
                //全部失效
                setPayBtnEnable(1, "当前订单没有可支付商品");
                mVgGoods.setData(mData.inVailList);
                setVgNoGoodsMargin(true);
            } else {
                //部分失效
                tv_no_goods_pay.setVisibility(View.GONE);
                setPayBtnEnable(0, "");
                mVgGoods.setData(mData.inVailList);
                setVgNoGoodsMargin(false);
            }

        } else {
            //是否存在地址
            if (TextUtils.isEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                //不存在地址的情况，全部可配送，部分可配送，没配置地址点击会提示
                setPayBtnEnable(1, "暂无收货地址");
                setVgNoGoodsMargin(false);
            } else {
                mVgGoods.setVisibility(View.GONE);
                setPayBtnEnable(0, "");
                setVgNoGoodsMargin(false);
            }
        }
        changePrice();
        setRp();

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
                mPresenter.GetDefualtOrderPre(ProductCreateOrderActivity.this);
            }
        });
    }


    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_create_order_head, (ViewGroup) rcv_product_pack.getParent(), false);
        rl_noaddress = view.findViewById(R.id.rl_noaddress);
        rl_address = view.findViewById(R.id.rl_address);
        tv_address_city = view.findViewById(R.id.tv_address_city);
        tv_address_detail = view.findViewById(R.id.tv_address_detail);
        tv_address_name = view.findViewById(R.id.tv_address_name);
        tv_address_phone = view.findViewById(R.id.tv_address_phone);
        rl_noaddress.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        return view;
    }

    private View getFooterView() {
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        View view = getLayoutInflater().inflate(R.layout.layout_create_order_foot, (ViewGroup) rcv_product_pack.getParent(), false);
        tv_totalprice = view.findViewById(R.id.tv_totalprice);

        tv_total_red_packet = view.findViewById(R.id.tv_total_red_packet);
        tv_deduct_red_packet = view.findViewById(R.id.tv_deduct_red_packet);
        tv_modify_red_packet = view.findViewById(R.id.tv_modify_red_packet);
        sb_red_packet_credit = view.findViewById(R.id.sb_red_packet_credit);
        tv_red_price_tip = view.findViewById(R.id.tv_red_price_tip);
        tv_red_price = view.findViewById(R.id.tv_red_price);
        tv_reward_price = view.findViewById(R.id.tv_reward_price);
        iv_reward_price_introduce = view.findViewById(R.id.iv_reward_price_introduce);
        mVgGoods = view.findViewById(R.id.vg_goods);
        rl_red_packet_reward = view.findViewById(R.id.rl_red_packet_reward);
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
                tv_actual_price.setTagText(StringUtils.getPrice(total_price));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(total_price));
                tv_modify_red_packet.setVisibility(View.GONE);
            }
        });
        iv_reward_price_introduce.setOnClickListener(this);
        tv_modify_red_packet.setOnClickListener(this);

        rl_invoice = view.findViewById(R.id.rl_invoice);
        tv_invoice_info = view.findViewById(R.id.tv_invoice_info);
        rl_invoice.setOnClickListener(this);
        return view;
    }


    public void setRp(){

        if (mPreBean != null || userBean != null) {
            if (mPreBean != null) {
                total_red_packet = mPreBean.getPointE2();
                if (total_price > total_red_packet) {
                    current_red_packet = mPreBean.getPointE2();
                } else {
                    current_red_packet = total_price;
                }
            } else {
                total_red_packet = userBean.getPointE2();
                if (total_price > total_red_packet) {
                    current_red_packet = userBean.getPointE2();
                } else {
                    current_red_packet = total_price;
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
                tv_actual_price.setTagText(StringUtils.getPrice(total_price - current_red_packet));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(total_price - current_red_packet));
                actual_price = total_price - current_red_packet;
            } else {
                sb_red_packet_credit.setChecked(false);
                sb_red_packet_credit.setEnabled(false);
                tv_deduct_red_packet.setText("无可抵");
                deduct_red_packet = 0;
                tv_red_price_tip.setVisibility(View.GONE);
                tv_red_price.setVisibility(View.GONE);
                tv_actual_price.setTagText(StringUtils.getPrice(total_price));
                tv_actual_price.setEndText(StringUtils.getPriceDecimal(total_price));
                actual_price = total_price;
            }

        }
    }

    @Override
    public void getDefaultOrderPreSuccess(CreateOrderDefaultPreBean createOrderDefaultPreBean) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        mPreBean = createOrderDefaultPreBean;
        if (createOrderDefaultPreBean != null) {
            addressId = createOrderDefaultPreBean.getAddressInfo() != null ? createOrderDefaultPreBean.getAddressInfo().getId() + "" : "";
            mProvinceId = createOrderDefaultPreBean.getAddressInfo() != null ? createOrderDefaultPreBean.getAddressInfo().getProvinceId() + "" : "";
            //计算区域问题
            setGoodsAdapter();
            if (createOrderDefaultPreBean.getAddressInfo() != null && !StringUtils.isTextEmpty(createOrderDefaultPreBean.getAddressInfo().getProvinceName())) {
                rl_noaddress.setVisibility(View.GONE);
                rl_address.setVisibility(View.VISIBLE);
                tv_address_city.setText(createOrderDefaultPreBean.getAddressInfo().getProvinceName() + createOrderDefaultPreBean.getAddressInfo().getCityName() + createOrderDefaultPreBean.getAddressInfo().getAreasName() + createOrderDefaultPreBean.getAddressInfo().getStreetName());
                tv_address_detail.setText(createOrderDefaultPreBean.getAddressInfo().getAddress());
                tv_address_name.setText(createOrderDefaultPreBean.getAddressInfo().getConsignee());
                tv_address_phone.setText(createOrderDefaultPreBean.getAddressInfo().getMobile());
            } else {

                rl_noaddress.setVisibility(View.VISIBLE);
                rl_address.setVisibility(View.GONE);
            }

        } else {
            setGoodsAdapter();
            rl_noaddress.setVisibility(View.VISIBLE);
            rl_address.setVisibility(View.GONE);
        }

    }

    @Override
    public void createOrderSuccess(String orderId, String orderParcelId) {
        if (sb_red_packet_credit.isChecked() && actual_price == 0) {
            PreferencesUtils.put(Constants.Key.KEY_orderId, orderId);
            PreferencesUtils.put(Constants.Key.KEY_orderTag, mPageName);
            Intent intent = new Intent(ProductCreateOrderActivity.this, WXPayEntryActivity.class);
            intent.putExtra(Constants.Key.KEY_1, false);
            intent.putExtra(Constants.Key.KEY_2, false);
            startActivity(intent);
        } else {
            if (sb_red_packet_credit.isChecked()) {
                Intent intent = new Intent(ProductCreateOrderActivity.this, ProductPaymentActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                intent.putExtra(Constants.Key.KEY_2, (int) actual_price);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ProductCreateOrderActivity.this, ProductPaymentActivity.class);
                intent.putExtra(Constants.Key.KEY_1, orderId);
                intent.putExtra(Constants.Key.KEY_2, total_price);
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

    }

    @Override
    public void getGoodsAreaSuccess(List<String> res) {
        //空实现
    }

    @Override
    public void getGoodsSupportAreaError(String error) {
        //空实现
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address:

                SelectAddressActivity.start(this, RequestCode.SELECT_ADDRESS_CALL_BACK);

                break;
            case R.id.rl_noaddress:
                Intent intent = new Intent(ProductCreateOrderActivity.this, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                intent.putExtra(Constants.Key.KEY_2, true);
                intent.putExtra(Constants.Key.KEY_4, false);
                startActivityForResult(intent, requestCode_addressAdd);
                break;
            case R.id.tv_gopay:
                //选择地址
                if (StringUtils.isTextEmpty(addressId) || TextUtils.equals(addressId, "0")) {
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("请填写收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "address");
                    return;
                }
                //判断状态
                if (mData.vailList.size() == mData.totalCount) { //全部可配送
                    toPay();
                } else if (mData.vailList.size() > 0) {//部分可配送
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("抱歉，部分商品无法配送，是否继续支付", "继续支付", "取消");
                    dialogFgm.setOnDialogListener(this::toPay);
                    dialogFgm.show(getSupportFragmentManager(), "part");

                } else {//不可配送
                    OrderTipDialogFgm dialogFgm = OrderTipDialogFgm.newInstance("抱歉，当前地址没有可配送商品，请确认收货地址", "确定", "");
                    dialogFgm.setOnDialogListener(dialogFgm::dismiss);
                    dialogFgm.show(getSupportFragmentManager(), "all");
                }

                break;
            case R.id.iv_reward_price_introduce:  //购物送红包说明
                DialogUtil.showRedPacket(ProductCreateOrderActivity.this);
                break;
            case R.id.tv_modify_red_packet:
                showModifyRedPacketDialog();
                break;
            case R.id.rl_invoice:  //发票
                showSelectInvoiceDialog();
                break;
        }
    }

    public void toPay() {

        JSONObject remark = new JSONObject();
        Map<String, EditText> map = createOrderPackAdapter.getHangYeEditTextMap();
        for (String key : map.keySet()) {
            try {
                remark.put(key, map.get(key).getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (sb_red_packet_credit.isChecked()) {
            UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
            if (userBean != null && userBean.getAddPayPasswordStatus() == 1) {  //设置过支付密码
                showCheckPayPwdDialog();
            } else {
                DialogUtil.showOperateDialog(ProductCreateOrderActivity.this,
                        getResources().getString(R.string.is_setting_pay_pwd),
                        getResources().getString(R.string.is_setting_pay_pwd_desc),
                        getResources().getString(R.string.dialog_set_paypwd_no),
                        getResources().getString(R.string.dialog_set_paypwd_yes), clickSureAction);
            }
        } else {
            String carIdString = "";
            for (int i = 0; i < mData.vailList.size(); i++) {
                if (i == mData.vailList.size() - 1) {
                    carIdString += mData.vailList.get(i).getCarId();
                } else {
                    carIdString += mData.vailList.get(i).getCarId() + "-";
                }
            }

            mPresenter.CreateOrder(addressId, carIdString, "", "",
                    isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber,
                    mobile, eamil, remark, ProductCreateOrderActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode_addressAdd) {
                mPresenter.GetDefualtOrderPre(ProductCreateOrderActivity.this);
            } else if (requestCode == RequestCode.SELECT_ADDRESS_CALL_BACK) {
                AddressListDataBean.AddressItemBean addressItemBean = (AddressListDataBean.AddressItemBean) data.getSerializableExtra(IntentKey.ADDRESS_SELECT_BACK);
                addressId = addressItemBean.getId() + "";
                mProvinceId = addressItemBean.getProvinceId() + "";
                tv_address_city.setText(addressItemBean.getProvinceName() + addressItemBean.getCityName() + addressItemBean.getAreasName() + addressItemBean.getStreetName());
                tv_address_detail.setText(addressItemBean.getAddress());
                tv_address_name.setText(addressItemBean.getConsignee());
                tv_address_phone.setText(addressItemBean.getMobile());
                setGoodsAdapter();
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
                StringBuilder carIdString = new StringBuilder();
                for (int i = 0; i < mData.vailList.size(); i++) {
                    if (i == mData.vailList.size() - 1) {
                        carIdString.append(mData.vailList.get(i).getCarId());
                    } else {
                        carIdString.append(mData.vailList.get(i).getCarId()).append("-");
                    }
                }
                JSONObject remark = new JSONObject();
                Map<String, EditText> map = createOrderPackAdapter.getHangYeEditTextMap();
                for (String key : map.keySet()) {
                    try {
                        remark.put(key, map.get(key).getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mPresenter.CreateOrder(addressId, carIdString.toString(), deduct_red_packet + "", pay_pwd,
                        isNeedInvoice + "", invoiceLookedUp, companyName, companyIdentifyNumber,
                        mobile, eamil, remark, ProductCreateOrderActivity.this);

            }
        });

        dialog_check_pay_pwd.show();
    }

    DialogUtil.ClickSureAction clickSureAction = postion -> startActivity(new Intent(ProductCreateOrderActivity.this, ModifyPayPwdCheckPhoneActivity.class));

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
        tv_sure.setOnClickListener(view14 -> {
            dialog_modify_red_packet.dismiss();
            if (total_price - current_red_packet < 0) {
                current_red_packet = total_price;
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
            actual_price = total_price - current_red_packet;
            tv_actual_price.setTagText(StringUtils.getPrice(actual_price));
            tv_actual_price.setEndText(StringUtils.getPriceDecimal(actual_price));
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
            tv_invoice_tip.setOnClickListener(view13 -> DialogUtil.showInvoiceIntroduce(ProductCreateOrderActivity.this));

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

    /**
     * 设置底部按钮状态
     *
     * @param states 0 可支付，1不可支付,
     */
    public void setPayBtnEnable(int states, String str) {
        if (states == 0) {
            tv_no_goods_pay.setVisibility(View.GONE);
        } else {
            tv_no_goods_pay.setText(str);
            tv_no_goods_pay.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置margin top，商品无可配送时top间距过大
     *
     * @param restMargin
     */
    public void setVgNoGoodsMargin(boolean restMargin) {
        if (restMargin) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVgGoods.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVgGoods.getLayoutParams();
            lp.setMargins(0, ScreenUtil.dip2px(12), 0, 0);
        }
    }

}
