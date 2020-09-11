package cn.sancell.xingqiu.bean

data class LiveRemmInfo(val batchId: String,
                        val id: String,
                        val accid: String,
                        val status: String
        /** 直播间状态（1启用、2关闭）**/
                        ,
                        val icon: String,
                        val liveStatus: String,
                        /**
                        1 未开始（预告）， 2直播中， 3 已结束（回放）**/
                        val liveName: String,
                        val liveIntro: String,
                        val roomId: String,
                        val pullUrl: String,
                        val onlineUser: String,
                        val replayUrl: String,
                        val tvUserGravatar: String,
                        val tvUserName: String,
                        val replayDate: Long,

                        var dataType: String = "",//1直播返回数据 2商品数据
                        var goodsTitle: String = "",
                        var goodsId: Int = 0,
                        var goodsSku: String = "",
                        var goodsCoverPic: String = "",
                        var grouponPriceE2: Long

) {
}