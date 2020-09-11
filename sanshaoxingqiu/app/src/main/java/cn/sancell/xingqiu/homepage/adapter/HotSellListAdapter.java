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
import cn.sancell.xingqiu.homepage.bean.HomePageHotSellDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/7/5.
 */

public class HotSellListAdapter extends BaseQuickAdapter<HomePageHotSellDataBean.HotSaleData.HotSellBean, BaseViewHolder> {
    private int currentPage = 0;
    private UserBean userBean;
    private int[] icon_hotsell_marks = new int[]{R.mipmap.icon_hotsell_first_mark, R.mipmap.icon_hotsell_second_mark, R.mipmap.icon_hotsell_third_mark,
            R.mipmap.icon_hotsell_mark4, R.mipmap.icon_hotsell_mark5, R.mipmap.icon_hotsell_mark6, R.mipmap.icon_hotsell_mark7, R.mipmap.icon_hotsell_mark8, R.mipmap.icon_hotsell_mark9};


    public HotSellListAdapter(int layoutResId, @Nullable List<HomePageHotSellDataBean.HotSaleData.HotSellBean> data) {
        super(layoutResId, data);
    }

    public HotSellListAdapter(@Nullable List<HomePageHotSellDataBean.HotSaleData.HotSellBean> data) {
        super(R.layout.item_hotsell_list, data);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
    }

    public HotSellListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageHotSellDataBean.HotSaleData.HotSellBean item) {
        if (userBean == null) {
            userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        }
        ((TextView) helper.getView(R.id.tv_price_orig)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
        ((TextView) helper.getView(R.id.tv_price_orig)).setText("¥" + StringUtils.getAllPrice(item.marketPriceE2));
        RelativeSizeTextView tv_price = helper.getView(R.id.tv_price);
        tv_price.setStartText("¥");
        tv_price.setStartProportion(0.72f);
        tv_price.setEndProportion(0.72f);
        tv_price.setEndText(StringUtils.getPriceDecimal(item.getUserRealPriceE2()));
        tv_price.setTagText(StringUtils.getPrice(item.getUserRealPriceE2()));
        helper.getView(R.id.tv_not_member_price_mark).setVisibility(View.GONE);
        if (item.getTagInfo().getDataCount() > 0) {
            String tag = "";
            List<HomePageHotSellDataBean.HotSaleData.HotSellBean.TagInfo.TagBean> data_tag = item.getTagInfo().getDataList();
            for (HomePageHotSellDataBean.HotSaleData.HotSellBean.TagInfo.TagBean temp : data_tag) {
                tag += temp.getTagName() + " ";
            }
            helper.setText(R.id.tv_tags_title, tag);
        }
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPic()));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.addOnClickListener(R.id.rl_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                if (position > 8) {
                    holder.getView(R.id.iv_ranking_mark).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.iv_ranking_mark).setVisibility(View.VISIBLE);
                    holder.setImageResource(R.id.iv_ranking_mark, icon_hotsell_marks[position]);
                }
                break;
        }
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
