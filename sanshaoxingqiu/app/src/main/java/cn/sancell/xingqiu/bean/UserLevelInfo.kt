package cn.sancell.xingqiu.bean

/**
 * Created by zj on 2020/6/19.
 */
data class UserLevelInfo(var vipEndTime: Long = 0,
                         var realMemberLevel: Int = 0,
                         var realMemberLevelStr: String = "",
                         var useCouponPoint: Int = 0//是否使用红包和代金券（1可以；0不可以）
) {
}