package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OrderInfoPackProductListAdapter extends BaseQuickAdapter<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean, BaseViewHolder> {
    OrderInfoPackListAdapter.ProductClickAction productClickAction;

    public OrderInfoPackProductListAdapter(int layoutResId, @Nullable List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data) {
        super(layoutResId, data);
    }

    public OrderInfoPackProductListAdapter(@Nullable List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data, OrderInfoPackListAdapter.ProductClickAction productClickAction) {
        super(R.layout.item_orderinfo_pack_productlist, data);
        this.productClickAction = productClickAction;
    }

    public OrderInfoPackProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean item) {
        helper.setText(R.id.tv_product_name, item.getOrderDetailTitle());
        helper.setText(R.id.tv_product_specs, item.getGoodsSpecification());
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setTagText(StringUtils.getPrice(item.getTotalAmtE2() / item.getGoodsNum()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndText(StringUtils.getPriceDecimal(item.getTotalAmtE2() / item.getGoodsNum()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndProportion(0.78f);
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartText("¥");
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartProportion(0.78f);
        helper.setText(R.id.tv_product_num, item.getGoodsNum() + "件");
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(8f));
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getGoodsCoverPic()));
        helper.getView(R.id.rl_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickAction.productClick(item.getGoodsId() + "");
            }
        });
    }
}
