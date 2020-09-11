package cn.sancell.xingqiu.homeuser.fragment

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.CountdownManager
import cn.sancell.xingqiu.im.entity.TabEntity
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.viewmodel.FightViewModel
import kotlinx.android.synthetic.main.fragment_my_fight_base_layout.*

/**
 * Created by zj on 2020/1/3.
 */
class MyFightOderFragment : BaseNotDataFragmentKt<FightViewModel>() {
    var page = 1
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun providerVMClass(): Class<FightViewModel>? {
        return FightViewModel::class.java
    }
    override fun getLayoutResId(): Int = R.layout.fragment_my_fight_base_layout

    override fun initView() {
        setTitleName("我的直拼")
        val mFragmetns = ArrayList<Fragment>()
        val mTabs = ArrayList<TabEntity>()
        mFragmetns.add(FightChildFragmetn.newFragment("0"))
        mFragmetns.add(FightChildFragmetn.newFragment("1"))
        mFragmetns.add(FightChildFragmetn.newFragment("2"))
        mFragmetns.add(FightChildFragmetn.newFragment("3"))
        mTabs.add(TabEntity("全部", 111))
        mTabs.add(TabEntity("直拼中", 112))
        mTabs.add(TabEntity("直拼成功", 113))
        mTabs.add(TabEntity("直拼失败", 114))
        tb_oder_title.addTab(tb_oder_title.newTab())
        tb_oder_title.addTab(tb_oder_title.newTab())
        tb_oder_title.addTab(tb_oder_title.newTab())
        tb_oder_title.addTab(tb_oder_title.newTab())
        val mAdapter = NormalPageAdapter(activity!!.supportFragmentManager, mFragmetns, mTabs)
        vp_fight.adapter = mAdapter
        vp_fight.setOffscreenPageLimit(4)
        tb_oder_title.setupWithViewPager(vp_fight)


    }

    override fun onDestroy() {
        super.onDestroy()
        CountdownManager.get().onCancer()
    }
    override fun initData() {
        getData()
    }
    fun getData(){

    }
}