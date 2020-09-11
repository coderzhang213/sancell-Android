package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.live.fragment.LiveUserInfoFgm
import cn.sancell.xingqiu.util.StatusBarUtil
import handbank.hbwallet.BaseViewModel

/**
 * 他人用户主页
 */
class LiveOtherInfoActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {
    override fun initData() {

    }

    companion object {
        fun start(context: Context, userId: String) {
            val intent = Intent(context, LiveOtherInfoActivity::class.java)
            intent.putExtra(UiHelper.LIVE_USER_ID, userId)
            context.startActivity(intent)
        }
    }

    override val loadFragment: Fragment?
        get() = LiveUserInfoFgm()

    override fun bindIntent(intent: Intent?, mLoadFragment: Fragment) {
        super.bindIntent(intent, mLoadFragment)
        mLoadFragment.apply {
            arguments = Bundle(2).apply {
                putString(UiHelper.LIVE_USER_ID, intent!!.getStringExtra(UiHelper.LIVE_USER_ID))
                putBoolean(UiHelper.FRAGMENT_IS_ACTIVITY, true)
            }
        }
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return BaseViewModel::class.java
    }


    override fun initView() {
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        super.initView()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mFragment != null) {
            mFragment!!.onActivityResult(requestCode, resultCode, data)
        }
    }


}