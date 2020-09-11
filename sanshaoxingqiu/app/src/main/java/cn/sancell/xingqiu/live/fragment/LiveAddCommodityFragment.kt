package cn.sancell.xingqiu.live.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveRelevCommInfo
import cn.sancell.xingqiu.dialog.ComfirmToDialog
import cn.sancell.xingqiu.dialog.LiveAddCommDialog
import cn.sancell.xingqiu.dialog.listener.OnComfirmLinsenr
import cn.sancell.xingqiu.interfaces.OnUpComfirLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.activity.LiveAddCommodityListAcitivy
import cn.sancell.xingqiu.live.adapter.LiveAddCommAdapter
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.fragment_live_set_gruou_layout.*

class LiveAddCommodityFragment : BaseNotDataFragmentKt<LiveViewModel>() {
    private var mBatchId = ""
    private var mLiveAddGroupAdapter: LiveAddCommAdapter? = null
    private val mList = ArrayList<LiveRelevCommInfo>()
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mInitRemmList.observe(this@LiveAddCommodityFragment, Observer {
                bindView(it.dataList)
            })
            mLiveAddRemGroup.observe(this@LiveAddCommodityFragment, Observer {
                ToastHelper.showToast("添加成功")
                getCommoditLits()
            })
            mLiveDeleteRemGroup.observe(this@LiveAddCommodityFragment, Observer {
                ToastHelper.showToast("删除成功")
                getCommoditLits()
            })
            mLiveUpNameRemGroup.observe(this@LiveAddCommodityFragment, Observer {
                ToastHelper.showToast("更新成功")
                getCommoditLits()
            })
            errMsg.observe(this@LiveAddCommodityFragment, Observer {
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
            mLiveAddGroupAdapter = LiveAddCommAdapter(mList)
            mLiveAddGroupAdapter?.setOnItemClickListener { adapter, view, position ->
                val mComfirmToDialog = ComfirmToDialog(context!!, "您确定要移除该商品吗？")
                mComfirmToDialog.setOnClickLinsener(object : OnComfirmLinsenr {
                    override fun OnCancerLinsenr() {
                    }

                    override fun OnComfirmLinsenr() {
                        mComfirmToDialog.dismiss()
                        val mInfo = mList.get(position)
                        mViewModel.deleteReemAndGroup(mBatchId, "a", mInfo.sku)
                    }
                })

                mComfirmToDialog.show()
            }
            mLiveAddGroupAdapter?.setOnItemChildClickListener { adapter, view, position ->

                val mLiveAddCommDialog = LiveAddCommDialog(context!!)
                mLiveAddCommDialog.setOnUpRemmodityNameLinsener(object : OnUpComfirLinsener {
                    override fun onComfirmLinser(newName: String) {
                        val mInfo = mList.get(position)
                        mViewModel.upRemmoidtyName(mBatchId, newName, mInfo.sku)
                    }
                })
                mLiveAddCommDialog.show()
            }
            live_grup_list.layoutManager = LinearLayoutManager(context)
            live_grup_list.adapter = mLiveAddGroupAdapter
        } else {
            mLiveAddGroupAdapter?.setNewData(mList)
        }

    }

    fun getCommoditLits() {
        mViewModel.getLiveCommentList(mBatchId, "a")
    }

    override fun getLayoutResId(): Int = R.layout.fragment_live_set_comm_layout

    override fun initView() {
        setTitleName("推广商品")
        ll_add_group.setOnClickListener(mOnClickLinsener)
    }

    override fun initData() {//LiveAddGroupDialog
        activity?.intent?.apply {
            mBatchId = getStringExtra(LiveConstant.BATCH_ID)
        }
        getCommoditLits()


    }

    private val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ll_add_group -> {//添加
                    val intent = Intent(context, LiveAddCommodityListAcitivy::class.java)
                    startActivityForResult(intent, 1001)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> {//选择商品
                if (resultCode == Activity.RESULT_OK) {
                    data?.apply {
                        val skuId = getStringExtra("skuId")
                        addLiveCommodity(skuId)
                    }
                }

            }
        }
    }

    fun addLiveCommodity(skuId: String) {
        mViewModel.addLiveReemAndGroup(mBatchId, "a", skuId, "")

    }
}