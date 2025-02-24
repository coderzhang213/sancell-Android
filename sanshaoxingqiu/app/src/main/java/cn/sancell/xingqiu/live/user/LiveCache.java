package cn.sancell.xingqiu.live.user;

import android.app.Activity;
import android.content.Context;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import cn.sancell.xingqiu.live.bean.RoomInfoEntity;

/**
 * Created by jezhee on 2/20/15.
 */
public class LiveCache {

    private static Context context;

    private static Activity visibleActivity; //处于 onResume~onPause生命周期内的Activity

    private static String account;

    private static String sid;

    //云信服务 token
    private static String token;

    //视频云点播服务 token
    private static String vodtoken;

    private static NimUserInfo userInfo;

    private static RoomInfoEntity roomInfoEntity;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        LiveCache.account = account;
        NimUIKit.setAccount(account);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        LiveCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        LiveCache.context = context.getApplicationContext();
    }

    public static NimUserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = NIMClient.getService(UserService.class).getUserInfo(account);
        }

        return userInfo;
    }

    public static String getSid() {
        if(sid == null){
            sid = Preferences.getUserSid();
        }
        return sid;
    }

    public static void setSid(String sid) {
        LiveCache.sid = sid;
        Preferences.saveUserSid(sid);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        LiveCache.token = token;
    }

    public static String getVodtoken() {
        return vodtoken;
    }

    public static void setVodtoken(String vodtoken) {
        LiveCache.vodtoken = vodtoken;
    }

    public static void setUserInfo(NimUserInfo userInfo) {
        LiveCache.userInfo = userInfo;
    }

    public static RoomInfoEntity getRoomInfoEntity() {
        return roomInfoEntity;
    }

    public static void setRoomInfoEntity(RoomInfoEntity roomInfoEntity) {
        LiveCache.roomInfoEntity = roomInfoEntity;
    }

    public static Activity getVisibleActivity() {
        return visibleActivity;
    }

    public static void setVisibleActivity(Activity visibleActivity) {
        LiveCache.visibleActivity = visibleActivity;
    }
}
