package cn.sancell.xingqiu.homepage;

import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homepage.adapter.HotSellListAdapter;
import cn.sancell.xingqiu.homepage.bean.HomePageHotSellDataBean;
import cn.sancell.xingqiu.homepage.contract.ProductListContract;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;
import cn.sancell.xingqiu.widget.SpaceItemDecoration;

/**
 * 商品列表（首页本周热销排行更多跳转）
 */
public class ProductListActivity extends BaseMVPToobarActivity<ProductListContract.ProductListPresenter>
        implements ProductListContract.ProductListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_product_list)
    RecyclerView rcv_product_list;
    HotSellListAdapter hotSellListAdapter;
    private List<HomePageHotSellDataBean.HotSaleData.HotSellBean> data_productList = new ArrayList<>();

    private String Tag;
    private String titel = "";
    private int totalcount;

    @Override
    protected ProductListContract.ProductListPresenter createPresenter() {
        return new ProductListContract.ProductListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(titel);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        titel = getIntent().getStringExtra(Constants.Key.KEY_1);
        tv_title.setText(titel);
        rcv_product_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rcv_product_list.addItemDecoration(new SpaceItemDecoration(2, ScreenUtils.dip2px(this, 4), ScreenUtils.dip2px(this, 12), ScreenUtils.dip2px(this, 8), ScreenUtils.dip2px(ProductListActivity.this, 12)));
        hotSellListAdapter = new HotSellListAdapter(data_productList);
        hotSellListAdapter.openLoadAnimation();
        rcv_product_list.setAdapter(hotSellListAdapter);
        hotSellListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_item:
//                        Intent intent = new Intent(ProductListActivity.this, ProductInfoActivity.class);
//                        intent.putExtra(Constants.Key.KEY_1, data_productList.get(position).getId() + "");
//                        startActivity(intent);
                        GoodsDetailActivity.start(ProductListActivity.this,Integer.parseInt(data_productList.get(position).getId()));

                        break;
                }
            }
        });
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (hotSellListAdapter != null) {
                    hotSellListAdapter.resetCurrentPage();
                }
                mPresenter.GetProductList(1, ProductListActivity.this);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hotSellListAdapter != null) {
                    if (data_productList.size() < totalcount) {
                        mPresenter.GetProductList(hotSellListAdapter.getNextPage(), ProductListActivity.this);
                    } else {
                        refreshLayout.setNoMoreData(true);
                    }
                } else {
                    mPresenter.GetProductList(1, ProductListActivity.this);
                }
            }
        });
        mPresenter.GetProductList(1, this);
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
                if (hotSellListAdapter != null) {
                    hotSellListAdapter.resetCurrentPage();
                }
                mPresenter.GetProductList(1, ProductListActivity.this);
            }
        });
    }

    @Override
    public void getHotSellListSuccess(HomePageHotSellDataBean homePageHotSellDataBean, int page) {
        totalcount = homePageHotSellDataBean.getHotSaleData().getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            mNetworkErrorLayout.setVisibility(View.GONE);
            data_productList.clear();
            if (homePageHotSellDataBean.getHotSaleData() != null && homePageHotSellDataBean.getHotSaleData().getDataList() != null) {
                data_productList.addAll(homePageHotSellDataBean.getHotSaleData().getDataList());
            }
            hotSellListAdapter.notifyDataSetChanged();
            hotSellListAdapter.correctCurrentPage();
        } else {
            if (homePageHotSellDataBean.getHotSaleData() != null && homePageHotSellDataBean.getHotSaleData().getDataList() != null) {
                data_productList.addAll(homePageHotSellDataBean.getHotSaleData().getDataList());
            }
            hotSellListAdapter.notifyDataSetChanged();
            hotSellListAdapter.correctCurrentPage();
        }
        if (data_productList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
