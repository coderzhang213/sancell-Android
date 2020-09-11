package cn.sancell.xingqiu.homeuser.afterSale.address;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

/**
 * @author Alan_Xiong
 * @desc: 修改地址adapter
 * @time 2019-11-28 11:51
 */
public class ApplyAdrAdapter extends BaseMultiItemQuickAdapter<ApplyMultipleEntity<AddressListDataBean.AddressItemBean>, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ApplyAdrAdapter(List<ApplyMultipleEntity<AddressListDataBean.AddressItemBean>> data) {
        super(data);
        //可配送
        addItemType(ApplyMultipleEntity.HEADER, R.layout.recycle_change_address_top);
        addItemType(ApplyMultipleEntity.CONTENT, R.layout.recycle_change_address);

        //不可配送
        addItemType(ApplyMultipleEntity.NOT_PS, R.layout.recycle_change_not_address);
        addItemType(ApplyMultipleEntity.NOT_PS_TITLE, R.layout.recycle_change_not_address_top);
    }


    @Override
    protected void convert(BaseViewHolder helper, ApplyMultipleEntity<AddressListDataBean.AddressItemBean> item) {
        switch (helper.getItemViewType()) {
            case ApplyMultipleEntity.HEADER:
                helper.addOnClickListener(R.id.tv_add_address);
                break;
            case ApplyMultipleEntity.CONTENT://可配送

                helper.setText(R.id.tv_name, item.data.getConsignee());
                helper.setText(R.id.tv_phone, item.data.getMobile());
                //默认
                AppCompatTextView tv_def = helper.getView(R.id.tv_def);
                if (item.data.getIsDefault() == 1) {
                    tv_def.setVisibility(View.VISIBLE);
                } else {
                    tv_def.setVisibility(View.GONE);
                }
                //标签
                AppCompatTextView tv_tag = helper.getView(R.id.tv_tag);
                if (!TextUtils.isEmpty(item.data.getMarkName())) {
                    tv_tag.setVisibility(View.VISIBLE);
                    tv_tag.setText(item.data.getMarkName());
                } else {
                    tv_tag.setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_address, item.data.getProvinceName() + item.data.getCityName() + item.data.getAreasName()
                        + item.data.getStreetName() + item.data.getAddress());
                helper.addOnClickListener(R.id.rl_check);

                AppCompatCheckBox cb = helper.getView(R.id.cb_select);
                if (item.data.isCheck) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }

                break;
            case ApplyMultipleEntity.NOT_PS://不可配送

                helper.setText(R.id.tv_name, item.data.getConsignee());
                helper.setText(R.id.tv_phone, item.data.getMobile());
                //默认
                AppCompatTextView tv_def_not = helper.getView(R.id.tv_def);
                if (item.data.getIsDefault() == 1) {
                    tv_def_not.setVisibility(View.VISIBLE);
                } else {
                    tv_def_not.setVisibility(View.GONE);
                }
                //标签
                AppCompatTextView tv_tag_not = helper.getView(R.id.tv_tag);
                if (!TextUtils.isEmpty(item.data.getMarkName())) {
                    tv_tag_not.setVisibility(View.VISIBLE);
                    tv_tag_not.setText(item.data.getMarkName());
                } else {
                    tv_tag_not.setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_address, item.data.getProvinceName() + item.data.getCityName() + item.data.getAreasName()
                        + item.data.getStreetName() + item.data.getAddress());

                break;
            default:
                break;
        }
    }
}
