package cn.sancell.xingqiu.homepage.adapter;

import android.graphics.Paint;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homepage.bean.OneYuanPurchaseBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/10/24.
 */

public class OneYuanPurchaseListAdapter  extends BaseQuickAdapter<OneYuanPurchaseBean.OneYuanPurchaseProductBean, BaseViewHolder> {
    private int currentPage = 0;
    public OneYuanPurchaseListAdapter(int layoutResId, @Nullable List<OneYuanPurchaseBean.OneYuanPurchaseProductBean> data) {
        super(layoutResId, data);
    }

    public OneYuanPurchaseListAdapter(@Nullable List<OneYuanPurchaseBean.OneYuanPurchaseProductBean> data) {
        super(R.layout.item_one_yuan_purchase_list,data);
    }

    public OneYuanPurchaseListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OneYuanPurchaseBean.OneYuanPurchaseProductBean oneYuanPurchaseProductBean) {
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(oneYuanPurchaseProductBean.getMarketPriceE2()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText(StringUtils.getPrice(oneYuanPurchaseProductBean.getOnePriceE2()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setEndText(StringUtils.getPriceDecimal(oneYuanPurchaseProductBean.getOnePriceE2()));
        ((SimpleDraweeView)baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(oneYuanPurchaseProductBean.getCoverPicThumb()));
        baseViewHolder.setText(R.id.tv_title, oneYuanPurchaseProductBean.getTitle());
        if(oneYuanPurchaseProductBean.getSellStockNumber()>0){
            baseViewHolder.setText(R.id.tv_status,"立即抢购");
            baseViewHolder.getView(R.id.tv_status).setBackgroundResource(R.drawable.round_color_e12c25_12);
        }else {
            baseViewHolder.setText(R.id.tv_status,oneYuanPurchaseProductBean.getAppInfo());
            baseViewHolder.getView(R.id.tv_status).setBackgroundResource(R.drawable.round_color_5f4141_12);
        }
        baseViewHolder.addOnClickListener(R.id.rl_item);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getNextPage() {
        return (this.currentPage + 1);
    }

    public void correctCurrentPage() {
        this.currentPage += 1;
    }

    public void resetCurrentPage() {
        this.currentPage = 0;
    }
}
