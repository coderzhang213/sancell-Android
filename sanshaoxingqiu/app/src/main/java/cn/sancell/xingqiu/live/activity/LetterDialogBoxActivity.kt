package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.ChatRes
import cn.sancell.xingqiu.bean.UserMsgRes
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.kt.BaseActivity
import cn.sancell.xingqiu.live.adapter.ChatAdapter
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LetterViewModel
import cn.sancell.xingqiu.widget.FocusForbidLinearLayoutManager
import kotlinx.android.synthetic.main.activity_letter_dialog_box.*
import kotlinx.android.synthetic.main.pay_result.*

/**
 * 私信对话
 */
class LetterDialogBoxActivity : BaseActivity<LetterViewModel>() {

    private var mAdapter: ChatAdapter? = null
    private var mPage: Int = 1
    private var mFriendId: String = ""
    private val msgList: MutableList<ChatRes> = ArrayList()
    private var needScroll = true

    companion object {
        fun start(context: Context, userName: String, friendId: String) {
            val intent = Intent(context, LetterDialogBoxActivity::class.java)
            intent.putExtra(UiHelper.LIVE_USER_NAME, userName)
            intent.putExtra(UiHelper.LIVE_USER_ID, friendId)
            context.startActivity(intent)
        }
    }

    override fun providerVMClass(): Class<LetterViewModel>? {
        return LetterViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.activity_letter_dialog_box


    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        btn_back.setOnClickListener { finish() }

        tv_send.setOnClickListener {
            mViewModel.sendMsg(mFriendId, et_msg.text.toString())
        }

        initFresh()

    }

    private fun initFresh() {
        refreshLayout.setOnRefreshListener {
            needScroll = false
            mPage++
            getData()
            refreshLayout.finishRefresh()

        }
        refreshLayout.setEnableLoadMore(false)
    }

    fun getData() {

        mViewModel.getMsgRecord(mFriendId, mPage)
    }

    override fun initData() {
        rv_chat.layoutManager = FocusForbidLinearLayoutManager(this)

        tv_title.text = intent.getStringExtra(UiHelper.LIVE_USER_NAME)
        mFriendId = intent.getStringExtra(UiHelper.LIVE_USER_ID)!!
        getData()

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mMsgRecordList.observe(this@LetterDialogBoxActivity, Observer {
                setChatAdapter(it)
            })

            mSendData.observe(this@LetterDialogBoxActivity, Observer {
                needScroll = true
                mPage = 1
                getData()
                et_msg.setText("")
            })
            errMsg.observe(this@LetterDialogBoxActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
            mException.observe(this@LetterDialogBoxActivity, Observer {
                Log.i("LetterDialogBoxActivity", it.toString())
            })
        }
    }


    private fun dealMsgList(data: MutableList<ChatRes>) {
        for (item in data) {
            if (item.receiverId == mFriendId) {
                item.chatItemType = UiHelper.chat_right_msg
            } else {
                item.chatItemType = UiHelper.chat_left_msg
            }
        }
        data.sortBy { it.sendTime }
    }

    private fun setChatAdapter(data: UserMsgRes) {

        if (mPage == 1) {
            msgList.clear()
            if (data.dataList.isNotEmpty()) {
                dealMsgList(data.dataList)
                msgList.addAll(data.dataList)
                mAdapter = ChatAdapter(msgList)
                rv_chat.adapter = mAdapter
            }
        } else {
            if (data.dataList.isNotEmpty()) {
                dealMsgList(data.dataList)
                msgList.addAll(0, data.dataList)
                mAdapter!!.notifyItemRangeInserted(0, data.dataList.size)

                if (rv_chat != null) {
                    val layoutManager = rv_chat.layoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager is LinearLayoutManager) {
                        //获取第一个可见view的位置
                        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                        if (firstItemPosition == 0) {
                            // 最顶部可见的View已经是FetchMoreView了，那么add数据&局部刷新后，要进行定位到上次的最顶部消息。
                            rv_chat.scrollToPosition(data.dataList.size)
                        }
                    } else {
                        rv_chat.scrollToPosition(data.dataList.size)
                    }
                }
            }
        }

        rv_chat.post {
            if (needScroll && msgList.size > 0) {
                rv_chat.scrollToPosition(msgList.size - 1)
            }
        }

    }


}