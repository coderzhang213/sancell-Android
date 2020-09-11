package cn.sancell.xingqiu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.bean.TabDb
import cn.sancell.xingqiu.bean.TabObject
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.constant.IntentKey
import cn.sancell.xingqiu.homeclassify.HomeClassifyFragment
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayHomeActivity
import cn.sancell.xingqiu.homepage.HomeFragment
import cn.sancell.xingqiu.homepage.bean.NavigationInfoBean
import cn.sancell.xingqiu.homeuser.HomeUserFragment
import cn.sancell.xingqiu.im.sys.LogoutHelper
import cn.sancell.xingqiu.im.sys.SessionHelper
import cn.sancell.xingqiu.im.ui.recent.RecentMessageActivity
import cn.sancell.xingqiu.im.ui.red.call.ScClient
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.login.CodeLoginActivity
import cn.sancell.xingqiu.util.DialogUtil
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.MainViewModel
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.log.LogUtil
import com.netease.nim.uikit.support.permission.MPermission
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.msg.model.IMMessage
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_home_tab_layout.*
import kotlinx.android.synthetic.main.activity_home_tab_layout.homebtn_tab_cart
import kotlinx.android.synthetic.main.activity_home_tab_layout.homebtn_tab_home
import kotlinx.android.synthetic.main.activity_home_tab_layout.iv_tab_home
import kotlinx.android.synthetic.main.activity_home_tab_layout.tv_tab_home

/**
 * Created by zj on 2019/12/26.
 */
class HomeTabsActivity : BaseActivity<MainViewModel>() {

    var lastFragment: Fragment? = null
    val mfragments = ArrayList<Fragment>()
    val FRAGMENT_CONTAINER_ID = R.id.container
    val mTabList = ArrayList<TabObject>()
    override fun getLayoutResId(): Int = R.layout.activity_home_tab_layout

    companion object {
        var isForeground = false
        const val MESSAGE_RECEIVED_ACTION = "cn.sancell.xingqiu.MESSAGE_RECEIVED_ACTION"
        const val KEY_MESSAGE = "message"
        const val KEY_EXTRAS = "extras"
        /**
         * 跳转的 tabPosition
         * @param context
         * @param tabPosition
         */
        fun start(context: Context, tabPosition: Int) {
            val intent = Intent(context, HomeTabsActivity::class.java)
            intent.putExtra(IntentKey.MAIN_TAB_POS, tabPosition)
            context.startActivity(intent)
        }

        const val BASIC_PERMISSION_REQUEST_CODE = 100
        val BASIC_PERMISSIONS = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun initView() {
        homebtn_tab_home.setOnClickListener(onClickLinsenr)
        homebtn_tab_cart.setOnClickListener(onClickLinsenr)
        homebtn_tab_more.setOnClickListener(onClickLinsenr)
        iv_activate_vip.setOnClickListener(onClickLinsenr)
        home_tba_class.setOnClickListener(onClickLinsenr)
        initFragment()

    }

    override fun onResume() {
        super.onResume()
//        //处理外链跳转
//        if (SCApp.schemeUri != null) {
//            var path: String? = SCApp.schemeUri.path
//            if (TextUtils.equals(path, "/goodsInfo")) {
//                var type: String? = SCApp.schemeUri.getQueryParameter("type")
//                var id: String? = SCApp.schemeUri.getQueryParameter("id")
//
//                if (TextUtils.equals(type, "1")) {
//                    GoodsDetailActivity.start(this, id!!.toInt());
//                } else if (TextUtils.equals(type, "2")) {
//                    startActivity(Intent(this, SeckillListActivity::class.java))
//                } else if (TextUtils.equals(type, "3")) {
//
//                } else if (TextUtils.equals(type, "4")) {
//                    startActivity(Intent(this, MemberVipGiftBuyActivity::class.java))
//                }
//            }
//            SCApp.schemeUri = null
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        showFragment()
        handleNotice(intent)

    }

    override fun onPause() {
        super.onPause()
        isForeground = false
    }

    //处理消息跳转
    private fun handleNotice(intent: Intent?) {
        if (intent == null) {
            return
        }
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            val messages: ArrayList<IMMessage> = intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT) as ArrayList<IMMessage>
            if (messages.size > 1) {
                RecentMessageActivity.start(this);
            } else {
                SessionHelper.startTeamSession(this, messages.get(0).getSessionId());
            }
        }
    }

    fun showFragment() {
        //现在默认进入直播首页
        LivePlayHomeActivity.startLivePlay(this, LivePlayType.RE_PLAY.type, true)
        finish()
//        val showIndex = intent.getIntExtra(IntentKey.MAIN_TAB_POS, 0)
//        when (showIndex) {
//            0 -> {
//                whileFragment(TabDb.TAB_HOME)
//            }
//            1 -> {
//                whileFragment(TabDb.TAB_CLASS)
//            }
//            2 -> {
//                whileFragment(TabDb.TAB_MY)
//            }
//            else -> {
//                whileFragment(TabDb.TAB_HOME)
//            }
//        }
    }

    override fun initData() {
        registerObservers(true)
        showFragment()


        requestBasicPermission()
        //广告提示
        val navigationInfo = PreferencesUtils.readObject(Constants.Key.KEY_NAVIGATIONINFO, NavigationInfoBean::class.java)
        navigationInfo?.apply {
            if (isShow == 1) {
                DialogUtil.showNavigationInfoDialog(this@HomeTabsActivity, navigationInfo)

            }
        }
    }

    fun initFragment() {
        // View.inflate(this,R.layout.view_tab_item_layout,null)
        mTabList.add(TabObject(iv_tab_home, tv_tab_home, TabDb.TAB_HOME, HomeFragment()))
        mTabList.add(TabObject(iv_home_tab_class, tv_home_tab_class, TabDb.TAB_CLASS, HomeClassifyFragment()))
        // mTabList.add(TabObject(iv_tab_cart, tv_tab_cart, TabDb.TAB_CART, HomeShoppingCarFragment()))
        mTabList.add(TabObject(iv_tab_my, tv_tab_my, TabDb.TAB_MY, HomeUserFragment.newInstance(false)))
    }

    fun whileFragment(mTag: String) {
        val mFragment = getFragemtn(mTag)
        mFragment?.apply {
            showFragment(this)
        }
        showTabStates(mTag)
    }

    fun getFragemtn(tag: String): Fragment? {
        for (mTab in mTabList) {
            if (tag.equals(mTab.tag)) {
                return mTab.fragemtn
            }
        }
        return null
    }

    fun showTabStates(tag: String) {
        for (mTab in mTabList) {
            if (tag.equals(mTab.tag)) {
                mTab.ivImg.isSelected = true
                mTab.tvText.isSelected = true
            } else {
                mTab.ivImg.isSelected = false
                mTab.tvText.isSelected = false
            }
        }
    }

    fun showFragment(mShowFragment: Fragment) {
        if (!mShowFragment.isVisible) {
            removeAllTopFragment()
            val mFragmentTransaction = supportFragmentManager.beginTransaction()
            if (!mfragments.contains(mShowFragment)) {
                mfragments.add(mShowFragment)
                mFragmentTransaction.add(FRAGMENT_CONTAINER_ID, mShowFragment)
            }
            lastFragment?.apply {
                mFragmentTransaction.hide(this)
            }

            mFragmentTransaction.show(mShowFragment)
            mFragmentTransaction.commitAllowingStateLoss()
            lastFragment = mShowFragment
        }
    }

    /**
     *
     */
    fun removeAllTopFragment() {
        val count = supportFragmentManager.backStackEntryCount
        for (index in 1..count) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private val onClickLinsenr = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.homebtn_tab_home -> {//首页
                    whileFragment(TabDb.TAB_HOME)
                }
                R.id.home_tba_class -> {//分类
                    whileFragment(TabDb.TAB_CLASS)
                }
                R.id.home_class -> {//购物车
                    whileFragment(TabDb.TAB_CART)
                }
                R.id.homebtn_tab_more -> {//我的
                    whileFragment(TabDb.TAB_MY)
                }
                R.id.iv_activate_vip -> {//点击激活Vip
                    DialogUtil.showRedPacketAgreement(this@HomeTabsActivity)
                }
            }

        }
    }
    private var mIsExit = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                finish()
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
                mIsExit = true
                Handler().postDelayed({ mIsExit = false }, 2000)
            }

        }
        return true
    }

    fun registerObservers(register: Boolean) {
        NIMClient.getService(AuthServiceObserver::class.java).observeOnlineStatus(userStatusObserver, register)
    }

    /**
     * 用户状态变化
     */
    private val userStatusObserver = Observer { code: StatusCode ->
        if (code.wontAutoLogin()) {
            kickOut(code)
        }
    } as Observer<StatusCode>

    private fun kickOut(code: StatusCode) {
        PreferencesUtils.put(Constants.Key.key_im_token, "")
        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error")
        } else {
            LogUtil.i("Auth", "Kicked!")
        }
        onLogout()
    }

    // 注销
    private fun onLogout() { // 清理缓存&注销监听&清除状态
        LogoutHelper.logout()
        ScClient.loginOut()
        val intent = Intent(SCApp.context, CodeLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(IntentKey.KICK_OUT, true)
        SCApp.context.startActivity(intent)
    }


    private fun requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS)
        MPermission.with(this@HomeTabsActivity).setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissionsSum(BASIC_PERMISSIONS).request()
    }

    @OnMPermissionGranted(100)
    fun onBasicPermissionSuccess() { //
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }

    @OnMPermissionDenied(100)
    @OnMPermissionNeverAskAgain(100)
    fun onBasicPermissionFailed() {
        try {
            ToastHelper.showToast(this, "未全部授权，部分功能可能无法正常运行！")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }
}