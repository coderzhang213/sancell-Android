package cn.sancell.xingqiu.dialog.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.bean.PurchaseInfo;

/**
 * Created by zj on 2019/12/13.
 */
public class PurchaseAdapter extends BaseQuickAdapter<PurchaseInfo, BaseViewHolder> {
    public PurchaseAdapter(@Nullable List<PurchaseInfo> data) {
        super(R.layout.view_pus_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PurchaseInfo item) {
        helper.setText(R.id.tv_tite, item.getTitle());
        helper.setText(R.id.tv_conent, item.getConent());
    }
}
