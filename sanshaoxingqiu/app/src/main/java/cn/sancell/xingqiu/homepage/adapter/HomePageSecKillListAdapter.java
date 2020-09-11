package cn.sancell.xingqiu.homepage.adapter;

import android.graphics.Paint;
import android.net.Uri;

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
 * Created by ai11 on 2019/9/5.
 */

public class HomePageSecKillListAdapter extends BaseQuickAdapter<SeckillSessionProductListBean.SeckillProductBean, BaseViewHolder> {
    public HomePageSecKillListAdapter(int layoutResId, @Nullable List<SeckillSessionProductListBean.SeckillProductBean> data) {
        super(layoutResId, data);
    }

    public HomePageSecKillListAdapter(@Nullable List<SeckillSessionProductListBean.SeckillProductBean> data) {
        super(R.layout.item_homepage_seckill_list, data);
    }

    public HomePageSecKillListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SeckillSessionProductListBean.SeckillProductBean secKillProductBean) {
        if (StringUtils.isTextEmpty(secKillProductBean.getGoodsInfo().getCoverPicThumb())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(secKillProductBean.getGoodsInfo().getCoverPicThumb()));
        }

        baseViewHolder.setText(R.id.tv_title, secKillProductBean.getGoodsInfo().getTitle());
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(secKillProductBean.getGoodsInfo().marketPriceE2));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText(StringUtils.getPrice(secKillProductBean.getSkillPriceE2()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setEndText(StringUtils.getPriceDecimal(secKillProductBean.getSkillPriceE2()));
        ((RelativeSizeTextView)baseViewHolder.getView(R.id.tv_seckill_save_price)).setTagText(StringUtils.getAllPrice(secKillProductBean.getGoodsInfo().marketPriceE2-secKillProductBean.getSkillPriceE2()));
        baseViewHolder.addOnClickListener(R.id.rl_item);
        baseViewHolder.addOnClickListener(R.id.iv_seckill_go);
    }
}
