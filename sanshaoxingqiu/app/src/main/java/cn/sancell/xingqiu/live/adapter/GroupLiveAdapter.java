package cn.sancell.xingqiu.live.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2019/11/27.
 */
public class GroupLiveAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public GroupLiveAdapter(List<String> data) {
        super(R.layout.view_group_live_layout, data);
    }
    public GroupLiveAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }

}
