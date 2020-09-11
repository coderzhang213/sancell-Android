package cn.sancell.xingqiu.Repository

import cn.sancell.xingqiu.bean.FirenedBaseInfo
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean
import cn.sancell.xingqiu.usermember.bean.UserMemberRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

/**
 * Created by zj on 2020/1/2.
 */
class UserRepository : BaseRepository() {
    suspend fun getInviteFriendsListData(par: Map<String, String>): ResResponse<InviteFriendsListBean> {
        return mServe.getInviteFriendsListData(par)
    }
}