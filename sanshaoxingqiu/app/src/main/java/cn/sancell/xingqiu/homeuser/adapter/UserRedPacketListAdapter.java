package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/8/23.
 */

public class UserRedPacketListAdapter extends BaseQuickAdapter<UserRedPacketListBean.RedPacketBean, BaseViewHolder> {
    private int currentPage = 0;

    public UserRedPacketListAdapter(int layoutResId, @Nullable List<UserRedPacketListBean.RedPacketBean> data) {
        super(layoutResId, data);
    }

    public UserRedPacketListAdapter(@Nullable List<UserRedPacketListBean.RedPacketBean> data) {
        super(R.layout.item_user_red_packet_list, data);
    }

    public UserRedPacketListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserRedPacketListBean.RedPacketBean redPacketBean) {
        baseViewHolder.setText(R.id.tv_title, redPacketBean.getPointFromTypeStr());
        if (redPacketBean.getIncreaseType() == 1) {  //增加
            ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_theme));
            ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText("+ ¥" + StringUtils.getAllPrice(redPacketBean.getPointMoneyE2()));
        } else {
            ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_function_error));
            ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_price)).setTagText("- ¥" + StringUtils.getAllPrice(redPacketBean.getPointMoneyE2()));
        }
        baseViewHolder.setText(R.id.tv_time, redPacketBean.getPublishTimeStr());
        if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
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
