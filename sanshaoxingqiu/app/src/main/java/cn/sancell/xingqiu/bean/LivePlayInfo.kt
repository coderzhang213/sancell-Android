package cn.sancell.xingqiu.bean

data class LivePlayInfo(var isFollow: String
        /** 0未关注；1已关注**/
                        ,
                        var isLike: String,
                        var batchInfo: BatchInfo,
                        var liveInviteData: LiveInviteData,
                        var liveRoomInfo: LiveRoomInfo,
                        var relationGoodsCount: Int,
                        var relationGroupCount: Int,
                        var isReserve: String
        /**是否预约 0未预约；1已预约 **/
                        ,
                        var reserveCount: Int
        /**预约人数 **/
                        ,
                        var likeCount: Int,
                        var fansCount: Int,
                        var recList: RecomLiveParInfo? = null//推荐列表
                        ,
                        var onlineUserCount: Int = 0,//在线人数,
                        var pushFlag: String = "",
                        var isBindingCoupon: Boolean = false//是否关联代金券



) {
}