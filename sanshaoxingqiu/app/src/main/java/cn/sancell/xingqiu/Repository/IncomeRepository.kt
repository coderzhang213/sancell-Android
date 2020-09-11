package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.DefalutResultInfo
import cn.sancell.xingqiu.bean.IncomeParInfo
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2020/6/18.
 */
class IncomeRepository : BaseRepository() {
    suspend fun addBankCard(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.addBankCard(par)
    }

    suspend fun userWitraw(par: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.userWitraw(par)
    }
    suspend fun useriNCOME(par: Map<String, String>): ResResponse<IncomeParInfo> {
        return mServe.useriNCOME(par)
    }
}