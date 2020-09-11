package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.EvaluatedProductListDataBean;

/**
 * Created by ai11 on 2019/7/18.
 */

public class EvaluatedProductListAdapter extends BaseQuickAdapter<EvaluatedProductListDataBean.EvaluatedProductBean, BaseViewHolder> {
    private int currentPage = 0;

    public EvaluatedProductListAdapter(int layoutResId, @Nullable List<EvaluatedProductListDataBean.EvaluatedProductBean> data) {
        super(layoutResId, data);
    }

    public EvaluatedProductListAdapter(@Nullable List<EvaluatedProductListDataBean.EvaluatedProductBean> data) {
        super(R.layout.item_evaluated_product_list, data);
    }

    public EvaluatedProductListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluatedProductListDataBean.EvaluatedProductBean item) {
        helper.setText(R.id.tv_product_name, item.getOrderDetailTitle());
        helper.setText(R.id.tv_product_specs, item.getGoodsSpecification());
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getGoodsCoverPic()));
        helper.addOnClickListener(R.id.tv_evaluate);
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
