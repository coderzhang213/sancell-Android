package cn.sancell.xingqiu.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zj on 2019/12/27.
 */
data class CommdoityData(
        val authorId: String,
        val createdAt: String,
        val desc: String,
        val id: String,
        val moduleId: String,
        val name: String,
        val show: String,
        val sort: String,
        val status: String,
        val updatedAt: String,
        val goods: MutableList<CommdoityChildData>,
        val coverPic: String

) : MultiItemEntity {

    override fun getItemType(): Int = 0
}

