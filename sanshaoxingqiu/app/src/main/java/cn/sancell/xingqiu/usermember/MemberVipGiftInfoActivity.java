package cn.sancell.xingqiu.usermember;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoPicsListAdapter;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.usermember.contract.MemberVipGiftInfoContract;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

public class MemberVipGiftInfoActivity extends BaseMVPToobarActivity<MemberVipGiftInfoContract.MemberVipGiftInfoPresenter>
        implements MemberVipGiftInfoContract.MemberVipGiftInfoView, View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_product_infopics)
    RecyclerView rcv_product_infopics;

    @BindView(R.id.cb_isagree)
    CheckBox cb_isagree;
    @BindView(R.id.btn_member_protocol)
    TextView btn_member_protocol;
    @BindView(R.id.btn_point_privacy)
    TextView btn_point_privacy;
    @BindView(R.id.iv_buy_pic)
    ImageView iv_buy_pic;
    @BindView(R.id.tv_bottom_price)
    RelativeSizeTextView tv_bottom_price;

    /**
     * 待抢购和无库存状态
     */
    @BindView(R.id.rl_other_status)
    RelativeLayout rl_other_status;
    @BindView(R.id.tv_no_stock)
    TextView tv_no_stock;
    @BindView(R.id.tv_buy_time)
    TextView tv_buy_time;
    @BindView(R.id.tv_buy_time_tip)
    TextView tv_buy_time_tip;
    @BindView(R.id.tv_limit_time_bottom_price)
    RelativeSizeTextView tv_limit_time_bottom_price;

    private String product_id;

    ProductInfoDataBean productInfoDataBean;

    @Override
    protected MemberVipGiftInfoContract.MemberVipGiftInfoPresenter createPresenter() {
        return new MemberVipGiftInfoContract.MemberVipGiftInfoPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member_vip_gift_info;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("");
        product_id = getIntent().getStringExtra(Constants.Key.KEY_1);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        refreshLayout.setRefreshHeader(new SancellClassicsHeader(this));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.GetGiftInfo(product_id, MemberVipGiftInfoActivity.this));

        rcv_product_infopics.setLayoutManager(new LinearLayoutManager(this));
        iv_buy_pic.setOnClickListener(this);
        btn_member_protocol.setOnClickListener(this);
        btn_point_privacy.setOnClickListener(this);
        rl_other_status.setOnClickListener(this);
        mPresenter.GetGiftInfo(product_id, this);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    /*private SimpleDraweeView sdv_index_pic;
    private TextView tv_title, tv_desc, tv_price;*/

    /*private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_member_giftinfo_top, (ViewGroup) rcv_product_infopics.getParent(), false);
        sdv_index_pic = view.findViewById(R.id.sdv_index_pic);
        tv_title = view.findViewById(R.id.tv_title);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_price = view.findViewById(R.id.tv_price);
        return view;
    }*/

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
                mPresenter.GetGiftInfo(product_id, MemberVipGiftInfoActivity.this);
            }
        });
    }


    private ProductInfoPicsListAdapter productInfoPicsListAdapter;

    @Override
    public void getGiftInfoSuccess(ProductInfoDataBean productInfoDataBean) {
        refreshLayout.finishRefresh();
        initActivityTitle(productInfoDataBean.getTitle());
        this.productInfoDataBean = productInfoDataBean;
        productInfoPicsListAdapter = new ProductInfoPicsListAdapter(productInfoDataBean.getDetailPicArr(), MemberVipGiftInfoActivity.this);
        //productInfoPicsListAdapter.addHeaderView(getHeaderView());
        rcv_product_infopics.setAdapter(productInfoPicsListAdapter);
        /*if (productInfoDataBean.getBannerPicArr() != null) {
            sdv_index_pic.setImageURI(Uri.parse(productInfoDataBean.getBannerPicArr().get(0)));
        }
        tv_title.setText(productInfoDataBean.getTitle());
        tv_desc.setText(productInfoDataBean.getDesc());
        tv_price.setText("市场价 ¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2()));*/
        if (productInfoDataBean.getSeckillGoodsStatus() == 1) {  //礼包秒杀中
            tv_bottom_price.setVisibility(View.GONE);
            iv_buy_pic.setVisibility(View.GONE);
            if (productInfoDataBean.getSeckillStockNum() > 0) {
                tv_no_stock.setVisibility(View.GONE);
                rl_other_status.setVisibility(View.VISIBLE);
                tv_buy_time.setText("购买礼包");
                tv_buy_time_tip.setText("赠送一年红猩VIP资格");
                tv_limit_time_bottom_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
            } else {
                tv_no_stock.setVisibility(View.VISIBLE);
                rl_other_status.setVisibility(View.GONE);
            }
        } else if (productInfoDataBean.getSeckillGoodsStatus() == 2) {  //待秒杀
            tv_bottom_price.setVisibility(View.GONE);
            iv_buy_pic.setVisibility(View.GONE);
            tv_no_stock.setVisibility(View.GONE);
            rl_other_status.setVisibility(View.VISIBLE);
            tv_buy_time.setText(productInfoDataBean.getSeckillGoodsStatusStr());
            tv_buy_time_tip.setText("敬请期待");
            tv_limit_time_bottom_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
        } else {
            tv_no_stock.setVisibility(View.GONE);
            rl_other_status.setVisibility(View.GONE);
            tv_bottom_price.setVisibility(View.VISIBLE);
            iv_buy_pic.setVisibility(View.VISIBLE);
                /*tv_bottom_price.setStartProportion(0.67f);
                tv_bottom_price.setStartText("¥");*/
            tv_bottom_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
            switch (productInfoDataBean.getGoodsLevel()) {
                case 2:
                    iv_buy_pic.setImageResource(R.mipmap.icon_gift_info_buy_bg1);
                    tv_bottom_price.setTextColor(getResources().getColor(R.color.color_text1));
                    tv_bottom_price.setStartTextColor(getResources().getColor(R.color.color_text1));
                    break;
                case 3:
                    iv_buy_pic.setImageResource(R.mipmap.icon_gift_info_buy_bg2);
                    tv_bottom_price.setTextColor(getResources().getColor(R.color.color_text1));
                    tv_bottom_price.setStartTextColor(getResources().getColor(R.color.color_text1));
                    break;
                case 4:
                    iv_buy_pic.setImageResource(R.mipmap.icon_gift_info_buy_bg3);
                    tv_bottom_price.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_bottom_price.setStartTextColor(getResources().getColor(R.color.colorWhite));
                    break;
            }

        }
    }

    String agreement = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_other_status:
                if (productInfoDataBean.getSeckillGoodsStatus() == 2) {  //待秒杀中
                    return;
                }
                if (PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getRealMemberLevel() > 1) {
                    SCApp.getInstance().showSystemCenterToast("您已经购买过会员礼包");
                    return;
                }
                if (!cb_isagree.isChecked()) {
                    SCApp.getInstance().showSystemCenterToast("请先阅读并勾选协议");
                    return;
                }
                Intent intent1 = new Intent(MemberVipGiftInfoActivity.this, MemberGiftCreateOrderActivity.class);
                intent1.putExtra(Constants.Key.KEY_1, product_id);
                intent1.putExtra(Constants.Key.KEY_2, productInfoDataBean.getTitle());
                intent1.putExtra(Constants.Key.KEY_3, productInfoDataBean.getSpecification());
                intent1.putExtra(Constants.Key.KEY_4, productInfoDataBean.getCoverPic());
                intent1.putExtra(Constants.Key.KEY_5, productInfoDataBean.getUserRealPriceE2());
                intent1.putExtra(Constants.Key.KEY_6, productInfoDataBean.getGoodsLevel());
                startActivity(intent1);
                break;
            case R.id.iv_buy_pic:
                if (PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getRealMemberLevel() > 1) {
                    SCApp.getInstance().showSystemCenterToast("您已经购买过会员礼包");
                    return;
                }
                if (productInfoDataBean.getStockNumber() <= 0) {
                    return;
                }
                if (!cb_isagree.isChecked()) {
                    SCApp.getInstance().showSystemCenterToast("请先阅读并勾选协议");
                    return;
                }
                Intent intent = new Intent(MemberVipGiftInfoActivity.this, MemberGiftCreateOrderActivity.class);
                intent.putExtra(Constants.Key.KEY_1, product_id);
                intent.putExtra(Constants.Key.KEY_2, productInfoDataBean.getTitle());
                intent.putExtra(Constants.Key.KEY_3, productInfoDataBean.getSpecification());
                intent.putExtra(Constants.Key.KEY_4, productInfoDataBean.getCoverPic());
                intent.putExtra(Constants.Key.KEY_5, productInfoDataBean.getUserRealPriceE2());
                intent.putExtra(Constants.Key.KEY_6, productInfoDataBean.getGoodsLevel());
                startActivity(intent);
                break;
            case R.id.btn_member_protocol:
                if (StringUtils.isTextEmpty(agreement)) {
                    List<StartupDataBean.MemberAgreementUrlData> memberAgreementUrlData = PreferencesUtils.readMemberAgreementListObject();
                    if (memberAgreementUrlData != null) {
                        for (StartupDataBean.MemberAgreementUrlData temp : memberAgreementUrlData
                                ) {
                            if (temp.getMemberLevelId() == productInfoDataBean.getGoodsLevel()) {
                                agreement = temp.getMemberVipAgreementUrl();
                                break;
                            }
                        }
                    }
                }
                if (StringUtils.isTextEmpty(agreement)) {
                    return;
                }
                Intent intent11 = new Intent(MemberVipGiftInfoActivity.this, UrlInfoActivity.class);
                intent11.putExtra(Constants.Key.KEY_1, agreement);
                intent11.putExtra(Constants.Key.KEY_2, "");
                startActivity(intent11);
                break;
            case R.id.btn_point_privacy:
                Intent intent2 = new Intent(MemberVipGiftInfoActivity.this, UrlInfoActivity.class);
                intent2.putExtra(Constants.Key.KEY_1, PreferencesUtils.getString(Constants.Key.KEY_integralRightsUrl, ""));
                intent2.putExtra(Constants.Key.KEY_2, "红包使用协议");
                startActivity(intent2);
                break;
        }
    }
}
