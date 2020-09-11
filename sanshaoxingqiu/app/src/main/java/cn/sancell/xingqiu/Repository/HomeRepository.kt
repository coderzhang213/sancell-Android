package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.homepage.bean.HomeActiveInfoBean
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.homepage.bean.HomeMenuInfo
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/24.
 */
class HomeRepository : BaseRepository() {

    suspend fun getHomeActiveInfoData(par: Map<String, String>): ResResponse<HomeActiveInfoBean> {

        return mServe.getHomeActiveInfoData(par)

    }

    suspend fun getHomeMenu(par: Map<String, String>): ResResponse<HomeMenuInfo> {

        return mServe.getHomeMenu(par)

    }
    suspend fun getTest(par: Map<String, String>): ResResponse<String> {

        return mServe.getTest(par)

    }

}