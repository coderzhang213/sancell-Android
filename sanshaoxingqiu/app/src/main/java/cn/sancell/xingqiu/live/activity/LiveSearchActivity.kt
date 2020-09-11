package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.TextView
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.LiveHotRes
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LiveSearchModel
import cn.sancell.xingqiu.widget.LabelsView
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_live_search.*
import kotlinx.android.synthetic.main.include_search_bar.*

/**
 * 直播搜索
 */
class LiveSearchActivity : BaseActivity<LiveSearchModel>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LiveSearchActivity::class.java)
            context.startActivity(intent)
        }
    }


    private var mHistoryDataList: MutableList<LiveHotRes.KeyBean> = ArrayList()

    override fun getLayoutResId(): Int = R.layout.activity_live_search

    var datas: MutableList<String> = ArrayList()
    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        tv_cancel.setOnClickListener { finish() }
        et_search.hint = "请输入主播昵称/主播id/直播标题搜索"
        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (et_search.text.toString().isNotEmpty()) {

                    if (actionId == 0 || actionId == 3) {
                        event.let {
                            LiveSearchDetailActivity.start(this@LiveSearchActivity, et_search.text.toString())
                            return true
                        }
                    }
                } else {
                    SCApp.getInstance().showSystemCenterToast("请输入搜索条件")
                }
                return false
            }
        })

        iv_delete_history.setOnClickListener {
            if (mHistoryDataList.size > 0) {
                mViewModel.clearHistory()
            } else {
                SCApp.getInstance().showSystemCenterToast("暂无历史记录")
            }
        }

    }


    override fun providerVMClass(): Class<LiveSearchModel>? {
        return LiveSearchModel::class.java
    }

    override fun initData() {

        mViewModel.getHotSearchData()
        mViewModel.getHistorySearch(5)

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mHotSearchData.observe(this@LiveSearchActivity, Observer {
                if (it.dataList != null && it.dataList.size > 0) {
                    view_hot.setLabels(it.dataList, object : LabelsView.LabelTextProvider<LiveHotRes.KeyBean> {

                        override fun getLabelText(label: TextView?, position: Int, data: LiveHotRes.KeyBean): CharSequence {
                            return data.keywords
                        }
                    })
                    view_hot.setOnLabelClickListener { _, data, _ ->
                        if (data is LiveHotRes.KeyBean) {
                            LiveSearchDetailActivity.start(this@LiveSearchActivity, data.keywords)
                        }
                    }
                }

            })

            mHistoryData.observe(this@LiveSearchActivity, Observer {
                if (it.dataList != null && it.dataList.size > 0) {
                    mHistoryDataList = it.dataList
                    view_history.setLabels(it.dataList, object : LabelsView.LabelTextProvider<LiveHotRes.KeyBean> {
                        override fun getLabelText(label: TextView?, position: Int, data: LiveHotRes.KeyBean): CharSequence {
                            return data.keywords
                        }
                    })
                    view_history.setOnLabelClickListener { _, data, _ ->
                        if (data is LiveHotRes.KeyBean)
                            LiveSearchDetailActivity.start(this@LiveSearchActivity, data.keywords)
                    }
                }
            })
            mClearCode.observe(this@LiveSearchActivity, Observer {
                if (mClearCode.value == 0) {
                    mHistoryDataList.clear()
                    view_history.setLabels(null)

                }
            })

        }
    }

}