package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.UpIdCardBean
import cn.sancell.xingqiu.bean.VerifyRes
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import cn.sancell.xingqiu.bean.VerifyResultRes
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiverIdentifyViewModel : BaseViewModel() {

    private val mRepository by lazy { LiveUserRepository() }

    var mLiverVerifyCode: MutableLiveData<VerifyRes> = MutableLiveData()

    var mCheckData: MutableLiveData<VerifyResultRes> = MutableLiveData()

    var mUpLoadData = MutableLiveData<UpIdCardBean>()

    fun getLiverVerify(name: String, idCard: String) {

        val params = ConvertUtils.getRequest()
        params["name"] = name
        params["idCard"] = idCard
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.liveVerify(params)
            }
            executeResponse(response, { mLiverVerifyCode.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun upIdCard(rawArr: String, fileType: Int, objId:String) {
        val params = ConvertUtils.getRequest()
        params["rawArr"] = rawArr
        params["fileType"] = fileType.toString()
        params["objId"] = objId
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.upImage(params)
            }
            response.retData.type = fileType
            executeResponse(response, { mUpLoadData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun checkVerifyResult() {
        val params = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mRepository.getLiverVerifyResult(params)
            }
            executeResponse(response, { mCheckData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }


}