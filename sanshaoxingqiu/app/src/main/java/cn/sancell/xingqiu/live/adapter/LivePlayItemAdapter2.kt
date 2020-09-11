package cn.sancell.xingqiu.live.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.sancell.xingqiu.bean.RecomLiveInfo
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener
import cn.sancell.xingqiu.live.play.LivePlayBaseFragment

/**
 * Created by zj on 2020/4/27.
 */
class LivePlayItemAdapter2(fragment: Fragment, mType: String, mList: List<RecomLiveInfo>, mOnAddRecommendLinsener: OnAddRecommendLinsener) : FragmentStateAdapter(fragment) {
    private var mList: List<RecomLiveInfo>? = null
    private var mOnAddRecommendLinsener: OnAddRecommendLinsener? = null
    private var mType: String? = null

    init {
        this.mType = mType
        this.mList = mList
        this.mOnAddRecommendLinsener = mOnAddRecommendLinsener
    }

    override fun getItemCount(): Int = mList!!.size

    override fun createFragment(position: Int): Fragment {
        val mFragment = LivePlayBaseFragment.getInstet(mType!!, mList?.get(position)?.recId!!, position, mOnAddRecommendLinsener!!)
        return mFragment
    }


    fun setmList(mList: List<RecomLiveInfo>) {
        this.mList = mList
        notifyDataSetChanged()
    }

}