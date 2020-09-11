package cn.sancell.xingqiu.live.fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveInitData
import cn.sancell.xingqiu.bean.LiveIntitParInfo
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.dialog.ComfirmToDialog
import cn.sancell.xingqiu.dialog.InvitationDatePickerDialog
import cn.sancell.xingqiu.dialog.LiveDefinitionSelectDialog
import cn.sancell.xingqiu.dialog.LiveIssueSucessDialog
import cn.sancell.xingqiu.dialog.listener.OnComfirmLinsenr
import cn.sancell.xingqiu.dialog.listener.OnDefinitionSelectLinsener
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.OnImagUpLoadLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.activity.AnchorHomeActivity
import cn.sancell.xingqiu.live.activity.LiveAddCommodityActivity
import cn.sancell.xingqiu.live.activity.LiveAddGroupActivity
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.live.nim.PublishParam
import cn.sancell.xingqiu.live.nim.PublishParamUtils
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.DialogUtil
import cn.sancell.xingqiu.util.ImgUpLoadManager
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.luck.picture.lib.PictureSelector
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.sys.TimeUtil
import com.tencent.liteav.demo.lvb.camerapush.CameraPusherActivity
import com.tencent.liteav.demo.lvb.liveplayer.LivePlayerActivity
import kotlinx.android.synthetic.main.fragment_lvie_setting_layout.*

/**
 * 直播设置
 */
class LiveSettingFragment : BaseNotDataFragmentKt<LiveViewModel>() {
    private var mBatchId = ""
    private var mSelectImgPath: String? = null
    private var startTime = 0L
    private var mImgUpLoadManager: ImgUpLoadManager? = null
    private var mSharpness = 1
    private var isAtPlay = false
    private var mIsUse = 0//是否复用 1为复用
    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_lvie_setting_layout

    override fun initView() {
        setTitleName("直播设置")
        rl_select_photo.setOnClickListener(mOnClickLinsenr)
        rl_select_ser.setOnClickListener(mOnClickLinsenr)
        live_time.setOnClickListener(mOnClickLinsenr)
        tv_is_yg.setOnClickListener(mOnClickLinsenr)
        tl_add_group.setOnClickListener(mOnClickLinsenr)
        rl_add_comm.setOnClickListener(mOnClickLinsenr)
        tv_start_live.setOnClickListener(mOnClickLinsenr)
        val isShowAlter = activity?.intent?.getBooleanExtra("isShowAlter", false)
        if (isShowAlter!!) {
            tv_alter.visibility = View.VISIBLE
            PreferencesUtils.put(UiHelper.KEY_LIVE_SETTING_WELCOME, false)
        } else {
            tv_alter.visibility = View.GONE
        }
    }

    fun bindDataView(mInfo: LiveInitData) {
        mInfo.apply {
            mBatchId = this.batchId
            getRemAndGorpSum()
            if (isUse.equals("1")) {
                val mComfirmToDialog = ComfirmToDialog(context!!, "是否复用上一场数据信息!")
                mComfirmToDialog.setOnClickLinsener(object : OnComfirmLinsenr {
                    override fun OnCancerLinsenr() {
                    }

                    override fun OnComfirmLinsenr() {
                        mComfirmToDialog.dismiss()
                        mViewModel.upLastLiveData(mBatchId)
                        mIsUse = 1
                        hisData?.apply {
                            setFyData(this)
                        }

                    }
                })
                mComfirmToDialog.show()
            } else {
                hisData?.apply {
                    setFyData(this)
                }
            }
        }
    }

    fun setFyData(hisData: LiveIntitParInfo) {
        getRemAndGorpSum()
        hisData.apply {
            if (sharpness > 0) {
                mSharpness = sharpness
                tv_def.setText(PublishParamUtils.serTypeToLocalTypeName(mSharpness))

            }
            if (preLiveStartTime > 0) {
                startTime = preLiveStartTime * 1000
                tv_live_time.setText(TimeUtil.getDateFromYYYYMMDDHHMMSS(startTime))
            }
            if (!TextUtils.isEmpty(liveIcon)) {
                mSelectImgPath = liveIcon
                tv_live_img_status.setText("更换封面")
                ImageLoaderUtils.loadImage(context!!, liveIcon, iv_livew_icon)
            }
            if (!TextUtils.isEmpty(liveName)) {
                et_live_name.setText(liveName)
            }

        }
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mInitSet.observe(this@LiveSettingFragment, Observer {
                bindDataView(it)
            })
            mLiveSetSave.observe(this@LiveSettingFragment, Observer {
                if (isAtPlay) {//去提示是否确认直播
                    val mComfirmToDialog = ComfirmToDialog(context!!, "本场直播预告时间为 " + TimeUtil.getDateFromYYYYMMDDHHMMSS(startTime) + " ，您\n确定要提前直播吗？")
                    mComfirmToDialog.setOnClickLinsener(object : OnComfirmLinsenr {
                        override fun OnCancerLinsenr() {
                        }

                        override fun OnComfirmLinsenr() {//跳转到直播界面
                            mComfirmToDialog.dismiss()
                            startLivePlay(it.pushData.pushUrl, mSharpness, it.pushData.roomId)
                        }
                    })
                    mComfirmToDialog.show()
                } else {
                    val mLiveIssueSucessDialog = LiveIssueSucessDialog(context!!)
                    mLiveIssueSucessDialog.setOnCancelListener {
                        activity?.finish()
                    }
                    mLiveIssueSucessDialog.show()
                }

            })
            mLiveUpLastData.observe(this@LiveSettingFragment, Observer {
                getRemAndGorpSum()
            })
            errMsg.observe(this@LiveSettingFragment, Observer {

                ToastHelper.showToast(it)
            })
            mException.observe(this@LiveSettingFragment, Observer {
            })

        }
    }

    override fun initData() {
        mImgUpLoadManager = ImgUpLoadManager(this@LiveSettingFragment)
        mViewModel.initLiveSet()

    }

    fun startLivePlay(pushUrl: String, definition: Int, roomId: String) {
        val publishParam = PublishParam()
        publishParam.pushUrl = pushUrl
        publishParam.definition = PublishParamUtils.serTypeToLocalType(definition)
        AnchorHomeActivity.startLive(context!!, roomId, mBatchId, publishParam, true)
        activity?.finish()
    }

    override fun onResume() {
        super.onResume()
        getRemAndGorpSum()
    }

    /**
     * 数据需要每次去获取
     */
    fun getRemAndGorpSum() {
        if (TextUtils.isEmpty(mBatchId)) {
            return
        }
        mViewModel.getLiveCommentListSum(mBatchId, "a").observe(this@LiveSettingFragment, Observer {
            if (it.dataList.size > 0) {
                tv_remm_sum.setText(it.dataList.size.toString())
            }

        })
        mViewModel.getLiveCommentListSum(mBatchId, "b").observe(this@LiveSettingFragment, Observer {
            if (it.dataList.size > 0) {
                tv_group_sum.setText(it.dataList.size.toString())
            }
        })
    }

    /**
     * 是否马上直播
     */
    private fun toLivePlay(isAtPlay: Boolean) {
        val et_live_name = et_live_name.text.toString()
        if (TextUtils.isEmpty(et_live_name)) {
            ToastHelper.showToast("请设置直播名称")
            return
        }
        if (TextUtils.isEmpty(mSelectImgPath)) {
            ToastHelper.showToast("请选择封面图")
            return
        }
        if (startTime <= 0) {
            ToastHelper.showToast("请选择直播时间")
            return
        }
        if (mSharpness == -1) {
            ToastHelper.showToast("请选择清晰度")
            return
        }
        if (TextUtils.isEmpty(mBatchId)) {
            ToastHelper.showToast("初始化失败")
            return
        }
        this.isAtPlay = isAtPlay
        if (TextUtils.isEmpty(mBatchId)) {
            ToastHelper.showToast("初始化失败")
            return
        }
        mViewModel.liveSetSave(mBatchId, et_live_name, mSharpness.toString(), (startTime / 1000).toString(), mIsUse)
    }

    protected val mOnClickLinsenr = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.live_time -> {//选择直播时间
                    val mTime = InvitationDatePickerDialog(context!!)
                    mTime.initDefaultTime()
                    mTime.setmOnTimeSelectLinsener(mSelecTime)
                    mTime.show()
                }
                R.id.rl_select_photo -> {//选择图片
                    DialogUtil.showModifyPhoto(activity)
                }
                R.id.rl_select_ser -> {//选择分辨率
                    val mLiveDefinitionSelectDialog = LiveDefinitionSelectDialog(context!!, mOnDefinitionSelectLinsener)
                    mLiveDefinitionSelectDialog.show()
                }
                R.id.tv_is_yg -> {//发布预告

                    toLivePlay(false)//LivePlayerActivity
//                    val intent = Intent(context, CameraPusherActivity::class.java)
//                    intent.putExtra("TITLE", "直播推流")
//                    intent.putExtra("TYPE", 0)
//                    startActivity(intent)
                }
                R.id.tv_start_live -> {//立即直播CameraPusherActivity

                    toLivePlay(true)
                }
                R.id.tl_add_group -> {//添加群组
                    if (TextUtils.isEmpty(mBatchId)) {
                        ToastHelper.showToast("初始化失败")
                        return
                    }
                    val intent = Intent(context, LiveAddGroupActivity::class.java)
                    intent.putExtra(LiveConstant.BATCH_ID, mBatchId)
                    startActivity(intent)
                }
                R.id.rl_add_comm -> {//添加商品
                    if (TextUtils.isEmpty(mBatchId)) {
                        ToastHelper.showToast("初始化失败")
                        return
                    }
                    val intent = Intent(context, LiveAddCommodityActivity::class.java)
                    intent.putExtra(LiveConstant.BATCH_ID, mBatchId)
                    startActivity(intent)
                }
            }
        }
    }
    private val mOnDefinitionSelectLinsener = object : OnDefinitionSelectLinsener {
        override fun OnSelectDefinition(df: String) {
            var dfName = ""
            mSharpness = PublishParamUtils.locTypeSerType(df)

            when (df) {
                PublishParam.HD -> {
                    dfName = "高清"
                }
                PublishParam.SD -> {
                    dfName = "标清"
                }
                PublishParam.LD -> {
                    dfName = "流畅"
                }
            }
            tv_def.setText(dfName)
        }
    }
    private val mSelecTime = object : InvitationDatePickerDialog.OnTimeSelectLinsener {
        override fun onTimeSelectLinsener(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
            val time = year.toString() + "-" + month + "-" + day + " " + hour + ":" + minute
            startTime = TimeUtil.getDataToLong(time, "yyyy-MM-dd HH:mm")
            tv_live_time.setText(time)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val mPictures = PictureSelector.obtainMultipleResult(data)
            if (mPictures != null && mPictures.size > 0) {
                if (TextUtils.isEmpty(mBatchId)) {
                    ToastHelper.showToast("初始化未完成")
                    return
                }
                mImgUpLoadManager?.uploadImageSelect("1103", mBatchId, mPictures.get(0), object : OnImagUpLoadLinsener {
                    override fun onImagUploadError(errorMsg: String) {
                        ToastHelper.showToast(errorMsg)
                    }

                    override fun onImagUploadSucess(fildPaht: String) {
                        mSelectImgPath = fildPaht
                        mIsUse = 0
                        tv_live_img_status.setText("更换封面")
                        ImageLoaderUtils.loadImage(context, fildPaht, iv_livew_icon)
                    }
                })


            }
        }
    }
}