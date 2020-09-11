package cn.sancell.xingqiu.live.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveRelevCommInfo
import cn.sancell.xingqiu.dialog.ComfirmToDialog
import cn.sancell.xingqiu.dialog.LiveAddGroupDialog
import cn.sancell.xingqiu.dialog.listener.OnComfirmLinsenr
import cn.sancell.xingqiu.interfaces.OnUpComfirLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.adapter.LiveAddGroupAdapter
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import handbank.hbwallet.BaseViewModel
import kotlinx.android.synthetic.main.fragment_live_set_gruou_layout.*

class LiveAddGroupFragment : BaseNotDataFragmentKt<LiveViewModel>() {
    private var mBatchId = ""
    private val mList = ArrayList<LiveRelevCommInfo>()
    private var mLiveAddGroupAdapter: LiveAddGroupAdapter? = null
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fragment_live_set_gruou_layout

    override fun initView() {
        setTitleName("推广社群")
        ll_add_group.setOnClickListener(mOnClickLinsener)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mLiveAddRemGroup.observe(this@LiveAddGroupFragment, Observer {
                ToastHelper.showToast("添加成功")
                getGroupList()
            })
            mLiveDeleteRemGroup.observe(this@LiveAddGroupFragment, Observer {
                ToastHelper.showToast("删除成功")
                getGroupList()
            })
            mInitRemmList.observe(this@LiveAddGroupFragment, Observer {
                bindView(it.dataList)
            })
            errMsg.observe(this@LiveAddGroupFragment, Observer {
                ToastHelper.showToast(it)

            })
        }
    }

    fun bindView(dataList: List<LiveRelevCommInfo>) {
        mList.clear()
        if (dataList.size > 0) {
            mList.addAll(dataList)
        }
        if (mLiveAddGroupAdapter == null) {
            mLiveAddGroupAdapter = LiveAddGroupAdapter(mList)
            mLiveAddGroupAdapter?.setOnItemClickListener { adapter, view, position ->
                val mComfirmToDialog = ComfirmToDialog(context!!, "您确定要移除该群吗？")
                mComfirmToDialog.setOnClickLinsener(object : OnComfirmLinsenr {
                    override fun OnCancerLinsenr() {
                    }

                    override fun OnComfirmLinsenr() {
                        val mInfo = mList.get(position)
                        mViewModel.deleteReemAndGroup(mBatchId, "b", mInfo.tid)
                    }
                })
                mComfirmToDialog.show()
            }
            live_grup_list.layoutManager = LinearLayoutManager(context)
            live_grup_list.adapter = mLiveAddGroupAdapter
        } else {
            mLiveAddGroupAdapter?.setNewData(mList)
        }

    }

    /**
     * 添加群组
     */
    fun addLiveGroup(tid: String) {
        mViewModel.addLiveReemAndGroup(mBatchId, "b", "", tid)

    }

    fun getGroupList() {
        mViewModel.getLiveCommentList(mBatchId, "b")
    }

    override fun initData() {//LiveAddGroupDialog


        activity?.intent?.apply {
            mBatchId = getStringExtra(LiveConstant.BATCH_ID)
        }
        getGroupList()
    }

    private val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ll_add_group -> {//添加
                    val mLiveAddGroupDialog = LiveAddGroupDialog(context!!)
                    mLiveAddGroupDialog.setOnUpRemmodityNameLinsener(object : OnUpComfirLinsener {
                        override fun onComfirmLinser(newName: String) {
                            mLiveAddGroupDialog.dismiss()
                            addLiveGroup(newName)
                        }

                    })
                    mLiveAddGroupDialog.setContentMsg("请输入推广社群群号")
                    mLiveAddGroupDialog.show()
                }
            }
        }
    }
}