package cn.sancell.xingqiu.im.sys;

import com.netease.nim.uikit.api.NimUIKit;

import cn.sancell.xingqiu.live.user.LiveCache;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        ImCache.clear();
        LiveCache.clear();
    }
}
