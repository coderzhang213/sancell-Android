package cn.sancell.xingqiu.homeuser.income.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.OnImagUpLoadLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.util.DialogUtil
import cn.sancell.xingqiu.util.ImgUpLoadManager
import cn.sancell.xingqiu.viewmodel.IncomeViewModel
import com.luck.picture.lib.PictureSelector
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.fragment_add_bank_card_layout.*

/**
 * Created by zj on 2020/6/17.
 */
class AddBnkCardFragment : BaseNotDataFragmentKt<IncomeViewModel>() {
    var userBackImgUrl = ""
    var userImgUrl = ""

    //是否正面身份证
    var isBackUserCodeImg = false
    private var mImgUpLoadManager: ImgUpLoadManager? = null
    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<IncomeViewModel>? {
        return IncomeViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mAddBnakRes.observe(this@AddBnkCardFragment, Observer {
                activity?.finish()
            })
            errMsg.observe(this@AddBnkCardFragment, Observer {
                ToastHelper.showToast(it)
            })
        }

    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_add_bank_card_layout

    override fun initView() {
        setTitleName("添加银行卡")
        tv_bind_band.setOnClickListener(mOnClickLinsener)
        ll_user_z.setOnClickListener(mOnClickLinsener)
        ll_user_f.setOnClickListener(mOnClickLinsener)
    }

    fun selectImg(isBack: Boolean) {
        isBackUserCodeImg = isBack
        DialogUtil.showModifyPhoto(activity)

    }

    private val mOnClickLinsener = View.OnClickListener {
        when (it.id) {
            R.id.tv_bind_band -> {
                addBnak()
            }
            R.id.ll_user_z -> {
                selectImg(false)
            }
            R.id.ll_user_f -> {
                selectImg(true)

            }
        }
    }

    fun addBnak() {
        val bankUserName = et_bank_user_name.text.toString().trim()
        if (TextUtils.isEmpty(bankUserName)) {
            ToastHelper.showToast("请输入持卡人姓名")
            return
        }
        val bankCode = et_bank_code.text.toString().trim()
        if (TextUtils.isEmpty(bankCode)) {
            ToastHelper.showToast("请输入银行卡号")
            return
        }
        val bankName = et_bank_name.text.toString().trim()
        if (TextUtils.isEmpty(bankName)) {
            ToastHelper.showToast("请输入银行名称")
            return
        }
        if (TextUtils.isEmpty(userImgUrl)) {
            ToastHelper.showToast("请选择身份证正面照")
            return
        }
        if (TextUtils.isEmpty(userBackImgUrl)) {
            ToastHelper.showToast("请选择身份证反面照")
            return
        }
        mViewModel.addBnakCard(bankUserName, bankCode, bankName)

    }

    override fun initData() {
        mImgUpLoadManager = ImgUpLoadManager(this@AddBnkCardFragment)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val mPictures = PictureSelector.obtainMultipleResult(data)
            if (mPictures != null && mPictures.size > 0) {
                var fileType = ""
                if (isBackUserCodeImg) {
                    fileType = "1302"
                } else {
                    fileType = "1301"
                }

                mImgUpLoadManager?.uploadImageSelect(2, fileType, "", mPictures.get(0), object : OnImagUpLoadLinsener {
                    override fun onImagUploadError(errorMsg: String) {
                        Log.i("keey", "errorMsg:" + errorMsg)
                        ToastHelper.showToast(errorMsg)
                    }

                    override fun onImagUploadSucess(fildPaht: String) {
                        Log.i("keey", "fildPaht:" + fildPaht)
                        var mSelectImgPath = ""
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            mSelectImgPath = mPictures.get(0).androidQToPath

                        } else {
                            mSelectImgPath = mPictures.get(0).path
                        }
                        setUserImg(mSelectImgPath)
                    }
                })


            }
        }
    }

    fun setUserImg(fildPaht: String) {
        if (isBackUserCodeImg) {
            userBackImgUrl = fildPaht
            ImageLoaderUtils.loadImage(context, fildPaht, rl_f_img)
        } else {
            userImgUrl = fildPaht
            ImageLoaderUtils.loadImage(context, fildPaht, rl_z_img)
        }
    }
}