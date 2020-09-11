package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.Repository.VideoPlayRepository
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes
import handbank.hbwallet.BaseViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2019/12/20.
 */
class VideoPlayViewModel : BaseViewModel() {
    val mVideoPlayInfo: MutableLiveData<VideoDetailRes> = MutableLiveData()
    val mVideoList: MutableLiveData<CommunityVideoListBean> = MutableLiveData()
    val mVideoInfo: MutableLiveData<VideoRelationRes> = MutableLiveData()
    val addCar: MutableLiveData<Any> = MutableLiveData()
    private val mVideoPlayRepository by lazy { VideoPlayRepository() }
    /**
     * 加入购物车
     */
    fun addCart(goodsId: String, goodsNum: String) {
        val par = ConvertUtils.getRequest()
        par["goodsId"] = goodsId
        par["goodsNum"] = goodsNum
        launch {
            val response = withContext(Dispatchers.IO) {
                mVideoPlayRepository.addCart(par)
            }
            executeResponse(response, { addCar.value = response.retData }, { errMsg.value = response.retMsg })

        }
    }

    /**
     * 获取直播信息
     */
    fun getVideoPlayInfo(playId: String) {
        val par = ConvertUtils.getRequest()
        par["videoId"] = playId
        launch {
            val response = withContext(Dispatchers.IO) {
                mVideoPlayRepository.getVideoPlayInfo(par)
            }
            executeResponse(response, { mVideoPlayInfo.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 获取视频关联商品信息
     */
    fun getVideoRelation(type: String, videoId: String, isLastGet: Boolean) {
        val par = ConvertUtils.getRequest()
        par["type"] = type
        par["videoId"] = videoId
        launch {
            val response = withContext(Dispatchers.IO) {
                mVideoPlayRepository.getVideoRelation(par)

            }

            response.retData.type = type
            response.retData.isLastGet = isLastGet
            executeResponse(response, { mVideoInfo.value = response.retData }, { errMsg.value = response.retMsg })

        }
    }

    /**
     * 获取视频列表
     */
    fun getVideoList(mPagerIndex: Int, mPagerSize: Int) {
        val par = ConvertUtils.getRequest()
        par["page"] = mPagerIndex.toString()
        par["pageSize"] = mPagerSize.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mVideoPlayRepository.getVideoList(par)
            }
            executeResponse(response, { mVideoList.value = response.retData }, { errMsg.value = response.retMsg })

        }

    }
}