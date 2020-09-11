package cn.sancell.xingqiu.im.dialog.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.VipManager;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.util.FontUtils;

public class VideoGoodRelAdapter extends BaseQuickAdapter<VideoRelationRes.InfoList, BaseViewHolder> {


    public VideoGoodRelAdapter(@Nullable List<VideoRelationRes.InfoList> data) {
        super(R.layout.recycle_dialog_video_rel_good_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, VideoRelationRes.InfoList item) {
        AppCompatImageView iv_goods = helper.getView(R.id.iv_goods);
        RelativeLayout rl_sold_out = helper.getView(R.id.rl_sold_out);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_watch = helper.getView(R.id.tv_watch);
        TextView tv_real_price = helper.getView(R.id.tv_real_price);
        if (item.stock > 0) {//判断售罄显示
            rl_sold_out.setVisibility(View.GONE);
            tv_name.setTextColor(Color.parseColor("#191A19"));
            tv_watch.setTextColor(Color.parseColor("#FF4019"));
            tv_real_price.setTextColor(Color.parseColor("#FA1905"));
        } else {
            rl_sold_out.setVisibility(View.VISIBLE);
            tv_name.setTextColor(Color.parseColor("#AFB1B3"));
            tv_watch.setTextColor(Color.parseColor("#AFB1B3"));
            tv_real_price.setTextColor(Color.parseColor("#AFB1B3"));
        }
        Glide.with(mContext).load(item.coverPicThumb).into(iv_goods);
        if (TextUtils.isEmpty(item.titleAlias)) {
            tv_name.setText(item.title);
        } else {
            tv_name.setText(item.titleAlias);
        }

        helper.setText(R.id.tv_sort, helper.getLayoutPosition() + 1 + "");
        tv_real_price.setText(FontUtils.getInstance().changeTextSize(mContext, 14,
                String.format(mContext.getResources().getString(R.string.price_rmb), item.getRelPrice()), 0, 1));

        AppCompatTextView tv_markPrice = helper.getView(R.id.tv_price_market);
        tv_markPrice.setText(String.format(mContext.getResources().getString(R.string.price_rmb), item.getNewMarketPriceE2()));

        tv_markPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        helper.addOnClickListener(R.id.iv_add_com);
        helper.addOnClickListener(R.id.rl_item);

    }


}
