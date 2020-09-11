package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/25.
 */
class ChatGroupRepository : BaseRepository() {

    suspend fun getCommunityRecommGroupListData(par: Map<String, String>): ResResponse<RecommendGroupListBean> {
        return mServe.getCommunityRecommGroupListData(par)

    }
}