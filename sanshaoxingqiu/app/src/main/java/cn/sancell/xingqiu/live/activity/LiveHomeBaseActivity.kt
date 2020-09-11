package cn.sancell.xingqiu.live.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.interfaces.OnBackPressedRegLinsenr
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.kt.BaseNotDataActivityKt
import cn.sancell.xingqiu.live.play.LivePlayStatusManager
import cn.sancell.xingqiu.util.StatusBarUtil
import handbank.hbwallet.BaseViewModel

abstract class LiveHomeBaseActivity<VM : BaseViewModel> : BaseNotDataActivityKt<VM>(), OnGetLivePalyStatusLinsener, OnBackPressedRegLinsenr {
    val mLivePlayStatusManager = LivePlayStatusManager()
    private val mBackLis = ArrayList<OnBackPressedLinsener>()
    var lastTabIndex = 0
    val mfragments = ArrayList<Fragment>()
    val mInitfragments = ArrayList<Fragment>()
    var lastFragment: Fragment? = null


    //获取添加的fragmentID
    abstract val getAddFragmentLayoutId: Int

    abstract fun initLoadFragment()


    override fun getLiveStatusManger(): LivePlayStatusManager = mLivePlayStatusManager


    override fun onCreate(savedInstanceState: Bundle?) {
        initLoadFragment()
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //初始化要加载的fragment

    }

    override fun onBackPressed() {
        if (mBackLis.size > 0) {
            onCallOnBack()
        } else {
            super.onBackPressed()

        }
    }


    fun onCallOnBack() {
        for (inof in mBackLis) {
            inof.onBackPressedLinsener()
        }
    }

    override fun onRegitsOnBackPressend(mOnBackPressedLinsener: OnBackPressedLinsener) {
        this.mBackLis.add(mOnBackPressedLinsener)
    }

    override fun unRegitsOnBackPressend(mOnBackPressedLinsener: OnBackPressedLinsener) {
        this.mBackLis.remove(mOnBackPressedLinsener)
    }

    fun removeAllTopFragment() {
        val count = supportFragmentManager.backStackEntryCount
        for (index in 1..count) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    fun getShowFragment(tabIndex: Int) {
        val fragment = mInitfragments.get(tabIndex)
        fragment.apply {
            showFragment(this)
        }

    }

    fun showFragment(mShowFragment: Fragment) {
        if (!mShowFragment.isVisible) {
            removeAllTopFragment()
            val mFragmentTransaction = supportFragmentManager.beginTransaction()
            if (!mfragments.contains(mShowFragment)) {
                mfragments.add(mShowFragment)
                mFragmentTransaction.add(getAddFragmentLayoutId, mShowFragment)
            }
            lastFragment?.apply {
                mFragmentTransaction.hide(this)
            }

            mFragmentTransaction.show(mShowFragment)
            mFragmentTransaction.commitAllowingStateLoss()
            lastFragment = mShowFragment
        }
    }

}