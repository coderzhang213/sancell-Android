package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.live.fragment.LiveSettingFragment
import handbank.hbwallet.BaseViewModel

/**
 * 直播设置
 */
class LiveSettingActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {

    companion object {
        fun startIntent(context: Context, isShowAlter: Boolean) {
            val intent = Intent(context, LiveSettingActivity::class.java)
            intent.putExtra("isShowAlter", isShowAlter)
            context.startActivity(intent)
        }
    }

    override val loadFragment: Fragment?
        get() = LiveSettingFragment()

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLoadFragment?.apply {
            onActivityResult(requestCode, resultCode, data)
        }
    }
}