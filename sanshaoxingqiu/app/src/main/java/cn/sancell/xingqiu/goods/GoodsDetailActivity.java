package cn.sancell.xingqiu.goods;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.common.media.imagepicker.camera.ConfirmationDialog;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.goods.fragment.comment.GoodsCommentFgm;
import cn.sancell.xingqiu.goods.fragment.info.GoodsInfoFragment;
import cn.sancell.xingqiu.goods.fragment.listener.OnGoodsInfoListener;
import cn.sancell.xingqiu.goods.fragment.listener.UmShareListener;
import cn.sancell.xingqiu.goods.fragment.params.GoodsParamsFgm;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter;
import cn.sancell.xingqiu.order.pin.PinOrderActivity;
import cn.sancell.xingqiu.order.entity.PinIntentBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 商品详情
 * @time 2019-12-25 10:00
 */
public class GoodsDetailActivity extends BaseMVPActivity<GoodsDetailPresenter> implements GoodsDetailView, View.OnClickListener, OnGoodsInfoListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    AppCompatImageView btn_back;
    @BindView(R.id.btn_share)
    AppCompatImageView btn_share;
    @BindView(R.id.vp_goods)
    ViewPager vp_goods;
    @BindView(R.id.tv_purchase_limit)
    AppCompatTextView tv_purchase_limit;
    @BindView(R.id.tv_service)
    AppCompatTextView tv_service;
    @BindView(R.id.tv_cart)
    AppCompatTextView tv_cart;
    @BindView(R.id.tv_purchase)
    AppCompatTextView tv_purchase;
    @BindView(R.id.ll_guide)
    LinearLayout ll_guide;
    @BindView(R.id.v_bar)
    View v_bar;
    @BindView(R.id.tv_guide_product)
    AppCompatTextView tv_guide_product;
    @BindView(R.id.tv_guide_parameter)
    AppCompatTextView tv_guide_parameter;
    @BindView(R.id.tv_guide_evaluate)
    AppCompatTextView tv_guide_evaluate;


    private List<Fragment> mFragments; //管理的fragment
    private ProductInfoDataBean mData; //商品数据

    private int mGoodsId;
    private String mRoomId;
    private float mFirstAlpha; //记录首页滑动的alpha
    private int totalHeight;
    private AddressListDataBean.AddressItemBean mAddress;


    /**
     * @param context context
     * @param goodsId 商品id
     */
    public static void start(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(IntentKey.GOODS_ID, goodsId);
        context.startActivity(intent);
    }

    /**
     * @param context context
     * @param goodsId 商品id
     * @param roomId  直播房间号
     */
    public static void start(Context context, int goodsId, String roomId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(IntentKey.GOODS_ID, goodsId);
        intent.putExtra(IntentKey.ROOM_ID, roomId);
        context.startActivity(intent);
    }


    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void initial() {
        mGoodsId = getIntent().getIntExtra(IntentKey.GOODS_ID, 0);
        mRoomId = getIntent().getStringExtra(IntentKey.ROOM_ID);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            v_bar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(this)));
        }
        initListener();
        initPageView();
    }

    public void initListener() {
        btn_back.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        tv_service.setOnClickListener(this);
        tv_cart.setOnClickListener(this);
        tv_purchase.setOnClickListener(this);
        tv_guide_product.setOnClickListener(this);
        tv_guide_evaluate.setOnClickListener(this);
        tv_guide_parameter.setOnClickListener(this);
    }


    public void initPageView() {

        rl_top.post(() -> totalHeight += rl_top.getHeight());

        v_bar.post(() -> totalHeight += v_bar.getHeight());

        mFragments = new ArrayList<>();
        //商品
        GoodsInfoFragment goodFragment = GoodsInfoFragment.newInstance(mGoodsId + "", mRoomId);
        goodFragment.setOnGoodsInfoListener(this);
        //   CopyGoodsInfoFragment goodFragment = CopyGoodsInfoFragment.newInstance(mGoodsId+"",mRoomId);
        mFragments.add(goodFragment);
        Log.i("fragment_add", "goodFragment");
        //参数
        mFragments.add(GoodsParamsFgm.newInstance());
        Log.i("fragment_add", "GoodsParamsFgm");
        //评论
        mFragments.add(GoodsCommentFgm.newInstance(mGoodsId + ""));
        Log.i("fragment_add", "GoodsCommentFgm");
        vp_goods.setOffscreenPageLimit(mFragments.size());
        NormalPageAdapter mAdapter = new NormalPageAdapter(getSupportFragmentManager(), mFragments, null);
        vp_goods.setAdapter(mAdapter);
        Log.i("fragment_setAdapter", "NormalPageAdapter");
        vp_goods.addOnPageChangeListener(this);

        //  vp_goods.setCurrentItem(0);

        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_text1_1);

    }


    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_purchase:
                if (mData != null) {
                    PinIntentBean bean = new PinIntentBean();
                    bean.good_pic = mData.getCoverPic();
                    bean.goodsId = mData.getId();
                    bean.good_spec = mData.getSpecification();
                    bean.good_title = mData.getTitle();
                    bean.total_price = mData.grouponInfo.grouponPriceE2;
                    bean.pinId = mData.grouponInfo.groupOrderId;
                    bean.supportArea = mData.getGoodsRegion().getProvinceIdList();
                    bean.mAddress = mAddress;
                    bean.buyNum = mData.getMinBuyNum();
                    bean.liveBatchId = mRoomId;
                    bean.mMaxRpCanUse = mData.astrictPointE2;
                    bean.goodsFlag = mData.getGoodsFlag();
                    PinOrderActivity.start(this, bean);
                }

                break;
            case R.id.tv_service:
                showCallPhone();
                break;
            case R.id.tv_cart:
                // do nothing
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                shareGoods();
                break;
            case R.id.tv_guide_parameter:
                vp_goods.setCurrentItem(1);
                break;
            case R.id.tv_guide_evaluate:
                vp_goods.setCurrentItem(2);
                break;
            case R.id.tv_guide_product:
                vp_goods.setCurrentItem(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollChange(float alpha) {
        //滑动切换颜色
        mFirstAlpha = alpha;
        setTopAlpha(alpha);
    }

    public void setTopAlpha(float alpha) {
        ll_guide.setAlpha(alpha);
        v_bar.setAlpha(alpha);
        float bgAlpha = alpha * 255;
        rl_top.setBackgroundColor(Color.argb((int) bgAlpha, 255, 255, 255));

    }

    @Override
    public void showShareBtn(boolean canShare) {
        btn_share.setVisibility(canShare ? View.VISIBLE : View.GONE);
    }

    @Override
    public void getGoodsInfo(ProductInfoDataBean data) {
        mData = data;
        //设置参数fgm，等待视图的绘制完成才会有totalHeight
        new Handler().postDelayed(() -> {
            if (mFragments != null) {
                for (Fragment fgm : mFragments) {
                    if (fgm instanceof GoodsParamsFgm) {
                        ((GoodsParamsFgm) fgm).setData(data.getAttrInfo().getDataList());
                        ((GoodsParamsFgm) fgm).setMarginHeight(totalHeight);
                    } else if (fgm instanceof GoodsCommentFgm) {
                        ((GoodsCommentFgm) fgm).setMarginHeight(totalHeight);
                    }
                }
            }
        }, 100);
        setBtnPay();
    }

    /**
     * 设置拼团购物状态
     */
    public void setBtnPay() {

//        if (mData.hasGroupon == 1 && mData.grouponInfo != null) {
//            if (mData.grouponInfo.grouponBuyPageStatus == 1) {
//                //正常拼团
//                tv_purchase.setEnabled(true);
//                tv_purchase.setText("发起直拼");
//
//            } else if (mData.grouponInfo.grouponBuyPageStatus == 2) {
//                //不能开团（开团人数已达上限）
//                tv_purchase.setEnabled(false);
//                tv_purchase.setText("发起名额已达上限");
//
//            } else {
//                //售罄（敬请期待下场团购）
//                tv_purchase.setEnabled(false);
//                tv_purchase.setText("已售罄，敬请期待下一场直拼");
//            }
//        }else{
//            tv_purchase.setEnabled(true);
//            tv_purchase.setText("立即购买");
//        }
        tv_purchase.setEnabled(true);
        tv_purchase.setText("立即购买");

    }

    @Override
    public void showCartTip(String str) {
        if (TextUtils.isEmpty(str)) {
            tv_purchase_limit.setVisibility(View.GONE);
            //购物车状态
            tv_purchase.setTextColor(getResources().getColor(R.color.white));
            tv_purchase.setClickable(true);
            tv_purchase.setEnabled(true);

        } else {
            tv_purchase_limit.setVisibility(View.VISIBLE);
            tv_purchase_limit.setText(str);
            tv_purchase.setEnabled(true);
            tv_purchase.setClickable(false);
            tv_purchase.setTextColor(getResources().getColor(R.color.white_60));
        }
        //切换地址会更新按钮状态
        setBtnPay();

    }

    @Override
    public void updateAddress(AddressListDataBean.AddressItemBean item) {
        mAddress = item;
    }

    @Override
    public void callHospital(String phone) {
        callPhone(phone);
    }

    /**
     * 分享商品
     */
    public void shareGoods() {
        if (mData != null && mData.getGoodsDetailShare() != null) {
            String title = mData.getGoodsDetailShare().getTitle();
            String description = mData.getGoodsDetailShare().getDesc();
            String linkUrl = mData.getGoodsDetailShare().getLink();
            UMImage image = new UMImage(this, mData.getGoodsDetailShare().getLogoUrl());
            DialogUtil.getShareDialog(this, image, linkUrl, title, description, new UmShareListener());
        }
    }

    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(this, mGoodsId + "", "");
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, position == 0 ? R.drawable.round_text1_1 : R.drawable.round_trans_1);
        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, position == 1 ? R.drawable.round_text1_1 : R.drawable.round_trans_1);
        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, position == 2 ? R.drawable.round_text1_1 : R.drawable.round_trans_1);
        setTopAlpha(position > 0 ? 1 : mFirstAlpha);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
