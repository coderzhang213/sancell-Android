package cn.sancell.xingqiu.homeuser.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.LogisticsListBean;

/**
 * Created by ai11 on 2019/9/26.
 */

public class LogisticsListAdapter extends BaseQuickAdapter<LogisticsListBean.OrderReceiveInfo.LogisticsBean, BaseViewHolder> {
    public LogisticsListAdapter(int layoutResId, @Nullable List<LogisticsListBean.OrderReceiveInfo.LogisticsBean> data) {
        super(layoutResId, data);
    }

    public LogisticsListAdapter(@Nullable List<LogisticsListBean.OrderReceiveInfo.LogisticsBean> data) {
        super(R.layout.item_logistics_list, data);
    }

    public LogisticsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LogisticsListBean.OrderReceiveInfo.LogisticsBean logisticsBean) {
        baseViewHolder.addOnClickListener(R.id.ll_item);
        baseViewHolder.setText(R.id.tv_logistics_name, logisticsBean.getCourierCompany());
        baseViewHolder.setText(R.id.tv_logistics_num, "快递单号:" + logisticsBean.getCourierNumber());
    }
}
