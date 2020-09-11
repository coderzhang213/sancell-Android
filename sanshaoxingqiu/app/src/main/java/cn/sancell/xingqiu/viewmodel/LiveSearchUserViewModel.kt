package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveSearchRepository
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.LiveFocusRes
import cn.sancell.xingqiu.bean.LiveSearchRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiveSearchUserViewModel : BaseViewModel() {

    private val mRepository by lazy { LiveSearchRepository() }

    private val mUserRepository by lazy { LiveUserRepository() }

    val mSetFocusState = MutableLiveData<LiveFocusRes>()

    val mSearchData: MutableLiveData<LiveSearchRes> = MutableLiveData()

    fun searchValue(keyword: String, page: Int, flag: Int) {
        val params = ConvertUtils.getRequest()
        params["keyword"] = keyword
        params["page"] = page.toString()
        params["pageSize"] = 10.toString()
        params["flag"] = flag.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.getSearchDetail(params)
            }
            executeResponse(response, { mSearchData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun setFocusLiver(liverId: String?, type: Int) {
        val params = ConvertUtils.getRequest()
        params["followedId"] = liverId
        params["type"] = type.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getLiveFocus(params)
            }
            executeResponse(response, { mSetFocusState.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }
}