package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/8/26.
 */

public class UserRedPacketSettledListAdapter extends BaseQuickAdapter<UserRedPacketListBean.RedPacketBean, BaseViewHolder> {
    private int currentPage = 0;

    public UserRedPacketSettledListAdapter(int layoutResId, @Nullable List<UserRedPacketListBean.RedPacketBean> data) {
        super(layoutResId, data);
    }

    public UserRedPacketSettledListAdapter(@Nullable List<UserRedPacketListBean.RedPacketBean> data) {
        super(R.layout.item_user_red_packet_settled_list, data);
    }

    public UserRedPacketSettledListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserRedPacketListBean.RedPacketBean redPacketBean) {
        baseViewHolder.addOnClickListener(R.id.tv_order_look);
        switch (redPacketBean.getPointFromType()) {
            case 1:
                baseViewHolder.getView(R.id.riv_user_photo).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.sdv_product_pic).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.tv_order_look).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_red_packet_title, "您购买的[" + StringUtils.getTitleDecimal(redPacketBean.getTitle(), 20) + "]获得下单奖励");
                if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
                } else {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(""));
                }
                break;
            case 2:
                baseViewHolder.getView(R.id.riv_user_photo).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.sdv_product_pic).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_order_look).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_red_packet_title, "您邀请的好友已下单，您获取好友下单红包奖励");
                if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
                } else {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(""));
                }
                break;
            case 3:
                baseViewHolder.getView(R.id.riv_user_photo).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.sdv_product_pic).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_order_look).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_red_packet_title, "您邀请的好友[" + StringUtils.getTitleDecimal(redPacketBean.getTitle(), 6) + "]已购买金猩VIP礼包，您获得邀请好友红包奖励");
                if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
                } else {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(""));
                }
                break;
            case 13:
                baseViewHolder.getView(R.id.riv_user_photo).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.sdv_product_pic).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_order_look).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_red_packet_title, "您邀请的好友[" + StringUtils.getTitleDecimal(redPacketBean.getTitle(), 6) + "]已购买红猩VIP礼包，您获得邀请好友红包奖励");
                if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
                } else {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(""));
                }
                break;
            default:
                baseViewHolder.getView(R.id.riv_user_photo).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.sdv_product_pic).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.tv_order_look).setVisibility(View.GONE);
                if(!redPacketBean.getPointFromTypeStr().equals("false")) {
                    baseViewHolder.setText(R.id.tv_red_packet_title, StringUtils.getTitleDecimal(redPacketBean.getTitle(), 20) + ",您获得" + redPacketBean.getPointFromTypeStr());
                }else {
                    baseViewHolder.setText(R.id.tv_red_packet_title,"您获得红包");
                }
                if (!StringUtils.isTextEmpty(redPacketBean.getLogoPic())) {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(redPacketBean.getLogoPic()));
                } else {
                    ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(""));
                }
                break;

        }
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_red_packet_price)).setTagText(StringUtils.getAllPrice(redPacketBean.getPointMoneyE2()));
        if(!redPacketBean.getPointFromTypeStr().equals("false")) {
            baseViewHolder.setText(R.id.tv_red_packet_type, redPacketBean.getPointFromTypeStr());
        }else {
            baseViewHolder.setText(R.id.tv_red_packet_type,"");
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
