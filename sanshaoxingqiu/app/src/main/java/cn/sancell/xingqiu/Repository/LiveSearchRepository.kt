package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.base.entity.EmptyBean
import cn.sancell.xingqiu.bean.LiveHotRes
import cn.sancell.xingqiu.bean.LiveSearchRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

class LiveSearchRepository : BaseRepository() {

    suspend fun getHotSearch(params: Map<String, String>): ResResponse<LiveHotRes> {
        return mServe.liveHotSearch(params)
    }

    suspend fun getHistorySearch(params: Map<String, String>): ResResponse<LiveHotRes> {
        return mServe.liveHistorySearch(params)
    }

    suspend fun clearSearchHistory(params: Map<String, String>): ResResponse<EmptyBean> {
        return mServe.liveHistoryClear(params)
    }

    suspend fun getSearchDetail(params: Map<String, String>): ResResponse<LiveSearchRes> {

        return mServe.liveSearch(params)
    }

}