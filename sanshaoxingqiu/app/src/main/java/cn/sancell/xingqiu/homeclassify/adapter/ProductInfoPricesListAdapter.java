package cn.sancell.xingqiu.homeclassify.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
 * Created by ai11 on 2019/8/20.
 */

public class ProductInfoPricesListAdapter extends BaseQuickAdapter<ProductInfoDataBean.PricesData, BaseViewHolder> {
    public ProductInfoPricesListAdapter(int layoutResId, @Nullable List<ProductInfoDataBean.PricesData> data) {
        super(layoutResId, data);
    }

    public ProductInfoPricesListAdapter(@Nullable List<ProductInfoDataBean.PricesData> data) {
        super(R.layout.item_productinfo_prices_list, data);
    }

    public ProductInfoPricesListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProductInfoDataBean.PricesData pricesData) {

    }
}
