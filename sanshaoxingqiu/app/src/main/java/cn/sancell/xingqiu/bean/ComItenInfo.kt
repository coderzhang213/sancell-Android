package cn.sancell.xingqiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zj on 2019/12/30.
 */
data class ComItenInfo(val goodsId: String,
                       val skuId: String,
                       val sort: String,
                       val title: String,
                       val marketPrice: Long,
                       val sellPrice: Long,
                       val coverPic: String
) : MultiItemEntity {
    override fun getItemType(): Int = 0
}
