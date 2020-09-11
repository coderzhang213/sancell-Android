package cn.sancell.xingqiu.im.sys;

import android.content.Context;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

public class ImCache {

    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        ImCache.account = account;
        NimUIKit.setAccount(account);
       // AVChatKit.setAccount(account);

    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        ImCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ImCache.context = context;

      //  AVChatKit.setContext(context);
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        ImCache.mainTaskLaunching = mainTaskLaunching;

       // AVChatKit.setMainTaskLaunching(mainTaskLaunching);
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }
}
