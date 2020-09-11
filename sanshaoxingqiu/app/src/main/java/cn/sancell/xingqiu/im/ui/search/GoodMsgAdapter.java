package cn.sancell.xingqiu.im.ui.search;

import android.graphics.Paint;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.LikeBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.NumberDinRegularTextView;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/8.
 */

public class GoodMsgAdapter extends BaseQuickAdapter<LikeBean, BaseViewHolder> {
    private int currentPage = 0;
    private UserBean userBean;

    public GoodMsgAdapter(int layoutResId, @Nullable List<LikeBean> data) {
        super(layoutResId, data);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
    }


    public GoodMsgAdapter(@Nullable List<LikeBean> data) {
        super(R.layout.item_homepage_like_list, data);
    }

    public GoodMsgAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikeBean item) {
        if (userBean == null) {
            userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        }
        ((NumberDinRegularTextView) helper.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((NumberDinRegularTextView) helper.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(item.marketPriceE2));
        RelativeSizeTextView tv_price = helper.getView(R.id.tv_price);
        tv_price.setStartText("¥");
        tv_price.setStartProportion(0.72f);
        tv_price.setEndProportion(0.72f);

        tv_price.setTagText(StringUtils.getPrice(item.getUserRealPriceE2()));
        tv_price.setEndText(StringUtils.getPriceDecimal(item.getUserRealPriceE2()));
        helper.getView(R.id.tv_not_member_price_mark).setVisibility(View.GONE);

        if (item.getTagInfo() != null && item.getTagInfo().getDataCount() > 0) {
            helper.getView(R.id.tv_tags_title).setVisibility(View.VISIBLE);
            String tag = "";
            List<LikeBean.TagInfo.TagBean> data_tag = item.getTagInfo().getDataList();
            for (LikeBean.TagInfo.TagBean temp : data_tag) {
                tag += temp.getTagName() + "丨";
            }
            tag = tag.substring(0, tag.length() - 1);
            helper.setText(R.id.tv_tags_title, tag);
        } else {
            helper.getView(R.id.tv_tags_title).setVisibility(View.GONE);
        }
        if (StringUtils.isTextEmpty(item.getCoverPicThumb())) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        } else {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPicThumb()));
        }

        helper.setText(R.id.tv_title, item.getTitle());
        if (item.getSeckillInfo() != null && item.getSeckillInfo().getSeckillGoodsStatus() == 1) {  //秒杀中
            helper.getView(R.id.iv_seckill_mark).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_seckill_mark).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.rl_item);
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
