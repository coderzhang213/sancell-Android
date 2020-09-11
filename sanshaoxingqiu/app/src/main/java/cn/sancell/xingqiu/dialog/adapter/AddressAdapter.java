package cn.sancell.xingqiu.dialog.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

/**
 * Created by zj on 2019/12/11.
 */
public class AddressAdapter extends BaseQuickAdapter<AddressListDataBean.AddressItemBean, BaseViewHolder> {
    public AddressAdapter(@Nullable List<AddressListDataBean.AddressItemBean> data) {
        super(R.layout.adapter_address_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListDataBean.AddressItemBean item) {
        TextView tv_default_address = helper.getView(R.id.tv_default_address);
        TextView tv_gs_address = helper.getView(R.id.tv_gs_address);
        TextView tv_name = helper.getView(R.id.tv_name);
        ImageView iv_select = helper.getView(R.id.iv_select);
        TextView tv_address = helper.getView(R.id.tv_address);
        TextView tv_phone = helper.getView(R.id.tv_phone);
        tv_phone.setText(item.getMobile());
        tv_address.setText(item.getCodeString());
        helper.addOnClickListener(R.id.tl_content);
        if (helper.getLayoutPosition() == 0) {
            iv_select.setImageResource(R.mipmap.icon_car_select_yes);
        } else {
            iv_select.setImageResource(R.mipmap.icon_car_select_no);

        }
        if (!TextUtils.isEmpty(item.getMarkName())) {
            tv_gs_address.setText(item.getMarkName());
            tv_gs_address.setVisibility(View.VISIBLE);
        } else {
            tv_gs_address.setVisibility(View.GONE);
        }

        tv_name.setText(item.getConsignee());
        if (item.getIsDefault() == 1) {
            tv_default_address.setVisibility(View.VISIBLE);
        } else {
            tv_default_address.setVisibility(View.GONE);

        }

    }
}
