package cn.sancell.xingqiu.homeuser.income.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.IncomeList
import cn.sancell.xingqiu.bean.IncomeParInfo
import cn.sancell.xingqiu.homeuser.adapter.IncomeListAdapter
import cn.sancell.xingqiu.homeuser.income.AddBnkCardActivity
import cn.sancell.xingqiu.homeuser.income.BankWithdrawActivity
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.util.PriceUtils
import cn.sancell.xingqiu.viewmodel.IncomeViewModel
import com.netease.nim.uikit.common.ToastHelper
import handbank.hbwallet.BaseViewModel
import kotlinx.android.synthetic.main.fragment_income_layout.*

/**
 * Created by zj on 2020/6/17.
 */
class UserincomeFragment : BaseNotDataFragmentKt<IncomeViewModel>() {
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = false

    override fun providerVMClass(): Class<IncomeViewModel>? {
        return IncomeViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mIncome.observe(this@UserincomeFragment, Observer {
                it?.apply {
                    setViewBindInfo(this)
                    incomeList?.apply {
                        bindIncomeListView(this)
                    }
                }
            })
        }
    }

    fun setViewBindInfo(mIncomeParInfo: IncomeParInfo) {
        sum_lin.setText(PriceUtils.getInstance().getPrice(mIncomeParInfo.sumIncome))
        tv_tx_cancer.setText(PriceUtils.getInstance().getPrice(mIncomeParInfo.withdrawing))
        tv_ktx.setText(PriceUtils.getInstance().getPrice(mIncomeParInfo.canWithdraw))
        tv_y_tx.setText(PriceUtils.getInstance().getPrice(mIncomeParInfo.withdrawCash))
    }

    /**
     * 设置列表
     */
    fun bindIncomeListView(mIncomeList: IncomeList) {
        mIncomeList.dataList?.apply {
            if (size > 0) {
                val sum = (size.toString() + "笔")
                tv_sum.setText(sum)
                rl_not_data.visibility = View.GONE
                val mIncomeListAdapter = IncomeListAdapter(this)
                rl_income_list.layoutManager = LinearLayoutManager(context)
                rl_income_list.adapter = mIncomeListAdapter
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_income_layout
    }

    override fun initView() {
        btn_back.setOnClickListener(mOnClickLinsener)
        tv_tx_list.setOnClickListener(mOnClickLinsener)
        tv_tx.setOnClickListener(mOnClickLinsener)
    }

    private val mOnClickLinsener = View.OnClickListener {
        when (it.id) {
            R.id.btn_back -> {
                activity?.finish()
            }
            R.id.tv_tx_list -> {
                ToastHelper.showToast("财务系统升级中，敬请期待")
               // startActivity(Intent(context, AddBnkCardActivity::class.java))
            }
            R.id.tv_tx -> {
                ToastHelper.showToast("财务系统升级中，敬请期待")
               // startActivity(Intent(context, BankWithdrawActivity::class.java))

            }
        }
    }

    override fun initData() {

        //获取收益信息
        mViewModel.useriNCOME()

    }
}