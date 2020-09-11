package cn.sancell.xingqiu.homeuser.income

import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeuser.income.fragment.AddBnkCardFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2020/6/17.
 */
class AddBnkCardActivity :DefaultFragmetnAttachActivity<BaseViewModel>() {
    override val loadFragment: Fragment?
        get() = AddBnkCardFragment()

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLoadFragment?.apply {
            onActivityResult(requestCode, resultCode, data)
        }
    }
}