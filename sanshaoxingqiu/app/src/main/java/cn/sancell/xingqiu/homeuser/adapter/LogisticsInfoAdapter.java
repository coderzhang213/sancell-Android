package cn.sancell.xingqiu.homeuser.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.LogisticsInfoBean;

/**
 * Created by ai11 on 2019/7/4.
 */

public class LogisticsInfoAdapter extends BaseQuickAdapter<LogisticsInfoBean.InfoBean, BaseViewHolder> {
    private List<LogisticsInfoBean.InfoBean> data;

    public LogisticsInfoAdapter(int layoutResId, @Nullable List<LogisticsInfoBean.InfoBean> data) {
        super(layoutResId, data);
    }

    public LogisticsInfoAdapter(@Nullable List<LogisticsInfoBean.InfoBean> data) {
        super(R.layout.item_logistics_info, data);
        this.data = data;
    }

    public LogisticsInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticsInfoBean.InfoBean item) {
        helper.setText(R.id.tv_status, item.getStatus());
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_context, item.getContext());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                if (position == 0) {
                    holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                } else {
                    if (data.get(position).getStatus().equals(data.get(position - 1).getStatus())) {
                        holder.getView(R.id.tv_time).setVisibility(View.GONE);
                        holder.getView(R.id.tv_status).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                    }

                }
                break;
        }
    }
}
