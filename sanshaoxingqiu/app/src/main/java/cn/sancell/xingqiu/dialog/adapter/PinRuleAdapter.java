package cn.sancell.xingqiu.dialog.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.dialog.entity.PinRuleBean;

public class PinRuleAdapter extends BaseQuickAdapter<PinRuleBean, BaseViewHolder> {

    public PinRuleAdapter(@Nullable List<PinRuleBean> data) {
        super(R.layout.dialog_pin_rule, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PinRuleBean item) {
       AppCompatTextView tv_title = helper.getView(R.id.tv_name);
       AppCompatTextView tv_desc = helper.getView(R.id.tv_desc);
       tv_title.setText(item.title);
       tv_desc.setText(item.desc);
    }
}
