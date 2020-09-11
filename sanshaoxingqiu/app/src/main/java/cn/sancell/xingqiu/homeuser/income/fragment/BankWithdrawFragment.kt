package cn.sancell.xingqiu.homeuser.income.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homeuser.income.AddBnkCardActivity
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.viewmodel.IncomeViewModel
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.fragment_inf_width_layout.*

/**
 * Created by zj on 2020/6/18.
 */
class BankWithdrawFragment : BaseNotDataFragmentKt<IncomeViewModel>() {
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int {
        return R.layout.fragment_inf_width_layout
    }

    override fun providerVMClass(): Class<IncomeViewModel>? {
        return IncomeViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mWidt.observe(this@BankWithdrawFragment, Observer {
                ToastHelper.showToast("提现成功")
            })
            errMsg.observe(this@BankWithdrawFragment, Observer {
                ToastHelper.showToast(it)
            })
        }
    }

    override fun initView() {
        setTitleName("申请提现")
        rl_add_bank.setOnClickListener(mOnClickLinsene)
        tv_width.setOnClickListener(mOnClickLinsene)
    }

    override fun initData() {
    }

    private val mOnClickLinsene = View.OnClickListener {
        when (it.id) {
            R.id.rl_add_bank -> {
                startActivity(Intent(context, AddBnkCardActivity::class.java))
            }
            R.id.tv_width -> {//提现
                sumintWithdraw()
            }
        }
    }

    fun sumintWithdraw() {
        val mode = et_wit_mon.text.toString().trim()
        if (TextUtils.isEmpty(mode)) {
            ToastHelper.showToast("请输入提现金额")
            return
        }
        mViewModel.userWitraw(mode.toInt(), "")

    }
}