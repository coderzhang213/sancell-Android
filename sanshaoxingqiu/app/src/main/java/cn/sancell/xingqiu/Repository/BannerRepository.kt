package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.CommdoityResData
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2019/12/27.
 */
class BannerRepository : BaseRepository() {
    suspend fun getHomeBannerData(par: Map<String, String>): ResResponse<HomeBannerDataBean> {

        return mServe.getHomeBannerData(par)

    }
}