package cn.sancell.xingqiu.ktenum

enum class LivePlayType(val type: String) {
    HOME_TO_TYPE("1"),//1主页推荐列表
    ATTENTIONS_TO_TYPE("2"),//2关注直播列表
    USER_CANER_TO_TYPE("3"),//3个人中心直播列表
    ATTEN_TJ("4"),//关注推荐
    AWESOME_TYPE("5"),//个人中心点赞列表
    RE_PLAY("6"),//回放
    LIVE_LIST("7"),//直播
    SERARCH_TYPE("100"),//搜索进入
    EXTERNAL_TYPE("101"),//外部进入
    USER_CAN_TYPE("102"),//个人中心进入直播
    HOME_BAND("103")//首页
}