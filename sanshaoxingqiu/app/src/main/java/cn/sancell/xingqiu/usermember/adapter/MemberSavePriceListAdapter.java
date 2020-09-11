package cn.sancell.xingqiu.usermember.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.usermember.bean.MemberSavePriceListBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/9/4.
 */

public class MemberSavePriceListAdapter extends BaseQuickAdapter<MemberSavePriceListBean.MemberSavePriceBean, BaseViewHolder> {
    public MemberSavePriceListAdapter(int layoutResId, @Nullable List<MemberSavePriceListBean.MemberSavePriceBean> data) {
        super(layoutResId, data);
    }

    public MemberSavePriceListAdapter(@Nullable List<MemberSavePriceListBean.MemberSavePriceBean> data) {
        super(R.layout.item_member_save_price_list, data);
    }

    public MemberSavePriceListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MemberSavePriceListBean.MemberSavePriceBean memberSavePriceBean) {
        baseViewHolder.setText(R.id.tv_month, memberSavePriceBean.getMonth() + "月");
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_month_total_price)).setTagText(StringUtils.getPrice(memberSavePriceBean.getMonthTotalSavePoint()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_month_total_price)).setEndText(StringUtils.getPriceDecimal(memberSavePriceBean.getMonthTotalSavePoint()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_member_save_price)).setTagText(StringUtils.getAllPrice(memberSavePriceBean.getSaveTotalAmtE2()));
        ((RelativeSizeTextView) baseViewHolder.getView(R.id.tv_red_price)).setTagText(StringUtils.getAllPrice(memberSavePriceBean.getTotalPointAmtE2()));

    }
}
