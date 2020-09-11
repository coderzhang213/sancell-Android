package cn.sancell.xingqiu.homecommunity.live.actviity

import android.content.Context
import android.content.Intent
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homecommunity.live.fragment.LivePlayBaseHoemFragment
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveHomeBaseActivity
import cn.sancell.xingqiu.live.constant.LiveConstant
import handbank.hbwallet.BaseViewModel

class LivePlayBaseHoemActivity : LiveHomeBaseActivity<BaseViewModel>() {

    companion object {
        /**
         * type等于3的时候需要目标主播ID 是用户自己界面，就传用户ID。目标主播个人中心就传目标用户ID
         * */
        fun startIntent(content: Context, type: String, batchId: String, anchorId: String) {
            val inetnet = Intent(content, LivePlayBaseHoemActivity::class.java)
            inetnet.putExtra(LiveConstant.LIVE_TYPE, type)
            inetnet.putExtra(LiveConstant.LIVE_BIACTH, batchId)
            inetnet.putExtra(LiveConstant.ANDID_ID, anchorId)
            content.startActivity(inetnet)
        }

        fun startIntent(content: Context, type: String, batchId: String) {
            //如果是主播个人中心界面进来，就不能用这个方法
            if (type == LivePlayType.USER_CANER_TO_TYPE.type) {
                return
            }
            startIntent(content, type, batchId, "")
        }
    }


    override fun onReloadData() {
    }

    override val loadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.activity_base_hoem_layout

    override fun initView() {

    }

    override fun initData() {
        getShowFragment(0)
    }

    override val getAddFragmentLayoutId: Int
        get() = R.id.rl_conent

    override fun initLoadFragment() {
        val type = intent.getStringExtra(LiveConstant.LIVE_TYPE)
        val batchId = intent.getStringExtra(LiveConstant.LIVE_BIACTH)
        val anchorId = intent.getStringExtra(LiveConstant.ANDID_ID)
        mInitfragments.add(LivePlayBaseHoemFragment.getInsener(type, batchId, anchorId))
    }


}