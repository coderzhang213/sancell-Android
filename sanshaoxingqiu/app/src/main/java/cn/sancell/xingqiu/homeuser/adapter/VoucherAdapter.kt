package cn.sancell.xingqiu.homeuser.adapter


import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.VoucherInfo
import cn.sancell.xingqiu.homeuser.activity.VoucherActivity
import cn.sancell.xingqiu.util.BigDecimalUtils
import cn.sancell.xingqiu.util.DateUtils
import cn.sancell.xingqiu.util.FontUtils
import cn.sancell.xingqiu.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class VoucherAdapter(data: MutableList<VoucherInfo>) : BaseMultiItemQuickAdapter<VoucherInfo, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.recycle_voucher_valid)
        addItemType(1, R.layout.recycle_voucher_invalid)
        addItemType(2, R.layout.recycle_voucher_bottom)
    }

    override fun convert(helper: BaseViewHolder, item: VoucherInfo) {
        when (item.itemType) {
            0 -> {
                //有效
                val detail = helper.getView<LinearLayout>(R.id.ll_voucher_more)
                val expand = helper.getView<LinearLayout>(R.id.ll_voucher_expand)
                val ivMore = helper.getView<AppCompatImageView>(R.id.iv_voucher_more)
                val tvUse = helper.getView<AppCompatTextView>(R.id.tv_voucher_goods)
                val llMore = helper.getView<LinearLayout>(R.id.ll_voucher_more)

                helper.getView<AppCompatTextView>(R.id.tv_voucher_coin).text = FontUtils.getInstance().changeTextSize(mContext, 14,
                        BigDecimalUtils.div(item.faceValueE2.toString(), "100", 0) + "元", "元")

                val tvLimit = helper.getView<AppCompatTextView>(R.id.tv_voucher_limit)
                if (item.type == 1 || item.type == 3) {
                    tvLimit.visibility = View.GONE
                } else if (item.type == 2) {
                    if (item.limitMinUseMoneyE2 > 0) {
                        tvLimit.visibility = View.VISIBLE
                        tvLimit.text = StringBuffer("满" + BigDecimalUtils.div(item.limitMinUseMoneyE2.toString(), "100", 0) + "可用")
                    } else {
                        tvLimit.visibility = View.GONE
                    }
                }
                helper.getView<AppCompatTextView>(R.id.tv_voucher_title).text = item.name
                if (item.useType == 1) {
                    tvUse.text = "全部商品"
                } else {
                    tvUse.text = "部分商品"
                }
                helper.getView<AppCompatTextView>(R.id.tv_voucher_use_time).text = StringBuffer(DateUtils.getTimeByStampWithYear(item.fixedUseBeginTime)
                        + "-" + DateUtils.getTimeByStampWithYear(item.fixedUseEndTime))
                if (item.desc.isEmpty()) {
                    llMore.visibility = View.GONE
                    helper.getView<AppCompatTextView>(R.id.tv_voucher_expand).text = ""
                } else {
                    llMore.visibility = View.VISIBLE
                    helper.getView<AppCompatTextView>(R.id.tv_voucher_expand).text = item.desc
                }

                detail.setOnClickListener {
                    item.show = !item.show
                    notifyItemChanged(helper.adapterPosition)
                }
                if (item.show) {
                    setRotation(true, ivMore)
                    expand.visibility = View.VISIBLE
                } else {
                    setRotation(false, ivMore)
                    expand.visibility = View.GONE
                }

            }
            1 -> {
                //无效
                val detail = helper.getView<LinearLayout>(R.id.ll_voucher_in_more)
                val expand = helper.getView<LinearLayout>(R.id.ll_voucher_in_expand)
                val ivMore = helper.getView<AppCompatImageView>(R.id.iv_voucher_in_more)
                val tvUse = helper.getView<AppCompatTextView>(R.id.tv_voucher_in_goods)
                val llMore = helper.getView<LinearLayout>(R.id.ll_voucher_in_more)
                val state = helper.getView<AppCompatImageView>(R.id.iv_voucher_state)

                helper.getView<AppCompatTextView>(R.id.tv_voucher_in_coin).text = FontUtils.getInstance().changeTextSize(mContext, 14,
                        BigDecimalUtils.div(item.faceValueE2.toString(), "100", 0) + "元", "元")
                val tvLimit = helper.getView<AppCompatTextView>(R.id.tv_voucher_in_limit)
                if (item.limitMinUseMoneyE2 > 0) {
                    tvLimit.visibility = View.VISIBLE
                    tvLimit.text = StringBuffer("满" + BigDecimalUtils.div(item.limitMinUseMoneyE2.toString(), "100", 0)+ "可用")

                } else {
                    tvLimit.visibility = View.GONE
                }
                helper.getView<AppCompatTextView>(R.id.tv_voucher_in_title).text = item.name
                if (item.useType == 1) {
                    tvUse.text = "全部商品"
                } else {
                    tvUse.text = "部分商品"
                }
                helper.getView<AppCompatTextView>(R.id.tv_voucher_in_time).text = StringBuffer(DateUtils.getTimeByStampWithYear(item.fixedUseBeginTime)
                        + "-" + DateUtils.getTimeByStampWithYear(item.fixedUseEndTime))
                if (item.desc.isEmpty()) {
                    llMore.visibility = View.GONE
                    helper.getView<AppCompatTextView>(R.id.tv_voucher_in_expand).text = ""
                } else {
                    llMore.visibility = View.VISIBLE
                    helper.getView<AppCompatTextView>(R.id.tv_voucher_in_expand).text = item.desc
                }

                when (item.showUseStatus) {
                    1 -> {
                        state.setImageResource(R.mipmap.image_used_couponlist_unusable)
                    }
                    3 -> {
                        state.setImageResource(R.mipmap.image_expired_couponlist_unusable)
                    }
                }

                detail.setOnClickListener {
                    item.show = !item.show
                    notifyItemChanged(helper.adapterPosition)
                }
                if (item.show) {
                    setRotation(true, ivMore)
                    expand.visibility = View.VISIBLE
                } else {
                    setRotation(false, ivMore)
                    expand.visibility = View.GONE
                }

            }
            2 -> {
                helper.getView<AppCompatTextView>(R.id.tv_invalid).setOnClickListener {
                    VoucherActivity.start(mContext, 1)
                }
            }
        }
    }

    private fun setRotation(rotate: Boolean, view: View) {
        val rotaAnimation: RotateAnimation
        if (rotate) {
            rotaAnimation = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        } else {
            rotaAnimation = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        }
        view.animation = rotaAnimation
        //保存动话
        rotaAnimation.fillAfter = true
        rotaAnimation.duration = 300
        view.startAnimation(rotaAnimation)


    }

}