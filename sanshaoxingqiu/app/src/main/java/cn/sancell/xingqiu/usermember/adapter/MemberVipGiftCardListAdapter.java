package cn.sancell.xingqiu.usermember.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.usermember.bean.MemberVipGiftCardBean;

/**
 * Created by ai11 on 2019/8/14.
 */

public class MemberVipGiftCardListAdapter extends BaseQuickAdapter<MemberVipGiftCardBean, BaseViewHolder> {
    private Context context;
    private UserBean userBean;

    public MemberVipGiftCardListAdapter(int layoutResId, @Nullable List<MemberVipGiftCardBean> data) {
        super(layoutResId, data);
    }

    public MemberVipGiftCardListAdapter(Context context, @Nullable List<MemberVipGiftCardBean> data, UserBean userBean) {
        super(R.layout.item_member_vipgift_card_list, data);
        this.context = context;
        this.userBean = userBean;
    }

    public MemberVipGiftCardListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberVipGiftCardBean vipGiftBean) {
        helper.setImageResource(R.id.iv_card_bg, vipGiftBean.getPicResourceId());
        if (vipGiftBean.getMemberLevel() > 1) {
            if (vipGiftBean.getCardLevel() == vipGiftBean.getMemberLevel()) {
                helper.getView(R.id.rl_level_info).setVisibility(View.VISIBLE);
                ((TextView) helper.getView(R.id.tv_nickname)).setTextColor(context.getResources().getColor(vipGiftBean.getMemberLevelTextColorId()));
                ((TextView) helper.getView(R.id.tv_member_id)).setTextColor(context.getResources().getColor(vipGiftBean.getMemberLevelTextColorId()));
                ((TextView) helper.getView(R.id.tv_member_valid_time)).setTextColor(context.getResources().getColor(vipGiftBean.getMemberLevelTextColorId()));
                helper.getView(R.id.iv_level_name).setVisibility(View.GONE);
                helper.getView(R.id.tv_card_status).setVisibility(View.GONE);
                ((SimpleDraweeView) helper.getView(R.id.sdv_member_photo)).setImageURI(userBean.getGravatar());
                helper.setText(R.id.tv_nickname, userBean.getNickname());
                helper.setText(R.id.tv_member_id, "ID:" + userBean.getUserId());
                helper.setText(R.id.tv_member_valid_time, userBean.getVipPeriodOfValidity());
            } else {
                helper.getView(R.id.rl_level_info).setVisibility(View.GONE);
                helper.getView(R.id.iv_level_name).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_card_status).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_card_status, vipGiftBean.getMemberLevelName());
                helper.getView(R.id.tv_card_status).setBackgroundResource(vipGiftBean.getStatusBgResourceId());
                ((TextView)helper.getView(R.id.tv_card_status)).setTextColor(context.getResources().getColor(vipGiftBean.getMemberLevelTextColorId()));
                helper.setImageResource(R.id.iv_level_name, vipGiftBean.getMemberLevelStrResourceId());
            }
        } else {
            helper.getView(R.id.rl_level_info).setVisibility(View.GONE);
            helper.getView(R.id.iv_level_name).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_card_status).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.iv_level_name, vipGiftBean.getMemberLevelStrResourceId());
            helper.setText(R.id.tv_card_status, vipGiftBean.getMemberLevelName());
            helper.addOnClickListener(R.id.rl_item);
        }
        /*RecyclerView rcv_gift = helper.getView(R.id.rcv_gift);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_gift.setLayoutManager(layoutManager);
        MemberVipGiftProductListAdapter memberVipGiftProductListAdapter = new MemberVipGiftProductListAdapter(context, vipGiftBean.getMemberVipData().getDataList());
        memberVipGiftProductListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.rl_item:
                        itemClick.itemClick(vipGiftBean.getMemberVipData().getDataList().get(i).getId() + "");
                        break;
                }
            }
        });
        rcv_gift.setAdapter(memberVipGiftProductListAdapter);
        helper.addOnClickListener(R.id.view_more);
        helper.addOnClickListener(R.id.view_privilege_more);*/

    }
}
