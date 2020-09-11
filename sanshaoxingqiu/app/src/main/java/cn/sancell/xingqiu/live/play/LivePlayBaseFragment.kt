package cn.sancell.xingqiu.live.play

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LivePlayInfo
import cn.sancell.xingqiu.bean.StartAudienceInfo
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.live.fragment.LiveRoomFragment
import cn.sancell.xingqiu.live.fragment.LiveSubscribeFragment
import cn.sancell.xingqiu.live.interfacep.OnPlayLinsenr
import cn.sancell.xingqiu.live.interfacep.PLAY_STATE
import cn.sancell.xingqiu.util.BackPressedUtils
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import kotlinx.android.synthetic.main.fragment_live_palyt_base_layout.*

/**
 *用来区分加载什么播放类型
 */
class LivePlayBaseFragment : BaseNotDataFragmentKt<LiveViewModel>(), OnBackPressedLinsener {
    var mOnPlayLinsenr: OnPlayLinsenr? = null
    private var isVisibleToUser: Boolean = false
    protected var batchId: String? = ""
    var fragment: Fragment? = null
    protected var postion: Int = 0
    private var mType: String? = LivePlayType.HOME_TO_TYPE.type
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    private var mOnAddRecommendLinsener: OnAddRecommendLinsener? = null
    private var isShowLiveEnd = false

    fun setOnAddRecommendLinsener(mEmpOnAddRecommendLinsener: OnAddRecommendLinsener) {
        this.mOnAddRecommendLinsener = mEmpOnAddRecommendLinsener
    }


    companion object {
        fun getInstet(mType: String, batchId: String, postion: Int, mOnAddRecommendLinsener: OnAddRecommendLinsener): LivePlayBaseFragment {
            val mLivePlayBaseFragment = LivePlayBaseFragment()
            mLivePlayBaseFragment.setOnAddRecommendLinsener(mOnAddRecommendLinsener)
            val bulid = Bundle()
            bulid.putString(LiveConstant.LIVE_BIACTH, batchId)
            bulid.putInt(LiveConstant.LIVE_CURR_INDEX, postion)
            bulid.putString(LiveConstant.LIVE_TYPE, mType)
            mLivePlayBaseFragment.arguments = bulid
            return mLivePlayBaseFragment

        }
    }

    /**
     * 监听fragment是否显示
     */
    private val liveHindObeser = OnObserver {
        if (it is Boolean) {
            isVisibleToUser = !it
            startLivePlay()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        startLivePlay()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BackPressedUtils.bindOnBack(activity, this)

        ObserverManger.getInstance(ObserverKey.LIVE_Hidden).registerObserver(liveHindObeser)
    }


    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            livePlayInfo.observe(this@LivePlayBaseFragment, Observer {
                hideLoadData()
                setLiveEnd(false)
                loadFragment(it)
            })
            errMsg.observe(this@LivePlayBaseFragment, Observer {

                hideLoadData()
                setLiveEnd(true)
                //  setNotData()
            })
            mException.observe(this@LivePlayBaseFragment, Observer {
                Log.i("keey", "e:" + it.message)
                hideLoadData()
                setNotData()
            })

        }
    }

    fun setLiveEnd(isShow: Boolean) {
        isShowLiveEnd = isShow
        if (isShow) {
            rl_live_end.visibility = View.VISIBLE
        } else {
            rl_live_end.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        BackPressedUtils.unBindOnBack(activity, this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        startLivePlay()

    }

    override fun onResume() {
        super.onResume()
        mOnGetLivePalyStatusLinsener?.apply {
            val showPostion = getLiveStatusManger().getCurrShowIndoex()
            if (postion == showPostion) {
                getLiveStatusManger().setPagerShow(true)
            }
        }

        isVisibleToUser = true
        startLivePlay()
    }

    override fun onPause() {
        super.onPause()
        mOnGetLivePalyStatusLinsener?.apply {
            val showPostion = getLiveStatusManger().getCurrShowIndoex()
            if (postion == showPostion) {
                getLiveStatusManger().setPagerShow(false)
            }
        }
        isVisibleToUser = false

        startLivePlay()
    }

    fun loadFragment(mInfo: LivePlayInfo?) {
        mInfo?.apply {
            val transaction = childFragmentManager.beginTransaction()
            val fragments = childFragmentManager.fragments
            recList?.apply {
                mOnAddRecommendLinsener?.onAddRecommendData(dataList)
            }


            if (fragments != null && fragments.size > 0) {
                for (fr in fragments) {
                    transaction.remove(fr)
                }
            }
            if (this.batchInfo.liveStatus == "1") {//未开始
                fragment = LiveSubscribeFragment.startIntent(batchId!!, postion, mType!!)
            } else if (this.batchInfo.liveStatus == "2") {//直播中

                val mStartAudienceInfo = StartAudienceInfo(this.liveRoomInfo.roomId, this.liveRoomInfo.pullUrl, true, this.batchInfo.liveBatchId, 0, userVisibleHint, postion, true, mType)
                fragment = LiveRoomFragment.startAudience(mStartAudienceInfo)

            } else if (this.batchInfo.liveStatus == "3") {//结束  回放
                if (TextUtils.isEmpty(this.batchInfo.replayUrl)) {
                    setLiveEnd(true)
                } else {
                    val mStartAudienceInfo = StartAudienceInfo(this.liveRoomInfo.roomId, this.batchInfo.replayUrl!!, true, this.batchInfo.liveBatchId, 0, userVisibleHint, postion, false, mType)
                    fragment = LiveRoomFragment.startAudience(mStartAudienceInfo)
                    // fragment = LivePlaybackFragment.getInstance(this.liveRoomInfo.roomId, this.batchInfo.liveBatchId, this.batchInfo.replayUrl, userVisibleHint, false, postion)

                }

            }
            fragment?.apply {
                if (this is OnPlayLinsenr) {
                    mOnPlayLinsenr = this
                }
            }

            fragment?.apply {
                transaction.add(R.id.rl_content, this).commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment?.apply {
            fragmentManager?.beginTransaction()?.remove(this)
        }
        ObserverManger.getInstance(ObserverKey.LIVE_Hidden).removeObserver(liveHindObeser)
    }

    /**
     * 去判断是否可以播放
     */
    fun startLivePlay() {

        mOnGetLivePalyStatusLinsener?.apply {

            getLiveStatusManger().apply {
                //主要靠这里来判断，其他地方只是触发
                isVisibleToUser = currPageIsShow(postion)
                mOnPlayLinsenr?.apply {
                    val states = mOnPlayLinsenr?.onBasePlayStates()
                    if (isVisibleToUser) {//如果处于可见
                        if (states == PLAY_STATE.PLAYT_STOP) {//
                            this.onBaseStart()
                        }
                    } else {
                        if (states == PLAY_STATE.PLAYT) {//
                            this.onBaseStop()
                        }
                    }
                    onSetPlayStatus(isVisibleToUser)
                }
            }
        }


    }


    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int {
        return R.layout.fragment_live_palyt_base_layout
    }

    override fun initView() {
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
    }

    override fun initData() {
        mType = arguments!!.getString(LiveConstant.LIVE_TYPE)
        batchId = arguments!!.getString(LiveConstant.LIVE_BIACTH)
        postion = arguments!!.getInt(LiveConstant.LIVE_CURR_INDEX)
        showLoadData()
        mViewModel.getLivePlayInfo(batchId!!, mType!!)


    }

    override fun onBackPressedLinsener(): Boolean {
        if (isShowLiveEnd) {
            activity?.finish()
        }
        return true
    }
}