package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

class ImagUploadRepository : BaseRepository() {


    //上传图片
    suspend fun imgUpLoadInfo(par: Map<String, String>): ResResponse<UpLoadPhotoInfoBean> {
        return mServe.imgUpLoadInfo(par)
    }

    //上传身份证图片
    suspend fun userImgUpLoadInfo(par: Map<String, String>): ResResponse<UpLoadPhotoInfoBean> {
        return mServe.userImgUpLoadInfo(par)
    }
}