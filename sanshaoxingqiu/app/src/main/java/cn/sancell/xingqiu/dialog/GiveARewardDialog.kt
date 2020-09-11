package cn.sancell.xingqiu.dialog

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ReadPackInfo
import cn.sancell.xingqiu.dialog.adapter.ReadPackerMoneryAdapter
import cn.sancell.xingqiu.interfaces.OnGiveReadLinsenr
import cn.sancell.xingqiu.interfaces.OnPlayPasswordLinsenr
import cn.sancell.xingqiu.live.utils.JsonUtils
import cn.sancell.xingqiu.util.AssetsUtils
import cn.sancell.xingqiu.util.BigDecimalUtils
import cn.sancell.xingqiu.util.PlayDialogUtils
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class GiveARewardDialog(activity: Activity, mOnGiveReadLinsenr: OnGiveReadLinsenr) : ReminderBaseDialog(activity) {
    var mReadPackSum: TextView? = null
    var mReadPackerMoneryAdapter: ReadPackerMoneryAdapter? = null
    private var mOnGiveReadLinsenr: OnGiveReadLinsenr? = null

    init {
        this.mOnGiveReadLinsenr = mOnGiveReadLinsenr
    }

    override fun setTitle(tv_title: TextView?) {
        super.setTitle(tv_title)
        tv_title?.setText("打赏主播")
    }

    fun setReadPackValue(mLiveViewModel: LiveViewModel?, fragment: Fragment) {
        mLiveViewModel?.getReadPackAccout()?.observe(fragment, Observer {
            mReadPackSum?.setText("红包余额：¥" + it.balance)
        })

    }

    override fun setAddView(tv_read_packer: LinearLayout?) {
        super.setAddView(tv_read_packer)
        setScreenRaiton(0.4)
        tv_read_packer?.visibility = View.VISIBLE
        val mView = View.inflate(context, R.layout.view_read_pack_layout, null)
        tv_read_packer?.addView(mView)
        mReadPackSum = mView.findViewById(R.id.tv_read_packer)
        mView.findViewById<TextView>(R.id.tv_play_tour).setOnClickListener {
            val money = mReadPackerMoneryAdapter?.getSelectReadPack()?.money
            val valueMoney = BigDecimalUtils.mul(money,"100",0)
            if (mOnGiveReadLinsenr != null) {
                mOnGiveReadLinsenr?.onClcikGive(valueMoney!!)
            }
            dismiss()
        }

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
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val mar = ScreenUtil.dip2px(10f)
        lp.setMargins(mar, mar, mar, mar)
        rl_list.layoutParams = lp
        val moneyText = AssetsUtils.getAssetsString("read_pack_money_list.txt")
        val mMoneyList = JsonUtils.getObjects(moneyText, ReadPackInfo::class.java)
        mReadPackerMoneryAdapter = ReadPackerMoneryAdapter(mMoneyList)
        rl_list.layoutManager = GridLayoutManager(context, 4)
        rl_list.adapter = mReadPackerMoneryAdapter
    }

}