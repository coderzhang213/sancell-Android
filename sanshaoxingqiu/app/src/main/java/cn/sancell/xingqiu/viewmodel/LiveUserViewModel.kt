package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.base.entity.EmptyBean
import cn.sancell.xingqiu.bean.LiveFocusRes
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.bean.LiveParFollowInfo
import cn.sancell.xingqiu.bean.LiveUserInfoBean
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiveUserViewModel : BaseViewModel() {

    private val mUserRepository by lazy { LiveUserRepository() }

    val mLiveAppointmentData = MutableLiveData<Int>()

    val mFocusLiverData = MutableLiveData<LiveFocusRes>()

    val mLiveUserInfo = MutableLiveData<LiveUserInfoBean>()

    val mChangeIntroData = MutableLiveData<String>()

    val mUpBgData = MutableLiveData<UpLoadPhotoInfoBean>()

    val mLiveDataList = MutableLiveData<LiveParFollowInfo>()

    val mUserLikeVideo = MutableLiveData<LiveParFollowInfo>()


    /**
     * 直播预约
     * type: 1预约 2取消
     * batchId: 直播批次id
     */
    fun liveAppointment(type: String, batchId: String): MutableLiveData<Int> {
        val params = ConvertUtils.getRequest()
        params["type"] = type
        params["batchId"] = batchId
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.liveAppointment(params)
            }
            executeResponse(response, { mLiveAppointmentData.value = response.retCode }, { errMsg.value = response.retMsg })
        }
        return mLiveAppointmentData
    }

    /**
     * 关注
     * type：1关注；2取消关注
     */
    fun focusLiver(liverId: String?, type: Int) {
        val params = ConvertUtils.getRequest()
        params["followedId"] = liverId
        params["type"] = type.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getLiveFocus(params)
            }
            executeResponse(response, { mFocusLiverData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 获取用户基本信息
     * userId:用户ID
     */
    fun getUserInfo(userId: String) {
        val params = ConvertUtils.getRequest()
        params["userId"] = userId
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getUserInfo(params)
            }
            executeResponse(response, { mLiveUserInfo.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 修改用户简介
     */
    fun modifyIntro(intro: String) {
        val params = ConvertUtils.getRequest()
        params["intro"] = intro
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.modifyUserIntro(params)
            }
            executeResponse(response, { mChangeIntroData.value = intro }, { errMsg.value = response.retMsg })
        }
    }

    fun getLiveList(userId: String, page: Int) {
        val params = ConvertUtils.getRequest()
        params["userId"] = userId
        params["page"] = page.toString()
        params["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getUserLiveList(params)
            }
            executeResponse(response, { mLiveDataList.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun upUserBg(rawArr: String, fileType: Int, objId: String) {
        val params = ConvertUtils.getRequest()
        params["rawArr"] = rawArr
        params["fileType"] = fileType.toString()
        params["objId"] = objId
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.upImageBg(params)
            }
            executeResponse(response, { mUpBgData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun getUserLikeVideo(userId: String, page: Int) {
        val params = ConvertUtils.getRequest();
        params["userId"] = userId
        params["page"] = page.toString()
        params["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mUserRepository.getUserLikeVideo(params)
            }
            executeResponse(response, { mUserLikeVideo.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }



}