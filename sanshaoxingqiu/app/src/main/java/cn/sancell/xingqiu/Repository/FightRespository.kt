package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.FightBaseData
import cn.sancell.xingqiu.order.entity.res.PinInviteRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2020/1/3.
 */
class FightRespository :BaseRepository(){
    suspend fun getFightDataList(par: Map<String, String>): ResResponse<FightBaseData> {

        return mServe.getFightDataList(par)

    }
}