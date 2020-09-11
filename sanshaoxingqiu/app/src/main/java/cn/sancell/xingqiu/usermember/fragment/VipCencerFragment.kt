package cn.sancell.xingqiu.usermember.fragment

import android.graphics.Color
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.base_title_layout.*

/**
 * Created by zj on 2020/1/2.
 */
class VipCencerFragment : BaseNotDataFragmentKt<UserViewModel>() {
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_vip_cancer_layout

    override fun initView() {
    }

    override fun initData() {
    }
}