package cn.sancell.xingqiu.homeclassify.adapter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by ai11 on 2019/6/17.
 */

public class ProductInfoBannerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ProductInfoBannerAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public ProductInfoBannerAdapter(@Nullable List<String> data) {
        super(R.layout.item_productinfo_banner, data);
    }

    public ProductInfoBannerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item));
        helper.addOnClickListener(R.id.sdv_pic);
    }
}
