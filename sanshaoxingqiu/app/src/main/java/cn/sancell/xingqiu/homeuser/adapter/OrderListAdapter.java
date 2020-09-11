package cn.sancell.xingqiu.homeuser.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListNoPayListDataBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/20.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderAllListNoPayListDataBean, BaseViewHolder> {
    private Context context;
    private int currentPage = 0;

    public OrderListAdapter(Context context, @Nullable List<OrderAllListNoPayListDataBean> data) {
        super(R.layout.item_order_list, data);
        this.context = context;
    }

    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderAllListNoPayListDataBean item) {
        helper.setText(R.id.tv_order_num, "订单：" + item.getOrderNumber());
        helper.addOnClickListener(R.id.rl_item);
        ((RelativeSizeTextView) helper.getView(R.id.tv_price)).setTagText(StringUtils.getPrice(item.getPayAmtE2()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_price)).setStartText("¥");
        ((RelativeSizeTextView) helper.getView(R.id.tv_price)).setStartProportion(0.78f);
        ((RelativeSizeTextView) helper.getView(R.id.tv_price)).setEndText(StringUtils.getPriceDecimal(item.getPayAmtE2()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_price)).setEndProportion(0.78f);
        helper.setText(R.id.tv_pack_name, item.getPackName());
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic_first)).setImageURI(Uri.parse(item.getShowProdutPics().get(0)));
        if (item.getShowProdutPics().size() > 1) {
            helper.getView(R.id.rl_only_one_info).setVisibility(View.GONE);
            helper.getView(R.id.rl_more_info).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_product_num, item.getProductNum() + "件商品");
            if (item.getShowProdutPics().size() == 2) {
                helper.getView(R.id.sdv_pic_third).setVisibility(View.GONE);
                helper.getView(R.id.sdv_pic_second).setVisibility(View.VISIBLE);
                ((SimpleDraweeView) helper.getView(R.id.sdv_pic_second)).setImageURI(Uri.parse(item.getShowProdutPics().get(1)));
            } else {
                helper.getView(R.id.sdv_pic_third).setVisibility(View.VISIBLE);
                helper.getView(R.id.sdv_pic_second).setVisibility(View.VISIBLE);
                ((SimpleDraweeView) helper.getView(R.id.sdv_pic_second)).setImageURI(Uri.parse(item.getShowProdutPics().get(1)));
                ((SimpleDraweeView) helper.getView(R.id.sdv_pic_third)).setImageURI(Uri.parse(item.getShowProdutPics().get(2)));
            }
        } else {
            helper.getView(R.id.rl_only_one_info).setVisibility(View.VISIBLE);
            helper.getView(R.id.rl_more_info).setVisibility(View.GONE);
            helper.setText(R.id.tv_product_name, item.getSingleProductName());
            helper.setText(R.id.tv_product_specs, item.getSingleProductSpecs());

            helper.setText(R.id.tv_single_product_num, item.getSingleProductNum() + "件");
        }


        switch (item.getShowState()) {
            case Constants.OrderShowStatus.KEY_nopay:  //待付款
                helper.getView(R.id.ll_nopay).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.GONE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_theme));
                helper.setText(R.id.tv_order_type, "待付款");
                helper.addOnClickListener(R.id.tv_nopay_cancel);
                helper.addOnClickListener(R.id.tv_nopay_gopay);
                helper.setText(R.id.tv_nopay_gopay, "去支付");
                break;
            case Constants.OrderShowStatus.KEY_undelivered:  //待发货
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_theme));
              //  helper.setText(R.id.tv_order_type, "待发货");
                helper.setText(R.id.tv_order_type, "待使用");
                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.GONE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_undelivered_againpay);
                //暂时无包裹取消功能
                helper.getView(R.id.tv_undelivered_cancel).setVisibility(View.GONE);
                //helper.addOnClickListener(R.id.tv_undelivered_cancel);
                break;
            case Constants.OrderShowStatus.KEY_delivered:  //已发货
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_theme));
                helper.setText(R.id.tv_order_type, "已发货");

                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_finish).setVisibility(View.GONE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_delivered_againpay);
                helper.addOnClickListener(R.id.tv_delivered_logistics);
                helper.addOnClickListener(R.id.tv_delivered_sure);
                break;
            case Constants.OrderShowStatus.KEY_RECEIVE:  //已完成但未全部评价完
              //  helper.setText(R.id.tv_order_type, "已签收");
                helper.setText(R.id.tv_order_type, "已完成");
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_00A637));
                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                //暂无包裹删除功能
                helper.getView(R.id.tv_finish_delete).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_finish_delete);
                helper.addOnClickListener(R.id.tv_finish_againpay);
                if (item.isEvaluation == 1){
                    helper.getView(R.id.tv_finish_evaluate).setVisibility(View.GONE);

                }else{
                    helper.getView(R.id.tv_finish_evaluate).setVisibility(View.VISIBLE);
                }
                helper.addOnClickListener(R.id.tv_finish_evaluate);
                break;
            case Constants.OrderShowStatus.KEY_evaluated:  //已完成且全部评价完
                //helper.setText(R.id.tv_order_type, "订单已完成");
                helper.setText(R.id.tv_order_type, "已完成");
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_00A637));
                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                //暂无包裹删除功能
                helper.getView(R.id.tv_finish_delete).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_finish_delete);
                helper.addOnClickListener(R.id.tv_finish_againpay);
                helper.getView(R.id.tv_finish_evaluate).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_finish_evaluate);
                break;

            case Constants.OrderShowStatus.KEY_processing:  //订单处理中
                helper.setText(R.id.tv_order_type, "订单处理中");
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_theme));
                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.GONE);
                helper.getView(R.id.ll_processing).setVisibility(View.VISIBLE);
                //暂时无包裹取消功能即无查看进度此功能
                helper.getView(R.id.tv_processing_progress).setVisibility(View.GONE);
                //helper.addOnClickListener(R.id.tv_processing_progress);
                helper.getView(R.id.ll_close).setVisibility(View.GONE);
                helper.addOnClickListener(R.id.tv_processing_againpay);
                break;
            case Constants.OrderShowStatus.KEY_closed:  //订单已关闭
                helper.setText(R.id.tv_order_type, "已关闭");
                ((TextView) helper.getView(R.id.tv_order_type)).setTextColor(context.getResources().getColor(R.color.color_text3));
                helper.getView(R.id.ll_nopay).setVisibility(View.GONE);
                helper.getView(R.id.ll_undelivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_delivered).setVisibility(View.GONE);
                helper.getView(R.id.ll_finish).setVisibility(View.GONE);
                helper.getView(R.id.ll_processing).setVisibility(View.GONE);
                helper.getView(R.id.ll_close).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.tv_close_delete);
                helper.addOnClickListener(R.id.tv_close_againpay);
                break;
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
