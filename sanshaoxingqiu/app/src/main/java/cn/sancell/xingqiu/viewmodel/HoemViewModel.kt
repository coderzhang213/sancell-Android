package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.HomeRepository
import cn.sancell.xingqiu.Repository.BannerRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homepage.bean.HomeActiveInfoBean
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.homepage.bean.HomeMenuInfo
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RSAUtils
import cn.sancell.xingqiu.util.StringUtils
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2019/12/24.
 */
class HoemViewModel : BaseViewModel() {
    val mHomeRepository by lazy { HomeRepository() }
    val mBannerRepository by lazy {
        BannerRepository()
    }
    val mBannerData: MutableLiveData<HomeBannerDataBean> = MutableLiveData()
    val activityData: MutableLiveData<HomeActiveInfoBean> = MutableLiveData()
    val menuData: MutableLiveData<HomeMenuInfo> = MutableLiveData()
    fun getHomeActiveInfoData() {
        val par = HashMap<String, String>()
        par["reqTime"] = StringUtils.getCurrentTime()
        par["skey"] = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        launch {
            val response = withContext(Dispatchers.IO) {
                mHomeRepository.getHomeActiveInfoData(par)
            }
            executeResponse(response, { activityData.value = response.retData }, { errMsg.value = response.retMsg })

        }
    }

    fun testHead() {
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mHomeRepository.getTest(par)
            }
            executeResponse(response, {}, {})
        }
    }

    /**
     * 获取首页轮播图
     */
    fun getHomeBannerData() {
        val par = HashMap<String, String>()
        par["bannerAdType"] = "11"
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

    /**
     * 获取九宫格
     */
    fun getHomeMenu() {
        val par = HashMap<String, String>()
        par["reqTime"] = StringUtils.getCurrentTime()
        par["skey"] = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        par["pageSize"] = "9"
        par["page"] = "1"
        launch {
            val response = withContext(Dispatchers.IO) {
                mHomeRepository.getHomeMenu(par)
            }

            executeResponse(response, { menuData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }
}