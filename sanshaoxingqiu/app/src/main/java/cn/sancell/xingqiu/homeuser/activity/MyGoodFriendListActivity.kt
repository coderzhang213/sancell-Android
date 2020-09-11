package cn.sancell.xingqiu.homeuser.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.fragment.MyGoodFriendListFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.viewmodel.UserViewModel

/**
 * Created by zj on 2020/1/2.
 */
class MyGoodFriendListActivity :DefaultFragmetnAttachActivity<UserViewModel>(){
    override val loadFragment: Fragment?
        get() = MyGoodFriendListFragment()

    override fun initData() {
    }
}