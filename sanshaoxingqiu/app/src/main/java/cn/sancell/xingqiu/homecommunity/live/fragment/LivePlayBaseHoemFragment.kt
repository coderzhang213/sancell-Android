package cn.sancell.xingqiu.homecommunity.live.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ObserverBuild
import cn.sancell.xingqiu.bean.RecomLiveInfo
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveSearchActivity
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.live.play.LivePlayBaseFragment
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import kotlinx.android.synthetic.main.fragment_play_base_layouy.*

class LivePlayBaseHoemFragment : BaseNotDataFragmentKt<LiveViewModel>(), OnAddRecommendLinsener {
    val mfragments = ArrayList<Fragment>()
    val mInitfragments = ArrayList<Fragment>()
    var lastFragment: Fragment? = null
    val FRAGMENT_CONTAINER_ID = R.id.rl_base_conent
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    var mType = LivePlayType.HOME_TO_TYPE.type

    companion object {
        fun getInsener(mType: String, batchId: String, anchorId: String): Fragment {//不要左右切换了，所以直接替换
            var framgent: Fragment? = null
            if (mType == LivePlayType.SERARCH_TYPE.type ||
                    mType == LivePlayType.EXTERNAL_TYPE.type ||
                    mType == LivePlayType.USER_CAN_TYPE.type ||
                    mType == LivePlayType.HOME_BAND.type
            ) {

                framgent = LivePlayBaseFragment.getInstet(mType, batchId, 0, object : OnAddRecommendLinsener {
                    override fun onAddRecommendData(dataList: List<RecomLiveInfo>?) {
                    }
                })
            } else {
                framgent = LivePlayListFragment2.startIntent(mType, batchId, anchorId)

            }
//            val framgent = LivePlayBaseHoemFragment()
//            val mBundle = Bundle()
//            mBundle.putString(LiveConstant.LIVE_TYPE, type)
//            mBundle.putString(LiveConstant.LIVE_BIACTH, batchId)
//            mBundle.putString(LiveConstant.ANDID_ID, anchorId)
//            framgent.arguments = mBundle
            return framgent

        }
    }


    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.fragment_play_base_layouy

    override fun initView() {
        mType = arguments!!.getString(LiveConstant.LIVE_TYPE)!!

        initFragment()
        iv_colse.setOnClickListener(mOnClickLinsener)
        iv_query.setOnClickListener(mOnClickLinsener)
        tv_to_attention.setOnClickListener(mOnClickLinsener)
        tv_resm.setOnClickListener(mOnClickLinsener)
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }

        if (mType == LivePlayType.HOME_TO_TYPE.type) {//只有推荐有推荐和关注
            // rl_top1.visibility = View.VISIBLE
        } else {
            //  rl_top1.visibility = View.GONE
        }
    }

    /**
     * 初始化要显示的
     */
    fun initFragment() {
        var framgent: Fragment? = null
        val batchId = arguments!!.getString(LiveConstant.LIVE_BIACTH)
        if (mType == LivePlayType.HOME_TO_TYPE.type ||
                mType == LivePlayType.ATTENTIONS_TO_TYPE.type ||
                mType == LivePlayType.USER_CANER_TO_TYPE.type ||
                mType == LivePlayType.ATTEN_TJ.type ||
                mType == LivePlayType.AWESOME_TYPE.type ||
                mType == LivePlayType.RE_PLAY.type ||
                mType == LivePlayType.LIVE_LIST.type) {
            val anchorId = arguments!!.getString(LiveConstant.ANDID_ID)
            framgent = LivePlayListFragment.startIntent(mType, batchId, anchorId!!)

        } else {
            framgent = LivePlayBaseFragment.getInstet(mType, batchId!!, 0, this)
        }

        framgent.apply {
            mInitfragments.add(this)
        }



        mInitfragments.add(LiveAttenListFragment())
    }

    override fun onAddRecommendData(dataList: List<RecomLiveInfo>?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ObserverManger.getInstance(ObserverKey.LIVE_CONCISE_OPEN).registerObserver(liveStatusObeser)
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverManger.getInstance(ObserverKey.LIVE_CONCISE_OPEN).removeObserver(liveStatusObeser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        lastFragment?.onHiddenChanged(hidden)
    }

    override fun initData() {//LivePlayListFragment
        getShowFragment(0)
    }

    //用来监听状态
    private val liveStatusObeser = OnObserver {
        if (it is ObserverBuild) {
            when (it.type) {
                "1" -> {
                    if (mType == LivePlayType.HOME_TO_TYPE.type) {//只有推荐列表才响应精简模式
                        val isConciseModel = it.data
                        if (isConciseModel is Boolean) {
                            if (isConciseModel) {//是否精简模式
                                // rl_top1.visibility = View.GONE
                            } else {
                                //    rl_top1.visibility = View.VISIBLE
                            }
                        }
                    }


                }
            }


        }

    }

    fun setToolBarStatus(isB: Boolean) {
        if (isB) {
            tv_resm.setTextColor(resources.getColor(R.color.white))
            tv_to_attention.setTextColor(resources.getColor(R.color.white))
            tv_resm.setTextSize(16f)
            tv_to_attention.setTextSize(14f)
            iv_query.setImageResource(R.mipmap.live_ser)
            iv_colse.setImageResource(R.mipmap.live_exit)
        } else {
            tv_resm.setTextColor(resources.getColor(R.color.black))
            tv_to_attention.setTextColor(resources.getColor(R.color.black))
            tv_resm.setTextSize(14f)
            tv_to_attention.setTextSize(16f)
            iv_query.setImageResource(R.mipmap.live_hont_ser)
            iv_colse.setImageResource(R.mipmap.home_live_exit)
        }
    }

    fun getShowFragment(tabIndex: Int) {
        //把当前显示的索引保存起来
        mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.setPlayBaseShowIndex(tabIndex)

        val fragment = mInitfragments.get(tabIndex)
        fragment.apply {
            showFragment(this)
        }
    }

    fun showFragment(mShowFragment: Fragment) {
        if (!mShowFragment.isVisible) {
            removeAllTopFragment()
            val mFragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            if (!mfragments.contains(mShowFragment)) {
                mfragments.add(mShowFragment)
                mFragmentTransaction.add(FRAGMENT_CONTAINER_ID, mShowFragment)
            }
            lastFragment?.apply {
                mFragmentTransaction.hide(this)
            }

            mFragmentTransaction.show(mShowFragment)
            mFragmentTransaction.commitAllowingStateLoss()
            lastFragment = mShowFragment
        }
    }

    fun removeAllTopFragment() {
        val count = activity!!.supportFragmentManager.backStackEntryCount
        for (index in 1..count) {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }

    val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.tv_resm -> {
                    setToolBarStatus(true)
                    getShowFragment(0)
                }
                R.id.iv_query -> {//查询
                    LiveSearchActivity.start(context!!)
                }
                R.id.tv_to_attention -> {//去关注界面
                    setToolBarStatus(false)
                    getShowFragment(1)
                }
                R.id.iv_colse -> {//关闭
                    ObserverManger.getInstance(ObserverKey.LIVE_REM_CLOSE).notifyObserver(null)
                    ObserverManger.getInstance(ObserverKey.LIVE_ATTEN_CLOSE).notifyObserver(null)
                }
            }
        }
    }
}