package cn.sancell.xingqiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class LiveFollowInfo(val batchId: String,
                          val id: String,
                          val accid: String,
                          val status: String,
                          /** 直播间状态（1启用、2关闭）**/
                          val icon: String,
                          var liveStatus: String = "2",
                          /**1 未开始（预告）， 2直播中， 3 已结束（回放） **/
                          val liveName: String,
                          val liveIntro: String,
                          val replayStatus: String,
                          val roomId: String,
                          val pullUrl: String,
                          val onlineUser: Int,
                          val relationGoodsCount: String,
                          var relationGoodsList: List<LiveFollowComInfo>,
                          val replayUrl: String,
                          val replayWatchUser: Int,
                          var isReserve: String
        /** 1已经预约；0未预约**/
                          ,
                          val reserveCount: String,
                          val reserveStartLiveTime: Long,
                          val replayDate: Long,
                          val tvUserName: String,
                          val tvUserGravatar: String,
                          val userId: String,
                          var isBindingCoupon: Boolean = false,
                          val likeNumber: Int
) : MultiItemEntity {
    override fun getItemType(): Int = liveStatus.toInt()
}