package cn.sancell.xingqiu.util

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import cn.sancell.xingqiu.interfaces.OnImagUpLoadLinsener
import cn.sancell.xingqiu.util.MyOSSUtils.OssUpCallback
import cn.sancell.xingqiu.viewmodel.ImagUploadViewModel
import com.luck.picture.lib.entity.LocalMedia

class ImgUpLoadManager {
    var mImagUploadViewModel: ImagUploadViewModel? = null
    private var activity: FragmentActivity? = null
    private var fragme: Fragment? = null
    private var context: Context? = null
    private var mOnImagUpLoadLinsener: OnImagUpLoadLinsener? = null
    private var mHandler: Handler? = null

    constructor(activity: FragmentActivity) {
        this.activity = activity
        context = activity
        mImagUploadViewModel = ViewModelProviders.of(activity).get(ImagUploadViewModel::class.java)
        startObs()
    }

    constructor(fragme: Fragment) {
        this.fragme = fragme
        context = fragme.context
        mImagUploadViewModel = ViewModelProviders.of(fragme).get(ImagUploadViewModel::class.java)
        startObs()
    }

    fun startObs() {
        mHandler = Handler(Looper.getMainLooper())
        mImagUploadViewModel?.apply {

            fragme?.apply {
                errMsg.observe(this, Observer {
                    mOnImagUpLoadLinsener?.onImagUploadError(it)

                })
            }
            activity?.apply {
                errMsg.observe(this, Observer {
                    mOnImagUpLoadLinsener?.onImagUploadError(it)
                })
            }
        }
    }

    private fun uploadImage(fileType: String, objId: String, filePath: String, mOnImagUpLoadLinsener: OnImagUpLoadLinsener, upType: Int) {
        this.mOnImagUpLoadLinsener = mOnImagUpLoadLinsener
        val bitmap = BitmapFactory.decodeFile(filePath)
        val split = filePath.split(".")
        val imgType = split.get(split.size - 1)
        var mObs: MutableLiveData<UpLoadPhotoInfoBean>? = null
        when (upType) {
            1 -> {
                mObs = mImagUploadViewModel?.upRemmoidtyName(fileType, objId, imgType, bitmap.width.toString(), bitmap.height.toString(), (bitmap.byteCount / 1024)
                        .toString())

            }
            2 -> {
                mObs = mImagUploadViewModel?.upUserRemmoidtyName(fileType, imgType, bitmap.width.toString(), bitmap.height.toString(), (bitmap.byteCount / 1024)
                        .toString())
            }
        }


        mObs?.apply {

            fragme?.apply {
                mObs.observe(this, Observer {
                    toImgUploadAlyun(it, filePath)
                })
            }
            activity?.apply {
                mObs.observe(this, Observer {
                    toImgUploadAlyun(it, filePath)
                })
            }
        }
    }

    /**
     * 图片上传
     */
    fun uploadImageSelect(fileType: String, objId: String, mLocalMedia: LocalMedia, mOnImagUpLoadLinsener: OnImagUpLoadLinsener) {
        uploadImageSelect(1, fileType, objId, mLocalMedia, mOnImagUpLoadLinsener)
    }

    fun uploadImageSelect(upType: Int, fileType: String, objId: String, mLocalMedia: LocalMedia, mOnImagUpLoadLinsener: OnImagUpLoadLinsener) {
        var mSelectImgPath = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mSelectImgPath = mLocalMedia.androidQToPath

        } else {
            mSelectImgPath = mLocalMedia.path
        }
        uploadImage(fileType, objId, mSelectImgPath, mOnImagUpLoadLinsener, upType)
    }

    //把图片上传到阿里云
    fun toImgUploadAlyun(mUpLoadPhotoInfoBean: UpLoadPhotoInfoBean, filePath: String) {

        val bucketName: String = mUpLoadPhotoInfoBean.getBackName()
        val objectKey: String = mUpLoadPhotoInfoBean.getKeyName()
        val accessKeyId: String = mUpLoadPhotoInfoBean.getAccessKeyId()
        val accessKeySecret = RSAUtils.decryptByPublic(mUpLoadPhotoInfoBean.getAccessKeySecret())
        val securityToken: String = mUpLoadPhotoInfoBean.getSecurityToken()
        MyOSSUtils.getInstance().getOSs(context!!, mUpLoadPhotoInfoBean.getEndpoint(), accessKeyId, accessKeySecret, securityToken)

        MyOSSUtils.getInstance().upImage(context!!, object : OssUpCallback {
            override fun successImg(img_url: String) {
                mHandler?.post {
                    mOnImagUpLoadLinsener?.onImagUploadSucess(img_url)
                }
            }

            override fun successVideo(video_url: String) {}
            override fun inProgress(progress: Long, zong: Long) {}
        }, bucketName, objectKey, filePath)
    }
}