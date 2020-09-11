package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homeclassify.ProductPublishEvaluateActivity;
import cn.sancell.xingqiu.homeuser.adapter.EvaluatedProductListAdapter;
import cn.sancell.xingqiu.homeuser.bean.EvaluatedProductListDataBean;
import cn.sancell.xingqiu.homeuser.contract.EvaluatedProductListContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellClassicsHeader;

/**
 * 待评价商品列表界面
 */
public class EvaluatedProductListActivity extends BaseMVPToobarActivity<EvaluatedProductListContract.EvaluatedProductListPresenter>
        implements EvaluatedProductListContract.EvaluatedProductListView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    View mEmptyLayout;
    @BindView(R.id.rcv_evaluate_product_list)
    RecyclerView rcv_evaluate_product_list;
    EvaluatedProductListAdapter evaluatedProductListAdapter;
    private List<EvaluatedProductListDataBean.EvaluatedProductBean> data_evaluated_productList = new ArrayList<>();
    private int totalcount;
    /**
     * 上个界面传来的订单id和包裹id
     */
    private String orderId, warehouseId;

    @Override
    protected EvaluatedProductListContract.EvaluatedProductListPresenter createPresenter() {
        return new EvaluatedProductListContract.EvaluatedProductListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evaluated_product_list;
    }


    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.evaluate_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        warehouseId = getIntent().getStringExtra(Constants.Key.KEY_2);
        rcv_evaluate_product_list.setLayoutManager(new LinearLayoutManager(this));
        evaluatedProductListAdapter = new EvaluatedProductListAdapter(data_evaluated_productList);
        rcv_evaluate_product_list.setAdapter(evaluatedProductListAdapter);
        evaluatedProductListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_item:
//                        Intent intent1 = new Intent(EvaluatedProductListActivity.this, ProductInfoActivity.class);
//                        intent1.putExtra(Constants.Key.KEY_1, data_evaluated_productList.get(position).getGoodsId() + "");
//                        startActivity(intent1);
                        GoodsDetailActivity.start(EvaluatedProductListActivity.this, data_evaluated_productList.get(position).getGoodsId());
                        break;
                    case R.id.tv_evaluate:
                        Intent intent = new Intent(EvaluatedProductListActivity.this, ProductPublishEvaluateActivity.class);
                        intent.putExtra(Constants.Key.KEY_1, data_evaluated_productList.get(position).getOrderId() + "");
                        intent.putExtra(Constants.Key.KEY_2, data_evaluated_productList.get(position).getId() + "");
                        intent.putExtra(Constants.Key.KEY_3, data_evaluated_productList.get(position).getGoodsCoverPic());
                        startActivity(intent);
                        break;
                }
            }
        });
        refreshLayout.setRefreshHeader(new SancellClassicsHeader(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (evaluatedProductListAdapter != null) {
                evaluatedProductListAdapter.resetCurrentPage();
            }
            mPresenter.GetEvaluateProductList(orderId, warehouseId, 1, EvaluatedProductListActivity.this);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (evaluatedProductListAdapter != null) {
                if (data_evaluated_productList.size() < totalcount) {
                    mPresenter.GetEvaluateProductList(orderId, warehouseId, evaluatedProductListAdapter.getNextPage(), EvaluatedProductListActivity.this);
                } else {
                    refreshLayout.setNoMoreData(true);
                }
            } else {
                mPresenter.GetEvaluateProductList(orderId, warehouseId, 1, EvaluatedProductListActivity.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetEvaluateProductList(orderId, warehouseId, 1, this);
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
            if (evaluatedProductListAdapter != null) {
                evaluatedProductListAdapter.resetCurrentPage();
            }
            mPresenter.GetEvaluateProductList(orderId, warehouseId, 1, EvaluatedProductListActivity.this);
        });
    }

    @Override
    public void getEvaluatedProductListSuccess(EvaluatedProductListDataBean dataBean, int page) {
        totalcount = dataBean.getDataCount();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) {
            if (dataBean != null && dataBean.getDataList() != null && dataBean.getDataList().size() > 0) {
                mEmptyLayout.setVisibility(View.GONE);
                mNetworkErrorLayout.setVisibility(View.GONE);
                data_evaluated_productList.clear();
                if (dataBean != null && dataBean.getDataList() != null) {
                    data_evaluated_productList.addAll(dataBean.getDataList());
                }
                evaluatedProductListAdapter.notifyDataSetChanged();
                evaluatedProductListAdapter.correctCurrentPage();
            } else {
                data_evaluated_productList.clear();
                evaluatedProductListAdapter.notifyDataSetChanged();
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (dataBean != null && dataBean.getDataList() != null) {
                data_evaluated_productList.addAll(dataBean.getDataList());
            }
            evaluatedProductListAdapter.notifyDataSetChanged();
            evaluatedProductListAdapter.correctCurrentPage();
        }
        if (data_evaluated_productList.size() == totalcount) {
            refreshLayout.setNoMoreData(true);
        }
    }
}
