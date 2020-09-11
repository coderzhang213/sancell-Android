package cn.sancell.xingqiu.homeclassify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.common.ToastHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.ProducAddressInfo;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.AddressInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.dialog.SelectAddressDialog;
import cn.sancell.xingqiu.homeclassify.adapter.ProductEvaluateListAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoBannerAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoParameterListAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoPicsListAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoSloganAdapter;
import cn.sancell.xingqiu.homeclassify.adapter.ProductViewAdapter;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeclassify.contract.ProductInfoContract;
import cn.sancell.xingqiu.homeclassify.help.ProductAddressHelp;
import cn.sancell.xingqiu.homepage.ImagePagerActivity;
import cn.sancell.xingqiu.homepage.SancelIntroduceActivity;
import cn.sancell.xingqiu.homeshoppingcar.ShoppingCarActivity;
import cn.sancell.xingqiu.homeshoppingcar.SingleProductCreateOrderActivity;
import cn.sancell.xingqiu.homeshoppingcar.bean.SingleOrderIntent;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.CountDownView;
import cn.sancell.xingqiu.widget.FlowLayout;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RecyclerViewPageChangeListenerHelper;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

public class ProductInfoActivity extends BaseMVPActivity<ProductInfoContract.ProductInfoPresenter>
        implements ProductInfoContract.ProductInfoView, View.OnClickListener, SelectAddressDialog.OnAddressSelectLinsener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    /* @BindView(R.id.toolbar)
     Toolbar toolbar;*/
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_share)
    ImageView btn_share;
    @BindView(R.id.iv_share_tip_gif)
    ImageView iv_share_tip_gif;
    @BindView(R.id.iv_one_yuan_share_tip_gif)
    ImageView iv_one_yuan_share_tip_gif;
    @BindView(R.id.ll_guide)
    LinearLayout ll_guide;
    @BindView(R.id.tv_guide_product)
    TextView tv_guide_product;
    @BindView(R.id.tv_guide_parameter)
    TextView tv_guide_parameter;
    @BindView(R.id.tv_guide_evaluate)
    TextView tv_guide_evaluate;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    private List<View> viewList = new ArrayList<View>();
    private ProductViewAdapter viewAdapter;
    private LayoutInflater inflater;
    //商品信息
    private View view_product;
    //商品规格参数
    private View view_parameter;
    //商品评价
    private View view_evaluate;

    @BindView(R.id.rl_available_purchase)
    RelativeLayout rl_available_purchase;
    @BindView(R.id.ll_contact)
    LinearLayout ll_contact;
    @BindView(R.id.rl_car)
    RelativeLayout rl_car;
    @BindView(R.id.tv_add_car)
    TextView tv_add_car;
    @BindView(R.id.rl_invalid_purchase)
    RelativeLayout rl_invalid_purchase;
    @BindView(R.id.rl_is_to_address)
    RelativeLayout rl_is_to_address;//是否可以配送
    @BindView(R.id.tv_ps_alter)
    TextView tv_ps_alter;//配送提示

    /**
     * 商品布局
     */
    private SmartRefreshLayout refreshLayout_product_infopics;
    private RecyclerView rcv_product_infopics;
    private ProductInfoPicsListAdapter productInfoPicsListAdapter;
    private List<ProductInfoDataBean.InfoPics> data_infopics;
    private RecyclerView rcv_pics_banner;
    private ProductInfoBannerAdapter productInfoBannerAdapter;
    private TextView tv_indicator;
    private RelativeLayout rl_normal_price;
    private ImageView iv_normal_member_price, iv_member_price;
    private RelativeSizeTextView tv_price;
    private RelativeSizeTextView tv_price_orig;
    //private RecyclerView rcv_prices;
    private RelativeLayout rl_other_memberlevel_price;
    private ImageView iv_other_price_mark;
    private TextView tv_other_price_name;
    private RelativeSizeTextView tv_other_price;
    private RelativeLayout rl_red_packet, rl_is_exchange_purchase;
    private TextView tv_purchaser_get_red_packet, tv_persenter_get_red_packet;
    private TextView tv_exchange_purchase_titel, tv_exchange_purchase_desc;

    /**
     * 秒杀商品价
     */
    private RelativeLayout rl_seckill_price;
    private ImageView iv_seckill_price_bg;
    private NumberDinRegularTextView tv_seckill_price_orig;
    private RelativeSizeTextView tv_seckill_price;
    private CountDownView countdownView;

    /**
     * 预告商品价格布局
     */
    private RelativeLayout rl_seckill_notice_price;
    private ImageView iv_seckill_notice_price_bg;
    private TextView tv_seckill_notice_time;
    private RelativeSizeTextView tv_seckill_notice_price;

    /**
     * 一元起购商品价格布局
     */
    private RelativeLayout rl_one_yuan_price;
    private ImageView iv_one_yuan_price_bg;
    private TextView tv_one_yuan_remain_num;
    private RelativeSizeTextView tv_one_yuan_price;
    private NumberDinRegularTextView tv_one_yuan_price_orig;
    private TextView tv_save;


    //商品名
    private TextView tv_name;
    //商品描述
    private TextView tv_desc;
    //地址
    private TextView tv_address;
    //
    private TextView tv_address_status;//地址状态
    //商品限制列表
    private FlowLayout rl_good_list;
    /*private CustomListView clv_tags;
    private ProductInfoTagsAdapter productInfoTagsAdapter;
    private List<ProductInfoDataBean.TagInfoListData.TagInfoBean> data_tags;*/
    /*private TextView tv_specs, tv_info_dialog_num;
    private ImageView iv_select_specs;*/
    private TextView tv_specs;
    //private ImageView iv_select_marks;

    /**
     * 上个界面传来的商品id
     */
    private String product_id;


    /**
     * 参数布局
     */
    RecyclerView rcv_product_parameterlist;
    private ProductInfoParameterListAdapter productInfoParameterListAdapter;

    /**
     * 评论布局
     */
    private SmartRefreshLayout refreshLayout;
    private View view_top;
    private RecyclerView rcv_evaluate_list;
    private View mEmptyLayout;
    private ProductEvaluateListAdapter productEvaluateListAdapter;
    private List<EvaluateListDataBean.EvaluateBean> data_evaluatelist = new ArrayList<>();

    /**
     * 双十二
     */
    private RelativeLayout rl_activity_12;
    private NumberDinRegularTextView tv_activity_12_line_price;
    private RelativeSizeTextView tv_activity_12_real_price;
    private AppCompatTextView tv_activity_12_remain_stock;
    private AppCompatTextView tv_activity_desc;
    private AppCompatTextView tv_activity_time;

    private ProductInfoDataBean productInfoDataBean;

    int scrollviewH;
    float alpha = 0;
    int dialog_edited_num = 1;

    LinearLayoutManager linearLayoutManager;
    int mFirstVisibleItems = 0;

    /**
     * 上个界面传的辨识是否是一元起购商品和剩余购买次数
     */
    private boolean isOneYuan;
    private int remain_purchase_num;
    /**
     * 地址选择弹框
     */
    private SelectAddressDialog mSelectAddressDialog;
    //地址列表
    private List<AddressListDataBean.AddressItemBean> mAddressLists;
    //当前用户选择地址
    private AddressListDataBean.AddressItemBean addressItemBean;
    //新建地址ID
    private AddressInfo mAddressInfo;
    private int isDeliveris = 0;//是否可以配送
    private Drawable background;//原有购买控件的背景

    @Override
    protected ProductInfoContract.ProductInfoPresenter createPresenter() {
        return new ProductInfoContract.ProductInfoPresenter();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_info;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initial() {
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarDarkTheme(ProductInfoActivity.this, true);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_top.setPadding(0, statusHeight, 0, 0);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_share_tip_gif.getLayoutParams();
            lp.topMargin = statusHeight + ScreenUtils.dip2px(this, 27);
            iv_share_tip_gif.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) iv_one_yuan_share_tip_gif.getLayoutParams();
            lp1.topMargin = statusHeight + ScreenUtils.dip2px(this, 27);
            iv_one_yuan_share_tip_gif.setLayoutParams(lp1);

        }
        myHandler = new MyHandler(this);
        product_id = getIntent().getStringExtra(Constants.Key.KEY_1);
        isOneYuan = getIntent().getBooleanExtra(Constants.Key.KEY_2, false);
        remain_purchase_num = getIntent().getIntExtra(Constants.Key.KEY_3, 0);
        inflater = LayoutInflater.from(this);
        btn_back.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        tv_guide_product.setOnClickListener(this);
        tv_guide_parameter.setOnClickListener(this);
        tv_guide_evaluate.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        rl_car.setOnClickListener(this);
        tv_add_car.setOnClickListener(this);
        view_pager.setOffscreenPageLimit(3);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rl_top.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                        tv_guide_product.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        tv_guide_parameter.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        tv_guide_evaluate.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        if (getUpDistance() == 0) {
                            tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        } else {
                            tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_text1_1);
                        }
                        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        break;
                    case 1:
                        rl_top.setBackgroundColor(Color.argb(255, 255, 255, 255));
                        tv_guide_product.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_parameter.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_text1_1);
                        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_evaluate.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        break;
                    case 2:
                        rl_top.setBackgroundColor(Color.argb(255, 255, 255, 255));
                        tv_guide_product.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_evaluate.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_text1_1);
                        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_parameter.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        view_product = inflater.inflate(R.layout.layout_product_info_basicinfo, null);
        refreshLayout_product_infopics = view_product.findViewById(R.id.refreshLayout);
        refreshLayout_product_infopics.setRefreshHeader(new SancellClassicsHeader(ProductInfoActivity.this));
        refreshLayout_product_infopics.setEnableLoadMore(false);
        refreshLayout_product_infopics.setOnRefreshListener(refreshLayout -> {
            mPresenter.GetProductInfo(product_id, ProductInfoActivity.this);
        });
        rcv_product_infopics = view_product.findViewById(R.id.rcv_product_infopics);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_product_infopics.setLayoutManager(linearLayoutManager);

        scrollviewH = ScreenUtils.getScreenWidth(this) - ScreenUtils.dip2px(this, 84);
        rcv_product_infopics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int h = getUpDistance();
                if (linearLayoutManager != null) {
                    mFirstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                }
                if (mFirstVisibleItems == 0) {
                    if (h <= 0) {
                        alpha = 0;
                        rl_top.setBackgroundColor(Color.argb(0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                        tv_guide_product.setTextColor(Color.argb(0, 17, 17, 17));
                        tv_guide_parameter.setTextColor(Color.argb(0, 17, 17, 17));
                        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_evaluate.setTextColor(Color.argb(0, 17, 17, 17));
                        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                    } else if (h > 0 && h <= scrollviewH) {
                        float scale = (float) h / scrollviewH;
                        alpha = (255 * scale);
                        // 只是layout背景透明(仿知乎滑动效果)
                        rl_top.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                        tv_guide_product.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        tv_guide_parameter.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        tv_guide_product.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_text1_1);
                        tv_guide_parameter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                        tv_guide_evaluate.setTextColor(Color.argb((int) alpha, 17, 17, 17));
                        tv_guide_evaluate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.round_trans_1);
                    } else {
                        alpha = 255;
                        rl_top.setBackgroundColor(Color.argb(255, 255, 255, 255));
                        tv_guide_product.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_parameter.setTextColor(Color.argb(255, 17, 17, 17));
                        tv_guide_evaluate.setTextColor(Color.argb(255, 17, 17, 17));
                    }
                }
            }
        });

        view_parameter = inflater.inflate(R.layout.layout_product_info_parameterinfo, null);
        rcv_product_parameterlist = view_parameter.findViewById(R.id.rcv_product_parameterlist);
        rcv_product_parameterlist.setLayoutManager(new LinearLayoutManager(this));
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) rcv_product_parameterlist.getLayoutParams();
            lp1.topMargin = statusHeight;
            rcv_product_parameterlist.setLayoutParams(lp1);
        }*/

        view_evaluate = inflater.inflate(R.layout.layout_product_info_evaluate, null);
        //view_top = view_evaluate.findViewById(R.id.view_top);
        refreshLayout = view_evaluate.findViewById(R.id.refreshLayout);
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) view_top.getLayoutParams();
            lp1.topMargin = statusHeight;
            view_top.setLayoutParams(lp1);
        }*/
        rcv_evaluate_list = view_evaluate.findViewById(R.id.rcv_evaluate_list);
        mEmptyLayout = view_evaluate.findViewById(R.id.empty);
        rcv_evaluate_list.setLayoutManager(new LinearLayoutManager(this));
        productEvaluateListAdapter = new ProductEvaluateListAdapter(data_evaluatelist, this, itemPicAction);
        productEvaluateListAdapter.addHeaderView(getEvaluateHeaderView());
        rcv_evaluate_list.setAdapter(productEvaluateListAdapter);
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(ProductInfoActivity.this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (productEvaluateListAdapter != null) {
                productEvaluateListAdapter.resetCurrentPage();
            }
            mPresenter.GetEvaluateList(product_id, 1, ProductInfoActivity.this);
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (productEvaluateListAdapter != null) {
                mPresenter.GetEvaluateList(product_id, productEvaluateListAdapter.getNextPage(), ProductInfoActivity.this);
            } else {
                if (productEvaluateListAdapter != null) {
                    productEvaluateListAdapter.resetCurrentPage();
                }
                mPresenter.GetEvaluateList(product_id, 1, ProductInfoActivity.this);
            }
        });


        viewList.add(view_product);
        viewList.add(view_parameter);
        viewList.add(view_evaluate);
        viewAdapter = new ProductViewAdapter(viewList);
        view_pager.setAdapter(viewAdapter);
        mPresenter.GetProductInfo(product_id, this);
        mPresenter.GetEvaluateList(product_id, 1, this);
        if (isOneYuan) {
            btn_share.setVisibility(View.VISIBLE);
            iv_one_yuan_share_tip_gif.setVisibility(View.VISIBLE);
            iv_share_tip_gif.setVisibility(View.GONE);
            Glide.with(this).load(R.mipmap.icon_productinfo_one_yuan_share)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                            if (drawable instanceof GifDrawable) {
                                GifDrawable gifDrawable = (GifDrawable) drawable;
                                gifDrawable.setLoopCount(1);
                                iv_one_yuan_share_tip_gif.setImageDrawable(drawable);
                                gifDrawable.start();
                            }
                        }
                    });
            // 为了减少代码使用匿名Handler创建一个延时的调用
            myHandler.postDelayed(() -> {
                if (iv_one_yuan_share_tip_gif != null) {
                    iv_one_yuan_share_tip_gif.setVisibility(View.GONE);
                }
            }, 6000);
        } else {
            btn_share.setVisibility(View.GONE);
            iv_one_yuan_share_tip_gif.setVisibility(View.GONE);
            iv_share_tip_gif.setVisibility(View.GONE);
        }
    }


    UserBean userBean;

    @Override
    public void onResume() {
        super.onResume();
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        /*if (userBean != null && userBean.getRealMemberLevel() == 1) {  //不是会员
            rl_available_purchase.setVisibility(View.GONE);
            rl_invalid_purchase.setVisibility(View.VISIBLE);
            rl_invalid_purchase.setOnClickListener(view -> {
                Intent intent = new Intent(ProductInfoActivity.this, SancelIntroduceActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                startActivity(intent);
            });
        } else {
            rl_available_purchase.setVisibility(View.VISIBLE);
            rl_invalid_purchase.setVisibility(View.GONE);
        }*/
    }

    LinearLayoutManager layoutManager;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_productinfo_top, (ViewGroup) rcv_product_infopics.getParent(), false);
        rcv_pics_banner = view.findViewById(R.id.rcv_pics_banner);

        rl_seckill_price = view.findViewById(R.id.rl_seckill_price);

        // ProductAddressHelp mProductAddressHelp=new ProductAddressHelp(this);
        //更多地址
        view.findViewById(R.id.ll_address).setOnClickListener(this);
        tv_address = view.findViewById(R.id.tv_address);
        tv_address_status = view.findViewById(R.id.tv_address_status);
        iv_seckill_price_bg = view.findViewById(R.id.iv_seckill_price_bg);
        tv_seckill_price_orig = view.findViewById(R.id.tv_seckill_price_orig);
        tv_seckill_price_orig.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        tv_seckill_price = view.findViewById(R.id.tv_seckill_price);
        iv_seckill_price_bg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) + ScreenUtils.dip2px(this, 28)));
        countdownView = view.findViewById(R.id.countdownView);
        countdownView.setHourTvBackgroundRes(R.drawable.round_count_down_bg)
                .setHourTvTextColorHex("#F17000")
                .setHourTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setHourTvTextSize(13)

                .setHourColonTvBackgroundColorHex("#00FFFFFF")
                .setHourColonTvSize(ScreenUtils.dip2px(this, 10), ScreenUtils.dip2px(this, 20))
                .setHourColonTvTextColorHex("#FFFFFF")
                .setHourColonTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setHourColonTvTextSize(14)

                .setMinuteTvBackgroundRes(R.drawable.round_count_down_bg)
                .setMinuteTvTextColorHex("#F17000")
                .setMinuteTvTextSize(13)

                .setMinuteColonTvBackgroundColorHex("#00FFFFFF")
                .setMinuteColonTvSize(ScreenUtils.dip2px(this, 10), ScreenUtils.dip2px(this, 20))
                .setMinuteColonTvTextColorHex("#FFFFFF")
                .setMinuteColonTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
                .setMinuteColonTvTextSize(14)

                .setSecondTvBackgroundRes(R.drawable.round_count_down_bg)
                .setSecondTvTextColorHex("#F17000")
                .setSecondTvTextSize(13)

                // 设置倒计时结束监听
                .setCountDownEndListener(() -> {
                    mPresenter.GetProductInfo(product_id, ProductInfoActivity.this);
                });

        rl_seckill_notice_price = view.findViewById(R.id.rl_seckill_notice_price);
        iv_seckill_notice_price_bg = view.findViewById(R.id.iv_seckill_notice_price_bg);
        iv_seckill_notice_price_bg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) + ScreenUtils.dip2px(this, 28)));
        tv_seckill_notice_time = view.findViewById(R.id.tv_seckill_notice_time);
        tv_seckill_notice_price = view.findViewById(R.id.tv_seckill_notice_price);

        rl_one_yuan_price = view.findViewById(R.id.rl_one_yuan_price);
        iv_one_yuan_price_bg = view.findViewById(R.id.iv_one_yuan_price_bg);
        tv_save = view.findViewById(R.id.tv_save);
        iv_one_yuan_price_bg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) + ScreenUtils.dip2px(this, 28)));
        tv_one_yuan_remain_num = view.findViewById(R.id.tv_one_yuan_remain_num);
        tv_one_yuan_price = view.findViewById(R.id.tv_one_yuan_price);
        tv_one_yuan_price_orig = view.findViewById(R.id.tv_one_yuan_price_orig);
        tv_one_yuan_price_orig.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_pics_banner.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcv_pics_banner);
        rcv_pics_banner.addOnScrollListener(new RecyclerViewPageChangeListenerHelper(snapHelper, new RecyclerViewPageChangeListenerHelper.OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                if (productInfoDataBean.getBannerPicArr() != null) {
                    tv_indicator.setText(position + 1 + "/" + productInfoDataBean.getBannerPicArr().size());
                }
            }
        }));
        tv_indicator = view.findViewById(R.id.tv_indicator);
        tv_price = view.findViewById(R.id.tv_price);
        tv_price_orig = view.findViewById(R.id.tv_price_orig);
        rl_normal_price = view.findViewById(R.id.rl_normal_price);
        iv_normal_member_price = view.findViewById(R.id.iv_normal_member_price);
        iv_member_price = view.findViewById(R.id.iv_member_price);
        tv_price_orig.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        tv_name = view.findViewById(R.id.tv_name);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_specs = view.findViewById(R.id.tv_specs);
        /*iv_select_marks = view.findViewById(R.id.iv_select_marks);
        iv_select_marks.setOnClickListener(this);*/
        rl_other_memberlevel_price = view.findViewById(R.id.rl_other_memberlevel_price);
        iv_other_price_mark = view.findViewById(R.id.iv_other_price_mark);
        tv_other_price_name = view.findViewById(R.id.tv_other_price_name);
        tv_other_price = view.findViewById(R.id.tv_other_price);
        /*rcv_prices = view.findViewById(R.id.rcv_prices);
        rcv_prices.setLayoutManager(new LinearLayoutManager(this));*/
        rl_red_packet = view.findViewById(R.id.rl_red_packet);
        rl_red_packet.setOnClickListener(this);
        tv_purchaser_get_red_packet = view.findViewById(R.id.tv_purchaser_get_red_packet);
        tv_persenter_get_red_packet = view.findViewById(R.id.tv_persenter_get_red_packet);
        rl_is_exchange_purchase = view.findViewById(R.id.rl_is_exchange_purchase);
        tv_exchange_purchase_titel = view.findViewById(R.id.tv_exchange_purchase_titel);
        tv_exchange_purchase_desc = view.findViewById(R.id.tv_exchange_purchase_desc);
        /*clv_tags = view.findViewById(R.id.clv_tags);
        clv_tags.setDividerHeight(ScreenUtils.dip2px(this, 8));
        clv_tags.setDividerWidth(ScreenUtils.dip2px(this, 8));*/
        /*tv_specs = view.findViewById(R.id.tv_specs);
        tv_info_dialog_num = view.findViewById(R.id.tv_info_dialog_num);
        iv_select_specs = view.findViewById(R.id.iv_select_specs);
        iv_select_specs.setOnClickListener(this);
        */
        //活动页
        bindActivityView(view);

        return view;
    }

    /**
     * 已滑动的距离
     */
    private int getUpDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rcv_product_infopics.getLayoutManager();
        View firstVisibItem = rcv_product_infopics.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    /**
     * 评论列表添加头部
     */
    private RelativeLayout rl_evaluate_top;
    //好评率
    private TextView tv_rate_acclaim;
    //评论数量
    private TextView tv_evaluate_num;

    private View getEvaluateHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_productinfo_evaluate_top, (ViewGroup) rcv_product_infopics.getParent(), false);
        rl_evaluate_top = view.findViewById(R.id.rl_evaluate_top);
        tv_rate_acclaim = view.findViewById(R.id.tv_rate_acclaim);
        tv_evaluate_num = view.findViewById(R.id.tv_evaluate_num);

        return view;
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
        tv_refresh.setOnClickListener(view -> {
            mPresenter.GetProductInfo(product_id, ProductInfoActivity.this);
            mPresenter.GetEvaluateList(product_id, 1, ProductInfoActivity.this);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getProductInfoSuccess(ProductInfoDataBean productInfoDataBean1) {

//        productInfoDataBean1.activtyConfig = new ProductInfoDataBean.ActivityConfigBean();
//        productInfoDataBean1.activtyConfig.status = 2;
//        productInfoDataBean1.activtyConfig.startTimeStr = "12.12 00:00";
//        productInfoDataBean1.activtyConfig.endTimeStr = "12.14 00:00";
//        productInfoDataBean1.activtyConfig.sellStockNumber = 100;
//        productInfoDataBean1.activtyConfig.salesVolume = 60;
//        productInfoDataBean1.activtyConfig.userRealPriceE2 = 8560;
//        productInfoDataBean1.activtyConfig.statusStr = "火爆开启";
//        productInfoDataBean1.setMarketPriceE2(6560);


        refreshLayout_product_infopics.finishRefresh();
        mNetworkErrorLayout.setVisibility(View.GONE);
        this.productInfoDataBean = productInfoDataBean1;

        dialog_edited_num = productInfoDataBean.getMinBuyNum() == 0 ? 1 : productInfoDataBean.getMinBuyNum();
        data_infopics = productInfoDataBean.getDetailPicArr();
        productInfoPicsListAdapter = new ProductInfoPicsListAdapter(data_infopics, ProductInfoActivity.this);
        productInfoPicsListAdapter.addHeaderView(getHeaderView());
        //显示商品限制条件
        rcv_pics_banner.post(new Runnable() {
            @Override
            public void run() {
                ProductAddressHelp mProductAddressHelp = new ProductAddressHelp(ProductInfoActivity.this, productInfoDataBean1);

            }
        });

        rcv_product_infopics.setAdapter(productInfoPicsListAdapter);
        //去获取用户地址列表
        mPresenter.getAddresssList(this);
        if (isOneYuan) {  //一元起购商品
            rl_invalid_purchase.setVisibility(View.GONE);
            rl_available_purchase.setVisibility(View.VISIBLE);
            rl_seckill_notice_price.setVisibility(View.GONE);
            rl_seckill_price.setVisibility(View.GONE);
            rl_red_packet.setVisibility(View.GONE);
            rl_normal_price.setVisibility(View.GONE);
            rl_one_yuan_price.setVisibility(View.VISIBLE);
            tv_one_yuan_price.setTagText(StringUtils.getPrice(productInfoDataBean.getOneShop().getOnePriceE2()));
            tv_one_yuan_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getOneShop().getOnePriceE2()));
            tv_one_yuan_remain_num.setText("当前仅剩" + productInfoDataBean.getOneShop().getSellStockNumber() + "件");
            tv_one_yuan_price_orig.setText("¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2()));
            tv_save.setText("省:¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2() - productInfoDataBean.getOneShop().getOnePriceE2()));
            if (remain_purchase_num > 0) {
                if (productInfoDataBean.getOneShop().getSellStockNumber() > 0) {
                    tv_add_car.setBackgroundResource(R.drawable.round_color_e55f37_18);
                    tv_add_car.setText("立即购买");
                    tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
                    tv_add_car.setText(productInfoDataBean.getOneShop().getAppInfo());
                    tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            } else {
                tv_add_car.setBackgroundResource(R.drawable.round_color_e55f37_18);
                tv_add_car.setText("邀请好友1元购买");
                tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        } else {
            if (productInfoDataBean.getGoodsDetailShare() != null && productInfoDataBean.getGoodsDetailShare().getIsShow() == 1) {
                btn_share.setVisibility(View.VISIBLE);
                iv_one_yuan_share_tip_gif.setVisibility(View.GONE);
                iv_share_tip_gif.setVisibility(View.VISIBLE);
                Glide.with(this).load(R.mipmap.icon_product_info_share).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                        if (drawable instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) drawable;
                            gifDrawable.setLoopCount(1);
                            iv_share_tip_gif.setImageDrawable(drawable);
                            gifDrawable.start();
                        }
                    }
                });
                // 为了减少代码使用匿名Handler创建一个延时的调用
                myHandler.postDelayed(() -> {
                    if (iv_share_tip_gif != null) {
                        iv_share_tip_gif.setVisibility(View.GONE);
                    }
                }, 6000);
            } else {
                btn_share.setVisibility(View.GONE);
                iv_one_yuan_share_tip_gif.setVisibility(View.GONE);
                iv_share_tip_gif.setVisibility(View.GONE);
            }
            if (productInfoDataBean.getSeckillInfo() != null && productInfoDataBean.getSeckillInfo().getSeckillGoodsStatus() == 1) {//秒杀中
                rl_red_packet.setVisibility(View.GONE);
                rl_one_yuan_price.setVisibility(View.GONE);
                if (userBean != null && userBean.getRealMemberLevel() == 1) {  //不是会员
                    rl_available_purchase.setVisibility(View.GONE);
                    rl_invalid_purchase.setVisibility(View.VISIBLE);
                    rl_invalid_purchase.setOnClickListener(view -> {
                        Intent intent = new Intent(ProductInfoActivity.this, SancelIntroduceActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, true);
                        startActivity(intent);
                    });
                } else {
                    rl_available_purchase.setVisibility(View.VISIBLE);
                    rl_invalid_purchase.setVisibility(View.GONE);
                }
                rl_normal_price.setVisibility(View.GONE);
                rl_seckill_notice_price.setVisibility(View.GONE);
                rl_seckill_price.setVisibility(View.VISIBLE);
                tv_seckill_price_orig.setText("¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2()));
                tv_seckill_price.setTagText(StringUtils.getPrice(productInfoDataBean.getSeckillInfo().getSkillPriceE2()));
                tv_seckill_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getSeckillInfo().getSkillPriceE2()));
                countdownView.setCountTime(productInfoDataBean.getSeckillInfo().getGapTime());
                countdownView.startCountDown();
                if (productInfoDataBean1.getSeckillInfo().getStocksNum() > 0) {
                    tv_add_car.setBackgroundResource(R.drawable.seckill_buy_bg);
                    tv_add_car.setText("立即购买");
                    tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
                    tv_add_car.setText("已售罄");
                    tv_add_car.setTextColor(getResources().getColor(R.color.color_text1));
                }
            } else if (productInfoDataBean.getSeckillInfo() != null && productInfoDataBean.getSeckillInfo().getSeckillGoodsStatus() == 2) {//待秒杀
                rl_invalid_purchase.setVisibility(View.GONE);
                rl_one_yuan_price.setVisibility(View.GONE);
                rl_available_purchase.setVisibility(View.VISIBLE);
                rl_seckill_price.setVisibility(View.GONE);
                rl_normal_price.setVisibility(View.VISIBLE);
                rl_seckill_notice_price.setVisibility(View.VISIBLE);
                rl_red_packet.setVisibility(View.VISIBLE);
                tv_seckill_notice_time.setText(productInfoDataBean.getSeckillInfo().getWaitBeginTimeStr() + " 开始");
                tv_seckill_notice_price.setTagText(StringUtils.getAllPrice(productInfoDataBean.getSeckillInfo().getSkillPriceE2()));
                countdownView.pauseCountDown();
                if (productInfoDataBean1.getStockNumber() > 0) {
                    tv_add_car.setBackgroundResource(R.drawable.round_359157_18);
                    tv_add_car.setText("加入购物车");
                    tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
                    tv_add_car.setText("该商品已售空");
                    tv_add_car.setTextColor(getResources().getColor(R.color.color_text1));
                }
            } else if (productInfoDataBean.activityConfig != null) {
                initActivity12(productInfoDataBean.activityConfig.status);

            } else {
                rl_invalid_purchase.setVisibility(View.GONE);
                rl_available_purchase.setVisibility(View.VISIBLE);
                rl_one_yuan_price.setVisibility(View.GONE);
                rl_seckill_notice_price.setVisibility(View.GONE);
                rl_seckill_price.setVisibility(View.GONE);
                rl_normal_price.setVisibility(View.VISIBLE);
                rl_red_packet.setVisibility(View.VISIBLE);
                countdownView.pauseCountDown();
                if (productInfoDataBean1.getStockNumber() > 0) {
                    tv_add_car.setBackgroundResource(R.drawable.round_359157_18);
                    tv_add_car.setText("加入购物车");
                    tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
                    tv_add_car.setText("该商品已售空");
                    tv_add_car.setTextColor(getResources().getColor(R.color.color_text1));
                }

            }
        }

        //去判断当前地址是否可以配送
        checkAddresDeliveris();
        tv_name.setText(productInfoDataBean.getTitle());
        tv_desc.setText(productInfoDataBean.getDesc());

        //tv_info_dialog_num.setText("，" + dialog_edited_num + "件");
        if (productInfoDataBean.getRefundDays() >= 7) {  //能退货
            tv_exchange_purchase_titel.setText("支持7天退换货");
            tv_exchange_purchase_desc.setText("满足相应条件，消费者可申请退换货”");
        } else {
            tv_exchange_purchase_titel.setText("不支持无理由退换货");
            tv_exchange_purchase_desc.setText("此商品不支持7天无理由退换货");
        }
        tv_price_orig.setVisibility(View.VISIBLE);
        tv_price_orig.setTagText("¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2()));
        tv_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
        tv_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getUserRealPriceE2()));
        tv_other_price.setTagText(StringUtils.getPrice(productInfoDataBean.getMemberLevelThreePriceE2()));
        tv_other_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getMemberLevelThreePriceE2()));
        if (userBean != null) {
            switch (userBean.getRealMemberLevel()) {
                case 1:
                    rl_other_memberlevel_price.setVisibility(View.VISIBLE);
                    iv_member_price.setVisibility(View.GONE);
                    iv_normal_member_price.setVisibility(View.VISIBLE);
                    tv_purchaser_get_red_packet.setText("购买此商品立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv0ProfitSharingE2()) + "红包");
                    tv_persenter_get_red_packet.setText("您的受邀者购买此商品您立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv0RecommProfitSharingE2()) + "红包");
                    break;
                case 2:
                    rl_other_memberlevel_price.setVisibility(View.VISIBLE);
                    iv_normal_member_price.setVisibility(View.GONE);
                    iv_member_price.setVisibility(View.VISIBLE);
                    iv_member_price.setImageResource(R.mipmap.icon_member_price1);
                    tv_purchaser_get_red_packet.setText("购买此商品立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv1ProfitSharingE2()) + "红包");
                    tv_persenter_get_red_packet.setText("您的受邀者购买此商品您立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv1RecommProfitSharingE2()) + "红包");
                    break;
                case 3:
                    rl_other_memberlevel_price.setVisibility(View.VISIBLE);
                    iv_normal_member_price.setVisibility(View.GONE);
                    iv_member_price.setVisibility(View.VISIBLE);
                    iv_member_price.setImageResource(R.mipmap.icon_member_price2);
                    tv_purchaser_get_red_packet.setText("购买此商品立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv2ProfitSharingE2()) + "红包");
                    tv_persenter_get_red_packet.setText("您的受邀者购买此商品您立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv2RecommProfitSharingE2()) + "红包");
                    break;
                case 4:
                    rl_other_memberlevel_price.setVisibility(View.GONE);
                    iv_normal_member_price.setVisibility(View.GONE);
                    iv_member_price.setVisibility(View.VISIBLE);
                    iv_member_price.setImageResource(R.mipmap.icon_member_price3);
                    tv_purchaser_get_red_packet.setText("购买此商品立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv3ProfitSharingE2()) + "红包");
                    tv_persenter_get_red_packet.setText("您的受邀者购买此商品您立得¥" +
                            StringUtils.getAllPrice(productInfoDataBean.getLv3RecommProfitSharingE2()) + "红包");
                    break;
            }
        } else {
            rl_other_memberlevel_price.setVisibility(View.VISIBLE);
        }


        tv_specs.setText(productInfoDataBean.getSpecification());
        if (productInfoDataBean.getBannerPicArr().size() > 1) {
            tv_indicator.setVisibility(View.VISIBLE);
            tv_indicator.setText("1/" + productInfoDataBean.getBannerPicArr().size());
        } else {
            tv_indicator.setVisibility(View.GONE);
        }

        tv_rate_acclaim.setText(productInfoDataBean1.getEvaluationPercentageStr() + "好评率");

        productInfoBannerAdapter = new ProductInfoBannerAdapter(productInfoDataBean.getBannerPicArr());
        rcv_pics_banner.setAdapter(productInfoBannerAdapter);

        productInfoBannerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.sdv_pic:
                    Intent intent = new Intent(ProductInfoActivity.this, ImagePagerActivity.class);
                    intent.putStringArrayListExtra(Constants.Key.KEY_1, (ArrayList<String>) productInfoDataBean.getBannerPicArr());
                    intent.putExtra(Constants.Key.KEY_2, position);
                    startActivity(intent);
                    break;
            }
        });

        /*data_tags = productInfoDataBean.getTagInfo().getDataList();
        productInfoTagsAdapter = new ProductInfoTagsAdapter(ProductInfoActivity.this, data_tags);
        clv_tags.setAdapter(productInfoTagsAdapter);*/

        data_slogan = productInfoDataBean.getServerInfo().getDataList();

        productInfoParameterListAdapter = new ProductInfoParameterListAdapter(productInfoDataBean.getAttrInfo().getDataList());
        rcv_product_parameterlist.setAdapter(productInfoParameterListAdapter);

        riv_goodsDetailImageView.setImageURI(Uri.parse(productInfoDataBean.getCoverPic()));
    }

    @Override
    public void addShoppingCarSuccess() {
        AddCartAnimation();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getEvaluateListSuccess(EvaluateListDataBean dataEvaluate, int page) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (page == 1) {
            if (dataEvaluate != null && dataEvaluate.getDataList() != null && dataEvaluate.getDataList().size() > 0) {
                rl_evaluate_top.setVisibility(View.VISIBLE);
                tv_evaluate_num.setText(dataEvaluate.getDataCount() + "条评论");
                mEmptyLayout.setVisibility(View.GONE);
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_evaluatelist.clear();
                data_evaluatelist.addAll(dataEvaluate.getDataList());
                productEvaluateListAdapter.notifyDataSetChanged();
                productEvaluateListAdapter.correctCurrentPage();
                if (data_evaluatelist.size() == dataEvaluate.getDataCount()) {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                rl_evaluate_top.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            data_evaluatelist.addAll(dataEvaluate.getDataList());
            productEvaluateListAdapter.notifyDataSetChanged();
            productEvaluateListAdapter.correctCurrentPage();
            if (data_evaluatelist.size() == dataEvaluate.getDataCount()) {
                refreshLayout.setNoMoreData(true);
            }
        }

    }

    @Override
    public void getAddressListSuccess(AddressListDataBean res) {
        if (res == null || res.getDataList() == null || res.getDataList().size() <= 0) {
            //暂无地址
            isDeliveris = 2;
            setPsStates();
            return;

        }
        if (mAddressInfo != null) {//要根据新建的ID去选择显示地址
            for (AddressListDataBean.AddressItemBean mAddressItemBean : res.getDataList()) {
                if (mAddressInfo.addressId == mAddressItemBean.getId()) {
                    mAddressInfo = null;
                    bindAddressView(mAddressItemBean);
                    break;
                }
            }
        } else {
            //设置数据
            bindAddressView(res.getDataList().get(0));
        }
        mAddressLists = res.getDataList();

    }


    /**
     * 设置当前地址
     */
    private void bindAddressView(AddressListDataBean.AddressItemBean addressItemBean) {
        this.addressItemBean = addressItemBean;
        tv_address.setText(addressItemBean.getCodeString());
        checkAddresDeliveris();
    }

    /**
     * 设置商品当前地址是否可以送达
     */
    private void checkAddresDeliveris() {
        if (productInfoDataBean == null || addressItemBean == null) {
            return;
        }
        ProducAddressInfo goodsRegion = productInfoDataBean.getGoodsRegion();
        if (goodsRegion == null || goodsRegion.getProvinceIdList() == null || goodsRegion.getProvinceIdList().size() <= 0) {
            return;
        }
        isDeliveris = 0;
        for (String goodAddressId : goodsRegion.getProvinceIdList()) {
            if (goodAddressId.equals(addressItemBean.getProvinceId() + "")) {
                isDeliveris = 1;
                break;
            }
        }

        setPsStates();


    }

    /**
     * 设置配送状态
     */
    private void setPsStates() {
        if (background == null) {
            background = tv_add_car.getBackground();
        }
        if (isDeliveris == 1) {//可以配送
            tv_address_status.setVisibility(View.GONE);
            tv_address_status.setText("当前地址不支持配送");
            rl_is_to_address.setVisibility(View.GONE);
            tv_add_car.setEnabled(true);
            tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_add_car.setBackground(background);

        } else if (isDeliveris == 2) {//暂无地址
            tv_address_status.setVisibility(View.GONE);
            rl_is_to_address.setVisibility(View.VISIBLE);
            tv_ps_alter.setText("暂无收货地址");
            tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
            tv_add_car.setTextColor(getResources().getColor(R.color.color_text1));

        } else {//不可配送
            tv_add_car.setBackgroundResource(R.drawable.round_color_stroke2_18);
            tv_add_car.setTextColor(getResources().getColor(R.color.color_text1));
            tv_add_car.setEnabled(false);
            tv_ps_alter.setText("非常抱歉，该商品在该区域不支持销售");
            tv_address_status.setVisibility(View.VISIBLE);
            rl_is_to_address.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getAddressListError(String error) {
        ToastHelper.showToast(this, error);

    }

    ProductEvaluateListAdapter.ItemPicAction itemPicAction = (data_pics, pos) -> {
        ArrayList<String> data = new ArrayList<>();
        for (EvaluateListDataBean.EvaluateBean.PicArr pic : data_pics) {
            data.add(pic.getCoverPic());
        }
        Intent intent = new Intent(ProductInfoActivity.this, ImagePagerActivity.class);
        intent.putStringArrayListExtra(Constants.Key.KEY_1, data);
        intent.putExtra(Constants.Key.KEY_2, pos);
        startActivity(intent);
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_address://更多地址
                if (mSelectAddressDialog == null) {

                    mSelectAddressDialog = new SelectAddressDialog(this, null);
                    mSelectAddressDialog.setOnAddressSelectLinsener(this);
                }
                mSelectAddressDialog.setAdapter(mAddressLists);
                mSelectAddressDialog.show();
                break;
            case R.id.btn_back:
                finishAfterTransition();
                break;
            case R.id.btn_share:  //分享
                ProductShare();
                break;
            case R.id.tv_guide_product:  //商品
                view_pager.setCurrentItem(0);
                break;
            case R.id.tv_guide_parameter:  //参数
                view_pager.setCurrentItem(1);
                break;
            case R.id.tv_guide_evaluate:
                view_pager.setCurrentItem(2);
                break;
            case R.id.ll_contact:  //联系客服
                showCallPhone();
                break;
            case R.id.rl_car:  //跳转购物车
                iv_car_red_point.setVisibility(View.GONE);
                startActivity(new Intent(ProductInfoActivity.this, ShoppingCarActivity.class));
                break;
            case R.id.tv_add_car:  //加入购物车
                if (isDeliveris != 1) {// 当前地址不支持配送
                    return;
                }
                if (isOneYuan) {
                    if (remain_purchase_num > 0) {  //可购买次数
                        if (productInfoDataBean != null && productInfoDataBean.getOneShop() != null && productInfoDataBean.getOneShop().getSellStockNumber() > 0) {
                            SingleOrderIntent data = new SingleOrderIntent();
                            data.good_id = product_id;
                            data.good_title = productInfoDataBean.getTitle();
                            data.good_spec = productInfoDataBean.getSpecification();
                            data.good_pic = productInfoDataBean.getCoverPic();
                            data.total_price = productInfoDataBean.getOneShop().getOnePriceE2();
                            data.isOneYuan = true;
                            SingleProductCreateOrderActivity.start(this, data);

                        }
                    } else {  //分享获取购买次数
                        OneYuanShare();
                    }
                } else if (productInfoDataBean.getSeckillInfo() != null && productInfoDataBean.getSeckillInfo().getSeckillGoodsStatus() == 1) {  //秒杀中
                    if (productInfoDataBean.getSeckillInfo().getStocksNum() > 0) {

                        SingleOrderIntent data = new SingleOrderIntent();
                        data.good_id = product_id;
                        data.good_title = productInfoDataBean.getTitle();
                        data.good_spec = productInfoDataBean.getSpecification();
                        data.good_pic = productInfoDataBean.getCoverPic();
                        data.total_price = productInfoDataBean.getUserRealPriceE2();
                        data.isOneYuan = false;
                        SingleProductCreateOrderActivity.start(this, data);

                    }
                } else if (productInfoDataBean.activityConfig != null) {
                    //活动立即购买
                    if (productInfoDataBean.activityConfig.status == 1 && productInfoDataBean.activityConfig.sellStockNumber - productInfoDataBean.activityConfig.salesVolume > 0 && productInfoDataBean.activityConfig.goodsStatus == 1) {

                        SingleOrderIntent data = new SingleOrderIntent();
                        data.good_id = product_id;
                        data.good_title = productInfoDataBean.getTitle();
                        data.good_spec = productInfoDataBean.getSpecification();
                        data.good_pic = productInfoDataBean.getCoverPic();
                        data.total_price = productInfoDataBean.activityConfig.userRealPriceE2;
                        data.isOneYuan = false;
                        data.mActivityId = productInfoDataBean.activityConfig.activityId;
                        SingleProductCreateOrderActivity.start(this, data);

                    }
                } else {
                    if (productInfoDataBean.getStockNumber() > 0) {
                        showSelectSpecs();
                    }
                }
                //mPresenter.AddShoppingCar(product_id, dialog_edited_num, ProductInfoActivity.this);
                break;
            case R.id.rl_red_packet:
                DialogUtil.showRedPacket(ProductInfoActivity.this);
                break;
            /*case R.id.iv_select_specs:
                showSelectSpecs();
                break;
            case R.id.iv_select_marks:
                if (data_slogan != null && data_slogan.size() > 0) {
                    showSancellSlogan();
                }
                break;*/

        }
    }

    /**
     * 商品分享
     */
    public void ProductShare() {
        if (isOneYuan) {  //一元购商品
            OneYuanShare();
        } else {
            if (productInfoDataBean.getGoodsDetailShare() != null) {
                String title = productInfoDataBean.getGoodsDetailShare().getTitle();
                String description = productInfoDataBean.getGoodsDetailShare().getDesc();
                String linkurl = productInfoDataBean.getGoodsDetailShare().getLink();
                UMImage image = new UMImage(this, productInfoDataBean.getGoodsDetailShare().getLogoUrl());
                DialogUtil.getShareDialog(ProductInfoActivity.this, image, linkurl, title, description, umShareListener);
            }
        }
    }


    /**
     * 一元起购分享获取购买次数
     */
    public void OneYuanShare() {
        if (userBean.getmActivityOneDollarData() != null) {
            String title = userBean.getmActivityOneDollarData().getTitle();
            String description = userBean.getmActivityOneDollarData().getDesc();
            String linkurl = userBean.getmActivityOneDollarData().getLink();
            UMImage image = new UMImage(this, userBean.getmActivityOneDollarData().getLogoUrl());
            DialogUtil.getShareDialog(ProductInfoActivity.this, image, linkurl, title, description, umShareListener);
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享成功"), 1000);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            new Handler().postDelayed(() -> SCApp.getInstance().showSystemCenterToast("分享失败"), 1000);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            /*if (platform != SHARE_MEDIA.QQ) {
                SCApp.getInstance().showSystemCenterToast("分享取消");
            }*/
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(ProductInfoActivity.this).onActivityResult(requestCode, resultCode, data);
    }

    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void showCallPhone() {
        DialogUtil.showCallPhone(ProductInfoActivity.this,"","");
    }

    /**
     * 选择规格和数量
     */
    private Dialog dialog_select_specs;
    private ImageView iv_dialog_close;
    private SimpleDraweeView sdv_dialog_pic;
    private TextView tv_dialog_productname;
    private RelativeSizeTextView tv_dialog_price;
    private TextView tv_dialog_specs_name;
    private TextView tv_dialog_specs;
    private ImageView iv_dialog_jia;
    private EditText ed_dialog_num;
    private ImageView iv_dialog_jian;
    private TextView tv_dialog_add_car;
    private TextView tv_min_buynum;


    @SuppressLint("SetTextI18n")
    public void showSelectSpecs() {
        if (dialog_select_specs == null) {
            View view = getLayoutInflater().inflate(R.layout.dialog_productinfo_select_specs,
                    null);
            dialog_select_specs = new Dialog(this, R.style.transparentFrameWindowStyle);
            dialog_select_specs.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
            sdv_dialog_pic = view.findViewById(R.id.sdv_dialog_pic);
            sdv_dialog_pic.setImageURI(Uri.parse(productInfoDataBean.getCoverPic()));
            tv_dialog_productname = view.findViewById(R.id.tv_dialog_productname);
            tv_dialog_productname.setText(productInfoDataBean.getTitle());
            tv_dialog_price = view.findViewById(R.id.tv_dialog_price);
            tv_dialog_price.setStartText("¥");
            tv_dialog_price.setStartProportion(0.7f);
            tv_dialog_price.setEndProportion(0.7f);

            tv_dialog_specs_name = view.findViewById(R.id.tv_dialog_specs_name);
            tv_dialog_specs = view.findViewById(R.id.tv_dialog_specs);
            tv_dialog_specs.setText(productInfoDataBean.getSpecification());
            iv_dialog_jia = view.findViewById(R.id.iv_dialog_jia);
            ed_dialog_num = view.findViewById(R.id.tv_dialog_num);
            iv_dialog_jian = view.findViewById(R.id.iv_dialog_jian);
            tv_min_buynum = view.findViewById(R.id.tv_min_buynum);
            if (productInfoDataBean.getMinBuyNum() > 1) {
                tv_min_buynum.setText(productInfoDataBean.getMinBuyNum() + "件起购");
            } else {
                tv_min_buynum.setText("");
            }
            tv_dialog_add_car = view.findViewById(R.id.tv_dialog_add_car);
            ed_dialog_num.addTextChangedListener(new TextWatcher() {
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
                        int num = Integer.valueOf(s);
                        if (num == 0) {
                            ed_dialog_num.setText("1");
                            dialog_edited_num = 1;
                            ed_dialog_num.setSelection(ed_dialog_num.getText().length());
                            iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian_invalid);
                        } else if (num <= productInfoDataBean.getMinBuyNum()) {
                            iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian_invalid);
                            dialog_edited_num = productInfoDataBean.getMinBuyNum();
                        } else {
                            dialog_edited_num = num;
                            iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian);
                        }
                        if (num > 200 || num > productInfoDataBean.getStockNumber()) {
                            if (productInfoDataBean.getStockNumber() > 200) {
                                dialog_edited_num = 200;
                                SCApp.getInstance().showSystemCenterToast("最多只能买200件哦！");
                            } else {
                                dialog_edited_num = productInfoDataBean.getStockNumber();
                                SCApp.getInstance().showSystemCenterToast("最多只能买" + productInfoDataBean.getStockNumber() + "件哦！");
                            }
                            ed_dialog_num.setText(dialog_edited_num + "");
                            ed_dialog_num.setSelection(ed_dialog_num.getText().length());
                            iv_dialog_jia.setImageResource(R.mipmap.icon_productinfo_jia_invalid);
                        } else {
                            iv_dialog_jia.setImageResource(R.mipmap.icon_productinfo_jia);
                        }
                    }
                }
            });
            iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian_invalid);
            iv_dialog_close.setOnClickListener(view1 -> dialog_select_specs.dismiss());
            iv_dialog_jia.setOnClickListener(view12 -> {
                /*if (userBean.getRealMemberLevel() == 1) {  //不是会员
                    return;
                }*/
                if (dialog_edited_num < productInfoDataBean.getStockNumber()) {
                    dialog_edited_num += 1;
                    iv_dialog_jia.setImageResource(R.mipmap.icon_productinfo_jia);
                    iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian);
                    ed_dialog_num.setText(dialog_edited_num + "");
                    ed_dialog_num.setSelection(ed_dialog_num.getText().length());
                    if (dialog_edited_num >= productInfoDataBean.getStockNumber()) {
                        iv_dialog_jia.setImageResource(R.mipmap.icon_productinfo_jia_invalid);
                    }
                }
            });
            iv_dialog_jian.setOnClickListener(view14 -> {
                /*if (userBean.getRealMemberLevel() == 1) {  //不是会员
                    return;
                }*/
                if (dialog_edited_num == productInfoDataBean.getMinBuyNum()) {
                    return;
                }
                dialog_edited_num -= 1;
                if (dialog_edited_num < productInfoDataBean.getStockNumber()) {
                    iv_dialog_jia.setImageResource(R.mipmap.icon_productinfo_jia);
                }
                if (dialog_edited_num == (productInfoDataBean.getMinBuyNum() == 0 ? 1 : productInfoDataBean.getMinBuyNum())) {
                    iv_dialog_jian.setImageResource(R.mipmap.icon_productinfo_jian_invalid);
                }
                ed_dialog_num.setText(dialog_edited_num + "");
                ed_dialog_num.setSelection(ed_dialog_num.getText().length());
            });

            tv_dialog_add_car.setOnClickListener(view13 -> {
                dialog_select_specs.dismiss();
                /*if (userBean.getRealMemberLevel() == 1) {  //不是会员
                    Intent intent = new Intent(ProductInfoActivity.this, SancelIntroduceActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, true);
                    startActivity(intent);
                } else {*/
                mPresenter.AddShoppingCar(product_id, dialog_edited_num, ProductInfoActivity.this);
                //}
            });
            tv_dialog_add_car.setText("加入购物车");
            tv_dialog_add_car.setBackgroundResource(R.drawable.round_359157_18);
            tv_dialog_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_dialog_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
            tv_dialog_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getUserRealPriceE2()));
            /*if (userBean.getRealMemberLevel() == 1) {  //不是会员
                tv_dialog_add_car.setText("加入购物车");
                tv_dialog_add_car.setBackgroundResource(R.drawable.round_359157_18);
                tv_dialog_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                *//*tv_dialog_add_car.setText("加入会员购买商品还可享受更多特权");
                tv_dialog_add_car.setBackgroundResource(R.drawable.round_191a19_20);
                tv_dialog_add_car.setTextColor(getResources().getColor(R.color.color_F5C48B));*//*
                tv_dialog_price.setTagText(StringUtils.getPrice(productInfoDataBean.getMinPriceE2()));
                tv_dialog_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getMinPriceE2()));
            } else {
                tv_dialog_add_car.setText("加入购物车");
                tv_dialog_add_car.setBackgroundResource(R.drawable.round_359157_18);
                tv_dialog_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_dialog_price.setTagText(StringUtils.getPrice(productInfoDataBean.getUserRealPriceE2()));
                tv_dialog_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.getUserRealPriceE2()));
            }*/
            dialog_select_specs.setOnDismissListener(dialogInterface -> {
                if (dialog_edited_num < productInfoDataBean.getMinBuyNum()) {
                    dialog_edited_num = productInfoDataBean.getMinBuyNum();
                }
                //tv_info_dialog_num.setText("，" + dialog_edited_num + "件");
            });
            Window window = dialog_select_specs.getWindow();
            // 设置显示动画
            assert window != null;
            window.setWindowAnimations(R.style.ani_bottom);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            // 设置显示位置
            dialog_select_specs.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            dialog_select_specs.setCanceledOnTouchOutside(true);
        }
        ed_dialog_num.setText(dialog_edited_num + "");
        ed_dialog_num.setSelection(ed_dialog_num.getText().length());
        ed_dialog_num.setCursorVisible(false);
        ed_dialog_num.setOnClickListener(v -> ed_dialog_num.setCursorVisible(true));
        dialog_select_specs.show();
    }

    /**
     * 平台服务信息
     */
    private Dialog dialog_slogan;
    private List<ProductInfoDataBean.ServerInfoListData.ServerInfo> data_slogan;

    public void showSancellSlogan() {
        if (dialog_slogan == null) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.dialog_sancell_slogan,
                    null);
            dialog_slogan = new Dialog(this, R.style.transparentFrameWindowStyle);
            dialog_slogan.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
            TextView tv_dialog_sure = view.findViewById(R.id.tv_dialog_sure);
            RecyclerView rcv_slogan = view.findViewById(R.id.rcv_slogan);
            rcv_slogan.setLayoutManager(new LinearLayoutManager(this));
            rcv_slogan.setAdapter(new ProductInfoSloganAdapter(data_slogan));
            iv_dialog_close.setOnClickListener(view1 -> dialog_slogan.dismiss());
            tv_dialog_sure.setOnClickListener(view12 -> dialog_slogan.dismiss());
            Window window = dialog_slogan.getWindow();
            // 设置显示动画
            window.setWindowAnimations(R.style.ani_bottom);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            // 设置显示位置
            dialog_slogan.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            dialog_slogan.setCanceledOnTouchOutside(true);
        }
        dialog_slogan.show();
    }

    @BindView(R.id.riv_goodsDetailImageView)
    SimpleDraweeView riv_goodsDetailImageView;
    @BindView(R.id.iv_shop_car)
    ImageView iv_shop_car;
    @BindView(R.id.iv_car_red_point)
    ImageView iv_car_red_point;

    private void AddCartAnimation() {
        float[] mCurrentPosition = new float[2];
        //计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        rl_available_purchase.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）（此图片控件添加到根布局，居中）
        int startLoc[] = new int[2];
        riv_goodsDetailImageView.getLocationInWindow(startLoc);

        //购物车控件的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        rl_car.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + riv_goodsDetailImageView.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + riv_goodsDetailImageView.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + rl_car.getWidth() / 2;
        float toY = endLoc[1] - parentLoc[1] + rl_car.getHeight() * 2 / 5;

        //透明度和缩放动画，动画持续时间和动画透明度可以自己根据想要的效果调整
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(280);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f, 0.16f, 1f, 0.16f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setDuration(200);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        //平移动画X轴 计算X轴要平移的距离，设置动画的偏移时间由透明度和缩放动画持续时间决定
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                toX - startX, 0, 0);
        translateAnimationX.setStartOffset(300);
        translateAnimationX.setDuration(300);
        //设置线性插值器
        translateAnimationX.setInterpolator(new LinearInterpolator());

        //平移动画Y轴 同X轴
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, toY - startY);
        translateAnimationY.setStartOffset(300);
        translateAnimationY.setDuration(300);
        //设置加速插值器
        translateAnimationY.setInterpolator(new AccelerateInterpolator());


        //动画集合
        final AnimationSet set = new AnimationSet(false);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation1);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimationX);
        set.addAnimation(translateAnimationY);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                riv_goodsDetailImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画执行完成
                riv_goodsDetailImageView.setVisibility(View.INVISIBLE);
                riv_goodsDetailImageView.clearAnimation();
                set.cancel();
                animation.cancel();
                //购物车商品数量更新
                iv_car_red_point.setVisibility(View.VISIBLE);
                /*if (checkDBTicket()) {
                    CartApi.getInstance().getCartInfo(new CartInfoCallback() {
                        @Override
                        public void cartInfo(CartNumModel cartModel) {
                            updateCartNum();
                        }
                    });
                }*/
                //购物车控件 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(ProductInfoActivity.this, R.anim.shop_car_scale);
                iv_shop_car.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //设置动画播放完以后消失，终止填充
        set.setFillAfter(false);
        riv_goodsDetailImageView.startAnimation(set);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownView != null) {
            countdownView.destoryCountDownView();
        }
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    MyHandler myHandler;

    @Override
    public void onAddressSelectListener(AddressListDataBean.AddressItemBean mAddressItemBean) {
        bindAddressView(mAddressItemBean);
    }

    @Override
    public void addAddressSuccess(AddressInfo mAddressInfo) {
        this.mAddressInfo = mAddressInfo;
        //去从新获取地址
        mPresenter.getAddresssList(this);
    }

    public static class MyHandler extends Handler {
        private WeakReference<ProductInfoActivity> reference;

        public MyHandler(ProductInfoActivity activity) {
            reference = new WeakReference<ProductInfoActivity>(activity);
        }
    }


    public void bindActivityView(View view) {
        rl_activity_12 = view.findViewById(R.id.rl_activity_12);
        tv_activity_12_line_price = view.findViewById(R.id.tv_activity_12_line_price);
        tv_activity_12_real_price = view.findViewById(R.id.tv_activity_12_real_price);
        tv_activity_12_remain_stock = view.findViewById(R.id.tv_activity_12_remain_stock);
        tv_activity_time = view.findViewById(R.id.tv_activity_time);
        tv_activity_desc = view.findViewById(R.id.tv_activity_desc);
    }

    /**
     * 初始化双12活动
     */
    public void initActivity12(int status) {

        rl_invalid_purchase.setVisibility(View.GONE);
        rl_one_yuan_price.setVisibility(View.GONE);
        rl_available_purchase.setVisibility(View.VISIBLE);
        rl_seckill_price.setVisibility(View.GONE);
        rl_normal_price.setVisibility(View.GONE);
        rl_seckill_notice_price.setVisibility(View.GONE);
        rl_red_packet.setVisibility(View.GONE);
        rl_activity_12.setVisibility(View.VISIBLE);

        //设置数据
        tv_activity_12_line_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        tv_activity_12_line_price.setText(new StringBuffer("¥" + StringUtils.getAllPrice(productInfoDataBean.getMarketPriceE2())));
        tv_activity_12_real_price.setTagText(StringUtils.getPrice(productInfoDataBean.activityConfig.userRealPriceE2));
        tv_activity_12_real_price.setEndText(StringUtils.getPriceDecimal(productInfoDataBean.activityConfig.userRealPriceE2));

        //库存量
        int stock = productInfoDataBean.activityConfig.sellStockNumber;

        if (status == 1) {
            //进行中
            tv_activity_desc.setText(productInfoDataBean.activityConfig.endTimeStr);
            tv_activity_12_remain_stock.setVisibility(View.VISIBLE);
            tv_activity_12_remain_stock.setText(String.format(getResources().getString(R.string.activity_stock_remain), stock));
            tv_activity_time.setText(R.string.activity_close_time);
            tv_activity_time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv_activity_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            if (productInfoDataBean.activityConfig.goodsStatus == 1) {
                if (stock <= 0) {
                    //库存不足
                    setCartBtnStatus(true, true);
                } else {
                    setCartBtnStatus(true, false);
                }
            } else {
                setCartBtnStatus(false, true);
            }

        } else if (status == 2) {
            //未开始
            tv_activity_12_remain_stock.setVisibility(View.VISIBLE);
            tv_activity_12_remain_stock.setText(String.format(getResources().getString(R.string.activity_limit_stock), productInfoDataBean.activityConfig.sellStockNumber));
            tv_activity_time.setText(productInfoDataBean.activityConfig.startTimeStr);
            setCartBtnStatus(false, false);
            tv_activity_desc.setText(R.string.activity_open);
            tv_activity_time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv_activity_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            //已结束
            tv_activity_time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv_activity_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv_activity_12_remain_stock.setVisibility(View.GONE);
            tv_activity_desc.setText(productInfoDataBean.activityConfig.endTimeStr);
            setCartBtnStatus(true, true);
            tv_activity_time.setText(R.string.activity_close_time);
        }

    }

    /**
     * 按钮状态
     *
     * @param isStart 活动是否开始
     * @param soldOut 库存是否充足
     */
    public void setCartBtnStatus(boolean isStart, boolean soldOut) {
        if (isStart) {
            if (soldOut) {
                tv_add_car.setBackground(getResources().getDrawable(R.drawable.shape_round_add_cart_gray_18));
                tv_add_car.setText(R.string.goods_sold_out);
                tv_add_car.setTextColor(getResources().getColor(R.color.color_text2));
            } else {
                tv_add_car.setText(R.string.immediately_purchase);
                tv_add_car.setBackground(getResources().getDrawable(R.mipmap.icon_add_cart_bg_activity));
                tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        } else {
            tv_add_car.setText(R.string.please_wait);
            tv_add_car.setBackground(getResources().getDrawable(R.mipmap.icon_add_cart_bg_activity));
            tv_add_car.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }


}
