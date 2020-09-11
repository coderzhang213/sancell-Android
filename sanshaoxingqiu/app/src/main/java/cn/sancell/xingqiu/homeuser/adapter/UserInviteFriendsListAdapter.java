package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.VipManager;
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

public class UserInviteFriendsListAdapter extends BaseQuickAdapter<InviteFriendsListBean.MyInviteInfo, BaseViewHolder> {
    private int currentPage = 0;

    public UserInviteFriendsListAdapter(int layoutResId, @Nullable List<InviteFriendsListBean.MyInviteInfo> data) {
        super(layoutResId, data);
    }

    public UserInviteFriendsListAdapter(@Nullable List<InviteFriendsListBean.MyInviteInfo> data) {
        super(R.layout.item_user_invite_friend_list, data);
    }

    public UserInviteFriendsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteFriendsListBean.MyInviteInfo item) {
        helper.setText(R.id.tv_invitees_name, item.getNickName());
        helper.setText(R.id.tv_invitees_id, "ID: " + item.getUserId());
        ((SimpleDraweeView) helper.getView(R.id.riv_invitees_photo)).setImageURI(Uri.parse(item.getGravatar()));

        //收益为0 不显示右侧数据
//        RelativeSizeTextView tvTotal = helper.getView(R.id.tv_invite_income);
//        TextView tv_income_desc = helper.getView(R.id.tv_income_desc);
//        if (item.getShareingTotal() <= 0) {
//            tv_income_desc.setVisibility(View.GONE);
//            tvTotal.setVisibility(View.GONE);
//        } else {
//            tvTotal.setVisibility(View.VISIBLE);
//            tv_income_desc.setVisibility(View.VISIBLE);
//            tvTotal.setTagText(StringUtils.getAllPrice(item.getShareingTotal()));
//        }
        helper.setText(R.id.tv_invite_time, item.getInviteTime());
        TextView iv_invitees_level = helper.getView(R.id.iv_invitees_level);
        if (item.getMemberLevel() > 1) {
            iv_invitees_level.setVisibility(View.VISIBLE);
            iv_invitees_level.setText(item.getRealMemberLevelStr());
        } else {
            iv_invitees_level.setVisibility(View.GONE);

        }

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
