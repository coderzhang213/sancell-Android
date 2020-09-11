package cn.sancell.xingqiu.util

import android.text.TextUtils

/**
 * Created by zj on 2020/3/26.
 */
object SensitiveWordsUtil {
    val swMsg = ArrayList<String>()

    init {
        swMsg.add("渠道")
        swMsg.add("干细胞")
        swMsg.add("静血排毒")
        swMsg.add("怎么合作")
        swMsg.add("怎么分成")
        swMsg.add("骗钱")
        swMsg.add("免疫细胞")
        swMsg.add("违法")
        swMsg.add("国家不允许")
        swMsg.add("副作用")
        swMsg.add("服务商")
        swMsg.add("美容院业绩分成")
        swMsg.add("分钱")
        swMsg.add("带客奖励")
        swMsg.add("考察")
    }

    /**
     * 替换敏感词
     */
    fun sensitiveWords(msg: String): String {
        var newMsg = msg
        for (info in swMsg) {
            if (TextUtils.isEmpty(newMsg)) {
                if (msg.contains(info)) {
                    newMsg = msg.replace(info, "***", true)
                }
            } else {
                if (newMsg.contains(info)) {
                    newMsg = newMsg.replace(info, "***", true)
                }
            }

        }
        return newMsg
    }
}