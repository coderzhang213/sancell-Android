package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.*
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData
import cn.sancell.xingqiu.homepage.bean.HomeRecommendLiverBean
import cn.sancell.xingqiu.im.entity.LiveCountInfo
import cn.sancell.xingqiu.im.entity.res.YueRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/23.
 */
class LiveRepository : BaseRepository() {
    suspend fun getLiveList(par: Map<String, String>): ResResponse<LiveData> {
        return mServe.getLiveList(par)
    }

    //直播登录
    suspend fun liveLogin(par: Map<String, String>): ResResponse<LiveLoginInfo> {
        return mServe.liveLogin(par)
    }

    //直播状态更新
    suspend fun upRoomStatus(par: Map<String, String>): ResResponse<LiveRoomUpInfo> {
        return mServe.upRoomStatus(par)
    }

    //直播推荐列表
    suspend fun getLiveV1PlayList(par: Map<String, String>): ResResponse<RecomLiveParInfo> {
        return mServe.getLiveV1PlayList(par)
    }

    //直播详情
    suspend fun getLivePlayInfo(par: Map<String, String>): ResResponse<LivePlayInfo> {
        return mServe.getLivePlayInfo(par)
    }

    //直播关注
    suspend fun upLiveFollow(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.upLiveFollow(par)
    }

    //获取直播人数
    suspend fun getLiveSums(par: Map<String, String>): ResResponse<LiveCountInfo> {
        return mServe.getLiveSums(par)
    }

    //直播预约
    suspend fun upLiveRescer(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.upLiveRescer(par)
    }

    //直播点赞
    suspend fun upLiveGilveLink(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.upLiveGilveLink(par)
    }

    //直播点赞
    suspend fun readPackLink(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.readPackLink(par)
    }

    //是否设置交易密码
    suspend fun checkPayPass(par: Map<String, String>): ResResponse<IsSetPassInfo> {
        return mServe.checkPayPass(par)
    }

    //直播关注列表
    suspend fun getLiveFollowList(par: Map<String, String>): ResResponse<HomeLiveListInfo> {
        return mServe.getLiveFollowList(par)
    }

    //直播推荐
    suspend fun getLiveRecommendList(par: Map<String, String>): ResResponse<LiveRemmParInfo> {
        return mServe.getLiveRecommendList(par)
    }

    suspend fun initLiveSet(par: Map<String, String>): ResResponse<LiveInitData> {
        return mServe.initLiveSet(par)
    }

    //获取直播关联商品
    suspend fun getLiveCommentList(par: Map<String, String>): ResResponse<LiveRelevPar> {
        return mServe.getLiveCommentList(par)
    }

    //直播添加商品或群组
    suspend fun addLiveReemAndGroup(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.addLiveReemAndGroup(par)
    }

    //直播删除关联商品或群组
    suspend fun deleteReemAndGroup(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.deleteReemAndGroup(par)
    }

    //更改商品名称别名
    suspend fun upRemmoidtyName(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.upRemmoidtyName(par)
    }

    //直播设置保存
    suspend fun liveSetSave(par: Map<String, String>): ResResponse<LiveSetResultParInfo> {
        return mServe.liveSetSave(par)
    }

    //获取红包余额
    suspend fun getReadPackAccout(par: Map<String, String>): ResResponse<YueRes> {
        return mServe.getReadPackAccout(par)
    }

    //检查是否有直播中的
    suspend fun checkLiveStatus(par: Map<String, String>): ResResponse<LiveBaseStatusInfo> {
        return mServe.checkLiveStatus(par)
    }

    suspend fun upLastLiveData(params: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.upLastLiveData(params)
    }

    //检查直播间状态
    suspend fun checkLiveRoomStatus(params: Map<String, String>): ResResponse<LiveRoomStatus> {
        return mServe.checkLiveRoomStatus(params)
    }

    suspend fun getRecommendLiver(params: Map<String, String>):ResResponse<HomeRecommendLiverBean>{
        return mServe.getRecommendLiver(params)
    }
    suspend fun remAncher(params: Map<String, String>):ResResponse<HomeRecommendLiverBean>{
        return mServe.remAncher(params)
    }
}