package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.ImagUploadRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.ImageUpParInfo
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import com.alibaba.fastjson.JSON
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImagUploadViewModel : BaseViewModel() {
    val mImagUploadRepository by lazy { ImagUploadRepository() }


    //上传图片
    fun upRemmoidtyName(fileType: String, objId: String, imgType: String, imgWidth: String, imgHeight: String, fileSize: String): MutableLiveData<UpLoadPhotoInfoBean> {
        val upload: MutableLiveData<UpLoadPhotoInfoBean> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        val mImageUpParInfo = ImageUpParInfo(imgWidth, imgHeight, imgType, fileSize)
        val mList = ArrayList<ImageUpParInfo>()
        mList.add(mImageUpParInfo)
        par["rawArr"] = JSON.toJSONString(mList)

        par["objId"] = objId
        par["fileType"] = fileType
        launch {
            val response = withContext(Dispatchers.IO) {
                mImagUploadRepository.imgUpLoadInfo(par)
            }
            executeResponse(response, { upload.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return upload
    }

    /**
     * 上传身份证图片
     */
    fun upUserRemmoidtyName(fileType: String, imgType: String, imgWidth: String, imgHeight: String, fileSize: String): MutableLiveData<UpLoadPhotoInfoBean> {
        val upload: MutableLiveData<UpLoadPhotoInfoBean> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        val mImageUpParInfo = ImageUpParInfo(imgWidth, imgHeight, imgType, fileSize)
        val mList = ArrayList<ImageUpParInfo>()
        mList.add(mImageUpParInfo)
        par["rawArr"] = JSON.toJSONString(mList)
        par["fileType"] = fileType
        launch {
            val response = withContext(Dispatchers.IO) {
                mImagUploadRepository.userImgUpLoadInfo(par)
            }
            executeResponse(response, { upload.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return upload
    }
}