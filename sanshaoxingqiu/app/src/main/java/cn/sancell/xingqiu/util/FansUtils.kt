package cn.sancell.xingqiu.util

import android.content.Context
import android.text.SpannableStringBuilder
import cn.sancell.xingqiu.R

class FansUtils {

    companion object {
        fun getFansOrFocusStr(strType: String, count: Int): String {
            val countStr: String = if (count >= 10000) {
                BigDecimalUtils.div(count.toString(), "10000", 1) + "w"
            } else {
                count.toString()
            }
            return countStr + "\t" + strType
        }

        fun getUserFansCount(context: Context, strType: String, count: Int): SpannableStringBuilder {
            val countStr: String = if (count >= 10000) {
                BigDecimalUtils.div(count.toString(), "10000", 1) + "w"
            } else {
                count.toString()
            }
            return FontUtils.getInstance().changeTextColor(context.resources.getColor(R.color.text_main), countStr + "\t" + strType, countStr)
        }

        fun getLikeCount(count: Int): String {
            return if (count >= 10000) {
                BigDecimalUtils.div(count.toString(), "10000", 1) + "w个赞"
            } else {
                count.toString() + "个赞"
            }
        }

        fun getFansOrFocusStrByList(strType: String, count: Int): String {
            val countStr: String = if (count >= 10000) {
                BigDecimalUtils.div(count.toString(), "10000", 1) + "w"
            } else {
                count.toString()
            }
            return strType + "\t" + countStr
        }

        fun getNumCount(count: Int): String {
            if (count >= 10000) {
                return BigDecimalUtils.div(count.toString(), "10000", 1) + "w"
            } else {
                return count.toString()
            }
        }
    }
}