package cn.sancell.xingqiu.im.fragment

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.im.sys.SessionHelper
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.util.RxTimerUtil
import cn.sancell.xingqiu.viewmodel.ChatGroupViewModel
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver
import com.netease.nim.uikit.api.model.user.UserInfoObserver
import com.netease.nim.uikit.business.recent.RecentContactsCallback
import com.netease.nim.uikit.business.recent.RecentContactsFragment
import com.netease.nim.uikit.business.recent.TeamMemberAitHelper
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter
import com.netease.nim.uikit.business.uinfo.UserInfoHelper
import com.netease.nim.uikit.common.CommonUtil
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.badger.Badger
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog
import com.netease.nim.uikit.common.ui.drop.DropCover.IDropCompletedListener
import com.netease.nim.uikit.common.ui.drop.DropManager
import com.netease.nim.uikit.common.ui.drop.DropManager.IDropListener
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener
import com.netease.nim.uikit.impl.NimUIKitImpl
import com.netease.nimlib.sdk.*
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.team.model.Team
import com.netease.nimlib.sdk.team.model.TeamMember
import kotlinx.android.synthetic.main.fragment_chat_group_msg_layout.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by zj on 2019/12/25.
 */
class ChatGroupMsgFragment : BaseNotDataFragmentKt<ChatGroupViewModel>() {
    private var userInfoObserver: UserInfoObserver? = null

    private var callback: RecentContactsCallback? = null
    // data
    private val items = ArrayList<RecentContact>()
    private var adapter: RecentContactAdapter? = null
    private var msgLoaded = false
    // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）
    private val cached = HashMap<String, RecentContact>(3)

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private val cacheMessages = HashMap<String, MutableSet<IMMessage?>?>()
    private val mRxTimerUtil = RxTimerUtil()
    private var loadedRecents: MutableList<RecentContact>? = null
    override fun onReloadData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.fragment_chat_group_msg_layout

    override fun initView() {
    }

    override fun initData() {
        initMessageList()
        requestMessages(true)
        registerObservers(true)
        registerDropCompletedListener(true)
        registerOnlineStateChangeListener(true)
    }

    private fun registerOnlineStateChangeListener(register: Boolean) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver,
                register)
    }

    var onlineStateChangeObserver = OnlineStateChangeObserver { accounts: Set<String?>? -> notifyDataSetChanged() }
    private fun requestMessages(delay: Boolean) {
        if (msgLoaded) {
            return
        }
        mRxTimerUtil.timer(250, object : RxTimerUtil.IRxNext {
            override fun doNext(number: Long) {
                if (msgLoaded) {
                    return
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService::class.java).queryRecentContacts().setCallback(
                        object : RequestCallback<MutableList<RecentContact>> {
                            override fun onSuccess(recents: MutableList<RecentContact>?) {
//                                if (code != ResponseCode.RES_SUCCESS.toInt()) {
//                                    return
//                                }
                                loadedRecents = recents
                                // 初次加载，更新离线的消息中是否有@我的消息
                                for (loadedRecent in loadedRecents!!) {
                                    if (loadedRecent.sessionType == SessionTypeEnum.Team) {
                                        updateOfflineContactAited(loadedRecent)
                                    }
                                }
                                // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
//
                                msgLoaded = true
                                if (!activity!!.isFinishing()) {
                                    onRecentContactsLoaded()
                                }
                            }

                            override fun onException(p0: Throwable?) {
                            }

                            override fun onFailed(p0: Int) {
                            }
                        })
            }
        })
    }

    private fun onRecentContactsLoaded() {
        items.clear()
        if (loadedRecents != null) {
            items.addAll(loadedRecents!!)
            loadedRecents = null
        }
        refreshMessages(true)
        if (callback != null) {
            callback!!.onRecentContactsLoaded()
        }
    }

    private fun updateOfflineContactAited(recentContact: RecentContact?) {
        if (recentContact == null || recentContact.sessionType != SessionTypeEnum.Team || recentContact.unreadCount <= 0) {
            return
        }
        // 锚点
        val uuid: MutableList<String> = java.util.ArrayList(1)
        uuid.add(recentContact.recentMessageId)
        val messages = NIMClient.getService(MsgService::class.java).queryMessageListByUuidBlock(uuid)
        if (messages == null || messages.size < 1) {
            return
        }
        val anchor = messages[0]
        // 查未读消息
        // 查未读消息
        // 查未读消息
        NIMClient.getService(MsgService::class.java).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.unreadCount - 1, false)
                .setCallback(object : RequestCallbackWrapper<MutableList<IMMessage?>?>() {
                    override fun onResult(code: Int, result: MutableList<IMMessage?>?, exception: Throwable) {
                        if (code == ResponseCode.RES_SUCCESS.toInt() && result != null) {
                            result.add(0, anchor)
                            var messages: MutableSet<IMMessage?>? = null
                            // 过滤存在的@我的消息
                            for (msg in result) {
                                if (TeamMemberAitHelper.isAitMessage(msg)) {
                                    if (messages == null) {
                                        messages = HashSet()
                                    }
                                    messages.add(msg)
                                }
                            }
                            // 更新并展示
                            if (messages != null) {
                                TeamMemberAitHelper.setRecentContactAited(recentContact, messages)
                                notifyDataSetChanged()
                            }
                        }
                    }
                })
    }

    /**
     * 初始化消息列表
     */
    private fun initMessageList() {
        // adapter
        // adapter
        adapter = RecentContactAdapter(recycler_view, items)
        initCallBack()
        adapter!!.setCallback(callback)
        // recyclerView
        // recyclerView
        recycler_view.setAdapter(adapter)
        recycler_view.setLayoutManager(LinearLayoutManager(context))
        recycler_view.addOnItemTouchListener(touchListener)
        // drop listener
        // drop listener
        DropManager.getInstance().setDropListener(object : IDropListener {
            override fun onDropBegin() {
                touchListener.setShouldDetectGesture(false)
            }

            override fun onDropEnd() {
                touchListener.setShouldDetectGesture(true)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mRxTimerUtil.cancel()
    }

    private fun showLongClickMenu(recent: RecentContact, position: Int) {
        val alertDialog = CustomAlertDialog(context)
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.contactId, recent.sessionType))
        var title = getString(com.netease.nim.uikit.R.string.main_msg_list_delete_chatting)
        alertDialog.addItem(title) {
            // 删除会话，删除后，消息历史被一起删除
            NIMClient.getService(MsgService::class.java).deleteRecentContact(recent)
            NIMClient.getService(MsgService::class.java).clearChattingHistory(recent.contactId, recent.sessionType)
            adapter!!.remove(position)
            mRxTimerUtil.timer(100, object : RxTimerUtil.IRxNext {
                override fun doNext(number: Long) {
                    if (!activity!!.isFinishing) {
                        refreshMessages(true)
                    }
                }
            })
        }
        title = if (CommonUtil.isTagSet(recent, RecentContactsFragment.RECENT_TAG_STICKY)) getString(
                com.netease.nim.uikit.R.string.main_msg_list_clear_sticky_on_top) else getString(com.netease.nim.uikit.R.string.main_msg_list_sticky_on_top)
        alertDialog.addItem(title) {
            if (CommonUtil.isTagSet(recent, RecentContactsFragment.RECENT_TAG_STICKY)) {
                CommonUtil.removeTag(recent, RecentContactsFragment.RECENT_TAG_STICKY)
            } else {
                CommonUtil.addTag(recent, RecentContactsFragment.RECENT_TAG_STICKY)
            }
            NIMClient.getService(MsgService::class.java).updateRecent(recent)
            refreshMessages(false)
        }
        val itemText = getString(com.netease.nim.uikit.R.string.delete_chat_only_server)
        alertDialog.addItem(itemText) {
            NIMClient.getService(MsgService::class.java)
                    .deleteRoamingRecentContact(recent.contactId,
                            recent.sessionType)
                    .setCallback(object : RequestCallback<Void?> {
                        override fun onSuccess(param: Void?) {
                            ToastHelper.showToast(context, "delete success")
                        }

                        override fun onFailed(code: Int) {
                            ToastHelper.showToast(context,
                                    "delete failed, code:$code")
                        }

                        override fun onException(exception: Throwable) {}
                    })
        }
        alertDialog.show()
    }

    private val touchListener: SimpleClickListener<RecentContactAdapter> = object : SimpleClickListener<RecentContactAdapter>() {
        override fun onItemClick(adapter: RecentContactAdapter, view: View, position: Int) {
            if (callback != null) {
                val recent = adapter.getItem(position)
                callback!!.onItemClick(recent)
            }
        }

        override fun onItemLongClick(adapter: RecentContactAdapter, view: View, position: Int) {
            showLongClickMenu(adapter.getItem(position), position)
        }

        override fun onItemChildClick(adapter: RecentContactAdapter, view: View, position: Int) {}
        override fun onItemChildLongClick(adapter: RecentContactAdapter, view: View, position: Int) {}
    }

    private fun initCallBack() {
        if (callback != null) {
            return
        }
        callback = object : RecentContactsCallback {
            override fun onRecentContactsLoaded() {}
            override fun onUnreadCountChange(unreadCount: Int) {}
            override fun onItemClick(recent: RecentContact) {
                if (recent.sessionType == SessionTypeEnum.SUPER_TEAM) {
                    ToastHelper.showToast(context, getString(com.netease.nim.uikit.R.string.super_team_impl_by_self))
                } else if (recent.sessionType == SessionTypeEnum.Team) { //群组内聊天
                    SessionHelper.startTeamSession(context, recent.contactId)
                } else if (recent.sessionType == SessionTypeEnum.P2P) { //好友聊天
                    NimUIKit.startP2PSession(context, recent.contactId)
                }
            }

            override fun getDigestOfAttachment(recentContact: RecentContact, attachment: MsgAttachment): String {
                return ""
            }

            override fun getDigestOfTipMsg(recent: RecentContact): String {
                return ""
            }
        }
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private fun registerObservers(register: Boolean) {
        val service = NIMClient.getService(MsgServiceObserve::class.java)
        service.observeReceiveMessage(messageReceiverObserver, register)
        service.observeRecentContact(messageObserver, register)
        service.observeMsgStatus(statusObserver, register)
        service.observeRecentContactDeleted(deleteObserver, register)
        registerTeamUpdateObserver(register)
        registerTeamMemberUpdateObserver(register)
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register)
        if (register) {
            registerUserInfoObserver()
        } else {
            unregisterUserInfoObserver()
        }
    }

    private fun registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = UserInfoObserver { refreshMessages(false) }
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true)
    }

    private fun unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false)
        }
    }

    var friendDataChangedObserver: ContactChangedObserver = object : ContactChangedObserver {
        override fun onAddedOrUpdatedFriends(accounts: List<String>) {
            refreshMessages(false)
        }

        override fun onDeletedFriends(accounts: List<String>) {
            refreshMessages(false)
        }

        override fun onAddUserToBlackList(account: List<String>) {
            refreshMessages(false)
        }

        override fun onRemoveUserFromBlackList(account: List<String>) {
            refreshMessages(false)
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private fun sortRecentContacts(list: List<RecentContact>) {
        if (list.size == 0) {
            return
        }
        //  Collections.sort(list, comp)
    }

//    private val comp = label@ Comparator { o1: RecentContact, o2: RecentContact ->
//        // 先比较置顶tag
//        val sticky = (o1.tag and RecentContactsFragment.RECENT_TAG_STICKY) - (o2.tag and RecentContactsFragment.RECENT_TAG_STICKY)
//        if (sticky != 0L) {
//            return@label if (sticky > 0) -1 else 1
//        } else {
//            val time = o1.time - o2.time
//            return@label if (time == 0L) 0 else if (time > 0) -1 else 1
//        }
//    }

    private fun refreshMessages(unreadChanged: Boolean) {
        sortRecentContacts(items)
        notifyDataSetChanged()
        if (unreadChanged) { // 方式一：累加每个最近联系人的未读（快）
            var unreadNum = 0
            for (r in items) {
                unreadNum += r.unreadCount
            }
            // 方式二：直接从SDK读取（相对慢）
//int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            if (callback != null) {
                callback!!.onUnreadCountChange(unreadNum)
            }
            Badger.updateBadgerCount(unreadNum)
        }
    }

    private fun notifyDataSetChanged() {
        adapter!!.notifyDataSetChanged()
        val empty = items.isEmpty() && msgLoaded
    }


    /**
     * 注册群信息&群成员更新监听
     */
    private fun registerTeamUpdateObserver(register: Boolean) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register)
    }

    private fun registerTeamMemberUpdateObserver(register: Boolean) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver,
                register)
    }

    var teamMemberDataChangedObserver: TeamMemberDataChangedObserver = object : TeamMemberDataChangedObserver {
        override fun onUpdateTeamMember(members: List<TeamMember>) {
            adapter!!.notifyDataSetChanged()
        }

        override fun onRemoveTeamMember(member: List<TeamMember>) {}
    }
    var teamDataChangedObserver: TeamDataChangedObserver = object : TeamDataChangedObserver {
        override fun onUpdateTeams(teams: List<Team>) {
            adapter!!.notifyDataSetChanged()
        }

        override fun onRemoveTeam(team: Team) {}
    }

    private fun registerDropCompletedListener(register: Boolean) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener)
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener)
        }
    }

    private var deleteObserver: Observer<RecentContact> = object : Observer<RecentContact> {

        override fun onEvent(recentContact: RecentContact?) {
            if (recentContact != null) {
                for (item in items) {
                    if (TextUtils.equals(item.contactId, recentContact.getContactId()) &&
                            item.sessionType == recentContact.getSessionType()) {
                        items.remove(item)
                        refreshMessages(true)
                        break
                    }
                }
            } else {
                items.clear()
                refreshMessages(true)
            }
        }
    }
    var statusObserver = Observer<IMMessage> { message ->
        val index: Int = getItemIndex(message.uuid)
        if (index >= 0 && index < items.size) {
            val item: RecentContact = items.get(index)
            item.msgStatus = message.status
            refreshViewHolderByIndex(index)
        }
    }

    var messageObserver = Observer<List<RecentContact>> { recentContacts ->
        if (!DropManager.getInstance().isTouchable) { // 正在拖拽红点，缓存数据
            for (r in recentContacts) {
                cached.put(r.contactId, r)
            }
            return@Observer
        }
        onRecentContactChanged(recentContacts)
    }
    //监听在线消息中是否有@我
    private val messageReceiverObserver: Observer<List<IMMessage>> = object : Observer<List<IMMessage>> {
        override fun onEvent(imMessages: List<IMMessage>) {
            for (imMessage in imMessages) {
                if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                    continue
                }
                var cacheMessageSet: MutableSet<IMMessage?>? = cacheMessages[imMessage.sessionId]
                if (cacheMessageSet == null) {
                    cacheMessageSet = HashSet()
                    cacheMessages[imMessage.sessionId] = cacheMessageSet
                }
                cacheMessageSet.add(imMessage)
            }
        }
    }
    var dropCompletedListener = IDropCompletedListener { id, explosive ->
        if (!cached.isEmpty()) { // 红点爆裂，已经要清除未读，不需要再刷cached
            if (explosive) {
                if (id is RecentContact) {
                    cached.remove(id.contactId)
                } else if (id is String && id.contentEquals("0")) {
                    cached.clear()
                }
            }
            // 刷cached
            if (!cached.isEmpty()) {
                val recentContacts: MutableList<RecentContact> = ArrayList<RecentContact>(cached.size)
                recentContacts.addAll(cached.values)
                cached.clear()
                onRecentContactChanged(recentContacts)
            }
        }
    }

    private fun getItemIndex(uuid: String): Int {
        for (i in items.indices) {
            val item = items[i]
            if (TextUtils.equals(item.recentMessageId, uuid)) {
                return i
            }
        }
        return -1
    }

    private fun onRecentContactChanged(recentContacts: List<RecentContact>) {
        var index: Int
        for (r in recentContacts) {
            index = -1
            for (i in items.indices) {
                if (r.contactId == items[i].contactId && r.sessionType == items[i]
                                .sessionType) {
                    index = i
                    break
                }
            }
            if (index >= 0) {
                items.removeAt(index)
            }
            items.add(r)
            if (r.sessionType == SessionTypeEnum.Team && cacheMessages.get(r.contactId) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.contactId))
            }
        }
        cacheMessages.clear()
        refreshMessages(true)
    }

    protected fun refreshViewHolderByIndex(index: Int) {
        activity!!.runOnUiThread(Runnable { adapter!!.notifyItemChanged(index) })
    }

}