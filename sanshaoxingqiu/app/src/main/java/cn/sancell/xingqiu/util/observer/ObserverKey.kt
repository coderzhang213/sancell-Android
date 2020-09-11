package cn.sancell.xingqiu.util.observer

/**
 * Created by zj on 2019/12/12.
 */
object ObserverKey {
    //创建新地址
    const val ADD_NEW_ADDRESS = "add_new_address"

    //如果服务器宕机了
    const val SERVER_ERROR = "server_error"

    //更新订单列表
    const val UP_FIGHT_LIST = "up_fight_list"

    const val LIVE_LIFE_STAUS = "live_life_staus"

    //简洁模式
    const val LIVE_CONCISE_OPEN = "live_concise_open"

    //推荐关闭
    const val LIVE_REM_CLOSE = "live_rem_close"

    //关注关闭
    const val LIVE_ATTEN_CLOSE = "live_atten_close"
    const val LIVE_Hidden = "live_hidden"

    //直播退出
    const val LIVE_UP_PLAAY_EXIT = "live_up_play_status"

    //离开直播间
    const val GO_AWAY_LIVE = "go_away_live"

    const val LIVE_FLOATE_PLAY = "live_play"

    //悬浮播放结束
    const val FLOAT_END = "live_float_end"
}