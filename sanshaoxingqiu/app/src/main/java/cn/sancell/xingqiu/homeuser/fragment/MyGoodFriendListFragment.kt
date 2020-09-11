package cn.sancell.xingqiu.homeuser.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.constant.VipManager
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homeuser.InviteFriendActivity
import cn.sancell.xingqiu.homeuser.adapter.UserInviteFriendsListAdapter
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.login.bean.UserBean
import cn.sancell.xingqiu.usermember.UserInviteActivity
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_my_good_fr_layout.*

/**
 * Created by zj on 2020/1/2.
 */
class MyGoodFriendListFragment : BaseNotDataFragmentKt<UserViewModel>() {
    var page: Int = 1
    var mUserInviteFriendsListAdapter: UserInviteFriendsListAdapter? = null
    val mAllList: MutableList<InviteFriendsListBean.MyInviteInfo>? = ArrayList()
    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<UserViewModel>? = UserViewModel::class.java
    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_my_good_fr_layout

    override fun initView() {
        setTitleName("我的粉丝")
        refreshLayout.setOnLoadMoreListener {
            refreshLayout.resetNoMoreData()
            page++
            getData()
        }
        refreshLayout.setOnRefreshListener {

            page = 1
            getData()
        }
    }

    override fun initData() {
        showLoadData()
        getData()
    }

    fun getData() {
        mViewModel.getInviteFriendsListData(page)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mFirenedBaseInfoList.observe(this@MyGoodFriendListFragment, Observer {
                hindLoadStatus()
                bindClaimInfo(it)
                bindClaimList(it)

            })
        }
    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
    }

    /**
     * 设置邀请列表
     */
    fun bindClaimList(mInfo: InviteFriendsListBean) {
        val mList = mInfo.myInviteInfo

        if (mList == null || mList.size <= 0) {
            if (page == 1) {
                tv_to_yq.setOnClickListener {
                    //去邀请
                    startActivity(Intent(context, InviteFriendActivity::class.java))
                }
                ll_not_yq_data.visibility = View.VISIBLE
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
            return
        }

        if (page == 1) {
            mAllList?.clear()
            invite_sum.setText(mInfo.inviteCount.toString())
            ll_not_yq_data.visibility = View.GONE
        }
        mAllList?.addAll(mList)
        if (mUserInviteFriendsListAdapter == null) {
            mUserInviteFriendsListAdapter = UserInviteFriendsListAdapter(mAllList)
            rl_yq_list.layoutManager = LinearLayoutManager(context)
            rl_yq_list.adapter = mUserInviteFriendsListAdapter
        } else {
            mUserInviteFriendsListAdapter?.setNewData(mAllList)
        }
    }


    /**
     * 设置被邀请人信息
     */
    fun bindClaimInfo(mInfo: InviteFriendsListBean) {
        if (page != 1) {
            return
        }
        val mInInfo = mInfo.inviteFromInfo
        if (mInInfo != null && !TextUtils.isEmpty(mInInfo.userId)) {
            rl_inviter_info.visibility = View.VISIBLE
            ll_to_yq.visibility = View.GONE
            tv_inviter_id.setText("ID:" + mInInfo.userId)
            tv_inviter_name.setText(mInInfo.nickName)
//            if (mInInfo.memberLevel >= VipManager.VIP_LEVE) {//如果是会员
//                iv_inviter_level.visibility = View.VISIBLE
//            }
            if(mInInfo.memberLevel>1){
                iv_inviter_level.visibility=View.VISIBLE
                iv_inviter_level.text=mInInfo.realMemberLevelStr
            }else{
                iv_inviter_level.visibility=View.GONE
            }


            ImageLoaderUtils.loadCircleImage(context, mInInfo.gravatar, riv_inviter_photo)

        } else {
            if (mInfo.isBindUser.equals("1")) {
                rl_inviter_info.visibility = View.GONE
                tv_al.setText(mInfo.remark)
                ll_to_yq.visibility = View.VISIBLE
                //绑定邀请人
                tv_bind_yq.setOnClickListener {
                    startActivity(Intent(context, UserInviteActivity::class.java))
                }
            } else {
                rl_head.visibility = View.GONE
            }
        }

    }
}