package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.UserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.FirenedBaseInfo
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2020/1/2.
 */
class UserViewModel : BaseViewModel() {
    val mUserRepository by lazy { UserRepository() }
    val mFirenedBaseInfoList = MutableLiveData<InviteFriendsListBean>()

    fun getInviteFriendsListData(page: Int) {
        val par = ConvertUtils.getRequest()
        par["page"] = page.toString()
        par["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getInviteFriendsListData(par)
            }
            executeResponse(response, { mFirenedBaseInfoList.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }
}