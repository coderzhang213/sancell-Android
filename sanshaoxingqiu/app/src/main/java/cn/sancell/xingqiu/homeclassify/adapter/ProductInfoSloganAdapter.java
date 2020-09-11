package cn.sancell.xingqiu.homeclassify.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
 * Created by ai11 on 2019/7/25.
 */

public class ProductInfoSloganAdapter extends BaseQuickAdapter<ProductInfoDataBean.ServerInfoListData.ServerInfo, BaseViewHolder> {
    public ProductInfoSloganAdapter(int layoutResId, @Nullable List<ProductInfoDataBean.ServerInfoListData.ServerInfo> data) {
        super(layoutResId, data);
    }

    public ProductInfoSloganAdapter(@Nullable List<ProductInfoDataBean.ServerInfoListData.ServerInfo> data) {
        super(R.layout.item_productinfo_slogan, data);
    }

    public ProductInfoSloganAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoDataBean.ServerInfoListData.ServerInfo item) {
        helper.setText(R.id.tv_slogan_name, item.getServerName());
        helper.setText(R.id.tv_slogan_desc, item.getServerDesc());
    }
}
