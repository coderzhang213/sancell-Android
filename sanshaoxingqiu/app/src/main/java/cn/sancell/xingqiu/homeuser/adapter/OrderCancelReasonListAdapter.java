package cn.sancell.xingqiu.homeuser.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OrderCancelReasonListAdapter extends BaseQuickAdapter<OrderCancelReasonListBean.OrderCancelReasonBean, BaseViewHolder> {


    public OrderCancelReasonListAdapter(int layoutResId, @Nullable List<OrderCancelReasonListBean.OrderCancelReasonBean> data) {
        super(layoutResId, data);
    }

    public OrderCancelReasonListAdapter(@Nullable List<OrderCancelReasonListBean.OrderCancelReasonBean> data) {
        super(R.layout.item_order_cancel_reason, data);
    }

    public OrderCancelReasonListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderCancelReasonListBean.OrderCancelReasonBean item) {
        helper.setText(R.id.tv_reason, item.getReasonStr());
        if (item.isSelect()) {
            helper.setImageResource(R.id.iv_select, R.mipmap.icon_car_select_yes);
        } else {
            helper.setImageResource(R.id.iv_select, R.mipmap.icon_car_select_no);
        }
        helper.addOnClickListener(R.id.ll_item);
    }
}
