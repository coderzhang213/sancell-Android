package cn.sancell.xingqiu.goods.fragment.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseLazyMVPFragment;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.goods.entity.req.GoodsCommentReq;
import cn.sancell.xingqiu.homeclassify.adapter.ProductEvaluateListAdapter;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.homepage.ImagePagerActivity;

/**
 * @author Alan_Xiong
 * @desc: 商品评论
 * @time 2019-12-27 14:50
 */
public class GoodsCommentFgm extends BaseLazyMVPFragment<GoodsCommentPresenter> implements GoodsCommentView {


    @BindView(R.id.rcv_evaluate_list)
    RecyclerView rv_comments;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mDataCount;

    /**
     * 评论列表添加头部
     */
    private RelativeLayout rl_evaluate_top;
    //好评率
    private TextView tv_rate_acclaim;
    //评论数量
    private TextView tv_evaluate_num;


    ProductEvaluateListAdapter mAdapter;

    private String mGoodsId;
    private int mCurrentPage = 1;
    private final int pageSize = 10;
    private List<EvaluateListDataBean.EvaluateBean> mList = new ArrayList<>();
    private int mHeight;

    public static GoodsCommentFgm newInstance(String goodsId) {

        Bundle args = new Bundle();
        args.putString(IntentKey.GOODS_ID, goodsId);
        GoodsCommentFgm fragment = new GoodsCommentFgm();
        fragment.setArguments(args);
        return fragment;
    }

    private void getComments() {
        GoodsCommentReq req = new GoodsCommentReq();
        req.goodsId = mGoodsId;
        req.page = mCurrentPage + "";
        req.pageSize = pageSize + "";
        mPresenter.getCommentList(req);
    }

    public void setMarginHeight(int height){
        mHeight = height;
    }

    @Override
    protected GoodsCommentPresenter createPresenter() {
        return new GoodsCommentPresenter(getContext());
    }

    @Override
    protected BaseView getMVPView() {
        return this;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_product_info_evaluate, container, false);
    }

    private void initFresh(){
        refreshLayout.setPadding(0,mHeight,0,0);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getComments();
            refreshLayout.finishRefresh();
        });
    }

    @Override
    protected void initData() {
        mGoodsId = getArguments().getString(IntentKey.GOODS_ID);
        rv_comments.setLayoutManager(new LinearLayoutManager(getContext()));
        getComments();
        initFresh();
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public void getCommentListSuccess(EvaluateListDataBean res) {

        if (res != null && res.getDataList() != null) {
            mDataCount = res.getDataCount();
            mAdapter.addHeaderView(getEvaluateHeaderView());
            rl_evaluate_top.setVisibility(View.VISIBLE);
            tv_evaluate_num.setText(res.getDataCount() + "条评论");
            rl_evaluate_top.setVisibility(View.VISIBLE);
            if (mCurrentPage != 1) {
                mList.addAll(res.getDataList());
                refreshLayout.finishLoadMore();
            } else {
                mList.clear();
                mList = res.getDataList();
            }
            mCurrentPage++;
            showEmpty(false);
            setAdapter();
        } else {
            if (mCurrentPage != 1) {
                //异常
                refreshLayout.finishLoadMore();
                refreshLayout.setEnableLoadMore(false);
            } else {
                if (rl_evaluate_top != null){
                    //第一次没数据未渲染
                    rl_evaluate_top.setVisibility(View.GONE);
                }
                showEmpty(true);
            }
        }
    }

    @Override
    public void getDataError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ProductEvaluateListAdapter(mList, getContext(), itemPicAction);
            mAdapter.addHeaderView(getEvaluateHeaderView());
            rv_comments.setAdapter(mAdapter);
        } else {
            mAdapter.setNewData(mList);
        }
    }

    private void showEmpty(boolean show) {
        if (show) {
            rv_comments.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            rv_comments.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        }
    }


    private View getEvaluateHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_productinfo_evaluate_top, null);
        rl_evaluate_top = view.findViewById(R.id.rl_evaluate_top);
        tv_rate_acclaim = view.findViewById(R.id.tv_rate_acclaim);
        tv_evaluate_num = view.findViewById(R.id.tv_evaluate_num);
        return view;
    }


    private ProductEvaluateListAdapter.ItemPicAction itemPicAction = (data_pics, pos) -> {
        ArrayList<String> data = new ArrayList<>();
        for (EvaluateListDataBean.EvaluateBean.PicArr pic : data_pics) {
            data.add(pic.getCoverPic());
        }
        Intent intent = new Intent(getContext(), ImagePagerActivity.class);
        intent.putStringArrayListExtra(Constants.Key.KEY_1, data);
        intent.putExtra(Constants.Key.KEY_2, pos);
        startActivity(intent);
    };

}
