package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.ChatRes
import cn.sancell.xingqiu.live.adapter.MsgListAdapter
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LetterViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_letter_main.*
import kotlinx.android.synthetic.main.pay_result.*

/**
 * 私信主页
 */
class LetterActivity : BaseActivity<LetterViewModel>() {

    var mAdapter: MsgListAdapter? = null
    private var mPage = 1
    private val mDataList: MutableList<ChatRes> = ArrayList()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LetterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_letter_main

    override fun providerVMClass(): Class<LetterViewModel>? {
        return LetterViewModel::class.java
    }

    private fun initFresh() {
        common_fresh.setOnRefreshListener {
            mPage = 1
            mViewModel.getMsgList(mPage)
            common_fresh.finishRefresh()
        }
        common_fresh.setOnLoadMoreListener {
            mPage++
            mViewModel.getMsgList(mPage)
            common_fresh.finishRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        common_fresh.autoRefresh()
    }

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        btn_back.setOnClickListener { finish() }
        tv_title.text = "消息"
        rv_msg.layoutManager = LinearLayoutManager(this)
        initFresh()
    }

    override fun initData() {

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mMsgDataList.observe(this@LetterActivity, Observer {
                if (it.dataList.isNotEmpty()) {

                    if (mPage == 1) {
                        mDataList.clear()
                    }
                    mDataList.addAll(it.dataList)
                    if (mAdapter == null) {
                        setEmpty(false)
                        mAdapter = MsgListAdapter(mDataList)
                        mAdapter!!.setOnItemClickListener(object : MsgListAdapter.OnItemClickListener {
                            override fun onItemClick(data: ChatRes, position: Int) {

                                LetterDialogBoxActivity.start(this@LetterActivity, data.friendNickName!!, data.friendId!!)
                            }
                        })
                        rv_msg.adapter = mAdapter

                    } else {
                        mAdapter!!.setNewData(it.dataList)
                    }
                } else {
                    if (mPage == 1) {
                        setEmpty(true)
                    }
                }
                if (mDataList.size < it.dataCount) {
                    common_fresh.setEnableLoadMore(true)
                } else {
                    common_fresh.setEnableLoadMore(false)
                }
            })

            errMsg.observe(this@LetterActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
            mException.observe(this@LetterActivity, Observer {
                Log.i("letterActivity ==== ", it.toString())
            })
        }
    }

    private fun setEmpty(show: Boolean) {
        if (show) {
            view_msg_empty.visibility = View.VISIBLE
            rv_msg.visibility = View.GONE
        } else {
            view_msg_empty.visibility = View.GONE
            rv_msg.visibility = View.VISIBLE
        }
    }



}