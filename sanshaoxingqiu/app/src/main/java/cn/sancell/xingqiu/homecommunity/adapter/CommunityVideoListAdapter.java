package cn.sancell.xingqiu.homecommunity.adapter;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;

public class CommunityVideoListAdapter extends BaseQuickAdapter<CommunityVideoListBean.VideoBean, BaseViewHolder> {
    private int currentPage = 0;

    public CommunityVideoListAdapter(int layoutResId, @Nullable List<CommunityVideoListBean.VideoBean> data) {
        super(layoutResId, data);
    }

    public CommunityVideoListAdapter(@Nullable List<CommunityVideoListBean.VideoBean> data) {
        super(R.layout.item_community_video_list, data);
    }

    public CommunityVideoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityVideoListBean.VideoBean item) {
        helper.setText(R.id.tv_video_name, item.getTitle());
        helper.setText(R.id.tv_video_desc, item.getIntro());
        if (!TextUtils.isEmpty(item.getIcon())){
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getIcon()));
        }
        helper.addOnClickListener(R.id.rl_item);
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
