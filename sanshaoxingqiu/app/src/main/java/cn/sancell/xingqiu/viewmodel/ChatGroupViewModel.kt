package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.BannerRepository
import cn.sancell.xingqiu.Repository.ChatGroupRepository
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RSAUtils
import cn.sancell.xingqiu.util.StringUtils
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2019/12/25.
 */
class ChatGroupViewModel : BaseViewModel() {
    val mBannerData: MutableLiveData<HomeBannerDataBean> = MutableLiveData()
    val mChatGroupRepository by lazy { ChatGroupRepository() }
    val mRemGroup = MutableLiveData<RecommendGroupListBean>()
    val mBannerRepository by lazy { BannerRepository() }

    fun getCommunityRecommGroupListData() {
        val par = HashMap<String, String>()
        par["reqTime"] = StringUtils.getCurrentTime()
        par["skey"] = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        launch {
            val response = withContext(Dispatchers.IO) {
                mChatGroupRepository.getCommunityRecommGroupListData(par)
            }
            executeResponse(response, { mRemGroup.value = response.retData }, { errMsg.value = response.retMsg })

        }
    }

    /**
     * 获取首页轮播图
     */
    fun getBannerData() {
        val par = HashMap<String, String>()
        par["bannerAdType"] = "3"
        par["reqTime"] = StringUtils.getCurrentTime()
        par["skey"] = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        launch {
            val response = withContext(Dispatchers.IO) {
                mBannerRepository.getHomeBannerData(par)
            }
            executeResponse(response, { mBannerData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }
}