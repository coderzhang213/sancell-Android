package cn.sancell.xingqiu.homeuser.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.homeuser.bean.MessageRedPacketListBean;

/**
 * Created by ai11 on 2019/8/23.
 */

public class MessageRedPacketListAdapter extends BaseQuickAdapter<MessageRedPacketListBean.MessageRedPacketBean, BaseViewHolder> {
    private int currentPage = 0;
    public MessageRedPacketListAdapter(int layoutResId, @Nullable List<MessageRedPacketListBean.MessageRedPacketBean> data) {
        super(layoutResId, data);
    }

    public MessageRedPacketListAdapter(@Nullable List<MessageRedPacketListBean.MessageRedPacketBean> data) {
        super(R.layout.item_message_red_packet_list, data);
    }

    public MessageRedPacketListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageRedPacketListBean.MessageRedPacketBean messageRedPacketBean) {
        if(messageRedPacketBean.getReadStatus()==2) { //未读
            ((TextView) baseViewHolder.getView(R.id.tv_message_title)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text1));
            ((TextView) baseViewHolder.getView(R.id.tv_message_desc)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text2));
        }else {
            ((TextView) baseViewHolder.getView(R.id.tv_message_title)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text6));
            ((TextView) baseViewHolder.getView(R.id.tv_message_desc)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text3));
        }
        baseViewHolder.setText(R.id.tv_message_title,messageRedPacketBean.getNoticeTitle());
        baseViewHolder.setText(R.id.tv_message_desc,messageRedPacketBean.getNoticeDesc());
        baseViewHolder.setText(R.id.tv_message_time,messageRedPacketBean.getPublicTimeStr());
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
