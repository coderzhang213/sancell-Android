package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homecommunity.live.fragment.LiveAttenListFragment
import cn.sancell.xingqiu.homeuser.HomeUserFragment
import cn.sancell.xingqiu.im.fragment.ChatGroupBaseFragment
import cn.sancell.xingqiu.interfaces.OnLiveBomTabLinsener
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.live.fragment.LiveRoomFragment
import cn.sancell.xingqiu.live.nim.NimController
import cn.sancell.xingqiu.live.nim.PublishParam
import cn.sancell.xingqiu.mall.fragment.MallHomeFragment
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.activit_live_paly_home_layout.*

class AnchorHomeActivity : LiveHomeBaseActivity<LiveViewModel>() {

    companion object {
        fun startLive(context: Context?, roomId: String, batchId: String, param: PublishParam, isUpStart: Boolean) {
            val intent = Intent(context, AnchorHomeActivity::class.java)
            intent.putExtra(LiveConstant.IS_AUDIENCE, false)
            intent.putExtra(LiveConstant.YXID, batchId)
            intent.putExtra(NimController.EXTRA_ROOM_ID, roomId)
            intent.putExtra(LiveConstant.IS_CHECK_LIVES, isUpStart)
            intent.putExtra(PublishParam.EXTRA_PARAMS, param)
            context?.startActivity(intent)
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mLiveCheckStatus.observe(this@AnchorHomeActivity, Observer {
                if (it.status == 2) {//审核通过在去检查是否有正在直播的
                    checkLiveIsPaly()
                } else {
                    LiveIdentifyActivity.start(this@AnchorHomeActivity)
                }
            })
            errMsg.observe(this@AnchorHomeActivity, Observer {
                ToastHelper.showToast(it)
            })
            mLiveCheckStaus.observe(this@AnchorHomeActivity, Observer {
                if (it.has.equals("1")) {//有直播，直接跳转到直播间
                    val publishParam = PublishParam()
                    publishParam.pushUrl = it.liveInfo.pushUrl
                    publishParam.definition = PublishParam.HD
                    startLive(this@AnchorHomeActivity, it.liveInfo.roomId, it.liveInfo.batchId, publishParam, false)
                } else {//没有去设置界面
                    LiveSettingActivity.startIntent(this@AnchorHomeActivity, false)
                }

            })
        }
    }

    //是否有直播间正在直播
    fun checkLiveIsPaly() {
        mViewModel.checkLiveStatus()
    }

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun onReloadData() {
    }

    override val loadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.activit_anchor_paly_home_layout

    override fun initView() {

        ll_bom.setOnLiveBomTabLinsener(object : OnLiveBomTabLinsener {
            override fun onTabAddClickLinser() {
                mViewModel.checkVerifyStatus()
            }

            override fun onTabClcikLinsener(postion: Int) {
                if (lastTabIndex == postion) {
                    return
                }
                lastTabIndex = postion
                mLivePlayStatusManager.setHomeShowIndex(postion)
                // getShowFragment(postion)
            }
        })


    }


    override fun initData() {
        val isUpStart = intent.getBooleanExtra(LiveConstant.IS_CHECK_LIVES, true)
        if (isUpStart) {//去调用开始接口
            getShowFragment(0)
//            mViewModel.upRoomStatus("2").observe(this@AnchorHomeActivity, Observer {
//                if (it.status >= 0) {
//                    getShowFragment(0)
//                } else {
//                    ToastHelper.showToast("开始直播失败")
//                }
//            })
        } else {//直接开始直播
            getShowFragment(0)
        }


    }

    override val getAddFragmentLayoutId: Int
        get() = R.id.rl_conent


    override fun initLoadFragment() {
        val roomId = intent.getStringExtra(NimController.EXTRA_ROOM_ID)
        val batchId = intent.getStringExtra(LiveConstant.YXID)
        //主播端
        val mPublishParam = intent.getSerializableExtra(PublishParam.EXTRA_PARAMS) as PublishParam

        val mLiveRoomFragment = LiveRoomFragment.startLive(roomId, mPublishParam, batchId)

        mInitfragments.add(mLiveRoomFragment)
        mInitfragments.add(LiveAttenListFragment())
        mInitfragments.add(MallHomeFragment.newInsener("7", false))
        mInitfragments.add(ChatGroupBaseFragment())
        mInitfragments.add(HomeUserFragment.newInstance(false))
    }


}