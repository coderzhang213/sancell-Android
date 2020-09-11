package cn.sancell.xingqiu.im.sys;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.netease.nim.uikit.api.wrapper.MessageRevokeTip;
import com.netease.nim.uikit.api.wrapper.NimUserInfoProvider;
import com.netease.nim.uikit.business.session.activity.TeamMessageActivity;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushConfig;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.IOException;

import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;

// TODO: 2019-11-15 key配置 
public class NimSDKOptionConfig {
    //设置消息忽略
    public static boolean KEY_MSG_IGNORE = false;
    // 保存在线状态订阅时间
    public static long KEY_SUBSCRIBE_TIME = 0;



    public static SDKOptions getSDKOptions(Context context) {
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);
        // 配置 APP 保存图片/语音/文件/log等数据的目录
        options.sdkStorageRootPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;
        // 配置附件缩略图的尺寸大小
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();
        // 通知栏显示用户昵称和头像
        options.userInfoProvider = new NimUserInfoProvider(SCApp.context);
        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;
        // 在线多端同步未读数
        options.sessionReadAck = true;
        // 动图的缩略图直接下载原图
        options.animatedImageThumbnailEnabled = true;
        // 采用异步加载SDK
        options.asyncInitSDK = true;
        // 是否是弱IM场景
        options.reducedIM = false;
        // 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
        options.checkManifestConfig = false;
        // 是否启用群消息已读功能，默认关闭
        options.enableTeamMsgAck = true;
        // 打开消息撤回未读数-1的开关
        options.shouldConsiderRevokedMessageUnreadCount = true;
        // 云信私有化配置项
        //configServerAddress(options, context);
        options.mixPushConfig = buildMixPushConfig();
        //        options.mNosTokenSceneConfig = createNosTokenScene();
        options.loginCustomTag = "登录自定义字段";
        return options;
    }
    

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    public static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + SCApp.context.getPackageName();
        }
        return storageRootPath;
    }

    private static void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        // load 用户的 StatusBarNotificationConfig 设置项
//        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
//        if (userConfig == null) {
//            userConfig = config;
//        } else {
//            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
//            // 新增 notificationColor 存储，兼容3.6以前版本
//            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
//            userConfig.notificationEntrance = config.notificationEntrance;
//            userConfig.notificationFolded = config.notificationFolded;
//            userConfig.notificationColor = config.notificationColor;
//        }
//        // 持久化生效
//        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效

        options.statusBarNotificationConfig = loadStatusBarNotificationConfig();
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private static StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = HomeTabsActivity.class;
        config.notificationSmallIconId = R.mipmap.icon_logo;
        config.notificationColor = ImCache.getContext().getResources().getColor(R.color.color_theme);
        // 通知铃声的uri字符串
      //  config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        config.notificationFolded = true;
        //        config.notificationFolded = false;
        config.downTimeEnableNotification = true;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 是否APP ICON显示未读数红点(Android O有效)
        config.showBadge = true;
        // save cache，留做切换账号备用
        ImCache.setNotificationConfig(config);
        return config;
    }




    private static MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {

        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeRevokeMsgTip(String revokeAccount, IMMessage item) {
            return MessageRevokeTip.getRevokeTipContent(item, revokeAccount);
        }
    };


    /**
     *推送配置
     * @return
     */
    private static MixPushConfig buildMixPushConfig(){
        MixPushConfig config = new MixPushConfig();

        //小米
        config.xmAppId = "2882303761517919495";
        config.xmAppKey = "5481791965495";
        config.xmCertificateName = "imMiPush";

        //华为
        config.hwCertificateName = "imHuaweiPush";

        //魅族
        config.mzAppId = "126522";
        config.mzAppKey = "aaee82cd04a9438f81f889c5ac0a4e9f";
        config.mzCertificateName = "imFlymePush";

        //oppo
        config.oppoAppId = "3730443";
        config.oppoAppKey = "d4d13c505cb448e59ac310465583f005";
        config.oppoAppSercet = "59cbde5267ea48c294e22cfcfd304668";
        config.oppoCertificateName = "imOppoPush";

        //vivo
        config.vivoCertificateName = "imVivoPush";

        //fcm
        config.fcmCertificateName = "imFcm";

        return config;
    }

}
