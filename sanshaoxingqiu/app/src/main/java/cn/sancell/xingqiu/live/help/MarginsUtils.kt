package cn.sancell.xingqiu.live.help

import android.view.View
import android.view.ViewGroup

/**
 * Created by zj on 2020/4/7.
 */
object MarginsUtils {
    /**
     * 设置mar
     */
    fun setMargins(view: View, l: Int, t: Int, r: Int, b: Int) {
        val lay = view.layoutParams
        if (lay is ViewGroup.MarginLayoutParams) {
            val p = lay
            p.setMargins(l, t, r, b)
            view.requestLayout()
        }
    }
}