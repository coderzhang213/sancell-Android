package cn.sancell.xingqiu.address.select;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;

/**
  * @author Alan_Xiong
  *
  * @desc: 选择地址adapter
  * @time 2019-12-12 17:15
  */
public class SelectAddressAdapter extends BaseQuickAdapter<AddressListDataBean.AddressItemBean, BaseViewHolder> {

    private onItemClickListener mListener;

    public SelectAddressAdapter(@Nullable List<AddressListDataBean.AddressItemBean> data) {
        super( R.layout.recycle_change_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListDataBean.AddressItemBean item) {
        AppCompatCheckBox cb =  helper.getView(R.id.cb_select);

        helper.setText(R.id.tv_name,item.getConsignee());
        helper.setText(R.id.tv_phone,item.getMobile());
        //默认
        AppCompatTextView tv_def = helper.getView(R.id.tv_def);
        if (item.getIsDefault() == 1){
            tv_def.setVisibility(View.VISIBLE);
        }else{
            tv_def.setVisibility(View.GONE);
        }
        //标签
        AppCompatTextView tv_tag = helper.getView(R.id.tv_tag);
        if (!TextUtils.isEmpty(item.getMarkName())){
            tv_tag.setVisibility(View.VISIBLE);
            tv_tag.setText(item.getMarkName());
        }else{
            tv_tag.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_address,item.getProvinceName()+item.getCityName()+item.getAreasName()
                +item.getStreetName()+item.getAddress());
        helper.getView(R.id.rl_check).setOnClickListener(v -> {
            if (mListener != null){
                cb.setSelected(true);
                mListener.onAddressSelect(item);
            }
        });

    }

    public interface onItemClickListener{
        void onAddressSelect(AddressListDataBean.AddressItemBean item);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }
}
