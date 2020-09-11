package cn.sancell.xingqiu.live.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LivePlayInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.interfaces.OnBackPressedRegLinsenr
import cn.sancell.xingqiu.ktenum.LivePagerType
import cn.sancell.xingqiu.live.base.LiveToolBaseFragment
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.sys.TimeUtil
import kotlinx.android.synthetic.main.fragemtn_subscribe_layout.*

/**
 * 直播预约
 */
class LiveSubscribeFragment : LiveToolBaseFragment(), OnBackPressedLinsener {
    private var mIsReserve: String? = "0"//是否预约 0未预约；1已预约
    private var batchId = ""


    override val getRoomId: String
        get() = batchId
    override val getCurrType: String
        get() = LivePagerType.RESERVATION.type


    override val layoutId: Int
        get() = R.layout.fragemtn_subscribe_layout

    companion object {
        fun startIntent(batchId: String, postion: Int, mType: String): LiveSubscribeFragment {
            val fragment = LiveSubscribeFragment()
            val bundl = Bundle()
            bundl.putString("batchId", batchId)
            bundl.putInt(LiveConstant.LIVE_SHOW_POSTION, postion)
            bundl.putString(LiveConstant.LIVE_TYPE, mType)
            fragment.arguments = bundl
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initViewLinsener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(mActivity, false)
        //设置状态栏透明
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(mActivity)
        bindOnBack()
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

    override fun onDestroy() {
        super.onDestroy()
        unBindOnBack()
    }

    @SuppressLint("FragmentLiveDataObserve")
    fun initViewLinsener() {
        ll_make.setOnClickListener {
            if (!TextUtils.isEmpty(mIsReserve) && !TextUtils.isEmpty(batchId)) {
                var type: String
                if (mIsReserve == "0") {
                    type = "1"
                } else {
                    type = "2"
                }
                mLiveViewModel?.upLiveRescer(batchId, type)?.observe(this@LiveSubscribeFragment, Observer {
                    if (mIsReserve == "0") {
                        mIsReserve = "1"
                        ToastHelper.showToast("设置成功, 主播开播我们会提醒您", Toast.LENGTH_LONG)
                    } else {
                        mIsReserve = "0"
                        ToastHelper.showToast("提醒已取消")
                    }
                    setIsReserve()
                })
            }

        }
        mLiveViewModel?.errMsg?.observe(this@LiveSubscribeFragment, Observer {
            ToastHelper.showToast(it)
        })
    }

    fun initData() {
        batchId = arguments?.getString("batchId")!!
        val postion = arguments?.getInt(LiveConstant.LIVE_SHOW_POSTION)
        val mType = arguments?.getString(LiveConstant.LIVE_TYPE)
        setShowPostion(postion!!)
        mLiveViewModel = ViewModelProviders.of(this@LiveSubscribeFragment).get(LiveViewModel::class.java)
        getLiveInfo(batchId, mType!!)
    }

    override fun bingView(mInfo: LivePlayInfo) {
        super.bingView(mInfo)

        mLivePlayInfo?.apply {
            if (TextUtils.equals(batchInfo.liveUserId.toString(), AppUtils.getUserId())) {
                ll_make.visibility = View.GONE
            } else {
                ll_make.visibility = View.VISIBLE
            }
            tv_des.setText(this.batchInfo.liveTitle)
            tv_start_time.setText(TimeUtil.getDateFromMMDDHHMM(this.batchInfo.liveStartTime * 1000))
            ImageLoaderUtils.loadImage(context, this.batchInfo.icon, iv_live_icon)
            mIsReserve = this.isReserve
            setIsReserve()
        }

    }

    fun setIsReserve() {
        if (mIsReserve == "1") {//已经预约
            ll_make.setText("取消提醒")
            ll_make.setBackgroundResource(R.drawable.live_not_sub_shape)
        } else {
            ll_make.setText("预约提醒")
            ll_make.setBackgroundResource(R.drawable.live_sub_shape)
        }
    }

    override fun setViewVisibility(view1: TextView, view2: View, view3: LinearLayout) {
        super.setViewVisibility(view1, view2, view3)
        view1.visibility = View.INVISIBLE
        view2.visibility = View.INVISIBLE
        view3.setBackgroundResource(0)
    }

    override fun onPagerCloce() {
        super.onPagerCloce()
        activity?.finish()
    }

    override fun onBackPressedLinsener(): Boolean {
        if (!currPagerIsShow()) {
            return false
        }
        showConfirmDialog("", "确定退出吗?", DialogInterface.OnClickListener { dialog, which ->
            activity!!.finish()
        }, null)
        return true
    }
}