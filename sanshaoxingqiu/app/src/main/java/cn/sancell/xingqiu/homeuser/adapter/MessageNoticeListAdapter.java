package cn.sancell.xingqiu.homeuser.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.MessageNoticeListBean;

/**
 * Created by ai11 on 2019/8/23.
 */

public class MessageNoticeListAdapter extends BaseQuickAdapter<MessageNoticeListBean.MessageNoticeBean, BaseViewHolder> {
    private int currentPage = 0;
    public MessageNoticeListAdapter(int layoutResId, @Nullable List<MessageNoticeListBean.MessageNoticeBean> data) {
        super(layoutResId, data);
    }

    public MessageNoticeListAdapter(@Nullable List<MessageNoticeListBean.MessageNoticeBean> data) {
        super(R.layout.item_message_notice_list, data);
    }

    public MessageNoticeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageNoticeListBean.MessageNoticeBean messageNoticeBean) {
        baseViewHolder.addOnClickListener(R.id.rl_item);
        baseViewHolder.setText(R.id.tv_notice_title,messageNoticeBean.getTitle());
        baseViewHolder.setText(R.id.tv_notice_desc,messageNoticeBean.getContent());
        baseViewHolder.setText(R.id.tv_notice_time,messageNoticeBean.getPublicTimeStr());
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
