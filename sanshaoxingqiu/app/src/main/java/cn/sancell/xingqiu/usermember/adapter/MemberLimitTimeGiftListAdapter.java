package cn.sancell.xingqiu.usermember.adapter;

import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;

/**
 * Created by ai11 on 2019/9/11.
 */

public class MemberLimitTimeGiftListAdapter extends BaseQuickAdapter<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean, BaseViewHolder> {
    private int currentPage = 0;

    public MemberLimitTimeGiftListAdapter(int layoutResId, @Nullable List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data) {
        super(layoutResId, data);
    }

    public MemberLimitTimeGiftListAdapter(@Nullable List<MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean> data) {
        super(R.layout.item_member_vip_all_gift_list, data);
    }

    public MemberLimitTimeGiftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MemberLimitTimeGiftListBean.MemberLimitTimeGiftBean memberLimitTimeGiftBean) {
        baseViewHolder.setText(R.id.tv_title, memberLimitTimeGiftBean.getGoodsInfo().getTitle());
        baseViewHolder.setText(R.id.tv_desc, memberLimitTimeGiftBean.getGoodsInfo().getDesc());
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_market)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) baseViewHolder.getView(R.id.tv_price_market)).setText("市场价¥" + StringUtils.getAllPrice(memberLimitTimeGiftBean.getGoodsInfo().getMarketPriceE2()));
        baseViewHolder.getView(R.id.tv_limit_status).setVisibility(View.VISIBLE);
        baseViewHolder.getView(R.id.tv_price_market).setVisibility(View.VISIBLE);
        baseViewHolder.getView(R.id.iv_normal_mark).setVisibility(View.GONE);
        ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(memberLimitTimeGiftBean.getGoodsInfo().getCoverPic()));
        if (memberLimitTimeGiftBean.getSeckillGoodsStatus() == 1) {  //秒杀中
            baseViewHolder.getView(R.id.tv_limit_time).setVisibility(View.GONE);
            if (memberLimitTimeGiftBean.getSeckillStockNum() > 0) {
                baseViewHolder.setText(R.id.tv_limit_status, memberLimitTimeGiftBean.getSeckillGoodsStatusStr());
                ((TextView) baseViewHolder.getView(R.id.tv_limit_status)).setTextColor(mContext.getResources().getColor(R.color.color_F17000));
            } else {
                baseViewHolder.setText(R.id.tv_limit_status, "已抢光");
                ((TextView) baseViewHolder.getView(R.id.tv_limit_status)).setTextColor(mContext.getResources().getColor(R.color.color_text3));
            }
        } else {
            baseViewHolder.getView(R.id.tv_limit_time).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.tv_limit_status, "即将开始");
            baseViewHolder.setText(R.id.tv_limit_time, memberLimitTimeGiftBean.getSeckillGoodsStatusStr());
            ((TextView) baseViewHolder.getView(R.id.tv_limit_status)).setTextColor(mContext.getResources().getColor(R.color.color_text3));
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
