package cn.sancell.xingqiu.live.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ReportItemBean
import cn.sancell.xingqiu.constant.RequestCode
import cn.sancell.xingqiu.kt.BaseActivity
import cn.sancell.xingqiu.live.adapter.ReportItemAdapter
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.ReportUserViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_report_user.*
import kotlinx.android.synthetic.main.layout_toolbar.*


/**
 * 举报
 */
class ReportActivity : BaseActivity<ReportUserViewModel>() {

    val datas: MutableList<ReportItemBean> = ArrayList()

    companion object {
        fun start(context: Activity) {
            val intent = Intent(context, ReportActivity::class.java)
            context.startActivityForResult(intent, RequestCode.LIVER_REPORT)
        }
    }

    private var mAdapter: ReportItemAdapter? = null


    override fun getLayoutResId(): Int = R.layout.activity_report_user

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        rv_report.layoutManager = LinearLayoutManager(this)
        btn_back.setOnClickListener { finish() }
        tv_title.text = "举报"

    }

    override fun initData() {
        addData()
        if (mAdapter == null) {
            mAdapter = ReportItemAdapter(datas)
            mAdapter!!.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }

            })
            rv_report.adapter = mAdapter
        } else {
            mAdapter!!.setNewData(datas)
        }


    }

    private fun addData() {

        datas.add(ReportItemBean(1, "违法违禁"))
        datas.add(ReportItemBean(2, "政治有害"))
        datas.add(ReportItemBean(3, "淫秽色情"))
        datas.add(ReportItemBean(4, "烟酒驾驶"))
        datas.add(ReportItemBean(5, "广告欺诈"))
        datas.add(ReportItemBean(6, "作弊/刷人气"))
        datas.add(ReportItemBean(7, "未成年人相关"))
        datas.add(ReportItemBean(8, "疑似自我伤害"))
        datas.add(ReportItemBean(8, "其他"))
    }

}