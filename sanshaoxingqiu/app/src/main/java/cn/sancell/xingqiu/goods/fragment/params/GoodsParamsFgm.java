package cn.sancell.xingqiu.goods.fragment.params;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseLazyFragment;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.homeclassify.adapter.ProductInfoParameterListAdapter;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
 * @author Alan_Xiong
 * @desc: 商品详情参数fgm
 * @time 2019-12-27 14:38
 */
public class GoodsParamsFgm extends BaseLazyFragment {

    @BindView(R.id.rcv_product_parameterlist)
    RecyclerView rv_params;

    ProductInfoParameterListAdapter mAdapter;
    private List<ProductInfoDataBean.AttrInfoListData.AttrInfo> mDatas;
    private int mHeight;

    public static GoodsParamsFgm newInstance() {

        Bundle args = new Bundle();
        GoodsParamsFgm fragment = new GoodsParamsFgm();
        fragment.setArguments(args);
        return fragment;
    }

    public void setData(List<ProductInfoDataBean.AttrInfoListData.AttrInfo> datas){
        mDatas = datas;
    }

    public void setMarginHeight(int height){
        mHeight = height;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_product_info_parameterinfo, container, false);
    }

    @Override
    protected void initData() {
        rv_params.setPadding(0,mHeight,0,0);
        rv_params.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mAdapter == null) {
            mAdapter = new ProductInfoParameterListAdapter(mDatas);
            rv_params.setAdapter(mAdapter);
        } else {
            mAdapter.setNewData(mDatas);
        }
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }
}
