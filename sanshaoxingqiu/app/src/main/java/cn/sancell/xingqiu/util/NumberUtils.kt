package cn.sancell.xingqiu.util

/**
 * Created by zj on 2020/4/16.
 */
object NumberUtils {
    //转换成万
    fun getNumberToWan(sum: Int = 0): String {

        if (sum < 10000) {
            return sum.toString()
        } else {
            return String.format("%.2f", (sum.toDouble() / 10000)) + "w"
        }

        return ""
    }
}