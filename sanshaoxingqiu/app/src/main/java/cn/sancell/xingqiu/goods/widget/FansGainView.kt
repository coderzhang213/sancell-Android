package cn.sancell.xingqiu.goods.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.BigDecimalUtils
import kotlinx.android.synthetic.main.vg_goods_star_rules.view.*


class FansGainView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {


    init {
        addView(LayoutInflater.from(context).inflate(R.layout.vg_goods_star_rules, rootView as ViewGroup, false))
    }

    fun setData(data: ProductInfoDataBean) {
        if (TextUtils.isEmpty(data.lv1FansSharingE2) && TextUtils.isEmpty(data.lv2FansSharingE2) && TextUtils.isEmpty(data.lv3FansSharingE2)) {
            tv_fans_gain_list.visibility = View.GONE
        } else {
            var levelStr = ""
            when (data.productType) {
                1 -> {
                    levelStr = "一星"
                }
                2 -> {
                    levelStr = "二星"
                }
                3 -> {
                    levelStr = "三星"
                }
            }
            tv_purchase_gain.text = "购买当前超值医美产品，赠送${levelStr}粉丝资格"
            tv_fans_gain_list.visibility = View.VISIBLE
            tv_fans_gain_list.text = "您为一星粉丝，将获得 ¥${BigDecimalUtils.div(data.lv1FansSharingE2, "100", 2)}" +
                    "\n 您为二星粉丝，将获得 ¥${BigDecimalUtils.div(data.lv2FansSharingE2, "100", 2)} " +
                    "\n 您为三级粉丝，将获得 ¥${BigDecimalUtils.div(data.lv3FansSharingE2, "100", 2)}"
        }
    }


}