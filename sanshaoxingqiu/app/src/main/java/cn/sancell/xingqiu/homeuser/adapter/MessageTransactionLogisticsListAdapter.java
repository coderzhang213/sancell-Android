package cn.sancell.xingqiu.homeuser.adapter;

import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.VipManager;
import cn.sancell.xingqiu.homeuser.bean.MessageTransactionLogisticsListBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/8/26.
 */

public class MessageTransactionLogisticsListAdapter extends BaseQuickAdapter<MessageTransactionLogisticsListBean.TransactionLogisticsBean, BaseViewHolder> {
    private int currentPage = 0;

    public MessageTransactionLogisticsListAdapter(int layoutResId, @Nullable List<MessageTransactionLogisticsListBean.TransactionLogisticsBean> data) {
        super(layoutResId, data);
    }

    public MessageTransactionLogisticsListAdapter(@Nullable List<MessageTransactionLogisticsListBean.TransactionLogisticsBean> data) {
        super(R.layout.item_message_transaction_logistics_list, data);
    }

    public MessageTransactionLogisticsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageTransactionLogisticsListBean.TransactionLogisticsBean transactionLogisticsBean) {
        baseViewHolder.addOnClickListener(R.id.rl_item);
        baseViewHolder.addOnClickListener(R.id.tv_evaluate);
        baseViewHolder.setText(R.id.tv_logistics_time, transactionLogisticsBean.getPublicTimeStr());
        if (!StringUtils.isTextEmpty(transactionLogisticsBean.getOrderDetailInfo().getGoodsCoverPic())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(transactionLogisticsBean.getOrderDetailInfo().getGoodsCoverPic()));
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.sdv_product_pic)).setImageURI(Uri.parse(""));
        }
        if (transactionLogisticsBean.getLogisticsNoticeType() == 3) {  //已收货
            baseViewHolder.setText(R.id.tv_logistics_status, "包裹已完成");
            if (!StringUtils.isTextEmpty(transactionLogisticsBean.getOrderDetailInfo().getOrderDetailTitle())) {
                baseViewHolder.setText(R.id.tv_logistics_title, "您购买的" + StringUtils.getTitleDecimal(transactionLogisticsBean.getOrderDetailInfo().getOrderDetailTitle(),20) + "已完成");
            } else {
                baseViewHolder.setText(R.id.tv_logistics_title, "");
            }
            if (transactionLogisticsBean.getIsEvaluation() == 1) {  //已评价
                baseViewHolder.getView(R.id.tv_logistics_num).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_evaluate_tip).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_evaluate).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_evaluated).setVisibility(View.VISIBLE);
            } else {  //未评价
                baseViewHolder.getView(R.id.tv_logistics_num).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_evaluated).setVisibility(View.GONE);
                if (PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class) != null && VipManager.Companion.isVipCheck()) {
                    baseViewHolder.getView(R.id.tv_evaluate_tip).setVisibility(View.VISIBLE);
                } else {
                    baseViewHolder.getView(R.id.tv_evaluate_tip).setVisibility(View.GONE);
                }
                baseViewHolder.getView(R.id.tv_evaluate).setVisibility(View.VISIBLE);
            }
        } else if (transactionLogisticsBean.getLogisticsNoticeType() == 1) {  //已发货
            baseViewHolder.setText(R.id.tv_logistics_status, "包裹已发货");
            if (!StringUtils.isTextEmpty(transactionLogisticsBean.getOrderDetailInfo().getOrderDetailTitle())) {
                baseViewHolder.setText(R.id.tv_logistics_title, "您购买的" + StringUtils.getTitleDecimal(transactionLogisticsBean.getOrderDetailInfo().getOrderDetailTitle(),20) + "已发货");
            } else {
                baseViewHolder.setText(R.id.tv_logistics_title, "");
            }
            baseViewHolder.getView(R.id.tv_logistics_num).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.tv_logistics_num, transactionLogisticsBean.getCourierCompany() + ":" + transactionLogisticsBean.getCourierNumber());
            baseViewHolder.getView(R.id.tv_evaluate_tip).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_evaluate).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_evaluated).setVisibility(View.GONE);
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
