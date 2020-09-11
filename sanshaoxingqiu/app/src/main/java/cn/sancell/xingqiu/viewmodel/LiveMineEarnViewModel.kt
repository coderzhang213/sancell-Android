package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.ProfileRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiveMineEarnViewModel : BaseViewModel() {

    val mRepository by lazy { LiveUserRepository() }

    var mProfileData = MutableLiveData<ProfileRes>()

    fun getProfile(page: Int) {

        val params = ConvertUtils.getRequest()
        params["page"] = page.toString()
        params["pageSize"] = "20"

        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.getProfile(params)
            }
            executeResponse(response, { mProfileData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }
}