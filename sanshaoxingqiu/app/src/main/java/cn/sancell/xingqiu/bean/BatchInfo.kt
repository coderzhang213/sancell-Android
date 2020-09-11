package cn.sancell.xingqiu.bean

data class BatchInfo(var gravatar: String,
                     var icon: String,
                     var liveBatchId: String,
                     var liveEndTime: Long,
                     var liveIntro: String,
                     var liveRoomId: String,
                     var liveStartTime: Long,
                     var liveStatus: String,
                     /**1 未开始， 2直播中， 3 已结束 **/
                     var liveTitle: String,
                     var liveType: String,
                     var liveUserAccid: String,
                     var liveUserId: Int,
                     var liveUserName: String,
                     var replayUrl: String,
                     var liveUserNumber: String,
                     var iconThumb: String = ""//封面缩略图

) {
}