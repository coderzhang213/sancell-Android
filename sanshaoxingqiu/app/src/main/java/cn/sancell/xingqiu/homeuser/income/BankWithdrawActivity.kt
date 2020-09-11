package cn.sancell.xingqiu.homeuser.income

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.income.fragment.BankWithdrawFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2020/6/18.
 */
class BankWithdrawActivity :DefaultFragmetnAttachActivity<BaseViewModel>() {
    override val loadFragment: Fragment?
        get() = BankWithdrawFragment()

    override fun initData() {
    }
}