package cn.sancell.xingqiu.usermember.adapter;

import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homepage.bean.LikeBean;

/**
 * Created by ai11 on 2019/8/15.
 */

public class MemberVipAllGiftListAdapter extends BaseQuickAdapter<LikeBean, BaseViewHolder> {
    private int currentPage = 0;

    public MemberVipAllGiftListAdapter(int layoutResId, @Nullable List<LikeBean> data) {
        super(layoutResId, data);
    }

    public MemberVipAllGiftListAdapter(@Nullable List<LikeBean> data) {
        super(R.layout.item_member_vip_all_gift_list, data);
    }

    public MemberVipAllGiftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LikeBean likeBean) {
        baseViewHolder.getView(R.id.tv_limit_status).setVisibility(View.GONE);
        baseViewHolder.getView(R.id.tv_price_market).setVisibility(View.GONE);
        baseViewHolder.getView(R.id.iv_normal_mark).setVisibility(View.VISIBLE);
        baseViewHolder.setText(R.id.tv_title, likeBean.getTitle());
        baseViewHolder.setText(R.id.tv_desc, likeBean.getDesc());
        ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(likeBean.getCoverPic()));
        baseViewHolder.addOnClickListener(R.id.rl_item);
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
