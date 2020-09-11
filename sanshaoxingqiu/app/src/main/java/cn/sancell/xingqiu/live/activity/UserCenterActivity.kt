package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.HomeUserFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.util.StatusBarUtil
import handbank.hbwallet.BaseViewModel

/**
 * 个人中心
 */
class UserCenterActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {

    override val loadFragment: Fragment?
        get() = HomeUserFragment.newInstance(true)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserCenterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }

    override fun initData() {

    }
}