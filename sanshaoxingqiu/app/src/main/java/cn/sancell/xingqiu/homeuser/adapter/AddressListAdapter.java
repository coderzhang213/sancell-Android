package cn.sancell.xingqiu.homeuser.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/6/19.
 */

public class  AddressListAdapter extends BaseQuickAdapter<AddressListDataBean.AddressItemBean, BaseViewHolder> {

    public AddressListAdapter(int layoutResId, @Nullable List<AddressListDataBean.AddressItemBean> data) {
        super(layoutResId, data);
    }

    public AddressListAdapter(@Nullable List<AddressListDataBean.AddressItemBean> data) {
        super(R.layout.item_address_list, data);
    }

    public AddressListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListDataBean.AddressItemBean item) {
        helper.setText(R.id.tv_username, item.getConsignee());
        helper.setText(R.id.tv_userphone, item.getMobile());
        helper.setText(R.id.tv_address,item.getProvinceName()+item.getCityName()+item.getAreasName()+item.getStreetName()+item.getAddress());
        if (StringUtils.isTextEmpty(item.getMarkName())) {
            helper.getView(R.id.tv_label).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.tv_label).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_label, item.getMarkName());
        }
        if (item.getIsDefault()==1) {
            helper.getView(R.id.tv_default).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_default).setVisibility(View.GONE);
        }
        helper.addOnLongClickListener(R.id.rl_item);
        helper.addOnClickListener(R.id.rl_item);
        helper.addOnClickListener(R.id.tv_edit);
    }
}
