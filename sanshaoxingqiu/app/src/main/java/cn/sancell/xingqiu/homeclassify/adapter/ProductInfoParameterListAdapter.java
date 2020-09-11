package cn.sancell.xingqiu.homeclassify.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
 * Created by ai11 on 2019/6/27.
 */

public class ProductInfoParameterListAdapter extends BaseQuickAdapter<ProductInfoDataBean.AttrInfoListData.AttrInfo, BaseViewHolder> {

    public ProductInfoParameterListAdapter(int layoutResId, @Nullable List<ProductInfoDataBean.AttrInfoListData.AttrInfo> data) {
        super(layoutResId, data);
    }

    public ProductInfoParameterListAdapter(@Nullable List<ProductInfoDataBean.AttrInfoListData.AttrInfo> data) {
        super(R.layout.item_productinfo_parameterlist, data);
    }

    public ProductInfoParameterListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoDataBean.AttrInfoListData.AttrInfo item) {
        helper.setText(R.id.tv_attr_name, item.getAttrName());
        helper.setText(R.id.tv_attr_desc, item.getAttrValue());
    }
}
