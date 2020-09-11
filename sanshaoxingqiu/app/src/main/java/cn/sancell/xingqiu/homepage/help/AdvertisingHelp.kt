package cn.sancell.xingqiu.homepage.help

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.viewmodel.HoemViewModel

/**
 * Created by zj on 2019/12/24.
 */
class AdvertisingHelp {
    var mHoemViewModel: HoemViewModel? = null
    var mFragment: Fragment? = null
    var mView: View? = null

    constructor(mFragment: Fragment, mHoemViewModel: HoemViewModel) {
        this.mHoemViewModel = mHoemViewModel
        this.mFragment = mFragment

        init()
    }

    fun initView() {
    }

    fun init() {
        mHoemViewModel?.getHomeBannerData()
        mHoemViewModel?.apply {
            mBannerData.observe(mFragment!!, Observer {


            })

        }
    }

    fun getAvdView(): View {
        mView = View.inflate(mFragment!!.context, R.layout.view_home_head_layout, null)
        initView()
        return mView!!
    }

}