package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/20.
 */
class VideoPlayRepository : BaseRepository() {
    suspend fun getVideoPlayInfo(param: Map<String, String>): ResResponse<VideoDetailRes> {
        return mServe.getVidoDetailKt(param)
    }

    suspend fun getVideoList(param: Map<String, String>): ResResponse<CommunityVideoListBean> {
        return mServe.getVideoList(param)
    }

    suspend fun getVideoRelation(param: Map<String, String>): ResResponse<VideoRelationRes> {
        return mServe.getVideoRelation(param)
    }

    suspend fun addCart(param: Map<String, String>): ResResponse<Any> {
        return mServe.addCart(param)
    }

}