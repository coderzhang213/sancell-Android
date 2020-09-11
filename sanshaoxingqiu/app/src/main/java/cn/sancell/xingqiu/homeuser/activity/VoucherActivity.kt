package cn.sancell.xingqiu.homeuser.activity


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.VoucherInfo
import cn.sancell.xingqiu.homeuser.adapter.VoucherAdapter
import cn.sancell.xingqiu.homeuser.viewModel.VoucherViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.widget.NormalEmptyView
import com.netease.nim.uikit.business.robot.parser.elements.group.LinearLayout
import kotlinx.android.synthetic.main.activity_user_voucher.*
import kotlinx.android.synthetic.main.fragment_live_capture_tool_layout.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class VoucherActivity : BaseActivity<VoucherViewModel>() {

    private var mType: Int = 0

    private var mAdapter: VoucherAdapter? = null

    companion object {
        /**
         * @type 0：有效券，1：无效券
         */
        fun start(context: Context, type: Int) {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun providerVMClass(): Class<VoucherViewModel>? {
        return VoucherViewModel::class.java
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_user_voucher
    }

    override fun initView() {

        StatusBarUtil.setStatusBarDarkTheme(this, true)
        btn_back.setOnClickListener { finish() }
        rv_voucher.layoutManager = LinearLayoutManager(this)
    }


    override fun initData() {
        mType = intent.getIntExtra("type", 0)
        if (mType == 0) {
            tv_title.text = "代金券"
        } else {
            tv_title.text = "不可用代金券"
        }

        mViewModel.getVoucherList(if (mType == 0) {
            2
        } else {
            1
        })
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mVoucherList.observe(this@VoucherActivity, Observer {
                bindView(it.dataList)
            })
            errMsg.observe(this@VoucherActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
            mException.observe(this@VoucherActivity, Observer {
                Log.i("777777", it.toString())
            })
        }
    }

    private fun bindView(data: MutableList<VoucherInfo>) {
        if (data.isNotEmpty()) {
            showEmpty(false)
            for (item in data) {
                item.voucherType = mType
            }
            if (mType == 0) {
                //添加尾部
                val voucher = VoucherInfo()
                voucher.voucherType = 2
                data.add(voucher)
            }
            if (mAdapter == null) {
                rv_voucher.adapter = VoucherAdapter(data)
            } else {
                mAdapter!!.setNewData(data)
            }
        } else {
            showEmpty(true)
        }
    }


    private fun showEmpty(show: Boolean) {
        if (show) {
            empty.visibility = View.VISIBLE
            rv_voucher.visibility = View.GONE
            empty.setEmptyImg(resources.getDrawable(R.mipmap.icon_coupon_no_data))
            empty.setEmptyDescColor(Color.parseColor("#B1B2B3"))
            if (mType == 0) {
                //显示不可用代金券
                empty.setEmptyDesc("暂无可用代金券")
                empty.setSecondFun(true, "不可用代金券", resources.getDrawable(R.mipmap.icon_more_unusable_couponlist),object :NormalEmptyView.OnFunClickListener{
                    override fun onSecondFun() {
                        VoucherActivity.start(this@VoucherActivity, 1)
                    }
                })
            } else {
                empty.setEmptyDesc("暂无不可用代金券")
                empty.setSecondFun(false, "", resources.getDrawable(R.mipmap.icon_more_unusable_couponlist),object :NormalEmptyView.OnFunClickListener{
                    override fun onSecondFun() {

                    }
                })
            }
        } else {
            empty.visibility = View.GONE
            rv_voucher.visibility = View.VISIBLE
        }

    }
}