package cn.sancell.xingqiu.live.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.live.adapter.GroupLiveAdapter;
import cn.sancell.xingqiu.live.bean.GroupInfo;


/**
 * Created by zj on 2019/11/27.
 */
public class GroupLiveDiloag extends LiveDialog<GroupInfo> {
    public GroupLiveDiloag(@NonNull Context context, List<GroupInfo> mList) {
        super(context, mList);
    }

    @Override
    public void setAdapter(RecyclerView rl_list) {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("i+" + i);
        }
        GroupLiveAdapter mCommodityLiveAdapter = new GroupLiveAdapter(mList);
        rl_list.setLayoutManager(new LinearLayoutManager(context));
        rl_list.setAdapter(mCommodityLiveAdapter);
    }
}
