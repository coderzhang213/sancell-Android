package cn.sancell.xingqiu.dialog

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CouponItemInfo
import cn.sancell.xingqiu.dialog.adapter.CouponListAdapter
import cn.sancell.xingqiu.interfaces.OnCoupnGetLinener
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class CouponDialog(activity: Activity) : ReminderBaseDialog(activity) {
    var mReadPackerMoneryAdapter: CouponListAdapter? = null
    private var mOnCoupnGetLinener: OnCoupnGetLinener? = null
    var dataList: List<CouponItemInfo>? = null
    var rl_e_list: RecyclerView? = null

    constructor(activity: Activity, mOnCoupnGetLinener: OnCoupnGetLinener, dataList: List<CouponItemInfo>?) : this(activity) {
        this.mOnCoupnGetLinener = mOnCoupnGetLinener
        this.dataList = dataList
        bindAdapter()
    }


    override fun setTitle(tv_title: TextView?) {
        super.setTitle(tv_title)
        tv_title?.setText("领取代金券")
    }


    override fun setAddView(tv_read_packer: LinearLayout?) {
        super.setAddView(tv_read_packer)
        setScreenRaiton(0.5)
        tv_read_packer?.visibility = View.VISIBLE

    }


    override fun setSubmitText(tv_new_address: TextView?) {
        super.setSubmitText(tv_new_address)
        tv_new_address?.visibility = View.GONE
    }

    override fun getAddView(): View {
        return View.inflate(context, R.layout.view_read_pack_layout, null)
    }

    override fun setAdapter(rl_list: RecyclerView) {
        super.setAdapter(rl_list)
        this.rl_e_list = rl_list
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val mar = ScreenUtil.dip2px(10f)
        lp.setMargins(mar, mar, mar, mar)
        rl_list.layoutParams = lp


    }

    fun bindAdapter() {
        dataList?.apply {
            mReadPackerMoneryAdapter = CouponListAdapter(this)
            mReadPackerMoneryAdapter?.setOnItemChildClickListener { adapter, view, position ->
                dismiss()
                mOnCoupnGetLinener?.onGetCounGet(this.get(position))
            }
            rl_e_list?.layoutManager = LinearLayoutManager(context)
            rl_e_list?.adapter = mReadPackerMoneryAdapter
        }
    }

}