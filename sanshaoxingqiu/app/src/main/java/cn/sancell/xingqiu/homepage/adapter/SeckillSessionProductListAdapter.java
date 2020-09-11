package cn.sancell.xingqiu.homepage.adapter;

import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionProductListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/9/8.
 */

public class SeckillSessionProductListAdapter extends BaseQuickAdapter<SeckillSessionProductListBean.SeckillProductBean, BaseViewHolder> {
    private int currentPage = 0;

    public SeckillSessionProductListAdapter(int layoutResId, @Nullable List<SeckillSessionProductListBean.SeckillProductBean> data) {
        super(layoutResId, data);
    }

    public SeckillSessionProductListAdapter(@Nullable List<SeckillSessionProductListBean.SeckillProductBean> data) {
        super(R.layout.item_seckill_product_list, data);
    }

    public SeckillSessionProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SeckillSessionProductListBean.SeckillProductBean seckillProductBean) {
        baseViewHolder.setText(R.id.tv_title, seckillProductBean.getGoodsInfo().getTitle());
        if (StringUtils.isTextEmpty(seckillProductBean.getGoodsInfo().getCoverPicThumb())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(seckillProductBean.getGoodsInfo().getCoverPicThumb()));
        }
        ((TextView) baseViewHolder.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(seckillProductBean.getGoodsInfo().marketPriceE2));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText(StringUtils.getAllPrice(seckillProductBean.getSkillPriceE2()));
        ((RelativeSizeTextView)baseViewHolder.getView(R.id.tv_seckill_save_price)).setTagText(StringUtils.getAllPrice(seckillProductBean.getGoodsInfo().marketPriceE2-seckillProductBean.getSkillPriceE2()));
        switch (seckillProductBean.getSeckillStatus()) {
            case 1:  //去秒杀
                baseViewHolder.getView(R.id.iv_seckill_remind).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_other_sell_status).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.iv_go_seckill).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.tv_progress).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.progressbar_seckill).setVisibility(View.VISIBLE);
                ((ProgressBar) baseViewHolder.getView(R.id.progressbar_seckill)).setMax(seckillProductBean.getSeckillGoodsNum());
                ((ProgressBar) baseViewHolder.getView(R.id.progressbar_seckill)).setProgress(seckillProductBean.getSeckillGoodsNum() - seckillProductBean.getStocksNum());
                baseViewHolder.setText(R.id.tv_progress, "已抢" + seckillProductBean.getStocksPercentage());
                break;
            case 2:  //已结束
                baseViewHolder.getView(R.id.tv_progress).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.progressbar_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.iv_seckill_remind).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_other_sell_status).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.tv_other_sell_status, seckillProductBean.getSeckillStatusStr());
                baseViewHolder.getView(R.id.iv_go_seckill).setVisibility(View.GONE);

                break;
            case 3:  //已售完
                baseViewHolder.getView(R.id.iv_seckill_remind).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_other_sell_status).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.tv_other_sell_status, seckillProductBean.getSeckillStatusStr());
                baseViewHolder.getView(R.id.iv_go_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_progress).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.progressbar_seckill).setVisibility(View.VISIBLE);
                ((ProgressBar) baseViewHolder.getView(R.id.progressbar_seckill)).setMax(100);
                ((ProgressBar) baseViewHolder.getView(R.id.progressbar_seckill)).setProgress(100);
                baseViewHolder.setText(R.id.tv_progress, "100%");
                break;
            case 4:  //提醒我
                baseViewHolder.getView(R.id.tv_progress).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.progressbar_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.iv_go_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_other_sell_status).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.iv_seckill_remind).setVisibility(View.VISIBLE);
                baseViewHolder.setImageResource(R.id.iv_seckill_remind, R.mipmap.icon_seckill_remind);
                baseViewHolder.addOnClickListener(R.id.iv_seckill_remind);
                break;
            case 5:  //已经提醒
                baseViewHolder.getView(R.id.tv_progress).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.progressbar_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.iv_go_seckill).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_other_sell_status).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.iv_seckill_remind).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_other_sell_status, "已提醒");
                break;
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
