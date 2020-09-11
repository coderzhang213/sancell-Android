package cn.sancell.xingqiu.homecommunity.live.actviity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import cn.jpush.android.api.JPushInterface
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.constant.RequestCode
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.homecommunity.live.fragment.LiveAttenListFragment
import cn.sancell.xingqiu.homecommunity.live.fragment.LivePlayBaseHoemFragment
import cn.sancell.xingqiu.homepage.LiveHomePageFgm
import cn.sancell.xingqiu.homepage.SeckillListActivity
import cn.sancell.xingqiu.homeuser.HomeUserFragment
import cn.sancell.xingqiu.im.fragment.ChatGroupBaseFragment
import cn.sancell.xingqiu.interfaces.OnLiveBomTabLinsener
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.AnchorHomeActivity
import cn.sancell.xingqiu.live.activity.LiveHomeBaseActivity
import cn.sancell.xingqiu.live.activity.LiveIdentifyActivity
import cn.sancell.xingqiu.live.activity.LiveSettingActivity
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.live.fragment.LiveUserInfoFgm
import cn.sancell.xingqiu.live.nim.PublishParam
import cn.sancell.xingqiu.mall.fragment.MallHomeFragment
import cn.sancell.xingqiu.usermember.MemberVipGiftBuyActivity
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.ServerUtils
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.CustomNotification
import kotlinx.android.synthetic.main.activit_live_paly_home_layout.*

/**
 * 直播播放列表
 */
class LivePlayHomeActivity : LiveHomeBaseActivity<LiveViewModel>() {

    companion object {
        const val LIVE_IS_SHOW_TAB = "live_is_show_tab"
        fun startLivePlay(context: Context, type: String, isShowTab: Boolean) {//1主页推荐列表；2关注直播列表；3个人中心直播列表
            val intent = Intent(context, LivePlayHomeActivity::class.java)
            intent.putExtra(LiveConstant.LIVE_TYPE, type)
            intent.putExtra(LIVE_IS_SHOW_TAB, isShowTab)
            context.startActivity(intent)
        }
    }

    override fun onReloadData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //启动用来监听退出直播的服务
        ServerUtils.startServer(this, false)
        NIMClient.getService(MsgServiceObserve::class.java).observeCustomNotification(mObserver, true)

    }

    private val mObserver = com.netease.nimlib.sdk.Observer<CustomNotification> {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            //todo 处理跳转

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(MsgServiceObserve::class.java).observeCustomNotification(mObserver, false)

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mLiveCheckStatus.observe(this@LivePlayHomeActivity, Observer {
                if (it.status == 2) {
                    checkLiveIsPaly()
                } else {
                    //直播设置需要第一次认证欢迎
                    PreferencesUtils.put(UiHelper.KEY_LIVE_SETTING_WELCOME, true)
                    LiveIdentifyActivity.start(this@LivePlayHomeActivity)
                }
            })
            errMsg.observe(this@LivePlayHomeActivity, Observer {
                ToastHelper.showToast(it)
            })
            mLiveCheckStaus.observe(this@LivePlayHomeActivity, Observer {
                if (it.has.equals("1")) {//有直播，直接跳转到直播间
                    val publishParam = PublishParam()
                    publishParam.pushUrl = it.liveInfo.pushUrl
                    publishParam.definition = PublishParam.HD
                    AnchorHomeActivity.startLive(this@LivePlayHomeActivity, it.liveInfo.roomId, it.liveInfo.batchId, publishParam, false)

                } else {//没有去设置界面
                    LiveSettingActivity.startIntent(this@LivePlayHomeActivity, PreferencesUtils.getBoolean(UiHelper.KEY_LIVE_SETTING_WELCOME, false))
                }

            })
        }
    }

    //是否有直播间正在直播
    fun checkLiveIsPaly() {
        mViewModel.checkLiveStatus()
    }


    override val loadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.activit_live_paly_home_layout

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
                getShowFragment(postion)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //处理外链跳转
        if (SCApp.schemeUri != null) {
            var path: String? = SCApp.schemeUri.path
            if (TextUtils.equals(path, "/goodsInfo")) {
                var type: String? = SCApp.schemeUri.getQueryParameter("type")
                var id: String? = SCApp.schemeUri.getQueryParameter("id")

                if (TextUtils.equals(type, "1")) {
                    GoodsDetailActivity.start(this, id!!.toInt());
                } else if (TextUtils.equals(type, "2")) {
                    startActivity(Intent(this, SeckillListActivity::class.java))
                } else if (TextUtils.equals(type, "3")) {

                } else if (TextUtils.equals(type, "4")) {
                    startActivity(Intent(this, MemberVipGiftBuyActivity::class.java))
                } else if (TextUtils.equals(type, "5")) {
                    LivePlayBaseHoemActivity.startIntent(this, LivePlayType.EXTERNAL_TYPE.type, id!!)
                }
            }
            SCApp.schemeUri = null
        }
    }


    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun initData() {
        //用户登陆成功后设置别名
        JPushInterface.setAlias(this, 1, AppUtils.getUserId())

        val isShowTab = intent.getBooleanExtra(LIVE_IS_SHOW_TAB, true)
        if (!isShowTab) {
            ll_bom.visibility = View.GONE
        }

        Handler().postDelayed({
            getShowFragment(0)

        }, 500)

    }

    external fun getCStr(): String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            for (fgm in mInitfragments) {
                if (fgm is LiveUserInfoFgm) {
                    fgm.onActivityResult(requestCode, resultCode, data)
                } else if (fgm is LivePlayBaseHoemFragment) {
                    //举报
                    if (requestCode == RequestCode.LIVER_REPORT) {
                        Handler().postDelayed(Runnable {
                            SCApp.getInstance().showSystemCenterToast("举报成功，感谢您的反馈")
                        }, 1000)
                    }

                }
            }
        }
    }

    override val getAddFragmentLayoutId: Int
        get() = R.id.rl_conent

    override fun initLoadFragment() {
        val framgent = LivePlayBaseHoemFragment.getInsener(intent.getStringExtra(LiveConstant.LIVE_TYPE), "", "")
        mInitfragments.add(LiveHomePageFgm())
       //  mInitfragments.add(LiveAttenListFragment())
        mInitfragments.add(framgent)
        mInitfragments.add(MallHomeFragment.newInsener("7", false))
        mInitfragments.add(ChatGroupBaseFragment())
        //   mInitfragments.add(NewHomeUserFragment())
        mInitfragments.add(HomeUserFragment.newInstance(false))
        // mInitfragments.add(LiveUserInfoFgm.newInstance(AppUtils.getUserId(),false))//LiveUserInfoFgm
    }

}