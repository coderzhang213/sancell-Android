package cn.sancell.xingqiu.usermember.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.usermember.fragment.VipCencerFragment
import cn.sancell.xingqiu.viewmodel.UserViewModel

/**
 * Created by zj on 2020/1/2.
 */
class VipCencerActivity : DefaultFragmetnAttachActivity<UserViewModel>() {
    override fun initData() {
    }

    override val loadFragment: Fragment? = VipCencerFragment()
}