package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.LiveFocusRes
import cn.sancell.xingqiu.bean.FansRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FocusListViewModel : BaseViewModel() {

    private val mUserRepository by lazy { LiveUserRepository() }

    val mFollowData = MutableLiveData<FansRes>()
    val mFanData = MutableLiveData<FansRes>()
    val mFocusData = MutableLiveData<LiveFocusRes>()

    fun getFollowData(userId: String, page: Int) {
        val params = ConvertUtils.getRequest()
        params["userId"] = userId
        params["page"] = page.toString()
        params["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getUserFollow(params)
            }
            executeResponse(response, { mFollowData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun getFansData(userId: String, page: Int) {
        val params = ConvertUtils.getRequest()
        params["userId"] = userId
        params["page"] = page.toString()
        params["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getUserFans(params)
            }
            executeResponse(response, { mFanData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun focusLiver(liverId: String?, type: Int) {
        val params = ConvertUtils.getRequest()
        params["followedId"] = liverId
        params["type"] = type.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getLiveFocus(params)
            }
            executeResponse(response, { mFocusData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

}