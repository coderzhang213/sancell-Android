package cn.sancell.xingqiu.homeuser.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean;

public class InviteFriendsListAdapter extends BaseQuickAdapter<InviteFriendsListBean.MyInviteInfo, BaseViewHolder> {
    private int currentPage = 0;
    public InviteFriendsListAdapter(int layoutResId, @Nullable List<InviteFriendsListBean.MyInviteInfo> data) {
        super(layoutResId, data);
    }

    public InviteFriendsListAdapter(@Nullable List<InviteFriendsListBean.MyInviteInfo> data) {
        super(R.layout.item_invite_friend_list,data);
    }

    public InviteFriendsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteFriendsListBean.MyInviteInfo item) {
        helper.setText(R.id.tv_friend_name,item.getMobile());
        helper.setText(R.id.tv_friend_time,item.getInviteTime());
        helper.setText(R.id.tv_friend_level,item.getRealMemberLevelStr());
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getNextPage() {
        return (this.currentPage + 1);
    }

    public void correctCurrentPage() {
        this.currentPage += 1;
    }

    public void resetCurrentPage() {
        this.currentPage = 0;
    }
}
