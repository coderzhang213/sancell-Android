package cn.sancell.xingqiu.im.dialog.adapter;

import android.graphics.Paint;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.util.FontUtils;

public class LiveGoodRelAdapter extends BaseQuickAdapter<VideoRelationRes.InfoList, BaseViewHolder> {


    public LiveGoodRelAdapter(@Nullable List<VideoRelationRes.InfoList> data) {
        super(R.layout.view_live_crem_layout, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, VideoRelationRes.InfoList item) {
        AppCompatImageView iv_goods = helper.getView(R.id.iv_goods);
        Glide.with(mContext).load(item.coverPicThumb).into(iv_goods);
        if (TextUtils.isEmpty(item.titleAlias)) {
            helper.setText(R.id.tv_name, item.title);
        } else {
            helper.setText(R.id.tv_name, item.titleAlias);
        }
        helper.setText(R.id.tv_sort, helper.getLayoutPosition() + 1 + "");

        helper.setText(R.id.tv_real_price, FontUtils.getInstance().changeTextSize(mContext, 14,
                String.format(mContext.getResources().getString(R.string.price_rmb), item.getRelPrice()), 0, 1));

        AppCompatTextView tv_markPrice = helper.getView(R.id.tv_price_market);
        tv_markPrice.setText(String.format(mContext.getResources().getString(R.string.price_rmb), item.getNewMarketPriceE2()));

        helper.setText(R.id.tv_cj_sum, item.liveVolume + "");
        helper.setText(R.id.tv_click_sum, item.goodsBrowse + "");
        tv_markPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        helper.addOnClickListener(R.id.rl_item);

    }


}
