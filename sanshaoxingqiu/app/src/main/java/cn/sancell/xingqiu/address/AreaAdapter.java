package cn.sancell.xingqiu.address;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by ZhouZi on 2018/9/29.
 * time:10:35
 * ----------Dragon be here!----------/
 * 　　　┏┓　　 ┏┓
 * 　　┏┛┻━━━┛┻┓━━━
 * 　　┃　　　　　 ┃
 * 　　┃　　　━　  ┃
 * 　　┃　┳┛　┗┳
 * 　　┃　　　　　 ┃
 * 　　┃　　　┻　  ┃
 * 　　┃　　　　   ┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛━━━━━
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━神兽出没━━━━━━━━━━━━━━
 */
public class AreaAdapter extends BaseQuickAdapter<AddressBean.CityBean.AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable List<AddressBean.CityBean.AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean.CityBean.AreaBean item) {
        helper.setText(R.id.textview, item.getLabel());
        helper.setTextColor(R.id.textview, item.isStatus() ? Color.parseColor("#0F9954") : Color.parseColor("#191A19"));
        helper.getView(R.id.iv_select_mark).setVisibility(item.isStatus() ? View.VISIBLE : View.GONE);
    }
}
