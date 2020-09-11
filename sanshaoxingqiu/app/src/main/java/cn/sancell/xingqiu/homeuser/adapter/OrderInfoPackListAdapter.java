package cn.sancell.xingqiu.homeuser.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OrderInfoPackListAdapter extends BaseQuickAdapter<OderInfoDataBean.PackListDataBean.PackBean, BaseViewHolder> {
    private List<OderInfoDataBean.PackListDataBean.PackBean> data_pack;
    private ProductClickAction productClickAction;
    private Context context;

    public OrderInfoPackListAdapter(int layoutResId, @Nullable List<OderInfoDataBean.PackListDataBean.PackBean> data) {
        super(layoutResId, data);
    }

    public OrderInfoPackListAdapter(Context context, @Nullable List<OderInfoDataBean.PackListDataBean.PackBean> data, ProductClickAction productClickAction) {
        super(R.layout.item_orderinfo_packlist, data);
        data_pack = data;
        this.productClickAction = productClickAction;
        this.context = context;
    }

    public OrderInfoPackListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OderInfoDataBean.PackListDataBean.PackBean item) {
        if(StringUtils.isTextEmpty(item.getRemark())){
            helper.getView(R.id.rl_remark).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.rl_remark).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_remark,item.getRemark());
        }
        RecyclerView rcv_product = helper.getView(R.id.rcv_product);
        rcv_product.setLayoutManager(new LinearLayoutManager(context));
        rcv_product.setAdapter(new OrderInfoPackProductListAdapter(item.getOrderDetail().getDataList(), productClickAction));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                if (data_pack.size() > 1) {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_pack_name).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_pack_name, data_pack.get(position - 1).getParcelName());
                    if (position == data_pack.size()) {
                        holder.getView(R.id.line).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.line).setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.getView(R.id.line).setVisibility(View.GONE);
                    holder.getView(R.id.tv_pack_name).setVisibility(View.GONE);
                }
                break;
        }
    }

    public interface ProductClickAction {
        void productClick(String id);
    }
}
