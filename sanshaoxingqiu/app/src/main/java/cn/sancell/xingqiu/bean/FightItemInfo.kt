package cn.sancell.xingqiu.bean

import cn.sancell.xingqiu.order.entity.res.InviteBean
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zj on 2020/1/3.
 */
data class FightItemInfo(
        val buyerId: String,
        var grouponOrderStatus: Int,
        val buyerStatus: Int,
        val currentTime: Long,
        val goodsIcon: String,
        val goodsId: String,
        val goodsTitle: String,
        val grouponNum: Int,
        val grouponOrderId: String,
        val id: String,
        val lastUserNum: Int,
        val payStatus: Int,
        var grouponPriceE2: Long,
        val grouponEndTime: Long,
        val parcelId: String,
        val grouponInviteData: InviteBean
) : MultiItemEntity {
    override fun getItemType(): Int = 1
}
