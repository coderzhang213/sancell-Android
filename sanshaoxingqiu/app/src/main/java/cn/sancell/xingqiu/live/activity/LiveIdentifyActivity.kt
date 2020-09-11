package cn.sancell.xingqiu.live.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.UpIdCardBean
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.bean.UpImageJsonBean
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayHomeActivity
import cn.sancell.xingqiu.homepage.UrlInfoActivity
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.util.*
import cn.sancell.xingqiu.viewmodel.LiverIdentifyViewModel
import cn.sancell.xingqiu.widget.LiveIdentifyErrorView
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_identify_liver.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 直播认证
 */
class LiveIdentifyActivity : BaseActivity<LiverIdentifyViewModel>() {

    private var selectFrom = 0 //选中上传的图片的类型 0证 1反


    private var mFrontUoLoadData: UpIdCardBean? = null
    private var mReverseUpLoadData: UpIdCardBean? = null
    private var mFrontPath = ""
    private var mReversePath = ""

    private var frontSuc = false
    private var reverseSuc = false

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LiveIdentifyActivity::class.java)
            return context.startActivity(intent)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_identify_liver
    }

    override fun providerVMClass(): Class<LiverIdentifyViewModel>? {
        return LiverIdentifyViewModel::class.java
    }

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        btn_back.setOnClickListener { finish() }
        tv_title.text = "主播认证"

        iv_card_reverse.setOnClickListener {
            selectFrom = 1
            DialogUtil.showModifyPhoto(this)
        }

        iv_card_front.setOnClickListener {
            selectFrom = 0
            DialogUtil.showModifyPhoto(this)
        }

        et_id_card.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                //失去焦点的时候
                val result = IDCardUtil.validateIDNum(et_id_card.text.toString())
                result.show("请输入有效的身份证号码")
            }

        }
        et_id_card.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                checkBtnEnable()
            }

        })
        btn_sure.setOnClickListener {

            if (et_name.text.toString().isEmpty()) {
                SCApp.getInstance().showSystemCenterToast("请输入有您的姓名")
                return@setOnClickListener
            }

            val result = IDCardUtil.validateIDNum(et_id_card.text.toString())
            if (!result.isLegal) {
                SCApp.getInstance().showSystemCenterToast("请输入有效的身份证号码")
                return@setOnClickListener
            }
            if (frontSuc && reverseSuc) {
                mViewModel.getLiverVerify(et_name.text.toString(), et_id_card.text.toString())
            } else {
                SCApp.getInstance().showSystemCenterToast("身份证照片不全，请先上传")
            }

        }

        tv_front_retry.setOnClickListener {
            selectFrom = 0
            DialogUtil.showModifyPhoto(this)
        }
        tv_reverse_retry.setOnClickListener {
            selectFrom = 1
            DialogUtil.showModifyPhoto(this)
        }

        et_name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length >= 20) {
                    SCApp.getInstance().showSystemCenterToast("姓名长度超出范围（20字以内）")
                }
                checkBtnEnable()
            }

        })

        tv_deal_link.setOnClickListener {
           UrlInfoActivity.start(this,"https://m.sanshaoxingqiu.cn/sancell-shop-app-hybrid/memberCenter/AnchorProtocol.html","主播协议")
        }

    }

    private fun checkBtnEnable() {
        if (et_name.text.toString().isEmpty()) {
            btn_sure.isEnabled = false
            return
        }
        val result = IDCardUtil.validateIDNum(et_id_card.text.toString())
        if (et_id_card.text.toString().isEmpty()){
            btn_sure.isEnabled = false
            return
        }
        if (!frontSuc || !reverseSuc) {
            btn_sure.isEnabled = false
            return
        }
        btn_sure.post {
            btn_sure.isEnabled = true
        }
    }

    override fun initData() {
        mViewModel.checkVerifyResult()
    }

    var counter = AtomicInteger()
    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mCheckData.observe(this@LiveIdentifyActivity, Observer {
                it.apply {

                    showVerifyByState(status, desc)
                }
            })
            mUpLoadData.observe(this@LiveIdentifyActivity, Observer {
                it.apply {
                    val path: String = if (type == 1201) {
                        mFrontPath
                    } else {
                        mReversePath
                    }
                    val accessKeySecret = RSAUtils.decryptByPublic(accessKeySecret)
                    MyOSSUtils.getInstance().getOSs(this@LiveIdentifyActivity, endpoint,
                            accessKeyId, accessKeySecret, securityToken)
                    MyOSSUtils.getInstance().upImage(this@LiveIdentifyActivity, object : MyOSSUtils.OssUpCallback {
                        override fun inProgress(progress: Long, zong: Long) {}

                        override fun successVideo(video_url: String?) {}

                        override fun successImg(img_url: String?) {
                            counter.incrementAndGet()
                            if (counter.get() == 2) {
                                ll_view.post{
                                    showVerifyByState(1, "您的申请正在审核中，请耐心等待")
                                }
                                showLoading(false)
                            }
                        }
                    }, backName, keyName, path)
                }
            })
            mLiverVerifyCode.observe(this@LiveIdentifyActivity, Observer {
                it.apply {
                    //上传照片
                    showLoading(true)
                    mViewModel.upIdCard(mFrontUoLoadData!!.json, mFrontUoLoadData!!.type, it.objId)
                    mViewModel.upIdCard(mReverseUpLoadData!!.json, mReverseUpLoadData!!.type, it.objId)
                    // showVerifyByState(1, "您的申请正在审核中，请耐心等待")
                }
            })
            errMsg.observe(this@LiveIdentifyActivity, Observer {
                it.apply {
                    SCApp.getInstance().showSystemCenterToast(it)
                    showLoading(false)
                }
            })
            mException.observe(this@LiveIdentifyActivity, Observer {
                it.apply {
                    SCApp.getInstance().showSystemCenterToast(it.toString())
                    showLoading(false)
                }
            })
        }
    }

    private fun showVerifyByState(state: Int, desc: String) {
        when (state) {
            0 -> {
                //认证
                ll_identify.visibility = View.VISIBLE
                error_view.visibility = View.GONE
            }
            1 -> {
                //待审核
                ll_identify.visibility = View.GONE
                error_view.visibility = View.VISIBLE
                error_view.showBtn(false)
                error_view.setFailedStr("审核中", "您的申请正在审核中，请耐心等待")
            }
            2 -> {//success

                LivePlayHomeActivity.startLivePlay(this, LivePlayType.HOME_TO_TYPE.type, true)
                finish()
            }
            3 -> {
                //不通过
                error_view.visibility = View.VISIBLE
                ll_identify.visibility = View.GONE
                error_view.setFailedStr("认证失败", desc)
                error_view.showBtn(true)
                error_view.setBtnListener(object : LiveIdentifyErrorView.OnRetryClick {
                    override fun retry() {
                        //重新认证
                        ll_identify.visibility = View.VISIBLE
                        error_view.visibility = View.GONE
                    }
                })
            }
            else -> {
                ll_identify.visibility = View.GONE
                error_view.visibility = View.VISIBLE
                error_view.setFailedStr("停用", desc)
            }
        }
    }

    //图片选取回调上传
    private fun upIdCard(mPictures: List<LocalMedia>?) {
        if (mPictures != null) {
            val bitmap: Bitmap?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (selectFrom == 0) {
                    mFrontPath = mPictures[0].androidQToPath
                } else {
                    mReversePath = mPictures[0].androidQToPath
                }
                bitmap = BitmapFactory.decodeFile(mPictures[0].androidQToPath)
            } else {
                if (selectFrom == 0) {
                    mFrontPath = mPictures[0].path
                } else {
                    mReversePath = mPictures[0].path
                }
                bitmap = BitmapFactory.decodeFile(mPictures[0].path)
            }
            if (bitmap != null) {
                val imageInfo = UpImageJsonBean(bitmap.width.toString(), bitmap.height.toString()
                        , "jpeg", (bitmap.byteCount / 1024).toString())
                val imgList = ArrayList<UpImageJsonBean>()
                imgList.add(imageInfo)
                if (selectFrom == 0) {
                    //正面
                    // mViewModel.upIdCard(Gson().toJson(imgList), 1201, userBean.userId)
                    frontSuc = true
                    mFrontUoLoadData = UpIdCardBean(Gson().toJson(imgList), 1201)

                    iv_card_front.post {
                        ImageLoaderUtils.loadRoundImage(this@LiveIdentifyActivity, bitmap
                                , iv_card_front, ScreenUtil.dip2px(8f))
                    }
                    tv_front_retry.post { tv_front_retry.visibility = View.VISIBLE }
                    iv_card_reverse_bg.post { iv_card_front_bg.visibility = View.GONE }

                } else {
                    //反面
                    // mViewModel.upIdCard(Gson().toJson(imgList), 1202, userBean.userId)

                    reverseSuc = true
                    mReverseUpLoadData = UpIdCardBean(Gson().toJson(imgList), 1202)
                    iv_card_reverse.post {
                        ImageLoaderUtils.loadRoundImage(this@LiveIdentifyActivity, bitmap
                                , iv_card_reverse, ScreenUtil.dip2px(8f))
                    }
                    tv_reverse_retry.post { tv_reverse_retry.visibility = View.VISIBLE }
                    iv_card_reverse_bg.post { iv_card_reverse_bg.visibility = View.GONE }

                }
                checkBtnEnable()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                //AndroidX路径为新的字段
                val mPictures = PictureSelector.obtainMultipleResult(data)
                upIdCard(mPictures)

            } else if (requestCode == PictureConfig.REQUEST_CAMERA) {
                val mPictures = PictureSelector.obtainMultipleResult(data)
                upIdCard(mPictures)
            }
        }
    }

}