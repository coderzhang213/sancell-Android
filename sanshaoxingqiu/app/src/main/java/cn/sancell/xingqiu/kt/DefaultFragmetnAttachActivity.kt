package cn.sancell.xingqiu.kt

import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2018/7/5 0005.
 */
abstract class DefaultFragmetnAttachActivity<VM : BaseViewModel> : BaseActivity<VM>() {
        var mLoadFragment:Fragment?=null
    override fun getLayoutResId(): Int {
        return R.layout.activity_default_layout
    }

    override fun initView() {
        mLoadFragment = loadFragment
        bindIntent(intent,mLoadFragment!!)
        if (mLoadFragment != null) { //把fragment添加到activity
            addFragment(R.id.rl_content, mLoadFragment)
        }
    }
   open fun bindIntent(intent: Intent?,mLoadFragment:Fragment){

    }

    protected abstract val loadFragment: Fragment?
}