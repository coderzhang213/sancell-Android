package cn.sancell.xingqiu.ktenum

/**
 * Created by zj on 2020/3/17.
 */
enum class LivePagerType(val type: String) {
    LIVE_PLAY("1"),//直播
    REPLAY("2"),//回放reservation
    RESERVATION("3")//预约
}