package cn.sancell.xingqiu.usermember.adapter;

import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberOpenRecordsListBean;

/**
 * Created by ai11 on 2019/8/13.
 */

public class MemberOpenRecordsListAdapter extends BaseQuickAdapter<MemberOpenRecordsListBean.MemberOpenRecordsBean, BaseViewHolder> {
    public MemberOpenRecordsListAdapter(int layoutResId, @Nullable List<MemberOpenRecordsListBean.MemberOpenRecordsBean> data) {
        super(layoutResId, data);
    }

    public MemberOpenRecordsListAdapter(@Nullable List<MemberOpenRecordsListBean.MemberOpenRecordsBean> data) {
        super(R.layout.item_member_records_list, data);
    }

    public MemberOpenRecordsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MemberOpenRecordsListBean.MemberOpenRecordsBean memberOpenRecordsBean) {
        baseViewHolder.setText(R.id.tv_level_name, memberOpenRecordsBean.getMemberLevelStr());
        baseViewHolder.setText(R.id.tv_open_time, memberOpenRecordsBean.getPayEndStr());
        baseViewHolder.setText(R.id.tv_open_status, memberOpenRecordsBean.getMemberVipStatusStr());
        ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_open_card)).setImageURI(Uri.parse(memberOpenRecordsBean.getBuyMemberVipSuccessfulImageUrl()));
        ((TextView)baseViewHolder.getView(R.id.tv_level_name)).setTextColor(android.graphics.Color.parseColor(memberOpenRecordsBean.getFontColor()));
        ((TextView)baseViewHolder.getView(R.id.tv_open_time)).setTextColor(android.graphics.Color.parseColor(memberOpenRecordsBean.getFontColor()));
        ((TextView)baseViewHolder.getView(R.id.tv_open_status)).setTextColor(android.graphics.Color.parseColor(memberOpenRecordsBean.getFontColor()));
    }
}
