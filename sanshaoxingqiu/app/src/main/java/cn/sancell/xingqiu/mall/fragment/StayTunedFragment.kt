package cn.sancell.xingqiu.mall.fragment

import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.viewmodel.MarketViewModel
import kotlinx.android.synthetic.main.fragment_stay_layout.*

/**
 * Created by zj on 2020/1/6.
 */
class StayTunedFragment : BaseNotDataFragmentKt<MarketViewModel>() {
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_stay_layout

    override fun initView() {
        val title = activity?.intent?.getStringExtra("title")
        setTitleName(title)
    }

    override fun initData() {
    }
}