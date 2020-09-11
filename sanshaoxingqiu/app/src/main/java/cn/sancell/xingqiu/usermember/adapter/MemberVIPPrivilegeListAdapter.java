package cn.sancell.xingqiu.usermember.adapter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberVIPPrivilegeBean;

/**
 * Created by ai11 on 2019/9/25.
 */

public class MemberVIPPrivilegeListAdapter extends BaseQuickAdapter<MemberVIPPrivilegeBean.VipPrivilegeListBean.SinglePrivilegeBean, BaseViewHolder> {

    public MemberVIPPrivilegeListAdapter(int layoutResId, @Nullable List<MemberVIPPrivilegeBean.VipPrivilegeListBean.SinglePrivilegeBean> data) {
        super(layoutResId, data);
    }

    public MemberVIPPrivilegeListAdapter(@Nullable List<MemberVIPPrivilegeBean.VipPrivilegeListBean.SinglePrivilegeBean> data) {
        super(R.layout.item_member_vip_privilege_list,data);
    }

    public MemberVIPPrivilegeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MemberVIPPrivilegeBean.VipPrivilegeListBean.SinglePrivilegeBean singlePrivilegeBean) {
        ((SimpleDraweeView)baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(singlePrivilegeBean.getCoverPic()));
    }
}
