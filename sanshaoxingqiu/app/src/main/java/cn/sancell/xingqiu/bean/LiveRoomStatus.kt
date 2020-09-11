package cn.sancell.xingqiu.bean

/**
 * Created by zj on 2020/4/13.
 */
data class LiveRoomStatus(var batchStatus: Int = 3//2 直播中 3已结束 1预告 4 时间段内未直播 5.急停
) {
}