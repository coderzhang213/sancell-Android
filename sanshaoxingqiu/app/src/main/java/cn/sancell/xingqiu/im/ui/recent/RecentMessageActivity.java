package cn.sancell.xingqiu.im.ui.recent;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.TeamMemberAitHelper;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.util.StatusBarUtil;

import static com.netease.nim.uikit.business.recent.RecentContactsFragment.RECENT_TAG_STICKY;

/**
  * @author Alan_Xiong
  *
  * @desc: 最近消息
  * @time 2019-11-15 17:09
  */
public class RecentMessageActivity extends BaseMVPToobarActivity<RecentMsgPresenter> implements RecentMsgView {

    RecyclerView recyclerView;
    TextView emptyBg;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObserver userInfoObserver;

    private Handler handler = new Handler();

    public static void start(Context context){
        Intent intent = new Intent(context,RecentMessageActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected RecentMsgPresenter createPresenter() {
        return new RecentMsgPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.nim_recent_contacts;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("消息");
        findView();
        initMessageList();
        requestMessages(true);
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    public void findView(){
        recyclerView = findViewById(R.id.recycler_view);
        emptyBg = findViewById(R.id.emptyBg);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);
        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);
        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(touchListener);
        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {

            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }
    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {

            @Override
            public void onRecentContactsLoaded() {
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.SUPER_TEAM) {
                    ToastHelper.showToast(RecentMessageActivity.this, getString(com.netease.nim.uikit.R.string.super_team_impl_by_self));
                } else if (recent.getSessionType() == SessionTypeEnum.Team) { //群组内聊天
                  //  NimUIKit.startTeamSession(RecentMessageActivity.this, recent.getContactId());
                    SessionHelper.startTeamSession(RecentMessageActivity.this,recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) { //好友聊天
                    NimUIKit.startP2PSession(RecentMessageActivity.this, recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }
    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {

        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {
        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {
        }
    };

    OnlineStateChangeObserver onlineStateChangeObserver = accounts -> notifyDataSetChanged();

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver,
                register);
    }

    private void showLongClickMenu(final RecentContact recent, final int position) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(RecentMessageActivity.this);
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        String title = getString(com.netease.nim.uikit.R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, () -> {
            // 删除会话，删除后，消息历史被一起删除
            NIMClient.getService(MsgService.class).deleteRecentContact(recent);
            NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
            adapter.remove(position);
            handler.postDelayed(() -> {
                if (isFinishing()){
                    return;
                }
                refreshMessages(true);

            },0);
        });
        title = (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY) ? getString(
                com.netease.nim.uikit.R.string.main_msg_list_clear_sticky_on_top) : getString(com.netease.nim.uikit.R.string.main_msg_list_sticky_on_top));
        alertDialog.addItem(title, () -> {
            if (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY)) {
                CommonUtil.removeTag(recent, RECENT_TAG_STICKY);
            } else {
                CommonUtil.addTag(recent, RECENT_TAG_STICKY);
            }
            NIMClient.getService(MsgService.class).updateRecent(recent);
            refreshMessages(false);
        });
        String itemText = getString(com.netease.nim.uikit.R.string.delete_chat_only_server);
        alertDialog.addItem(itemText, () -> NIMClient.getService(MsgService.class)
                .deleteRoamingRecentContact(recent.getContactId(),
                        recent.getSessionType())
                .setCallback(new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        ToastHelper.showToast(RecentMessageActivity.this, "delete success");
                    }

                    @Override
                    public void onFailed(int code) {
                        ToastHelper.showToast(RecentMessageActivity.this,
                                "delete failed, code:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }
                }));
        alertDialog.show();
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        handler.postDelayed(() -> {
            if (msgLoaded) {
                return;
            }
            // 查询最近联系人列表数据
            NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(
                    new RequestCallbackWrapper<List<RecentContact>>() {

                        @Override
                        public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                return;
                            }
                            loadedRecents = recents;
                            // 初次加载，更新离线的消息中是否有@我的消息
                            for (RecentContact loadedRecent : loadedRecents) {
                                if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                    updateOfflineContactAited(loadedRecent);
                                }
                            }
                            // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                            //
                            msgLoaded = true;
                            if (!isFinishing()) {
                                onRecentContactsLoaded();
                            }
                        }
                    });
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);
        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();
        if (unreadChanged) {
            // 方式一：累加每个最近联系人的未读（快）
            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }
            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }
            Badger.updateBadgerCount(unreadNum);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = (o1, o2) -> {
        // 先比较置顶tag
        long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
        if (sticky != 0) {
            return sticky > 0 ? -1 : 1;
        } else {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time > 0 ? -1 : 1);
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver,
                register);
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }
                return;
            }
            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId()) && r.getSessionType() == (items.get(i)
                        .getSessionType())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                items.remove(index);
            }
            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }
        cacheMessages.clear();
        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {

        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }
                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();
                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {

        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {

        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId()) &&
                            item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {
        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        RecentMessageActivity.this.runOnUiThread(() -> adapter.notifyItemChanged(index));
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {

                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {

        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team ||
                recentContact.getUnreadCount() <= 0) {
            return;
        }
        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());
        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);
        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);
        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

                    @Override
                    public void onResult(int code, List<IMMessage> result, Throwable exception) {
                        if (code == ResponseCode.RES_SUCCESS && result != null) {
                            result.add(0, anchor);
                            Set<IMMessage> messages = null;
                            // 过滤存在的@我的消息
                            for (IMMessage msg : result) {
                                if (TeamMemberAitHelper.isAitMessage(msg)) {
                                    if (messages == null) {
                                        messages = new HashSet<>();
                                    }
                                    messages.add(msg);
                                }
                            }
                            // 更新并展示
                            if (messages != null) {
                                TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

    }

}
