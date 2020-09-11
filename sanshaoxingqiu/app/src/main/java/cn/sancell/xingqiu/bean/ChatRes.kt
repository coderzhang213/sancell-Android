package cn.sancell.xingqiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class ChatRes : MultiItemEntity,Comparable<ChatRes>{
    override fun compareTo(other: ChatRes): Int {
        return if (this.sendTime > other.sendTime){
            0
        }else{
            1
        }
    }

    var chatItemType = 0

    var id: String? = null

    var userId: String? = null

    var userGravatar: String? = null

    var friendId: String? = null

    var friendNickName: String? = null

    var friendGravatar: String? = null

    var senderId: String? = null

    var receiverId: String? = null

    var msgTitle: String? = null

    var msgContent: String? = null

    var messageType: String? = null

    var readStatus: Int = 0

    var sendTime: Long = 0

    var status: String? = null

    override fun getItemType(): Int = chatItemType



}