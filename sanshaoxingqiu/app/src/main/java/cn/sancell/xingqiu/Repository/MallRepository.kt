package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.ComInfoList
import cn.sancell.xingqiu.bean.CommdoityResData
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/27.
 */
class MallRepository : BaseRepository() {
    suspend fun getMallList(par: Map<String, String>): ResResponse<CommdoityResData> {

        return mServe.getMallList(par)

    }

    suspend fun getMallInfoList(par: Map<String, String>): ResResponse<ComInfoList> {

        return mServe.getMallInfoList(par)

    }

    suspend fun getThirdClassifyListData(par: Map<String, String>): ResResponse<HomePageLikeListDataBean.LikeListDataBean> {

        return mServe.getThirdClassifyListData(par)

    }
}