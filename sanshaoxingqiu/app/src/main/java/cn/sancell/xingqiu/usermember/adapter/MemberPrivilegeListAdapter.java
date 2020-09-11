package cn.sancell.xingqiu.usermember.adapter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberPrivilegeListBean;

/**
 * Created by ai11 on 2019/8/13.
 */

public class MemberPrivilegeListAdapter extends BaseQuickAdapter<MemberPrivilegeListBean.MemberPrivilegeBean, BaseViewHolder> {
    public MemberPrivilegeListAdapter(int layoutResId, @Nullable List<MemberPrivilegeListBean.MemberPrivilegeBean> data) {
        super(layoutResId, data);
    }

    public MemberPrivilegeListAdapter(@Nullable List<MemberPrivilegeListBean.MemberPrivilegeBean> data) {
        super(R.layout.item_member_privilege_list, data);
    }

    public MemberPrivilegeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberPrivilegeListBean.MemberPrivilegeBean memberPrivilegeBean) {
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(memberPrivilegeBean.getCoverPic()));
        helper.addOnClickListener(R.id.sdv_pic);
    }
}
