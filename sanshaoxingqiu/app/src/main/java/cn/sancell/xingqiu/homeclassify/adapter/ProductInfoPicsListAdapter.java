package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
 * Created by ai11 on 2019/6/17.
 */

public class ProductInfoPicsListAdapter extends BaseQuickAdapter<ProductInfoDataBean.InfoPics, BaseViewHolder> {
    Context context;
    int width;
    public ProductInfoPicsListAdapter(@Nullable List<ProductInfoDataBean.InfoPics> data, Context context) {
        super(R.layout.item_product_info_pics, data);
        this.context=context;
        width= ScreenUtils.getScreenWidth(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoDataBean.InfoPics item) {
        helper.getView(R.id.sdv_pic).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width*item.getPic_hight()/item.getPic_width()));
        /*if (item.getPic_hight() != 0) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setAspectRatio(item.getPic_width() / item.getPic_hight());
        }*/
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getPic_url()));
    }
}
