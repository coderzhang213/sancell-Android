package cn.sancell.xingqiu.util;


import cn.sancell.xingqiu.bean.AppStatus;

/**
 * Created by huyingying on 2019/3/21.
 */

public class AppStatusManager {
    public int appStatus = AppStatus.STATUS_RECYVLE;    //APP状态 初始值为不在前台状态

    public static AppStatusManager appStatusManager;

    //单例模式
    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
