package cn.sancell.xingqiu.homeuser.income

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.income.fragment.UserincomeFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.util.StatusBarUtil
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2020/6/17.
 */
class UserincomeActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {
    override val loadFragment: Fragment?
        get() = UserincomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
    }

    override fun initData() {
    }
}