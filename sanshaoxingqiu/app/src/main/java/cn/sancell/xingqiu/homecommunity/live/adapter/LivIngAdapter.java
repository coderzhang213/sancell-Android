package cn.sancell.xingqiu.homecommunity.live.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveListRes;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
 * @author Alan_Xiong
 * @desc: 直播列表适配器
 * @time 2019-11-27 13:30
 */
public class LivIngAdapter extends BaseMultiItemQuickAdapter<LiveListRes, BaseViewHolder> {

    public LivIngAdapter(@Nullable List<LiveListRes> data) {
        super(data);
        addItemType(0, R.layout.recycle_home_live);
        addItemType(1, R.layout.view_live_not_data_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveListRes item) {
        if (item.getItemType() == 0) {//正常条目
            if (TextUtils.equals(item.liveStatus, "2")) {//1 未开始， 2直播中， 3 已结束
                //播放gif
                AppCompatImageView iv_play = helper.getView(R.id.iv_play);
                Glide.with(mContext).load(R.drawable.living).into(iv_play);

                //直播中
                helper.getView(R.id.ll_living).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_replay).setVisibility(View.GONE);
            } else {
                if (TextUtils.equals(item.liveStatus, "3")) {
                    helper.setText(R.id.tv_live_status, "回放");

                } else {
                    helper.setText(R.id.tv_live_status, "未开始");

                }
                helper.getView(R.id.ll_living).setVisibility(View.GONE);
                helper.getView(R.id.ll_replay).setVisibility(View.VISIBLE);
            }
            helper.getView(R.id.ll_par).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {

                        getOnItemClickListener().onItemClick(LivIngAdapter.this, null, helper.getLayoutPosition());
                    }
                }
            });
            AppCompatImageView icon = helper.getView(R.id.iv_live);
            Glide.with(mContext).load(item.icon).into(icon);
            helper.setText(R.id.tv_title, item.liveName);
            helper.setText(R.id.tv_intro, item.liveIntro);
            LinearLayout ll_goods = helper.getView(R.id.ll_goods);

            AppCompatImageView iv_goods_one = helper.getView(R.id.iv_goods_one);
            AppCompatImageView iv_goods_two = helper.getView(R.id.iv_goods_two);
            AppCompatImageView iv_goods_thr = helper.getView(R.id.iv_goods_thr);
            ImageView iv_more = helper.getView(R.id.iv_more);
            //多少人观看
            TextView tv_audience_num = helper.getView(R.id.tv_audience_num);
            tv_audience_num.setText(item.onlineUser + "人观看");
            //关联图片
            if (item.relationGoodsList != null && item.relationGoodsList.size() > 0) {
                ll_goods.setVisibility(View.VISIBLE);
                if (item.relationGoodsList.size() > 3) {
                    iv_more.setVisibility(View.VISIBLE);
                    showGoodsView(item, iv_goods_one, iv_goods_two, iv_goods_thr);

                } else {
                    iv_more.setVisibility(View.GONE);
                    showGoodsView(item, iv_goods_one, iv_goods_two, iv_goods_thr);
                }

            } else {
                ll_goods.setVisibility(View.GONE);
            }
        } else {//暂无数据条目
            RelativeLayout rl_not_data_layout = helper.getView(R.id.rl_not_data_layout);
            ViewGroup.LayoutParams layoutParams = rl_not_data_layout.getLayoutParams();
            layoutParams.height= ScreenUtils.getScreenHeight(SCApp.getContext());

        }


    }

    /**
     * 判断数据的大小展示
     *
     * @param item
     * @param iv1
     * @param iv2
     * @param iv3
     */
    void showGoodsView(LiveListRes item, AppCompatImageView iv1, AppCompatImageView iv2, AppCompatImageView iv3) {
        if (item.relationGoodsList.size() >= 1) {
            Glide.with(mContext).load(item.relationGoodsList.get(0).coverPicThumb).into(iv1);
        }
        if (item.relationGoodsList.size() >= 2) {
            Glide.with(mContext).load(item.relationGoodsList.get(1).coverPicThumb).into(iv2);
        }
        if (item.relationGoodsList.size() >= 3) {

            Glide.with(mContext).load(item.relationGoodsList.get(2).coverPicThumb).into(iv3);
        }
    }
}
