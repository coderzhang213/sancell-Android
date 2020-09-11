package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveSearchRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.LiveHotRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiveSearchModel : BaseViewModel() {

    private val mRepository by lazy { LiveSearchRepository() }
    val mHotSearchData: MutableLiveData<LiveHotRes> = MutableLiveData()
    val mHistoryData: MutableLiveData<LiveHotRes> = MutableLiveData()
    val mClearCode: MutableLiveData<Int> = MutableLiveData()

    fun getHotSearchData() {
        val params = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.getHotSearch(params)
            }
            executeResponse(response, { mHotSearchData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun getHistorySearch(count:Int) {
        val params = ConvertUtils.getRequest()
        params["count"] = count.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.getHistorySearch(params)
            }
            executeResponse(response, { mHistoryData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun clearHistory() {
        val params = ConvertUtils.getRequest()
        ConvertUtils.getRequest(params)
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.clearSearchHistory(params)
            }
            executeResponse(response, { mClearCode.value = response.retCode }, { errMsg.value = response.retMsg })
        }
    }



}