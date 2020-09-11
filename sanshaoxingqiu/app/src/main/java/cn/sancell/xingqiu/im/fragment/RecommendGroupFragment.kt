package cn.sancell.xingqiu.im.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean
import cn.sancell.xingqiu.homecommunity.live.RemmendGroupAdapterr
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm
import cn.sancell.xingqiu.im.ui.red.call.ScClient
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.viewmodel.ChatGroupViewModel
import com.netease.nimlib.sdk.RequestCallback
import kotlinx.android.synthetic.main.fragment_chat_group_rem_layout.*
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener as TeamApplyListener1

/**
 * Created by zj on 2019/12/25.
 */
class RecommendGroupFragment : BaseNotDataFragmentKt<ChatGroupViewModel>() {
    var mRemmendGroupAdapterr: RemmendGroupAdapterr? = null
    private val mAllList = ArrayList<RecommendGroupListBean.RecommGroupBean>()
    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<ChatGroupViewModel>? {
        return ChatGroupViewModel::class.java
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.fragment_chat_group_rem_layout

    override fun initView() {
    }

    override fun onResume() {
        super.onResume()
        getDataInfo()
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mRemGroup.observe(this@RecommendGroupFragment, Observer {
                it?.apply {
                    mAllList.clear()
                    mAllList.addAll(it.dataList)
                    if (mRemmendGroupAdapterr == null) {
                        val mRemmendGroupAdapterr = RemmendGroupAdapterr(mAllList)
                        recycler_view.layoutManager = LinearLayoutManager(context)
                        recycler_view.adapter = mRemmendGroupAdapterr
                        mRemmendGroupAdapterr.setOnItemChildClickListener { adapter, view, position ->
                            val accId = PreferencesUtils.getString(Constants.Key.key_im_accid, "")
                            val mInfo = mAllList.get(position)
                            if (mInfo.inGroup == 0) {//加入群
                                ScClient.applyJoinWithVer(context, mInfo.tid, accId,
                                        object : TeamApplyListener1 {
                                            override fun onSuccess(p0: Any?) {
                                                mAllList.get(position).inGroup = 1
                                                mRemmendGroupAdapterr.notifyItemChanged(position)
                                            }

                                            override fun showInputDialog() {
                                                val dialogFgm = ApplyJoinTeamInputDialogFgm.newInstance()
                                                dialogFgm.setOnApplyListener {
                                                    ScClient.applyJoinTeam(context, mInfo.tid, accId, "", object : RequestCallback<Any> {
                                                        override fun onSuccess(p0: Any?) {
                                                            mAllList.get(position).inGroup = 1
                                                            mRemmendGroupAdapterr.notifyItemChanged(position)
                                                        }

                                                        override fun onException(p0: Throwable?) {
                                                        }

                                                        override fun onFailed(p0: Int) {
                                                        }

                                                    })
                                                }
                                                dialogFgm.show(fragmentManager!!, "apply")
                                            }

                                            override fun onException(p0: Throwable?) {
                                            }

                                            override fun onFailed(p0: Int) {
                                            }
                                        }
                                )
                            } else {//参与群聊
                                ScClient.enterTeamChat(activity, mInfo.tid)

                            }

                        }

                    } else {
                        mRemmendGroupAdapterr?.setNewData(it.dataList)
                    }


                }


            })

        }

    }

    fun getDataInfo() {
        mViewModel.getCommunityRecommGroupListData()

    }

    override fun initData() {
    }
}