package cn.sancell.xingqiu.live.nim

object PublishParamUtils {


    fun serTypeToLocalType(sType: Int): String {
        var mLocType = PublishParam.HD
        when (sType) {
            1 -> {
                mLocType = PublishParam.HD
            }
            2 -> {
                mLocType = PublishParam.SD
            }
            3 -> {
                mLocType = PublishParam.LD
            }
        }
        return mLocType
    }

    fun serTypeToLocalTypeName(sType: Int): String {
        var mLocType = "高清"
        when (sType) {
            1 -> {
                mLocType = "高清"
            }
            2 -> {
                mLocType = "标清"
            }
            3 -> {
                mLocType = "流畅"
            }
        }
        return mLocType
    }

    fun locTypeSerType(sType: String): Int {
        var mSerType = 1
        when (sType) {
            PublishParam.HD -> {
                mSerType = 1
            }
            PublishParam.SD -> {
                mSerType = 2
            }
            PublishParam.LD -> {
                mSerType = 3
            }
        }
        return mSerType
    }
}