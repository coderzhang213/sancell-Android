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
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberVipGiftProductListAdapter extends BaseQuickAdapter<LikeBean, BaseViewHolder> {
    Context context;

    public MemberVipGiftProductListAdapter(int layoutResId, @Nullable List<LikeBean> data) {
        super(layoutResId, data);
    }

    public MemberVipGiftProductListAdapter(Context context, @Nullable List<LikeBean> data) {
        super(R.layout.item_member_vipgift_product_list, data);
        this.context = context;
    }

    public MemberVipGiftProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LikeBean likeBean) {
        ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(likeBean.getCoverPic()));
        baseViewHolder.setText(R.id.tv_title, likeBean.getTitle());
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText(StringUtils.getAllPrice(likeBean.getUserRealPriceE2()));
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(likeBean.getMarketPriceE2()));
        baseViewHolder.addOnClickListener(R.id.rl_item);
        baseViewHolder.getView(R.id.tv_gift_normal).setVisibility(View.GONE);
        baseViewHolder.getView(R.id.iv_gift_normal).setVisibility(View.GONE);
        baseViewHolder.getView(R.id.tv_gift_other).setVisibility(View.GONE);
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTextColor(context.getResources().getColor(R.color.color_text1));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setStartTextColor(context.getResources().getColor(R.color.color_text1));

    }
}
