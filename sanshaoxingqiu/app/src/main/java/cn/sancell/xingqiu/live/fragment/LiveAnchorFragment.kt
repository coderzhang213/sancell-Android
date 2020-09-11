package cn.sancell.xingqiu.live.fragment

import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveLoginInfo
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.activity.AnchorHomeActivity
import cn.sancell.xingqiu.live.nim.PublishParam
import cn.sancell.xingqiu.viewmodel.LiveViewModel

class LiveAnchorFragment : BaseNotDataFragmentKt<LiveViewModel>() {
    protected var mLiveLoginInfo: LiveLoginInfo? = null
    override val isLoadNotDat: Boolean
        get() = true

    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            liveLogin.observe(this@LiveAnchorFragment, Observer {
                hideLoadData()
                mLiveLoginInfo = it
                //  mViewModel.upRoomStatus("accid_test_173476", "2")

//                NIMClient.getService(AuthService::class.java).login(LoginInfo("accid_test_173476", RSAUtils.encryptByPublic("15921444119"))).setCallback(object : RequestCallback<Any> {
//                    override fun onSuccess(o: Any) {
//                        Log.i("keey", "onSuccess")
//                    }
//
//                    override fun onFailed(i: Int) {
//                        Log.i("keey", "onFailed")
//                    }
//
//                    override fun onException(throwable: Throwable) {
//                        Log.i("keey", "onException")
//                    }
//                })
            })
            errMsg.observe(this@LiveAnchorFragment, Observer {
                hideLoadData()
                setNotData()

            })
            mException.observe(this@LiveAnchorFragment, Observer {
                hideLoadData()
                showNewWorkError()
            })
            liveUpRoom.observe(this@LiveAnchorFragment, Observer {
                mLiveLoginInfo?.also {

                    val publishParam = PublishParam()
                    publishParam.pushUrl = it.pushUrl
                    publishParam.definition = PublishParam.HD
                    AnchorHomeActivity.startLive(context!!, it.roomId, "", publishParam,true)
                }


            })
        }
    }

    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_live_an_layout

    override fun initView() {
    }

    override fun initData() {
        showLoadData()
        mViewModel.liveLogin("accid_test_173476", "15921444119")
    }
}