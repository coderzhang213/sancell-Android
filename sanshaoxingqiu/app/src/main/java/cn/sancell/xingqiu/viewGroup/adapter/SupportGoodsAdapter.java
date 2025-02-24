package cn.sancell.xingqiu.viewGroup.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;

/**
  * @author Alan_Xiong
  *
  * @desc: 不支持配送的商品
  * @time 2019-12-12 15:09
  */
public class SupportGoodsAdapter extends BaseQuickAdapter<HomeShoppingCarDataBean.ShopingCarProductBean, BaseViewHolder> {
    public SupportGoodsAdapter(@Nullable List<HomeShoppingCarDataBean.ShopingCarProductBean> data) {
        super(R.layout.recycle_support_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeShoppingCarDataBean.ShopingCarProductBean item) {

        AppCompatImageView ivGoods = helper.getView(R.id.iv_goods);
        RequestOptions options = new RequestOptions().centerCrop().transform(new RoundedCorners(ScreenUtil.dip2px(6)));
        Glide.with(mContext).load(item.getCoverPic()).apply(options).into(ivGoods);
    }
}
