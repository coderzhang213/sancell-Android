package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.BannerRepository
import cn.sancell.xingqiu.Repository.MallRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.ComInfoList
import cn.sancell.xingqiu.bean.CommdoityResData
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RSAUtils
import cn.sancell.xingqiu.util.StringUtils
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2019/12/26.
 */
class MarketViewModel : BaseViewModel() {
    val mBannerRepository by lazy { BannerRepository() }
    val mMallRepository by lazy { MallRepository() }
    val mBannerData: MutableLiveData<HomeBannerDataBean> = MutableLiveData()
    val mMallListData: MutableLiveData<CommdoityResData> = MutableLiveData()
    val mComInfoListData: MutableLiveData<ComInfoList> = MutableLiveData()
    /**
     * 获取活动图
     */
    fun getBannerData(bannerAdType: String) {
        val par = HashMap<String, String>()
        par["bannerAdType"] = bannerAdType
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
     * 获取商城列表数据
     */
    fun getMallList(module: String, page: Int) {
        val par = ConvertUtils.getRequest(null)
        par["pageSize"] = "10"
        par["page"] = page.toString()
        par["module"] = module

        launch {
            val response = withContext(Dispatchers.IO) {
                mMallRepository.getMallList(par)
            }
            executeResponse(response, { mMallListData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 获取商品列表
     */
    fun getMallInfoList(page: Int, module: String) {
        val par = ConvertUtils.getRequest(null)
        par["pageSize"] = "10"
        par["page"] = page.toString()
        par["module"] = module
        launch {
            val response = withContext(Dispatchers.IO) {
                mMallRepository.getMallInfoList(par)
            }
            executeResponse(response, { mComInfoListData.value = response.retData }, { errMsg.value = response.retMsg })

        }

    }
}