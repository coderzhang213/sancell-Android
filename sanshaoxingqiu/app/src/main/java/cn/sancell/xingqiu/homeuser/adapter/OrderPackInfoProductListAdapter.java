package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/7/6.
 */

public class OrderPackInfoProductListAdapter extends BaseQuickAdapter<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean, BaseViewHolder> {

    public OrderPackInfoProductListAdapter(int layoutResId, @Nullable List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data) {
        super(layoutResId, data);
    }

    public OrderPackInfoProductListAdapter(@Nullable List<OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean> data) {
        super(R.layout.item_order_new_packinfo_productlist, data);
    }

    public OrderPackInfoProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OderInfoDataBean.PackListDataBean.PackBean.ProductListBean.ProductBean item) {
        helper.setText(R.id.tv_product_name, item.getOrderDetailTitle());
        helper.setText(R.id.tv_product_specs, item.getGoodsSpecification());
//        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setTagText(StringUtils.getPrice(item.getTotalAmtE2() / item.getGoodsNum()));
//        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndText(StringUtils.getPriceDecimal(item.getTotalAmtE2() / item.getGoodsNum()));
//        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndProportion(0.78f);
//        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartText("¥");
//        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartProportion(0.78f);
        //   helper.setText(R.id.tv_product_num,  item.getGoodsNum()+"件");
        helper.setText(R.id.tv_goods_price, "¥"+StringUtils.getAllPrice(item.getTotalAmtE2()/item.getGoodsNum()));
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(8f));
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getGoodsCoverPic()));
        helper.addOnClickListener(R.id.rl_item);
        AppCompatTextView tvRemarkTitle = helper.getView(R.id.tv_remark_title);
        AppCompatTextView tvRemarkDesc = helper.getView(R.id.tv_remark_detail);
        if (!TextUtils.isEmpty(item.mRemark)) {
            tvRemarkTitle.setVisibility(View.VISIBLE);
            tvRemarkDesc.setVisibility(View.VISIBLE);
            tvRemarkDesc.setText(item.mRemark);
        } else {
            tvRemarkTitle.setVisibility(View.GONE);
            tvRemarkDesc.setVisibility(View.GONE);
        }

    }
}
