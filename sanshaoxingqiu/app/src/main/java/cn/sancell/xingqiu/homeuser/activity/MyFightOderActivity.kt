package cn.sancell.xingqiu.homeuser.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.fragment.MyFightOderFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.viewmodel.FightViewModel

/**
 * Created by zj on 2020/1/3.
 */
class MyFightOderActivity : DefaultFragmetnAttachActivity<FightViewModel>() {
    override val loadFragment: Fragment?
        get() = MyFightOderFragment()

    override fun initData() {
    }
}
