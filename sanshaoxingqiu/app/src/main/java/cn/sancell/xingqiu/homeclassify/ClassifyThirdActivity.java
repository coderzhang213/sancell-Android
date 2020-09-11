package cn.sancell.xingqiu.homeclassify;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.adapter.ClassifyScreeningAdapter;
import cn.sancell.xingqiu.homeclassify.bean.ClassifyChannelBean;
import cn.sancell.xingqiu.homeclassify.bean.ClassifyScreeningDataBean;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifySecondDataBean;
import cn.sancell.xingqiu.homeclassify.bean.ScreeningInfoEntity;
import cn.sancell.xingqiu.homeclassify.contract.ClassifyThirdContract;
import cn.sancell.xingqiu.homepage.adapter.ClassifyPagerAdapter;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.Solve7PopupWindow;

public class ClassifyThirdActivity extends BaseMVPActivity<ClassifyThirdContract.ClassifyThirdPresenter>
        implements ClassifyThirdContract.ClassifyThirdView, View.OnClickListener {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.mColumnHorizontalScrollView)
    HorizontalScrollView mColumnHorizontalScrollView;
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroup_content;
    private List<ClassifyChannelBean> userChannelList;

    @BindView(R.id.ll_sort_general)
    LinearLayout ll_sort_general;
    @BindView(R.id.tv_sort_general)
    TextView tv_sort_general;
    @BindView(R.id.iv_sort_general)
    ImageView iv_sort_general;
    @BindView(R.id.tv_sales)
    TextView tv_sales;
    @BindView(R.id.ll_sort_pirce)
    LinearLayout ll_sort_pirce;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.iv_sort_price)
    ImageView iv_sort_price;
    @BindView(R.id.tv_sort_screening)
    TextView tv_sort_screening;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private ClassifyPagerAdapter classifyPagerAdapter;
    /**
     * 上个界面传的三级分类列表
     */
    private List<HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean> data_classify_third;
    /**
     * 上个界面传的二级分类名称
     */
    private String title;
    private int childPosition;
    /**
     * 上个界面传的一级级分类id
     */
    private int first_id;
    /**
     * 上个界面传的二级级分类id
     */
    private int second_id;
    /**
     * 当前界面选择的属性
     */
    private List<ScreeningInfoEntity> infoEntities = new ArrayList<>();
    private String select_sort_general = "4";
    private String select_sort_price = "-1";
    private String select_sort_sales = "-1";
    private String select_screening_active_ids = "";
    private String select_screening_mark_ids = "";
    private String edit_lowest_price = "";
    private String edit_highest_price = "";


    @Override
    protected ClassifyThirdContract.ClassifyThirdPresenter createPresenter() {
        return new ClassifyThirdContract.ClassifyThirdPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classify_third;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        title = getIntent().getStringExtra(Constants.Key.KEY_1);
        data_classify_third = (List<HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean>) getIntent().getSerializableExtra(
                Constants.Key.KEY_2);
        childPosition = getIntent().getIntExtra(Constants.Key.KEY_3, 0);
        first_id = getIntent().getIntExtra(Constants.Key.KEY_4, 0);
        second_id = getIntent().getIntExtra(Constants.Key.KEY_5, 0);
        tv_title.setText(title);
        mInflater = LayoutInflater.from(this);
        initColumnData();
        initTabColumn();
        btn_back.setOnClickListener(this);
        ll_sort_general.setOnClickListener(this);
        tv_sales.setOnClickListener(this);
        ll_sort_pirce.setOnClickListener(this);
        tv_sort_screening.setOnClickListener(this);
        refreshEntity();
        classifyPagerAdapter = new ClassifyPagerAdapter(getSupportFragmentManager());
        classifyPagerAdapter.init(infoEntities);
        viewPager.setAdapter(classifyPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
                initTabColumn();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(childPosition);
        mPresenter.GetClassifyScreeningList(ClassifyThirdActivity.this);
    }

    public void refreshEntity() {
        infoEntities.clear();
        for (ClassifyChannelBean temp : userChannelList
        ) {
            ScreeningInfoEntity screeningInfoEntity = new ScreeningInfoEntity();
            screeningInfoEntity.setFirst_id(first_id);
            screeningInfoEntity.setSecond_id(second_id);
            screeningInfoEntity.setThird_classify_name(temp.getName());
            screeningInfoEntity.setThird_classify_id(temp.getId() + "");
            screeningInfoEntity.setSort_general(select_sort_general);
            screeningInfoEntity.setSort_price(select_sort_price);
            screeningInfoEntity.setSort_sales(select_sort_sales);
            screeningInfoEntity.setScreening_active_ids(select_screening_active_ids);
            screeningInfoEntity.setScreening_mark_ids(select_screening_mark_ids);
            screeningInfoEntity.setEdit_highest_price(edit_highest_price);
            screeningInfoEntity.setEdit_lowest_price(edit_lowest_price);
            infoEntities.add(screeningInfoEntity);
        }
        if (classifyPagerAdapter != null) {
            classifyPagerAdapter.refreshAllFragment(infoEntities);
        }
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
                mPresenter.GetClassifyScreeningList(ClassifyThirdActivity.this);
            }
        });
    }

    @Override
    public void getClassifyScreeningListSuccess(List<ClassifyScreeningDataBean.ScreeningItemBean> data) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        data_product_mark = data;
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        userChannelList = new ArrayList<>();
        ClassifyChannelBean all = new ClassifyChannelBean();
        all.setName("全部");
        all.setId(0);
        userChannelList.add(all);
        if (data_classify_third == null || data_classify_third.size() <= 0) {
            return;
        }
        for (HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean temp :
                data_classify_third) {
            ClassifyChannelBean classifyChannelBean = new ClassifyChannelBean();
            classifyChannelBean.setId(temp.getId());
            classifyChannelBean.setName(temp.getName());
            userChannelList.add(classifyChannelBean);
        }
    }

    /**
     * 初始化Column栏目项
     */
    View view;
    LayoutInflater mInflater;
    /**
     * 当前选中的栏目
     */
    private int columnSelectIndex = 0;

    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            Log.e("i===", "" + i);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view = mInflater.inflate(R.layout.item_third_classify, null);
            TextView tv = view.findViewById(R.id.tv_name);
            tv.setId(i);
            tv.setText(userChannelList.get(i).getName());
            tv.setTextColor(getResources().getColor(R.color.color_text3));
            tv.getPaint().setFakeBoldText(false);
            if (columnSelectIndex == i) {
                Log.e("columnSelectIndex===", "" + columnSelectIndex);
                tv.setTextColor(getResources().getColor(R.color.color_text1));
                tv.getPaint().setFakeBoldText(true);
                view.setSelected(true);
            }
            view.setOnClickListener(v -> {
                for (int i1 = 0; i1 < mRadioGroup_content.getChildCount(); i1++) {
                    View localView = mRadioGroup_content.getChildAt(i1);
                    if (localView == v) {
                        viewPager.setCurrentItem(i1);
                        break;
                    }
                }
            });
            mRadioGroup_content.addView(view, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - ScreenUtils.getScreenWidth(this) / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        // 判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                ClassifyThirdActivity.this.finish();
                break;
            case R.id.ll_sort_general:  //综合
                showWindowGeneral(ll_sort_general);
                break;
            case R.id.tv_sales:   //销量
                select_sort_sales = "1";
                select_sort_general = "-1";
                select_sort_price = "-1";
                tv_sort_general.setTextColor(getResources().getColor(R.color.color_text3));
                iv_sort_general.setImageResource(R.mipmap.icon_sort_general_no);
                tv_price.setTextColor(getResources().getColor(R.color.color_text3));
                tv_sales.setTextColor(getResources().getColor(R.color.color_theme));
                iv_sort_price.setImageResource(R.mipmap.icon_sort_price);
                refreshEntity();
                break;
            case R.id.ll_sort_pirce:  //价格
                tv_sort_general.setTextColor(getResources().getColor(R.color.color_text3));
                iv_sort_general.setImageResource(R.mipmap.icon_sort_general_no);
                tv_price.setTextColor(getResources().getColor(R.color.color_theme));
                tv_sales.setTextColor(getResources().getColor(R.color.color_text3));
                if (select_sort_price.equals("2")) { //升序
                    select_sort_price = "3";
                    iv_sort_price.setImageResource(R.mipmap.icon_sort_price_down);
                } else if (select_sort_price.equals("3")) {  //降序
                    select_sort_price = "2";
                    iv_sort_price.setImageResource(R.mipmap.icon_sort_price_up);
                } else {
                    select_sort_price = "2";
                    iv_sort_price.setImageResource(R.mipmap.icon_sort_price_up);
                }
                select_sort_sales = "-1";
                select_sort_general = "-1";
                refreshEntity();
                break;
            case R.id.tv_sort_screening:  //筛选
                showWindowScreening(toolbar);
                break;
        }
    }

    /**
     * 显示筛选
     *
     * @param parent
     */
    private Solve7PopupWindow popupWindowScreening;
    private View view_Screening;
    private RelativeLayout rl_all;
    private LinearLayout ll_operate;
    private RelativeLayout rl_screening;
    Animation animation, animation_out;

    /**
     * 活动筛选
     */
    private RecyclerView rcv_active_type;
    private List<ClassifyScreeningDataBean.ScreeningItemBean> data_active_type;
    private ClassifyScreeningAdapter classifyScreeningAdapter_active;
    private TextView tv_active_screen;
    /**
     * 商品特色
     */
    private RecyclerView rcv_product_mark;
    private List<ClassifyScreeningDataBean.ScreeningItemBean> data_product_mark;
    private ClassifyScreeningAdapter classifyScreeningAdapter_mark;
    private TextView tv_product_mark;
    /**
     * 价格区间
     */
    private EditText ed_lowest_price, ed_highest_price;
    private TextView tv_sure, tv_reset;


    private void showWindowScreening(View parent) {
        if (popupWindowScreening == null) {
            animation = AnimationUtils.loadAnimation(ClassifyThirdActivity.this, R.anim.slide_right_in_500);
            animation.setFillAfter(true);
            animation_out = AnimationUtils.loadAnimation(ClassifyThirdActivity.this, R.anim.slide_right_out_500);
            animation_out.setFillAfter(true);
            animation_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (popupWindowScreening != null) {
                        popupWindowScreening.dismiss();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view_Screening = layoutInflater.inflate(R.layout.layout_classify_screening_popwindow, null);
            rl_screening = view_Screening.findViewById(R.id.rl_screening);
            rl_screening.setOnClickListener(null);
            rl_all = view_Screening.findViewById(R.id.rl_all);
            /*rl_all.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom - oldBottom < -1) {
                        //软键盘弹上去了,动态设置高度为0
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        ll_operate.setLayoutParams(params);

                    } else if (bottom - oldBottom > 1) {
                        //软键盘弹下去了，动态设置高度，恢复原先控件高度
                        //（"1"这个高度值可以换做：屏幕高度的1/3）
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                (int) ScreenUtils.dip2px(ClassifyThirdActivity.this, 62));
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        ll_operate.setLayoutParams(params);
                    }
                }
            });*/
            ll_operate = view_Screening.findViewById(R.id.ll_operate);
            rl_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rl_screening.startAnimation(animation_out);
                }
            });
            tv_active_screen = view_Screening.findViewById(R.id.tv_active_screen);
            tv_product_mark = view_Screening.findViewById(R.id.tv_product_mark);
            rcv_active_type = view_Screening.findViewById(R.id.rcv_active_type);
            rcv_product_mark = view_Screening.findViewById(R.id.rcv_product_mark);
            ed_lowest_price = view_Screening.findViewById(R.id.ed_lowest_price);
            ed_highest_price = view_Screening.findViewById(R.id.ed_highest_price);
            tv_sure = view_Screening.findViewById(R.id.tv_sure);
            tv_reset = view_Screening.findViewById(R.id.tv_reset);
            if (data_active_type != null && data_active_type.size() > 0) {
                tv_active_screen.setVisibility(View.VISIBLE);
                rcv_active_type.setVisibility(View.VISIBLE);
                rcv_active_type.setLayoutManager(new GridLayoutManager(ClassifyThirdActivity.this, 3));
                classifyScreeningAdapter_active = new ClassifyScreeningAdapter(ClassifyThirdActivity.this, data_active_type);
                rcv_active_type.setAdapter(classifyScreeningAdapter_active);
                classifyScreeningAdapter_active.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.tv_name:
                                if (data_active_type.get(position).isSelect()) {
                                    data_active_type.get(position).setSelect(false);
                                } else {
                                    data_active_type.get(position).setSelect(true);
                                }
                                classifyScreeningAdapter_active.notifyDataSetChanged();
                                break;
                        }

                    }
                });
            } else {
                rcv_active_type.setVisibility(View.GONE);
                tv_active_screen.setVisibility(View.GONE);
            }
            if (data_product_mark != null && data_product_mark.size() > 0) {
                tv_product_mark.setVisibility(View.VISIBLE);
                rcv_product_mark.setVisibility(View.VISIBLE);
                rcv_product_mark.setLayoutManager(new GridLayoutManager(ClassifyThirdActivity.this, 3));
                classifyScreeningAdapter_mark = new ClassifyScreeningAdapter(ClassifyThirdActivity.this, data_product_mark);
                rcv_product_mark.setAdapter(classifyScreeningAdapter_mark);
                classifyScreeningAdapter_mark.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.tv_name:
                                for (int i = 0; i < data_product_mark.size(); i++) {
                                    if (i == position) {
                                        data_product_mark.get(position).setSelect(true);
                                        select_screening_mark_ids = data_product_mark.get(position).getId() + "";
                                    } else {
                                        data_product_mark.get(i).setSelect(false);
                                    }
                                }
                                /*if (data_product_mark.get(position).isSelect()) {
                                    data_product_mark.get(position).setSelect(false);
                                } else {
                                    data_product_mark.get(position).setSelect(true);
                                }*/
                                classifyScreeningAdapter_mark.notifyDataSetChanged();

                                break;
                        }

                    }
                });
            } else {
                rcv_product_mark.setVisibility(View.GONE);
                tv_product_mark.setVisibility(View.GONE);
            }

            tv_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (data_product_mark != null && data_product_mark.size() > 0) {
                        select_screening_mark_ids = "&";
                        for (ClassifyScreeningDataBean.ScreeningItemBean item : data_product_mark
                                ) {
                            if (item.isSelect()) {
                                select_screening_mark_ids += item.getId() + "&";
                            }
                        }
                    }*/

                    edit_highest_price = ed_highest_price.getText().toString();
                    edit_lowest_price = ed_lowest_price.getText().toString();
                    if (!StringUtils.isTextEmpty(edit_highest_price) && !StringUtils.isTextEmpty(edit_lowest_price)) {
                        if (Integer.valueOf(edit_highest_price) < Integer.valueOf(edit_lowest_price)) {
                            edit_highest_price = ed_highest_price.getText().toString();
                            edit_lowest_price = ed_lowest_price.getText().toString();
                        }
                    }
                    if (popupWindowScreening != null) {
                        popupWindowScreening.dismiss();
                    }
                    refreshEntity();
                }
            });
            tv_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popupWindowScreening != null) {
                        popupWindowScreening.dismiss();
                    }
                    select_screening_active_ids = "";
                    select_screening_mark_ids = "";
                    edit_highest_price = "";
                    edit_lowest_price = "";
                    refreshEntity();
                }
            });
            // 创建一个PopuWidow对象
            popupWindowScreening = new Solve7PopupWindow(view_Screening, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindowScreening.setAnimationStyle(R.style.AnimPopupwindow);
            popupWindowScreening.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindowScreening.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindowScreening.setFocusable(true);
            // 使其聚集
            /*popupWindowScreening.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            popupWindowScreening.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);*/
            popupWindowScreening.setFocusable(true);
            popupWindowScreening.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });
            // 设置允许在外点击消失
            popupWindowScreening.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindowScreening.setBackgroundDrawable(new BitmapDrawable());


        }

        if (data_active_type != null && data_active_type.size() > 0) {
            for (ClassifyScreeningDataBean.ScreeningItemBean item : data_active_type
            ) {
                if (select_screening_active_ids.contains("&" + item.getId() + "&")) {
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
            }
            classifyScreeningAdapter_active.notifyDataSetChanged();
        }
        if (data_product_mark != null && data_product_mark.size() > 0) {
            /*for (ClassifyScreeningDataBean.ScreeningItemBean item : data_product_mark
                    ) {
                if (select_screening_mark_ids.contains("&" + item.getId() + "&")) {
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
            }*/
            for (ClassifyScreeningDataBean.ScreeningItemBean item : data_product_mark
            ) {
                if (select_screening_mark_ids.equals(item.getId() + "")) {
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
            }
            classifyScreeningAdapter_mark.notifyDataSetChanged();
        }
        ed_highest_price.setText(edit_highest_price);
        ed_lowest_price.setText(edit_lowest_price);
        popupWindowScreening.showAsDropDown(parent);
        rl_screening.startAnimation(animation);
    }

    /**
     * 显示综合选择
     *
     * @param parent
     */
    private Solve7PopupWindow popupWindowGeneral;
    private View view_general;

    private void showWindowGeneral(View parent) {
        if (popupWindowGeneral == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view_general = layoutInflater.inflate(R.layout.layout_classify_general_popwindow, null);
            View tranparent = (View) view_general.findViewById(R.id.tranparent);
            tranparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popupWindowGeneral != null) {
                        popupWindowGeneral.dismiss();
                    }
                }
            });
            final TextView tv_general = (TextView) view_general.findViewById(R.id.tv_general);
            final TextView tv_new = (TextView) view_general.findViewById(R.id.tv_new);
            if (select_sort_general.equals("4")) {  //综合
                tv_general.setTextColor(getResources().getColor(R.color.color_theme));
                tv_new.setTextColor(getResources().getColor(R.color.color_text3));
                tv_general.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_sort_general_sub_yes, 0);
                tv_new.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else if (select_sort_general.equals("1")) {  //新品
                tv_general.setTextColor(getResources().getColor(R.color.color_text3));
                tv_new.setTextColor(getResources().getColor(R.color.color_theme));
                tv_general.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tv_new.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_sort_general_sub_yes, 0);
            } else {
                tv_general.setTextColor(getResources().getColor(R.color.color_text3));
                tv_new.setTextColor(getResources().getColor(R.color.color_text3));
                tv_general.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tv_new.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            tv_general.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_sort_general = "4";
                    select_sort_sales = "-1";
                    select_sort_price = "-1";
                    tv_general.setTextColor(getResources().getColor(R.color.color_theme));
                    tv_new.setTextColor(getResources().getColor(R.color.color_text3));
                    tv_general.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_sort_general_sub_yes, 0);
                    tv_new.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if (popupWindowGeneral != null) {
                        popupWindowGeneral.dismiss();
                    }
                    refreshEntity();
                }
            });

            tv_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_sort_general = "1";
                    select_sort_sales = "-1";
                    select_sort_price = "-1";
                    tv_general.setTextColor(getResources().getColor(R.color.color_text3));
                    tv_new.setTextColor(getResources().getColor(R.color.color_theme));
                    tv_general.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tv_new.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_sort_general_sub_yes, 0);
                    if (popupWindowGeneral != null) {
                        popupWindowGeneral.dismiss();
                    }
                    refreshEntity();
                }
            });

            // 创建一个PopuWidow对象
            popupWindowGeneral = new Solve7PopupWindow(view_general, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindowGeneral.setAnimationStyle(R.style.AnimPopupwindow);
            // 使其聚集
            popupWindowGeneral.setFocusable(true);
            popupWindowGeneral.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (!select_sort_general.equals("-1")) {
                        tv_sales.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_price.setTextColor(getResources().getColor(R.color.color_text3));
                        tv_sort_general.setTextColor(getResources().getColor(R.color.color_theme));
                        iv_sort_price.setImageResource(R.mipmap.icon_sort_price);
                        if (select_sort_general.equals("4")) {
                            tv_sort_general.setText("综合");
                        } else {
                            tv_sort_general.setText("新品");
                        }
                    } else {
                        tv_sort_general.setTextColor(getResources().getColor(R.color.color_text3));
                    }
                }
            });
            // 设置允许在外点击消失
            popupWindowGeneral.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindowGeneral.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindowGeneral.showAsDropDown(parent);
    }


}
