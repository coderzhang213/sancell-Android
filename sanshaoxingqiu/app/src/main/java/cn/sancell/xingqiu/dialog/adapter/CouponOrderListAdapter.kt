package cn.sancell.xingqiu.dialog.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ReadPackInfo
import cn.sancell.xingqiu.bean.VoucherInfo
import cn.sancell.xingqiu.util.BigDecimalUtils
import cn.sancell.xingqiu.util.DateUtils
import cn.sancell.xingqiu.util.FontUtils
import cn.sancell.xingqiu.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 选取代金券adapter
 */
class CouponOrderListAdapter(data: List<VoucherInfo>) : BaseQuickAdapter<VoucherInfo, BaseViewHolder>(R.layout.recycle_voucher_chose, data) {


    var mChoseId: String = ""

    override fun convert(helper: BaseViewHolder, item: VoucherInfo) {
        val tvUse = helper.getView<AppCompatTextView>(R.id.tv_voucher_goods)

        helper.getView<AppCompatTextView>(R.id.tv_voucher_coin).text = FontUtils.getInstance().changeTextSize(mContext, 14,
                BigDecimalUtils.div(item.faceValueE2.toString(), "100", 0) + "元", "元")

        val tvLimit = helper.getView<AppCompatTextView>(R.id.tv_voucher_limit)
        if (item.limitMinUseMoneyE2 > 0) {
            tvLimit.visibility = View.VISIBLE
            tvLimit.text = StringBuffer("满" + BigDecimalUtils.div(item.limitMinUseMoneyE2.toString(),"100",0) + "可用")

        } else {
            tvLimit.visibility = View.GONE
        }
        helper.getView<AppCompatTextView>(R.id.tv_voucher_title).text = item.name
        if (item.useType == 1) {
            tvUse.text = "全部商品"
        } else {
            tvUse.text = "部分商品"
        }

        helper.getView<AppCompatTextView>(R.id.tv_voucher_use_time).text = StringBuffer(DateUtils.getTimeByStampWithYear(item.fixedUseBeginTime)
                + "-" + DateUtils.getTimeByStampWithYear(item.fixedUseEndTime))
        val imgChose = helper.getView<AppCompatImageView>(R.id.iv_chose)

        imgChose.isSelected = mChoseId == item.couponReceiveId

    }

    fun setChoseId(voucherId: String) {
        mChoseId = voucherId
    }

}