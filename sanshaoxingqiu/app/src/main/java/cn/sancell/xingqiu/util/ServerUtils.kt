package cn.sancell.xingqiu.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import cn.sancell.xingqiu.base.FloatLiveInfo
import cn.sancell.xingqiu.live.play.services.LivePlayExitServer
import cn.sancell.xingqiu.service.FloatWindowService

/**
 * Created by zj on 2020/4/27.
 */
object ServerUtils {
    fun isServiceRunning(context: Context, className: String): Boolean {
        if (className.isNullOrEmpty()) {
            return false
        }
        var isRunning: Boolean = false
        var activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var serviceList: List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(30)
        if (serviceList.isEmpty()) {
            return false
        }
        serviceList.forEach { item ->
            if (item.service.className == className) {
                isRunning = true
                return@forEach
            }
        }
        return isRunning
    }

    fun startServer(context: Context, isLiveRoomExit: Boolean) {
        val intent = Intent(context, LivePlayExitServer::class.java)
        intent.putExtra(LivePlayExitServer.IS_LIVE_ROOM_IEXT, isLiveRoomExit)
        context.startService(intent)
    }

    /**
     * 启动悬浮服务
     */
    fun startFloatServer(context: Context, mFloatLiveInfo: FloatLiveInfo) {
        val intent = Intent(context?.getApplicationContext(), FloatWindowService::class.java)
        intent.action = FloatWindowService.ACTION_LIVE_PLAY
        intent.putExtra(FloatWindowService.INFO_NAME, mFloatLiveInfo)
        context?.startService(intent)
    }
}