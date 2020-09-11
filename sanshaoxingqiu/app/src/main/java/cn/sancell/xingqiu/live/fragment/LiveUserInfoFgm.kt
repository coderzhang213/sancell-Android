package cn.sancell.xingqiu.live.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.LiveUserInfoBean
import cn.sancell.xingqiu.bean.UpImageJsonBean
import cn.sancell.xingqiu.constant.RequestCode
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.dialog.BlackTipDialog
import cn.sancell.xingqiu.dialog.InfoDialogFgm
import cn.sancell.xingqiu.dialog.UserEarnDialogFgm
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import cn.sancell.xingqiu.im.entity.TabEntity
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.interfaces.OnBackPressedRegLinsenr
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LetterActivity
import cn.sancell.xingqiu.live.activity.LetterDialogBoxActivity
import cn.sancell.xingqiu.live.activity.ReportActivity
import cn.sancell.xingqiu.live.activity.UserFocusActivity
import cn.sancell.xingqiu.live.dialog.ComfirmDialog
import cn.sancell.xingqiu.live.dialog.LikeDialogFgm
import cn.sancell.xingqiu.live.dialog.ModifyIntroDialogFgm
import cn.sancell.xingqiu.util.*
import cn.sancell.xingqiu.viewmodel.LiveUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.jrmf360.normallib.base.utils.KeyboardUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import handbank.hbwallet.BaseFragment
import kotlinx.android.synthetic.main.fgm_user_info_page.*
import kotlinx.android.synthetic.main.fragment_live_user_info.*

/**
 * 直播用户的个人信息
 */
class LiveUserInfoFgm : BaseFragment<LiveUserViewModel>(), OnBackPressedLinsener {


    override fun getLayoutResId(): Int = R.layout.fragment_live_user_info
    private var mSelectImgPath = "" //选择的图片路径

    val tabs = java.util.ArrayList<TabEntity>()
    private val mFragments = java.util.ArrayList<Fragment>()
    var mAdapter: NormalPageAdapter? = null
    private var mUserId = ""
    private var mInfo: LiveUserInfoBean? = null
    private var isMine = false
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    private var showEmpty = false
    private var isActivity = false
    private var hasLoad = false


    companion object {
        /**
         * @userId 用户id，必须
         * @isActivity 页面承载是否是activity
         * */
        fun newInstance(userId: String, isActivity: Boolean) = LiveUserInfoFgm().apply {
            arguments = Bundle(2).apply {
                putString(UiHelper.LIVE_USER_ID, userId)
                putBoolean(UiHelper.FRAGMENT_IS_ACTIVITY, isActivity)
            }
        }
    }

    override fun providerVMClass(): Class<LiveUserViewModel>? {
        return LiveUserViewModel::class.java
    }

    override fun onResume() {
        super.onResume()
        if (hasLoad) {
            getUserInfo()
        }
        hasLoad = true
    }

    override fun initView() {

        iv_more.setOnClickListener {
            if (isMine) {
                val earnDialog = UserEarnDialogFgm.newInstance()
                earnDialog.show(parentFragmentManager, "earn")
            } else {
                val isBlack = PreferencesUtils.getBoolean(mUserId.toString(), false)
                val infoDialogFgm = InfoDialogFgm.newInstance(
                        if (isBlack) {
                            2
                        } else {
                            1
                        }
                )

                infoDialogFgm.setListener(object : InfoDialogFgm.OnBlackListener {
                    override fun onBlack(type: Int) {
                        //拉黑
                        if (type == 1) {
                            val blackDialog = BlackTipDialog()
                            blackDialog.addBlackListener(object : BlackTipDialog.OnAddBlackListener {
                                override fun addBlack() {
                                    PreferencesUtils.put(mUserId, true)
                                }

                            })
                            blackDialog.show(parentFragmentManager, "black")
                        } else {
                            PreferencesUtils.put(mUserId, false)
                            SCApp.getInstance().showSystemCenterToast("已解除拉黑")
                        }
                    }

                    override fun onReport() {
                        //举报
                        ReportActivity.start(activity!!)

                    }
                })
                infoDialogFgm.show(parentFragmentManager, "info")
            }

        }
        iv_at_back.setOnClickListener {
            activity!!.finish()
        }
        iv_head_img.setOnClickListener { return@setOnClickListener }
        iv_live_bg.setOnClickListener {
            if (isMine) {
                DialogUtil.showModifyPhoto(activity)
            }
        }
        tv_modify_info.setOnClickListener {
            if (isMine) {
                val dialogFgm = ModifyIntroDialogFgm.newInstance(mInfo?.userIntro)
                dialogFgm.setIntroSureListener(object : ModifyIntroDialogFgm.OnIntroSureListener {
                    override fun onSure(intro: String) {
                        KeyboardUtil.hideKeyboard(this@LiveUserInfoFgm.activity)
                        mViewModel.modifyIntro(intro)
                    }

                    override fun hide() {
                        KeyboardUtil.hideKeyboard(this@LiveUserInfoFgm.activity)
                    }

                })
                dialogFgm.show(parentFragmentManager, "intro")
            } else {
                if (mInfo?.isFollow == 0) {
                    mViewModel.focusLiver(mUserId, 1)
                } else {
                    mViewModel.focusLiver(mUserId, 2)
                }
            }
        }
        tv_copy.setOnClickListener {
            FontUtils.getInstance().copyText(context, mUserId)
        }
        tv_msg.setOnClickListener {
            if (isMine) {
                LetterActivity.start(context!!)
            } else {
                LetterDialogBoxActivity.start(context!!, mInfo!!.tvUserName, mUserId!!)
            }
        }

        tv_fans_num.setOnClickListener {
            UserFocusActivity.start(context!!, 1, mUserId, mInfo!!.tvUserName)
        }

        tv_focus_num.setOnClickListener {
            UserFocusActivity.start(context!!, 0, mUserId, mInfo!!.tvUserName)
        }
        tv_like_num.setOnClickListener {
            LikeDialogFgm.newInstance(mInfo!!.tvUserName, mInfo!!.likeCount).show(fragmentManager!!, "like")
        }

        tb_live.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (!isActivity) {
                    if (p0!!.position == 0) {
                        if (showEmpty) {
                            live_empty.visibility = View.VISIBLE
                            live_empty.showImg(false)
                            live_empty.startMove()
                        } else {
                            live_empty.visibility = View.GONE
                            live_empty.cancelAnim()
                        }
                    } else {
                        live_empty.visibility = View.GONE
                        live_empty.cancelAnim()
                    }
                } else {
                    live_empty.visibility = View.GONE
                    live_empty.cancelAnim()
                }
                app_bar_layout.setExpanded(true)
            }

        })
        btn_is_live.setOnClickListener {
            if (mInfo?.liveInfo != null) {
                LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.USER_CAN_TYPE.type, mInfo!!.liveInfo.batchId)
            }
        }
        initFresh()
    }

    override fun initData() {
        try {
            mUserId = arguments!!.getString(UiHelper.LIVE_USER_ID)!!
            isActivity = arguments!!.getBoolean(UiHelper.FRAGMENT_IS_ACTIVITY, false)
            if (mUserId == AppUtils.getUserId()) {
                isMine = true
            }
            tv_user_id.text = String.format(resources.getString(R.string.user_id), mUserId)
            if (isActivity) {
                iv_at_back.visibility = View.VISIBLE
            } else {
                iv_at_back.visibility = View.GONE
            }

            //add fgm
            tabs.add(TabEntity("个人作品", 0))
            tabs.add(TabEntity("点赞作品", 1))
            val workFgm = LiveUserInfoPageFgm.newInstance(0, mUserId)
            workFgm.setOnFreshListener(object : LiveUserInfoPageFgm.OnFreshListener {
                override fun showEmpty(show: Boolean) {
                    showEmpty = show
                    if (show) {
                        live_empty.visibility = View.VISIBLE
                        live_empty.showLive(true)
                        live_empty.showImg(false)
                        live_empty.startMove()
                    } else {
                        live_empty.visibility = View.GONE
                        live_empty.cancelAnim()
                    }
                }


            })
            val likeWorkFgm = LiveUserInfoPageFgm.newInstance(1, mUserId)
            mFragments.add(workFgm)
            mFragments.add(likeWorkFgm)
            mAdapter = NormalPageAdapter(childFragmentManager, mFragments, tabs)
            vp_page.adapter = mAdapter
        } catch (e: Exception) {
            Log.e("77777", e.toString())
        }
        refreshLayout.autoRefresh()
    }

    fun getUserInfo() {
        mViewModel.getUserInfo(mUserId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindOnBack()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unBindOnBack()
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mUpBgData.observe(this@LiveUserInfoFgm, Observer {
                upLoadAliCloud(it)
            })
            mLiveUserInfo.observe(this@LiveUserInfoFgm, Observer {
                mInfo = it
                setInfo(it)
            })

            mChangeIntroData.observe(this@LiveUserInfoFgm, Observer {
                mInfo?.userIntro = it
                setIntro(it)
            })
            mViewModel.mFocusLiverData.observe(this@LiveUserInfoFgm, Observer {
                when {
                    it.mutual == 0 -> {
                        mInfo?.isFollow = 0
                        SCApp.getInstance().showSystemCenterToast("已取消关注")
                    }
                    it.mutual == 1 -> {
                        mInfo?.isFollow = 2
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                    else -> {
                        mInfo?.isFollow = 1
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                }
                setFollowStates(mInfo?.isFollow)

            })

            errMsg.observe(this@LiveUserInfoFgm, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
            mException.observe(this@LiveUserInfoFgm, Observer {
                Log.e("user_info", it.toString())
            })
        }
    }

    private fun initFresh() {
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setOnRefreshListener {
            getUserInfo()
            for (fgm in mFragments) {
                if (fgm is LiveUserInfoPageFgm) {
                    fgm.refresh()
                }
            }
            refreshLayout.finishRefresh()
        }
    }

    private fun setCharacter(focus: Int, isLiving: Int) {
        if (isMine) { //我自己
            tv_msg.visibility = View.VISIBLE
            tv_msg.text = "消息"
            btn_is_live.visibility = View.GONE
            iv_head_img_living.visibility = View.GONE
            tv_modify_info.text = "编辑资料"
            tv_modify_info.setBackgroundResource(R.drawable.shape_live_user_modify_info)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv_modify_info.setTextColor(resources.getColor(R.color.text_main, null))
            } else {
                tv_modify_info.setTextColor(resources.getColor(R.color.text_main))
            }

        } else { //别人

            if (focus >= 1) {
                tv_msg.visibility = View.VISIBLE
                tv_msg.text = "发私信"
            } else {
                tv_msg.visibility = View.GONE
            }
            if (isLiving == 1) {
                btn_is_live.visibility = View.VISIBLE
                iv_head_img_living.visibility = View.VISIBLE
            } else {
                btn_is_live.visibility = View.GONE
                iv_head_img_living.visibility = View.GONE
            }
            setFollowStates(focus)
        }
    }

    //关注
    private fun setFollowStates(states: Int?) {
        when (states) {
            0 -> {
                tv_modify_info.isSelected = true
                tv_modify_info.text = "+关注"
                tv_msg.visibility = View.GONE
            }
            1 -> {
                tv_modify_info.isSelected = false
                tv_modify_info.text = "已关注"
                tv_msg.visibility = View.VISIBLE
            }
            else -> {
                tv_modify_info.isSelected = false
                tv_modify_info.text = "相互关注"
                tv_msg.visibility = View.VISIBLE
            }
        }
    }


    private fun setInfo(data: LiveUserInfoBean) {
        setIntro(data.userIntro)
        tv_fans_num.text = FansUtils.getUserFansCount(context!!, "粉丝", data.fansCount)
        tv_like_num.text = FansUtils.getUserFansCount(context!!, "获赞", data.likeCount)
        tv_focus_num.text = FansUtils.getUserFansCount(context!!, "关注", data.followCount)
        if (data.backgroundImg.isNotEmpty()) {
            ImageLoaderUtils.loadImage(context, data.backgroundImg, iv_live_bg)
        }
        ImageLoaderUtils.loadCircleImage(context, data.tvUserGravatar, iv_head_img)
        tv_name.text = data.tvUserName
        setCharacter(data.isFollow, data.hasLiveing)
        if (data.isVip == 1) {
            val iconVip = resources.getDrawable(R.mipmap.icon_live_user_vip, null)
            iconVip.setBounds(0, 0, iconVip.minimumWidth, iconVip.minimumHeight)
            tv_name.setCompoundDrawables(null, null, iconVip, null)
        } else {
            tv_name.setCompoundDrawables(null, null, null, null)
        }
    }


    private fun upLoadAliCloud(bean: UpLoadPhotoInfoBean) {
        val accessKeySecret = RSAUtils.decryptByPublic(bean.accessKeySecret)
        MyOSSUtils.getInstance().getOSs(context, bean.endpoint,
                bean.accessKeyId, accessKeySecret, bean.securityToken)
        MyOSSUtils.getInstance().upImage(context, object : MyOSSUtils.OssUpCallback {
            override fun inProgress(progress: Long, zong: Long) {}

            override fun successVideo(video_url: String?) {}

            override fun successImg(img_url: String?) {
                //上传成功
                //主线程处理
                iv_live_bg.post {
                    ImageLoaderUtils.loadImage(context, img_url, iv_live_bg)
                }
            }
        }, bean.backName, bean.keyName, mSelectImgPath)
    }

    private fun uploadBg(mPictures: List<LocalMedia>?) {
        if (mPictures != null) {
            val bitmap: Bitmap?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mSelectImgPath = mPictures[0].androidQToPath
                bitmap = BitmapFactory.decodeFile(mPictures[0].androidQToPath)
            } else {
                mSelectImgPath = mPictures[0].path
                bitmap = BitmapFactory.decodeFile(mPictures[0].path)
            }
            if (bitmap != null) {
                val imageInfo = UpImageJsonBean(bitmap.width.toString(), bitmap.height.toString()
                        , "jpeg", (bitmap.byteCount / 1024).toString())
                val imgList = ArrayList<UpImageJsonBean>()
                imgList.add(imageInfo)
                mViewModel.upUserBg(Gson().toJson(imgList), 1203, mUserId)
            } else {
                SCApp.getInstance().showSystemCenterToast("图片异常")
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                //AndroidX路径为新的字段
                val mPictures = PictureSelector.obtainMultipleResult(data)
                uploadBg(mPictures)

            } else if (requestCode == PictureConfig.REQUEST_CAMERA) {
                val mPictures = PictureSelector.obtainMultipleResult(data)
                uploadBg(mPictures)
            } else {
                if (requestCode == RequestCode.LIVER_REPORT) {
                    //拉黑
                    Handler().postDelayed(Runnable {
                        SCApp.getInstance().showSystemCenterToast("举报成功，感谢您的反馈")
                    }, 1000)
                }

            }
        }
    }

    private fun setIntro(str: String) {
        if (!TextUtils.isEmpty(str)) {
            tv_desc.text = str
        } else {
            tv_desc.text = "填写个人简介让大家更好的认识你~"
        }

    }

    /**
     * 绑定返回键监听
     */
    private fun bindOnBack() {
        val activity = activity
        if (activity is OnBackPressedRegLinsenr) {
            (activity as OnBackPressedRegLinsenr).onRegitsOnBackPressend(this)
        }
    }

    //解除回调监听
    private fun unBindOnBack() {
        val activity = activity
        if (activity is OnBackPressedRegLinsenr) {
            (activity as OnBackPressedRegLinsenr).unRegitsOnBackPressend(this)
        }
    }

    override fun onBackPressedLinsener(): Boolean {

        mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.personagePagerIsShow().apply {
            if (this!!) {
                val mComfirmDialog = ComfirmDialog(activity!!)
                mComfirmDialog.setCommitMsg("确定")
                mComfirmDialog.setMsg("确定退出吗!")
                mComfirmDialog.setCancelable(false)

                mComfirmDialog.setOnCutCityLinsener(object : ComfirmDialog.OnCutCityLinsener {
                    override fun onCancerLinsener() {
                    }

                    override fun onConfirmLinsener() {
                        activity?.finish()
                    }
                })
                mComfirmDialog.show()
            }

        }
        return true
    }

}