package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.UserMsgRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LetterViewModel : BaseViewModel() {

    val mReposonity by lazy { LiveUserRepository() }
    var mMsgDataList = MutableLiveData<UserMsgRes>()
    var mMsgRecordList = MutableLiveData<UserMsgRes>()
    val mSendData = MutableLiveData<String>()

    fun getMsgList(page: Int) {
        val params = ConvertUtils.getRequest()
        params["page"] = page.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mReposonity.getMsgList(params)
            }
            executeResponse(response, { mMsgDataList.value = response.retData }, { errMsg.value = response.retMsg })

        }
    }

    fun getMsgRecord(userId: String, page: Int) {
        val params = ConvertUtils.getRequest()
        params["page"] = page.toString()
        params["friendId"] = userId
        params["pageSize"] = "15"
        launch {
            val response = withContext(Dispatchers.IO) {
                mReposonity.getMsgRecord(params)
            }
            executeResponse(response, { mMsgRecordList.value = response.retData }, { errMsg.value = errMsg.value })
        }
    }

    fun sendMsg(friendId: String, content: String) {
        val params = ConvertUtils.getRequest()
        params["friendId"] = friendId
        params["content"] = content
        launch {
            val response = withContext(Dispatchers.IO) {
                mReposonity.sendMsg(params)
            }
            executeResponse(response, { mSendData.value = content }, { errMsg.value = response.retMsg })
        }
    }

}