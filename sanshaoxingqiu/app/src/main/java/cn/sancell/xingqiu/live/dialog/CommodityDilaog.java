package cn.sancell.xingqiu.live.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.live.adapter.CommodityLiveAdapter;
import cn.sancell.xingqiu.live.bean.CommentItenmInfo;


/**
 * Created by zj on 2019/11/27.
 */
public class CommodityDilaog extends LiveDialog<CommentItenmInfo> {
    public CommodityDilaog(@NonNull Context context, List<CommentItenmInfo> mLists) {
        super(context, mLists);
    }

    @Override
    public void setAdapter(RecyclerView rl_list) {
        CommodityLiveAdapter mCommodityLiveAdapter = new CommodityLiveAdapter(mLists);
        rl_list.setLayoutManager(new LinearLayoutManager(context));
        rl_list.setAdapter(mCommodityLiveAdapter);
    }
}
