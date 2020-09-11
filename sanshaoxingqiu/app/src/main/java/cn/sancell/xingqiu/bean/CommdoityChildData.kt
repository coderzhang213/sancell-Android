package cn.sancell.xingqiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zj on 2019/12/27.
 */
data class CommdoityChildData(val goodsId: String,
                              val marketPrice: Long,
                              val sellPrice: Long,
                              val skuId: String,
                              val sort: String,
                              val title: String,
                              val coverPic: String
) : MultiItemEntity {

    override fun getItemType(): Int = 0
}

