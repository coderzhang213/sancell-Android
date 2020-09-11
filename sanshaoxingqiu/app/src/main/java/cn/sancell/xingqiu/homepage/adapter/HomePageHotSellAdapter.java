package cn.sancell.xingqiu.homepage.adapter;

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
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.VipManager;
import cn.sancell.xingqiu.homepage.bean.HomePageHotSellDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomePageHotSellAdapter extends BaseQuickAdapter<HomePageHotSellDataBean.HotSaleData.HotSellBean, BaseViewHolder> {
    private UserBean userBean;

    private int[] icon_hotsell_marks = new int[]{R.mipmap.icon_hotsell_first_mark, R.mipmap.icon_hotsell_second_mark, R.mipmap.icon_hotsell_third_mark,
            R.mipmap.icon_hotsell_mark4, R.mipmap.icon_hotsell_mark5, R.mipmap.icon_hotsell_mark6, R.mipmap.icon_hotsell_mark7, R.mipmap.icon_hotsell_mark8, R.mipmap.icon_hotsell_mark9};

    public HomePageHotSellAdapter(int layoutResId, @Nullable List<HomePageHotSellDataBean.HotSaleData.HotSellBean> data) {
        super(layoutResId, data);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
    }

    public HomePageHotSellAdapter(@Nullable List<HomePageHotSellDataBean.HotSaleData.HotSellBean> data) {
        super(R.layout.item_homepage_hotsell_list, data);
    }

    public HomePageHotSellAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageHotSellDataBean.HotSaleData.HotSellBean item) {
        if (userBean == null) {
            userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        }
        helper.setText(R.id.tv_title, item.getTitle());
        ((TextView) helper.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((RelativeSizeTextView) helper.getView(R.id.tv_price_orig)).setTagText("¥" + StringUtils.getAllPrice(item.marketPriceE2));
        RelativeSizeTextView tv_price = helper.getView(R.id.tv_price);

        tv_price.setTagText(StringUtils.getAllPrice(item.getUserRealPriceE2()));
        helper.getView(R.id.tv_not_member_price_mark).setVisibility(View.GONE);

        if (StringUtils.isTextEmpty(item.getCoverPicThumb())) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        } else {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPicThumb()));
        }
        helper.addOnClickListener(R.id.rl_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        if (viewType == 0) {
            if (position < 9) {
                holder.getView(R.id.iv_ranking_mark).setVisibility(View.VISIBLE);
                holder.setImageResource(R.id.iv_ranking_mark, icon_hotsell_marks[position]);
            } else {
                holder.getView(R.id.iv_ranking_mark).setVisibility(View.GONE);
            }
        }
    }

}
