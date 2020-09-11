package cn.sancell.xingqiu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.firefly1126.permissionaspect.PermissionCheckSDK;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.Bugly;
import com.tencent.liteav.demo.lvb.liveroom.debug.GenerateTestUserSig;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.sancell.xingqiu.base.entity.BaseApplication;
import cn.sancell.xingqiu.constant.BaseInterceptor;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.CookieReadInterceptor;
import cn.sancell.xingqiu.constant.CookiesSaveInterceptor;
import cn.sancell.xingqiu.constant.HttpInterceptor;
import cn.sancell.xingqiu.constant.ServerErrorInterceptor;
import cn.sancell.xingqiu.constant.network.NetWorkMonitorManager;
import cn.sancell.xingqiu.im.sys.ImCache;
import cn.sancell.xingqiu.im.sys.NimSDKOptionConfig;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.im.event.DemoOnlineStateContentProvider;
import cn.sancell.xingqiu.im.manager.NIMInitManager;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.push.ImMixPushMessageHandler;
import cn.sancell.xingqiu.push.ImPushContentProvider;
import cn.sancell.xingqiu.util.AppUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.SensitiveWordsUtils;
import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static cn.sancell.xingqiu.constant.Constants.APP_ID;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description: application
 */

public class SCApp extends BaseApplication {
    private static OkHttpClient mOkHttpClient;
    public static Context context;
    public static Uri schemeUri; //h5与app交互的uri

    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/442a93c113745d2c786b12b0ea3ade73/TXLiveSDK.licence";
    String licenseKey = GenerateTestUserSig.SECRETKEY;

    //微信api
    public  static IWXAPI api;


    @Override
    protected void mainInitApp() {
        initNewWork();
        //initOKHttp();
        regToWx();
        ToastHelper.init(this);
        LiveCache.setContext(getApplicationContext());
    }

    @Override
    protected void initApp() {
        try {

            UMConfigure.init(this, "5c107509b465f5229e0005f2"
                    , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
            context = getApplicationContext();
            disableAPIDialog();
            initFresco();
            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(this);// 初始化 JPush
            initIM();
            SensitiveWordsUtils.init(this);
            PermissionCheckSDK.init(this);
            Bugly.init(getApplicationContext(), "77fc641aae", false);
            //Beta.canShowUpgradeActs.add(MainActivity.class);
            AutoSizeConfig.getInstance().setExcludeFontScale(true);//屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
            TXLiveBase.getInstance().setLicence(context, licenceUrl, licenseKey);

        } catch (Exception e) {
            Log.i("keey", "appe:" + e.getMessage());
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(Constants.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    /**
     * 监听网络变化
     */
    private void initNewWork() {
        NetWorkMonitorManager.getInstance().init(this);
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wxb42cb41315f88904", "5c97659ca22e2856d076ad90102d4073");
        //新浪微博(暂时无此功能)
        PlatformConfig.setSinaWeibo("82463260", "125179b5ea09a7d3e27b1d82912aaf4d", "http://sns.whalecloud.com");

        PlatformConfig.setQQZone("1107057730", "8nGAHktdlMWQkSj8");

    }

    static {
        //设置下拉刷新全局Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        //设置下拉刷新全局Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //加载完成时滚动列表显示新的内容
                //  layout.setEnableScrollContentWhenLoaded(true);
                return new ClassicsFooter(context);
            }
        });
    }

    public void initIM() {
        ImCache.setContext(this);
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));
        if (NIMUtil.isMainProcess(this)) {
            // 初始化红包模块，在初始化UIKit模块之前执行
//            NIMRedPacketClient.init(this);
            // init pinyin
            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new ImMixPushMessageHandler());

            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            //开启消息提醒
            NIMClient.toggleNotification(true);
            //音视频模块
            //  initAVChatKit();
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);

        }


    }

    private void initUIKit() {

        // 初始化
        NimUIKit.init(this, buildUIKitOptions());
        // IM 会话窗口的定制初始化。
        SessionHelper.init();
        NimUIKit.setCustomPushContentProvider(new ImPushContentProvider());
        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());

    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    public LoginInfo getLoginInfo() {
        String account = PreferencesUtils.getString(Constants.Key.key_im_accid, "");
        String imToken = PreferencesUtils.getString(Constants.Key.key_im_token, "");
        if (!TextUtils.isEmpty(account) || !TextUtils.isEmpty(imToken)) {
            ImCache.setAccount(account.toLowerCase());
            LiveCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, imToken);

        } else {
            return null;
        }
    }


    private void initFresco() {
        mMemoryTrimmable = new ArrayList<>();
        //对ImagePipelineConfig进行一些配置
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                //.setDownsampleEnabled(true)// 对图片进行自动缩放
                .setResizeAndRotateEnabledForNetwork(true)// 对网络图片进行resize处理，减少内存消耗
                .setBitmapsConfig(Bitmap.Config.RGB_565)//图片设置RGB_565，减小内存开销  fresco默认情况下是RGB_8888
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .build();
        Fresco.initialize(this, config);
    }


    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                    .retryOnConnectionFailure(true)
                    //添加日志拦截器
                    //cookie
                    //.addInterceptor(new HttpInterceptor(AppUtils.getVersionName(SCApp.context)))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(new CookieReadInterceptor())
                    .addInterceptor(new CookiesSaveInterceptor())
                    .addInterceptor(new BaseInterceptor(AppUtils.getVersionName(SCApp.context)))
                    .addInterceptor(new ServerErrorInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }


    /*private String getSpEnv(){
        SpStore spStore = new SpStore(this, EnvSettingActivity.SP_NAME_SETTING);
        return spStore.get(EnvSettingActivity.ENV_KEY, null);
    }*/

    @Override
    protected void uninitApp() {

    }

    private List<MemoryTrimmable> mMemoryTrimmable;
    MemoryTrimmableRegistry memoryTrimmableRegistry = new MemoryTrimmableRegistry() {
        @Override
        public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
            mMemoryTrimmable.add(trimmable);
            Log.d("MainApplication", "registerMemoryTrimmable size: " + mMemoryTrimmable.size());
        }

        @Override
        public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
            mMemoryTrimmable.remove(trimmable);
            Log.d("MainApplication", "registerMemoryTrimmable size: " + mMemoryTrimmable.size());
        }
    };

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (MemoryTrimmable m : mMemoryTrimmable) {
            m.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
            Log.d("MainApplication", "MemoryTrimmable trim " + mMemoryTrimmable.size());
        }
    }
}

