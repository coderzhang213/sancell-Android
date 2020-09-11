package cn.sancell.xingqiu.usermember.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/9/24.
 */

public class MemberVipGiftSeckillProductListAdapter extends BaseQuickAdapter<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean, BaseViewHolder> {
    Context context;

    public MemberVipGiftSeckillProductListAdapter(int layoutResId, @Nullable List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data) {
        super(layoutResId, data);
    }

    public MemberVipGiftSeckillProductListAdapter(Context context, @Nullable List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data) {
        super(R.layout.item_member_vipgift_product_list, data);
        this.context = context;
    }

    public MemberVipGiftSeckillProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean memberLimitTimeGiftBean) {
        ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(memberLimitTimeGiftBean.getGoodsInfo().getCoverPic()));
        baseViewHolder.setText(R.id.tv_title, memberLimitTimeGiftBean.getGoodsInfo().getTitle());
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText(StringUtils.getAllPrice(memberLimitTimeGiftBean.getGoodsInfo().getUserRealPriceE2()));
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(memberLimitTimeGiftBean.getGoodsInfo().getMarketPriceE2()));
        baseViewHolder.addOnClickListener(R.id.rl_item);
        if (memberLimitTimeGiftBean.getSeckillStockNum() > 0) {
            baseViewHolder.getView(R.id.tv_gift_normal).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.iv_gift_normal).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.tv_gift_other).setVisibility(View.GONE);
            if(memberLimitTimeGiftBean.getSeckillGoodsStatus()==1){  //秒杀中
                baseViewHolder.setText(R.id.tv_gift_normal,"立即抢购");
            }else {
                baseViewHolder.setText(R.id.tv_gift_normal,memberLimitTimeGiftBean.getSeckillGoodsStatusStr());
            }
        } else {
            baseViewHolder.getView(R.id.tv_gift_normal).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.iv_gift_normal).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_gift_other).setVisibility(View.VISIBLE);
        }
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTextColor(context.getResources().getColor(R.color.color_theme));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setStartTextColor(context.getResources().getColor(R.color.color_theme));
    }
}
