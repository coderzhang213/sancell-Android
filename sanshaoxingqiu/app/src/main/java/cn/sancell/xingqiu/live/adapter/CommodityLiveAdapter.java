package cn.sancell.xingqiu.live.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.live.bean.CommentItenmInfo;

/**
 * Created by zj on 2019/11/27.
 */
public class CommodityLiveAdapter extends BaseQuickAdapter<CommentItenmInfo, BaseViewHolder> {

    public CommodityLiveAdapter(List<CommentItenmInfo> data) {
        super(R.layout.view_com_live_layout, data);
    }
    public CommodityLiveAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentItenmInfo item) {

    }

}
