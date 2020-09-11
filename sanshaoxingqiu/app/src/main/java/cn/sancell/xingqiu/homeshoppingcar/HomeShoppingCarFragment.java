package cn.sancell.xingqiu.homeshoppingcar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPFragment;
import cn.sancell.xingqiu.base.fragment.BaseNotDataFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homepage.adapter.HomepageLikeListAdapter;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.homeshoppingcar.adapter.HomeShoppingCarAdapter;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.homeshoppingcar.contract.HomeShoppingCarContract;
import cn.sancell.xingqiu.homeuser.EvaluatedProductListActivity;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.MaxRecyclerView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;
import cn.sancell.xingqiu.widget.SpaceItemDecoration;

/**
 * 购物车界面
 */
public class HomeShoppingCarFragment extends BaseNotDataFragment<HomeShoppingCarContract.HomeShoppingCarPresenter>
        implements HomeShoppingCarContract.HomeShoppingCarView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_edit)
    TextView tv_edit;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_shoppingcar_list)
    MaxRecyclerView rcv_shoppingcar_list;
    HomeShoppingCarAdapter homeShoppingCarAdapter;
    private List<HomeShoppingCarDataBean.ShopingCarProductBean> data_shoppingcar_list;
    private int invalid_nostock_num;
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rcv_like_list)
    MaxRecyclerView rcv_like_list;
    HomepageLikeListAdapter homepageLikeListAdapter;
    private List<LikeBean> data_likeList = new ArrayList<>();

    @BindView(R.id.iv_select_all)
    ImageView iv_select_all;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.tv_all_price)
    RelativeSizeTextView tv_all_price;

    private boolean isEditorStatus;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homeshopping_car;
    }

    @Override
    protected void initViewListener() {
    }

    @Override
    protected void initData() {
        mPresenter.GetLikeList(1, getActivity());
    }

    @Override
    protected void initView() {
        int statusHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
            lp.topMargin = statusHeight;
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) tv_edit.getLayoutParams();
            lp1.topMargin = statusHeight;
            tv_title.setLayoutParams(lp);
            tv_edit.setLayoutParams(lp1);
        }
        initial();
    }

    @Override
    protected void onReloadData() {
        if (homepageLikeListAdapter != null) {
            homepageLikeListAdapter.resetCurrentPage();
        }
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mPresenter.GetShoppingCarList(getActivity(), false);
        }
    }

    @Override
    public boolean isLoadNotDat() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    public void initial() {
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(getActivity()));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.GetShoppingCarList(getActivity(), false);
            if (homepageLikeListAdapter != null) {
                homepageLikeListAdapter.resetCurrentPage();
            }
            mPresenter.GetLikeList(1, getActivity());
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (homepageLikeListAdapter != null) {
                mPresenter.GetLikeList(homepageLikeListAdapter.getNextPage(), getActivity());
            } else {
                mPresenter.GetLikeList(1, getActivity());
            }
        });
        rcv_shoppingcar_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_all_price.setTagText("0");
        tv_account.setText(R.string.noselect_account);
        tv_edit.setOnClickListener(view -> {   //编辑按钮功能
            if (homeShoppingCarAdapter != null && homeShoppingCarAdapter.getSelectData() != null) {
                if (isEditorStatus) {  //结算
                    isEditorStatus = false;
                    tv_edit.setText(R.string.editor);
                    tv_all_price.setVisibility(View.VISIBLE);
                    tv_account.setText(getResources().getString(R.string.account_with_brackets) + homeShoppingCarAdapter.getSelectData().size() + "）");
                } else {  //编辑
                    isEditorStatus = true;
                    tv_edit.setText(R.string.finish);
                    tv_all_price.setVisibility(View.GONE);
                    tv_account.setText(getResources().getString(R.string.delete_with_brackets) + homeShoppingCarAdapter.getSelectData().size() + "）");
                }
                refreshPrice();
            }
        });
        tv_account.setOnClickListener(view -> {
            if (isEditorStatus) {  //删除操作
                String carIds = "";
                if (homeShoppingCarAdapter.getSelectData() != null) {
                    for (int i = 0; i < homeShoppingCarAdapter.getSelectData().size(); i++) {
                        if (i == homeShoppingCarAdapter.getSelectData().size() - 1) {
                            carIds += homeShoppingCarAdapter.getSelectData().get(i).getCarId();
                        } else {
                            carIds += homeShoppingCarAdapter.getSelectData().get(i).getCarId() + "-";
                        }
                    }
                    mPresenter.BatchDeleteShoppingCar(carIds, getContext());
                }
            } else {  //结算操作
                if (homeShoppingCarAdapter.getSelectData() != null && homeShoppingCarAdapter.getSelectData().size() > 0) {
                    mPresenter.GetShoppingCarList(getActivity(), true);
                }
            }
        });
        iv_select_all.setOnClickListener(view -> {  //全选按钮功能
            if (null == homeShoppingCarAdapter) {
                return;
            }
            if (homeShoppingCarAdapter.getSelectData() == null) {
                return;
            }
            boolean isSelectAll = true;
            if (data_shoppingcar_list.size() - invalid_nostock_num == homeShoppingCarAdapter.getSelectData().size()) {
                isSelectAll = false;
            } else {
                isSelectAll = true;
            }
            mPresenter.ModifyAllShoppingCarSelectStatus(isSelectAll ? 1 : 2, getActivity());
            /*for (HomeShoppingCarDataBean.ShopingCarProductBean item : data_shoppingcar_list) {
                if (item.getType() == 1) {
                    item.setIsSelected(isSelectAll ? 1 : 2);
                }
            }
            homeShoppingCarAdapter.setSelectData();
            // 更改全选按钮状态
            iv_select_all
                    .setImageResource(isSelectAll ? R.mipmap.icon_car_select_yes
                            : R.mipmap.icon_car_select_no);
            homeShoppingCarAdapter.notifyDataSetChanged();
            refreshPrice();*/
        });

        rcv_shoppingcar_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv_like_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rcv_like_list.addItemDecoration(new SpaceItemDecoration(2, ScreenUtils.dip2px(getActivity(), 4), ScreenUtils.dip2px(getActivity(), 12), ScreenUtils.dip2px(getActivity(), 8), ScreenUtils.dip2px(getActivity(), 12)));
        rcv_shoppingcar_list.setHasFixedSize(true);
        rcv_shoppingcar_list.setNestedScrollingEnabled(false);
        rcv_like_list.setHasFixedSize(true);
        rcv_like_list.setNestedScrollingEnabled(false);
        homepageLikeListAdapter = new HomepageLikeListAdapter(R.layout.item_homepage_like_list, data_likeList);
        homepageLikeListAdapter.openLoadAnimation();
        rcv_like_list.setAdapter(homepageLikeListAdapter);
        homepageLikeListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.rl_item:
//                    Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
//                    intent.putExtra(Constants.Key.KEY_1, data_likeList.get(position).getId() + "");
//                    startActivity(intent);
                    GoodsDetailActivity.start(getActivity(), data_likeList.get(position).getId());

                    break;
            }
        });

    }

    /**
     * 刷新价格
     */
    public void refreshPrice() {
        if (homeShoppingCarAdapter != null && homeShoppingCarAdapter.getSelectData() != null) {
            if (homeShoppingCarAdapter.getSelectData().size() > 0 && homeShoppingCarAdapter.getSelectData().size() == data_shoppingcar_list.size() - invalid_nostock_num) {
                iv_select_all.setImageResource(R.mipmap.icon_car_select_yes);
            } else {
                iv_select_all.setImageResource(R.mipmap.icon_car_select_no);
            }
            if (isEditorStatus) {
                tv_account.setText(getResources().getString(R.string.delete_with_brackets) + homeShoppingCarAdapter.getSelectData().size() + "）");
            } else {
                int totalPrice = homeShoppingCarAdapter.getTotlePrice();
                tv_all_price.setTagText(StringUtils.getPrice(totalPrice));
                tv_all_price.setEndText(StringUtils.getPriceDecimal(totalPrice));
                tv_all_price.setEndProportion(0.75f);
                tv_account.setText(getResources().getString(R.string.account_with_brackets) + homeShoppingCarAdapter.getSelectData().size() + "）");
            }
        }

    }

    @Override
    protected HomeShoppingCarContract.HomeShoppingCarPresenter createPresenter() {
        return new HomeShoppingCarContract.HomeShoppingCarPresenter();
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    public Object initAnalyticsScreenName() {
        return null;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        showNewWorkError();
    }

    @Override
    public void getShoppingCarListSuccess(List<HomeShoppingCarDataBean.ShopingCarProductBean> dataList, int invalid_nostock_num, boolean isCheckData) {
        goneNewWorkError();
        refreshLayout.finishRefresh();
        data_shoppingcar_list = null;
        homeShoppingCarAdapter = null;
        data_shoppingcar_list = dataList;
        if (data_shoppingcar_list != null && data_shoppingcar_list.size() > 0) {
            this.invalid_nostock_num = invalid_nostock_num;
            if (data_shoppingcar_list.size() - invalid_nostock_num == 0) {
                tv_edit.setTextColor(getResources().getColor(R.color.color_text4));
                tv_edit.setClickable(false);
                iv_select_all.setClickable(false);
            } else {
                tv_edit.setTextColor(getResources().getColor(R.color.color_text1));
                tv_edit.setClickable(true);
                iv_select_all.setClickable(true);
            }
            homeShoppingCarAdapter = new HomeShoppingCarAdapter(data_shoppingcar_list);
            homeShoppingCarAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                switch (view.getId()) {
                    case R.id.iv_select:
                        mPresenter.ModifyShoppingCarSelectStatus(data_shoppingcar_list.get(position).getCarId(), data_shoppingcar_list.get(position).getIsSelected() == 1 ? 2 : 1, position, getActivity());
                        break;
                    case R.id.iv_jia:
                        if (data_shoppingcar_list.get(position).getGoodsStockNumber() == data_shoppingcar_list.get(position).getGoodsNum() || data_shoppingcar_list.get(position).getGoodsNum() == 200) {
                            return;
                        }
                        if (data_shoppingcar_list.get(position).getGoodsNum() < data_shoppingcar_list.get(position).getGoodsMinBuyNum()) {
                            mPresenter.NumAddShoppingCar(data_shoppingcar_list.get(position).getCarId(), data_shoppingcar_list.get(position).getGoodsMinBuyNum() - data_shoppingcar_list.get(position).getGoodsNum(), position, getActivity());
                        } else {
                            mPresenter.NumAddShoppingCar(data_shoppingcar_list.get(position).getCarId(), 1, position, getActivity());
                        }
                        break;
                    case R.id.iv_jian:
                        if (data_shoppingcar_list.get(position).getGoodsNum() <= 1) {
                            return;
                        }
                        if (data_shoppingcar_list.get(position).getGoodsNum() <= data_shoppingcar_list.get(position).getGoodsMinBuyNum()) {
                            return;
                        }
                        mPresenter.NumDecreaseShoppingCar(data_shoppingcar_list.get(position).getCarId(), 1, position, getActivity());
                        break;
                    case R.id.tv_clear_invalid:  //清除无效商品
                        mPresenter.ClearInvalid(getActivity());
                        break;
                    case R.id.tv_clear_nostock:  //清除无库存商品
                        mPresenter.ClearUnderStock(getActivity());
                        break;
                    case R.id.tv_arrival_reminder:  //到货提醒

                        break;
                    case R.id.tv_delete:  //商品删除
                        mPresenter.DeleteShoppingCar(data_shoppingcar_list.get(position).getCarId(), getActivity());
                        break;
                    case R.id.rl_item:
//                        Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
//                        intent.putExtra(Constants.Key.KEY_1, data_shoppingcar_list.get(position).getGoodsId() + "");
//                        startActivity(intent);
                        GoodsDetailActivity.start(getActivity(),Integer.parseInt(data_shoppingcar_list.get(position).getGoodsId()));
                        break;
                    case R.id.tv_product_num:  //修改数量
                        showModifyPruductNumDialog(position);
                        break;
                }
            });
            rcv_shoppingcar_list.setAdapter(homeShoppingCarAdapter);
            mEmptyLayout.setVisibility(View.GONE);
            refreshPrice();
        } else {
            homeShoppingCarAdapter = new HomeShoppingCarAdapter(data_shoppingcar_list);
            rcv_shoppingcar_list.setAdapter(homeShoppingCarAdapter);
            mEmptyLayout.setVisibility(View.VISIBLE);
            tv_edit.setTextColor(getResources().getColor(R.color.color_text4));
            tv_edit.setClickable(false);
            iv_select_all.setClickable(false);
            refreshPrice();
        }
        if (isCheckData) {  //结算按钮点击之后重新获取购物车数据来检查当前商品状态是否发生变化
            for (HomeShoppingCarDataBean.ShopingCarProductBean temp : homeShoppingCarAdapter.getSelectData()
            ) {
                if (temp.getGoodsNum() < temp.getGoodsMinBuyNum()) {
                    SCApp.getInstance().showSystemCenterToast(R.string.product_changed_tip);
                    return;
                }
            }
            Intent intent = new Intent(getActivity(), ProductCreateOrderActivity.class);
            intent.putExtra(Constants.Key.KEY_1, (Serializable) homeShoppingCarAdapter.getSelectData());
            intent.putExtra(Constants.Key.KEY_2, homeShoppingCarAdapter.getTotlePrice());
            intent.putExtra(Constants.Key.KEY_3, homeShoppingCarAdapter.getTotleRedPacketReward());
            startActivity(intent);
        }
    }

    @Override
    public void deleteShoppingCarSuccess() {
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    @Override
    public void batchDeleteShoppingCarSuccess() {
        isEditorStatus = false;
        tv_edit.setText(R.string.editor);
        tv_all_price.setVisibility(View.VISIBLE);
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    @Override
    public void numAddShoppingCarSuccess(int pos, int goodsNum) {
        data_shoppingcar_list.get(pos).setGoodsNum(data_shoppingcar_list.get(pos).getGoodsNum() + goodsNum);
        homeShoppingCarAdapter.notifyDataSetChanged();
        refreshPrice();
    }

    @Override
    public void numDecreaseShoppingCarSuccess(int pos, int goodsNum) {
        data_shoppingcar_list.get(pos).setGoodsNum(data_shoppingcar_list.get(pos).getGoodsNum() - goodsNum);
        homeShoppingCarAdapter.notifyDataSetChanged();
        refreshPrice();
    }

    @Override
    public void modifyShoppingCarSelectStatusSuccess(int pos) {
        data_shoppingcar_list.get(pos).setIsSelected(data_shoppingcar_list.get(pos).getIsSelected() == 1 ? 2 : 1);
        homeShoppingCarAdapter.notifyDataSetChanged();
        if (data_shoppingcar_list.get(pos).getIsSelected() == 1) {
            homeShoppingCarAdapter.addSelectData(data_shoppingcar_list.get(pos));
        } else {
            homeShoppingCarAdapter.removeSelectData(data_shoppingcar_list.get(pos));
        }
        refreshPrice();
    }

    /**
     * 全选按钮数据回调
     */
    @Override
    public void modifyAllShoppingCarSelectStatusSuccess() {
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    @Override
    public void clearUnderStockSuccess() {
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    @Override
    public void clearInvalidSuccess() {
        mPresenter.GetShoppingCarList(getActivity(), false);
    }

    @Override
    public void getLikeListSuccess(HomePageLikeListDataBean dataLike, int page) {
        refreshLayout.finishLoadMore();
        if (page == 1) {
            goneNewWorkError();
            data_likeList.clear();
            data_likeList.addAll(dataLike.getLikeData().getDataList());
            homepageLikeListAdapter.notifyDataSetChanged();
            homepageLikeListAdapter.correctCurrentPage();
        } else {
            data_likeList.addAll(dataLike.getLikeData().getDataList());
            homepageLikeListAdapter.notifyDataSetChanged();
            homepageLikeListAdapter.correctCurrentPage();
        }
        if (data_likeList.size() == dataLike.getLikeData().getDataCount()) {
            refreshLayout.setNoMoreData(true);
        }
    }

    /**
     * 修改商品数量对话框
     */
    private Dialog dialog_modify_num;
    private int dialog_num;

    public void showModifyPruductNumDialog(int postion) {
        View view = getLayoutInflater().inflate(R.layout.dialog_car_modify_productnum,
                null);
        dialog_modify_num = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog_modify_num.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_modify_num.setContentView(view, new ViewGroup.LayoutParams((ScreenUtils.getScreenWidth(getActivity()) * 295 / 375),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 设置点击外围解散
        dialog_modify_num.setCanceledOnTouchOutside(true);
        EditText ed_dialog_num = view.findViewById(R.id.ed_dialog_num);
        dialog_num = data_shoppingcar_list.get(postion).getGoodsNum();
        ed_dialog_num.setText(data_shoppingcar_list.get(postion).getGoodsNum() + "");
        ed_dialog_num.setSelection(ed_dialog_num.getText().length());
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
                        dialog_num = 1;
                        ed_dialog_num.setText(dialog_num + "");
                        ed_dialog_num.setSelection(ed_dialog_num.getText().length());
                    } else if (num <= data_shoppingcar_list.get(postion).getGoodsMinBuyNum()) {
                        dialog_num = num;
                    } else {
                        dialog_num = num;
                    }
                    if (num > 200 || num > data_shoppingcar_list.get(postion).getGoodsStockNumber()) {
                        if (data_shoppingcar_list.get(postion).getGoodsStockNumber() > 200) {
                            dialog_num = 200;
                            SCApp.getInstance().showSystemCenterToast("最多只能买200件哦！");
                        } else {
                            dialog_num = data_shoppingcar_list.get(postion).getGoodsStockNumber();
                            SCApp.getInstance().showSystemCenterToast("最多只能买" + dialog_num + "件哦！");
                        }
                        ed_dialog_num.setText(dialog_num + "");
                        ed_dialog_num.setSelection(ed_dialog_num.getText().length());
                    }
                } else {
                    dialog_num = 0;
                }
            }
        });
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        /** 3.自动弹出软键盘 **/
        dialog_modify_num.setOnShowListener(dialog -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(ed_dialog_num, InputMethodManager.SHOW_IMPLICIT);
        });
        tv_cancel.setOnClickListener(view1 -> dialog_modify_num.dismiss());
        tv_sure.setOnClickListener(view12 -> {
            if (dialog_num == 0) {
                return;
            }
            dialog_modify_num.dismiss();
            if (dialog_num > data_shoppingcar_list.get(postion).getGoodsNum()) {
                mPresenter.NumAddShoppingCar(data_shoppingcar_list.get(postion).getCarId(), dialog_num - data_shoppingcar_list.get(postion).getGoodsNum(), postion, getActivity());
            } else if (dialog_num < data_shoppingcar_list.get(postion).getGoodsNum()) {
                mPresenter.NumDecreaseShoppingCar(data_shoppingcar_list.get(postion).getCarId(), data_shoppingcar_list.get(postion).getGoodsNum() - dialog_num, postion, getActivity());
            }
        });
        dialog_modify_num.show();

        ed_dialog_num.postDelayed(() -> {
            ed_dialog_num.requestFocus();
            InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null) manager.showSoftInput(ed_dialog_num, 0);
        }, 300);
    }


}
