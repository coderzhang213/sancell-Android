package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.FightRespository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.FightBaseData
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2020/1/3.
 */
class FightViewModel : BaseViewModel() {
    val mFightRespository by lazy { FightRespository() }
    val mFightBaseData = MutableLiveData<FightBaseData>()

    fun getFightDataList(type: String, page: Int) {
        val par = ConvertUtils.getRequest()
        par["orderStatus"] = type
        par["page"] = page.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mFightRespository.getFightDataList(par)
            }
            executeResponse(response, { mFightBaseData.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }
}