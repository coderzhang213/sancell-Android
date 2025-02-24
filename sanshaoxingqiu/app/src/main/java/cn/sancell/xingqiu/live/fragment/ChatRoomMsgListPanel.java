package cn.sancell.xingqiu.live.fragment;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.listview.AutoRefreshListView;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.impl.preference.UserPreferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.sancell.xingqiu.live.list.MsgAdapter;
import cn.sancell.xingqiu.live.utils.JsonUtils;
import cn.sancell.xingqiu.live.viewholder.ChatRoomMsgViewHolderFactory;
import cn.sancell.xingqiu.live.widget.extension.CoustInfoBase;
import cn.sancell.xingqiu.live.widget.extension.GiftAttachment;
import cn.sancell.xingqiu.live.widget.extension.LikeAttachment;
import cn.sancell.xingqiu.util.LiveCoumsUtils;

/**
 * 聊天室消息收发模块
 * Created by huangjun on 2016/1/27.
 */
public class ChatRoomMsgListPanel implements TAdapterDelegate {
    private static final int MESSAGE_CAPACITY = 500;

    // container
    private Container container;
    private View rootView;
    private Handler uiHandler;

    // message list view
    private MessageListView messageListView;
    private LinkedList<IMMessage> items;
    private MsgAdapter adapter;

    ExtraDelegate extraDelegate;

    public interface ExtraDelegate {
        void onReceivedCustomAttachment(ChatRoomMessage msg);

        void onMessageClick(IMMessage imMessage);
    }

    public void setExtraDelegate(ExtraDelegate extraDelegate) {
        this.extraDelegate = extraDelegate;
    }

    public ChatRoomMsgListPanel(Container container, View rootView) {
        this.container = container;
        this.rootView = rootView;

        init();
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);
        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
        return false;
    }

    private void init() {
        initListView();
        this.uiHandler = new Handler();
        registerObservers(true);
    }

    private void initListView() {
        items = new LinkedList<>();
        adapter = new MsgAdapter(container.activity, items, this);
        adapter.setEventListener(new MsgItemEventListener());

        messageListView = rootView.findViewById(com.netease.nim.uikit.R.id.messageListView);
        messageListView.requestDisallowInterceptTouchEvent(true);

        messageListView.setMode(AutoRefreshListView.Mode.START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        messageListView.setAdapter(adapter);

        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                container.proxy.shouldCollapseInputPanel();
            }
        });
        //fixme 取消注释后可收取历史消息
        //messageListView.setOnRefreshListener(new MessageLoader());
    }

    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }, 200);
    }

    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
        boolean needRefresh = false;
        List<ChatRoomMessage> addedListItems = new ArrayList<>(messages.size());
        for (ChatRoomMessage message : messages) {
            if (message == null) {
                break;
            }
            if (message.getMsgType() == MsgTypeEnum.custom) {//我们自己的定义消息
                Log.i("room_custom_msg", "msg:" + JSON.toJSONString(message));
                if (message.getRemoteExtension() == null || !isMyMessage(message)) {//不是同一聊天室
                    break;
                }
                CoustInfoBase mCoustInfoBase = JsonUtils.getObject(JSON.toJSONString(message.getRemoteExtension()), CoustInfoBase.class);
                message.setAttachment(LiveCoumsUtils.INSTANCE.jsonToType(mCoustInfoBase));
                if (extraDelegate != null) {
                    extraDelegate.onReceivedCustomAttachment(message);
                }
            } else if ((message.getAttachment() instanceof LikeAttachment
                    || message.getAttachment() instanceof GiftAttachment)) {  //点赞 与 礼物 信息不显示在聊天列表
                if (extraDelegate != null) {
                    extraDelegate.onReceivedCustomAttachment(message);
                }
            } else if (isMyMessage(message)) {//保证显示到界面上的消息，来自同一个聊天室
                Log.i("room_text_msg", "msg:" + JSON.toJSONString(message));
                saveMessage(message, false);
                addedListItems.add(message);
                needRefresh = true;
            }
            //对通知类信息再进行一次人员列表变更处理
            if (message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                if (extraDelegate != null) {
                    extraDelegate.onReceivedCustomAttachment(message);
                }
            }
        }
        if (needRefresh) {
            adapter.notifyDataSetChanged();
        }

        // incoming messages tip
        ChatRoomMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg) && needScrollToBottom) {
            ListViewUtil.scrollToBottom(messageListView);
        }
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        // add to listView and refresh
        saveMessage(message, false);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);

        adapter.notifyDataSetChanged();
        //  ListViewUtil.scrollToPosition(messageListView, 0, 0);
        ListViewUtil.scrollToBottom(messageListView);
    }

    private void saveMessage(final List<IMMessage> messageList, boolean addFirst) {
        if (messageList == null || messageList.isEmpty()) {
            return;
        }

        for (IMMessage msg : messageList) {
            saveMessage(msg, addFirst);
        }
    }

    public void saveMessage(final IMMessage message, boolean addFirst) {
        if (message == null) {
            return;
        }

        if (items.size() >= MESSAGE_CAPACITY) {
            items.poll();
        }

        if (addFirst) {
            items.add(0, message);
        } else {
            items.add(message);
        }
    }

    /**
     * *************** implements TAdapterDelegate ***************
     */
    @Override
    public int getViewTypeCount() {
        return ChatRoomMsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return ChatRoomMsgViewHolderFactory.getViewHolderByType(items.get(position));
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }


    /**
     * *************** MessageLoader ***************
     */
    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {

        private static final int LOAD_MESSAGE_COUNT = 10;

        private IMMessage anchor;

        private boolean firstLoad = true;

        public MessageLoader() {
            anchor = null;
            loadFromLocal();
        }

        private RequestCallback<List<ChatRoomMessage>> callback = new RequestCallbackWrapper<List<ChatRoomMessage>>() {
            @Override
            public void onResult(int code, List<ChatRoomMessage> messages, Throwable exception) {
                if (messages != null) {
                    onMessageLoaded(messages);
                } else {
                    messageListView.onRefreshComplete(LOAD_MESSAGE_COUNT, LOAD_MESSAGE_COUNT, false);
                }
            }
        };

        private void loadFromLocal() {
            messageListView.onRefreshStart(AutoRefreshListView.Mode.START);
            NIMClient.getService(ChatRoomService.class).pullMessageHistory(container.account, anchor().getTime(), LOAD_MESSAGE_COUNT)
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return (anchor == null ? ChatRoomMessageBuilder.createEmptyChatRoomMessage(container.account, 0) : anchor);
            } else {
                return items.get(0);
            }
        }

        /**
         * 历史消息加载处理
         */
        private void onMessageLoaded(List<ChatRoomMessage> messages) {
            int count = messages.size();

            if (items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }

            List<IMMessage> result = new ArrayList<>();
            for (IMMessage message : messages) {
                result.add(message);
            }
            saveMessage(result, false);

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                ListViewUtil.scrollToBottom(messageListView);
            }

            refreshMessageList();
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);

            firstLoad = false;
        }

        /**
         * *************** OnRefreshListener ***************
         */
        @Override
        public void onRefreshFromStart() {
            loadFromLocal();
        }

        @Override
        public void onRefreshFromEnd() {
        }
    }


    /**
     * ************************* 观察者 ********************************
     */
    private void registerObservers(boolean register) {
        ChatRoomServiceObserver service = NIMClient.getService(ChatRoomServiceObserver.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    Observer<ChatRoomMessage> messageStatusObserver = new Observer<ChatRoomMessage>() {
        @Override
        public void onEvent(ChatRoomMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };

    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    public boolean isMyMessage(ChatRoomMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {

        @Override
        public void onFailedBtnClick(IMMessage message) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                if (message.getStatus() == MsgStatusEnum.fail) {
                    resendMessage(message); // 重发
                } else {
                    if (message.getAttachment() instanceof FileAttachment) {
                        FileAttachment attachment = (FileAttachment) message.getAttachment();
                        if (TextUtils.isEmpty(attachment.getPath())
                                && TextUtils.isEmpty(attachment.getThumbPath())) {
                            showReDownloadConfirmDlg(message);
                        }
                    } else {
                        resendMessage(message);
                    }
                }
            } else {
                showReDownloadConfirmDlg(message);
            }
        }

        @Override
        public void onMessageClick(IMMessage message) {
            extraDelegate.onMessageClick(message);
        }

//        @Override
//        public void onFooterClick(IMMessage message) {
//            extraDelegate.onMessageClick(message);
//
//        }


        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {
            return true;
        }

        // 重新下载(对话框提示)
        private void showReDownloadConfirmDlg(final IMMessage message) {
            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {

                @Override
                public void doCancelAction() {
                }

                @Override
                public void doOkAction() {
                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
                        NIMClient.getService(ChatRoomService.class).downloadAttachment((ChatRoomMessage) message, true);
                }
            };

            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
                    container.activity.getString(com.netease.nim.uikit.R.string.repeat_download_message), true, listener);
            dialog.show();
        }

        // 重发消息到服务器
        private void resendMessage(IMMessage message) {
            // 重置状态为unsent
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                IMMessage item = items.get(index);
                item.setStatus(MsgStatusEnum.sending);
                refreshViewHolderByIndex(index);
            }

            NIMClient.getService(ChatRoomService.class).sendMessage((ChatRoomMessage) message, true);
        }

    }

    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }
}
