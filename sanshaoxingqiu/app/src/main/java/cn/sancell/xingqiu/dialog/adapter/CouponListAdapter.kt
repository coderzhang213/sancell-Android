package cn.sancell.xingqiu.dialog.adapter

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CouponItemInfo
import cn.sancell.xingqiu.bean.ReadPackInfo
import cn.sancell.xingqiu.util.BigDecimalUtils
import cn.sancell.xingqiu.util.FontUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.TimeUtil

/**
 * Created by zj on 2020/5/11.
 */
class CouponListAdapter(data: List<CouponItemInfo>) : BaseQuickAdapter<CouponItemInfo, BaseViewHolder>(R.layout.dialog_coupon_item_laout, data) {
    override fun convert(helper: BaseViewHolder, item: CouponItemInfo) {
        // helper.setText(R.id.tv_voucher_coin, BigDecimalUtils.div(item.faceValueE2, "100", 0) + "元")
        helper.getView<AppCompatTextView>(R.id.tv_voucher_coin).text = FontUtils.getInstance().changeTextSize(mContext, 14,
                BigDecimalUtils.div(item.faceValueE2, "100", 0) + "元", "元")
        val tv_voucher_limit = helper.getView<TextView>(R.id.tv_voucher_limit)
        if (item.limitMinUseMoneyE2 > 0) {
            tv_voucher_limit.visibility = View.VISIBLE
            tv_voucher_limit.setText("满" + BigDecimalUtils.div(item.limitMinUseMoneyE2.toString(), "100", 0) + "元可用")

        } else {
            tv_voucher_limit.visibility = View.GONE
        }
        helper.setText(R.id.tv_voucher_title, item.name)
        if (item.useType.equals("1")) {
            helper.setText(R.id.tv_voucher_goods, "全部商品")
        } else {
            helper.setText(R.id.tv_voucher_goods, "限定商品")
        }
        helper.setText(R.id.tv_voucher_use_time, TimeUtil.getDateFromYYYYMMDD(item.fixedUseBeginTime * 1000) + "-" + TimeUtil.getDateFromYYYYMMDD(item.fixedUseEndTime * 1000))
        //helper.addOnClickListener(R.id.tv_get_coup)
        val tv_get_coup = helper.getView<TextView>(R.id.tv_get_coup)
        when (item.showStatus) {//1可领取；2已领过；3被抢完
            "1" -> {
                tv_get_coup.setText("立即领取")
                tv_get_coup.setBackgroundResource(R.drawable.coupon_get_bg)
            }
            "2" -> {
                tv_get_coup.setText("已领取")
                tv_get_coup.setBackgroundResource(R.drawable.coupon_get_not_bg)
            }
            "3" -> {
                tv_get_coup.setText("已抢光")
                tv_get_coup.setBackgroundResource(R.drawable.coupon_get_not_bg)
            }


        }
        helper.getView<View>(R.id.ll_conent).setOnClickListener {
            if (item.showStatus.equals("1")) {//领取
                onItemChildClickListener?.onItemChildClick(this, null, helper.layoutPosition)

            }
        }
    }
}