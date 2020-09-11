package cn.sancell.xingqiu.live.help

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.animator.FiftItemAnimator
import cn.sancell.xingqiu.live.adapter.GifListAdapter
import cn.sancell.xingqiu.live.widget.extension.RedEnvelopeInfo
import cn.sancell.xingqiu.util.RxTimerUtil

/**
 * Created by zj on 2020/4/2.
 */
class GifHelp {
    var recvie: RecyclerView? = null
    var mContext: Context? = null
    var mGifListAdapter: GifListAdapter? = null
    val mAllList = ArrayList<RedEnvelopeInfo>()
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mRxTimerUtil: RxTimerUtil? = null
    var isStartLoopTime = false

    constructor(mContext: Context, recvie: RecyclerView) {
        this.mContext = mContext
        this.recvie = recvie
    }

    fun initAdapter() {
        mGifListAdapter = GifListAdapter(mContext!!, mAllList)
        mLinearLayoutManager = LinearLayoutManager(mContext)
        recvie?.layoutManager = mLinearLayoutManager
        val mFiftItemAnimator = FiftItemAnimator()
        mFiftItemAnimator.addDuration = 1000
        mFiftItemAnimator.removeDuration = 1000
        recvie?.itemAnimator = mFiftItemAnimator
        recvie?.adapter = mGifListAdapter
    }

    fun addGiftInfo(mRedEnvelopeInfo: RedEnvelopeInfo) {
        mRedEnvelopeInfo.addListTime = System.currentTimeMillis()
        mAllList.add(mRedEnvelopeInfo)
        startLoopTime()
        //动画要调用这个
        mGifListAdapter?.notifyItemInserted(mAllList.size - 1)
        mLinearLayoutManager?.scrollToPositionWithOffset((mGifListAdapter!!.itemCount - 1), Integer.MIN_VALUE)
    }

    /**
     * 开始循环移除
     */
    fun startLoopTime() {
        if (isStartLoopTime) {
            return
        }
        if (mRxTimerUtil == null) {
            mRxTimerUtil = RxTimerUtil()
        }
        isStartLoopTime = true
        val mEmpList = ArrayList<RedEnvelopeInfo>()

        mRxTimerUtil?.interval(1000, {
            if (mAllList.size <= 0) {
                isStartLoopTime = false
                onDestroy()
                return@interval
            }
            for (pos in 0..mAllList.size - 1) {
                val info = mAllList.get(pos)
                if (System.currentTimeMillis() - info.addListTime >= 3000) {
                    mEmpList.add(info)
                }
            }
            if (mEmpList.size > 0) {
                mAllList.removeAll(mEmpList)
                mGifListAdapter?.notifyDataSetChanged()
            }

        })
    }
//
//    fun removeData(pos: Int, info: RedEnvelopeInfo) {
//        mAllList.remove(info)
//        mGifListAdapter?.notifyDataSetChanged()
//
//    }

    fun onDestroy() {
        mRxTimerUtil?.cancel()
        mRxTimerUtil = null
    }
}