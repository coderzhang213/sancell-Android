package cn.sancell.xingqiu.homecommunity.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean;

public class RecommendGroupListAdapter extends BaseQuickAdapter<RecommendGroupListBean.RecommGroupBean, BaseViewHolder> {
    private int currentPage = 0;

    public RecommendGroupListAdapter(int layoutResId, @Nullable List<RecommendGroupListBean.RecommGroupBean> data) {
        super(layoutResId, data);
    }

    public RecommendGroupListAdapter(@Nullable List<RecommendGroupListBean.RecommGroupBean> data) {
        super(R.layout.item_recommend_group_list, data);
    }

    public RecommendGroupListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendGroupListBean.RecommGroupBean item) {
        helper.setText(R.id.tv_group_name, item.getGroupName());
        helper.setText(R.id.tv_group_desc, item.getIntro());
        if (!TextUtils.isEmpty(item.getIcon())){
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getIcon()));
        }else{
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_def_team));
        }
        if (item.getInGroup() == 0) { //未加入
            helper.getView(R.id.iv_apply_join).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_join_group_chat).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.iv_apply_join).setVisibility(View.GONE);
            helper.getView(R.id.iv_join_group_chat).setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.iv_apply_join);
        helper.addOnClickListener(R.id.iv_join_group_chat);
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
