package cn.sancell.xingqiu.im.sys;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.im.SessionTeamCustomization;
import cn.sancell.xingqiu.im.action.GoodsAction;
import cn.sancell.xingqiu.im.action.RedPacketAction;
import cn.sancell.xingqiu.im.extension.CustomAttachParser;
import cn.sancell.xingqiu.im.extension.CustomAttachment;
import cn.sancell.xingqiu.im.extension.GoodsAttachment;
import cn.sancell.xingqiu.im.extension.RedPacketAttachment;
import cn.sancell.xingqiu.im.extension.RedPacketOpenedAttachment;
import cn.sancell.xingqiu.im.extension.StickerAttachment;
import cn.sancell.xingqiu.im.ui.history.MessageHistoryActivity;
import cn.sancell.xingqiu.im.ui.history.SearchMessageActivity;
import cn.sancell.xingqiu.im.ui.user.UserProfileActivity;
import cn.sancell.xingqiu.im.viewholder.MsgGoodsViewHolder;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderDefCustom;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderFile;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderOpenRedPacket;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderRedPacket;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderSticker;
import cn.sancell.xingqiu.im.viewholder.MsgViewHolderTip;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getRecentCustomization;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {

    private static final int ACTION_HISTORY_QUERY = 0;

    private static final int ACTION_SEARCH_MESSAGE = 1;

    private static final int ACTION_CLEAR_MESSAGE = 2;

    private static final int ACTION_CLEAR_P2P_MESSAGE = 3;

    private static SessionCustomization normalTeamCustomization;

    private static SessionCustomization advancedTeamCustomization;

    public static String rpTeamId;

    private static NIMPopupMenu popupMenu;

    private static List<PopupMenuItem> menuItemList;

    public static final boolean USE_LOCAL_ANTISPAM = true;


    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();
        // 设置会话中点击事件响应处理
        setSessionListener();
        // 注册消息转发过滤器
        registerMsgForwardFilter();
        // 注册消息撤回过滤器
        registerMsgRevokeFilter();
        // 注册消息撤回监听器
        registerMsgRevokeObserver();
        NimUIKit.setCommonTeamSessionCustomization(getTeamCustomization(null));
        NimUIKit.setRecentCustomization(getRecentCustomization());
    }


    public static void startTeamSession(Context context, String tid) {
        SessionHelper.rpTeamId = tid;
        startTeamSession(context, tid, null);
    }

    public static void startTeamSession(Context context, String tid, IMMessage anchor) {
        NimUIKit.startTeamSession(context, tid, getTeamCustomization(tid), anchor);
    }

    // 打开群聊界面(用于 UIKIT 中部分界面跳转回到指定的页面)
    public static void startTeamSession(Context context, String tid, Class<? extends Activity> backToClass,
                                        IMMessage anchor) {
        NimUIKit.startChatting(context, tid, SessionTypeEnum.Team, getTeamCustomization(tid), backToClass, anchor);
    }


    private static boolean checkLocalAntiSpam(IMMessage message) {
        if (!USE_LOCAL_ANTISPAM) {
            return true;
        }
        LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(message.getContent(),
                "**");
        int operator = result == null ? 0 : result.getOperator();
        switch (operator) {
            case 1: // 替换，允许发送
                message.setContent(result.getContent());
                return true;
            case 2: // 拦截，不允许发送
                return false;
            case 3: // 允许发送，交给服务器
                message.setClientAntiSpam(true);
                return true;
            case 0:
            default:
                break;
        }
        return true;
    }

    /**
     * 群聊属性设置
     * @param tid
     * @return
     */
    private static SessionCustomization getTeamCustomization(String tid) {
        rpTeamId = tid;
        if (normalTeamCustomization == null) { //普通群
            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            //加入红包
            actions.add(new RedPacketAction(tid));
          //  actions.add(new FileAction());
            actions.add(new GoodsAction());
            SessionTeamCustomization.SessionTeamCustomListener listener = new SessionTeamCustomization.SessionTeamCustomListener() {

                @Override
                public void initPopupWindow(Context context, View view, String sessionId,
                                            SessionTypeEnum sessionTypeEnum) {
                    initPopuptWindow(context, view, sessionId, sessionTypeEnum);
                }

                @Override
                public void onSelectedAccountsResult(ArrayList<String> selectedAccounts) {

                }

                @Override
                public void onSelectedAccountFail() {

                }
            };
            normalTeamCustomization = new SessionTeamCustomization(listener) {

                @Override
                public boolean isAllowSendMessage(IMMessage message) {
                    return checkLocalAntiSpam(message);
                }
            };
            normalTeamCustomization.actions = actions;
        }
        if (advancedTeamCustomization == null) { //高级群
            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
//            actions.add(new FileAction());
            actions.add(new RedPacketAction(tid));
            actions.add(new GoodsAction());
            SessionTeamCustomization.SessionTeamCustomListener listener = new SessionTeamCustomization.SessionTeamCustomListener() {

                @Override
                public void initPopupWindow(Context context, View view, String sessionId,
                                            SessionTypeEnum sessionTypeEnum) {
                    initPopuptWindow(context, view, sessionId, sessionTypeEnum);
                }


                @Override
                public void onSelectedAccountsResult(ArrayList<String> selectedAccounts) {

                }

                @Override
                public void onSelectedAccountFail() {

                }
            };
            advancedTeamCustomization = new SessionTeamCustomization(listener) {

                @Override
                public boolean isAllowSendMessage(IMMessage message) {
                    return checkLocalAntiSpam(message);
                }
            };
            advancedTeamCustomization.actions = actions;
        }
        if (TextUtils.isEmpty(tid)) {
            return normalTeamCustomization;
        } else {
            Team team = TeamDataCache.getInstance().getTeamById(tid);
            if (team != null && team.getType() == TeamTypeEnum.Advanced) {
                return advancedTeamCustomization;
            }
        }
        return normalTeamCustomization;
    }

    /**
     * 注册默认的消息样式
     */
    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(FileAttachment.class, MsgViewHolderFile.class);
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);
        NimUIKit.registerCustomHolder(GoodsAttachment.class,MsgGoodsViewHolder.class);
        registerRedPacketViewHolder();
    }

    /**
     * 注册红包holder与样式
     */
    private static void registerRedPacketViewHolder() {
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);
        NimUIKit.registerMsgItemViewHolder(RedPacketOpenedAttachment.class, MsgViewHolderOpenRedPacket.class);
        NimUIKit.registerMsgItemViewHolder(GoodsAttachment.class, MsgGoodsViewHolder.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {

            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
                UserProfileActivity.start(context, message.getFromAccount());
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }

            @Override
            public void onAckMsgClicked(Context context, IMMessage message) {
                // 已读回执事件处理，用于群组的已读回执事件的响应，弹出消息已读详情
            }
        };
        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {

            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getMsgType() == MsgTypeEnum.custom && message.getAttachment() != null && message.getAttachment() instanceof RedPacketAttachment)
                {
                    // 白板消息和阅后即焚消息，红包消息 不允许转发
                    return true;
                }else if (message.getMsgType() == MsgTypeEnum.robot && message.getAttachment() != null &&
                        ((RobotAttachment) message.getAttachment()).isRobotSend()) {
                    return true; // 如果是机器人发送的消息 不支持转发
                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {

            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getAttachment() != null && message.getAttachment() instanceof RedPacketAttachment) {
                    // 视频通话消息和白板消息，红包消息 不允许撤回
                    return true;
                } else if (ImCache.getAccount().equals(message.getSessionId())) {
                    // 发给我的电脑 不允许撤回
                    return true;
                }
                return false;
            }
        });
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }


    private static void initPopuptWindow(Context context, View view, String sessionId,
                                         SessionTypeEnum sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
      //  menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private static NIMPopupMenu.MenuItemClickListener listener = item -> {
        switch (item.getTag()) {
            case ACTION_HISTORY_QUERY:
                MessageHistoryActivity.start(item.getContext(), item.getSessionId(),
                        item.getSessionTypeEnum()); // 漫游消息查询
                break;
            case ACTION_SEARCH_MESSAGE:
                SearchMessageActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum());
                break;
            case ACTION_CLEAR_MESSAGE:
                EasyAlertDialogHelper.createOkCancelDiolag(item.getContext(), null, "确定要清空吗？", true,
                        new EasyAlertDialogHelper.OnDialogActionListener() {

                            @Override
                            public void doCancelAction() {
                            }

                            @Override
                            public void doOkAction() {
                                NIMClient.getService(MsgService.class)
                                        .clearChattingHistory(
                                                item.getSessionId(),
                                                item.getSessionTypeEnum());
                                MessageListPanelHelper.getInstance()
                                        .notifyClearMessages(
                                                item.getSessionId());
                            }
                        }).show();
                break;
            case ACTION_CLEAR_P2P_MESSAGE:
                String title = item.getContext().getString(R.string.message_p2p_clear_tips);
                CustomAlertDialog alertDialog = new CustomAlertDialog(item.getContext());
                alertDialog.setTitle(title);
                alertDialog.addItem("确定", new CustomAlertDialog.onSeparateItemClickListener() {

                    @Override
                    public void onClick() {
                        NIMClient.getService(MsgService.class).clearServerHistory(item.getSessionId(),
                                item.getSessionTypeEnum());
                        MessageListPanelHelper.getInstance().notifyClearMessages(item.getSessionId());
                    }
                });
                String itemText = item.getContext().getString(R.string.sure_keep_roam);
                alertDialog.addItem(itemText, new CustomAlertDialog.onSeparateItemClickListener() {

                    @Override
                    public void onClick() {
                        NIMClient.getService(MsgService.class).clearServerHistory(item.getSessionId(),
                                item.getSessionTypeEnum(), false);
                        MessageListPanelHelper.getInstance().notifyClearMessages(item.getSessionId());
                    }
                });
                alertDialog.addItem("取消", new CustomAlertDialog.onSeparateItemClickListener() {

                    @Override
                    public void onClick() {
                    }
                });
                alertDialog.show();
                break;
        }
    };

    private static List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId,
                                                        SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, ACTION_HISTORY_QUERY, sessionId, sessionTypeEnum,
                ImCache.getContext().getString(R.string.message_history_query)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_SEARCH_MESSAGE, sessionId, sessionTypeEnum,
                ImCache.getContext().getString(R.string.message_search_title)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_CLEAR_MESSAGE, sessionId, sessionTypeEnum,
                ImCache.getContext().getString(R.string.message_clear)));
        if (sessionTypeEnum == SessionTypeEnum.P2P) {
            moreMenuItems.add(new PopupMenuItem(context, ACTION_CLEAR_P2P_MESSAGE, sessionId, sessionTypeEnum,
                    ImCache.getContext().getString(R.string.message_p2p_clear)));
        }
        return moreMenuItems;
    }
}
