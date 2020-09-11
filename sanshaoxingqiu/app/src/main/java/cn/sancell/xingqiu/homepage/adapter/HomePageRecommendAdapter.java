package cn.sancell.xingqiu.homepage.adapter;

import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homepage.bean.HomePageRecommDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomePageRecommendAdapter extends BaseQuickAdapter<HomePageRecommDataBean.RecommData.RecommBean, BaseViewHolder> {
    private UserBean userBean;

    public HomePageRecommendAdapter(int layoutResId, @Nullable List<HomePageRecommDataBean.RecommData.RecommBean> data) {
        super(layoutResId, data);
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
    }

    public HomePageRecommendAdapter(@Nullable List<HomePageRecommDataBean.RecommData.RecommBean> data) {
        super(R.layout.item_homepage_recommend_list, data);
    }

    public HomePageRecommendAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageRecommDataBean.RecommData.RecommBean item) {
        if (userBean == null) {
            userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        }
        helper.setText(R.id.tv_title, item.getTitle());
        RelativeSizeTextView tv_price = helper.getView(R.id.tv_price);

        tv_price.setTagText(StringUtils.getPrice(item.getUserRealPriceE2()));
        tv_price.setEndText(StringUtils.getPriceDecimal(item.getUserRealPriceE2()));
        helper.getView(R.id.tv_not_member_price_mark).setVisibility(View.GONE);
        
        if (StringUtils.isTextEmpty(item.getCoverPicThumb())) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        } else {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPicThumb()));
        }
        helper.addOnClickListener(R.id.rl_item);
    }
}
