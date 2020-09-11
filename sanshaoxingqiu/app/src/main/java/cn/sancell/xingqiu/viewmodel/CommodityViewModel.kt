package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.MallRepository
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RSAUtils
import cn.sancell.xingqiu.util.StringUtils
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommodityViewModel : BaseViewModel() {
    val mMallRepository by lazy { MallRepository() }
    val mComListData = MutableLiveData<HomePageLikeListDataBean.LikeListDataBean>()

    /**
     * 搜索商品数据
     */
    fun getThirdClassifyListData(keyWord: String, page: Int) {
        val par = HashMap<String, String>()
        val reqTime = StringUtils.getCurrentTime()
        val skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["page"] = page.toString()
        par["pageSize"] = "10"
        par["keyWord"] = keyWord
        par["reqTime"] = reqTime
        par["skey"] = skey
        par["hashToken"] = RSAUtils.encryptByPublic(reqTime + skey)
        launch {
            val response = withContext(Dispatchers.IO) {
                mMallRepository.getThirdClassifyListData(par)
            }
            executeResponse(response, { mComListData.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }
}