package cn.sancell.xingqiu.live.fragment;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.netease.nim.uikit.common.ToastHelper;
import com.tencent.liteav.demo.lvb.camerapush.PusherBGMFragment;
import com.tencent.liteav.demo.lvb.camerapush.PusherMoreFragment;
import com.tencent.liteav.demo.lvb.camerapush.PusherSettingFragment;
import com.tencent.liteav.demo.lvb.camerapush.PusherTroubleshootingFragment;
import com.tencent.liteav.demo.lvb.common.utils.FileUtils;
import com.tencent.liteav.demo.lvb.common.view.TXPushVisibleLogView;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.dialog.TxFilterDialog;
import cn.sancell.xingqiu.ktenum.LivePagerType;
import cn.sancell.xingqiu.live.base.LiveCaptureToolBaseFragment;
import cn.sancell.xingqiu.live.interfacep.OnActivityUiLinsenr;
import cn.sancell.xingqiu.live.nim.PublishParam;

/**
 * 腾讯云 {@link TXLivePusher} 推流器使用参考 Demo
 * <p>
 * 有以下功能参考 ：
 * <p>
 * 1. 基本功能参考： 启动推流 {@link #startRTMPPush()} 与 结束推流 {@link #stopRTMPPush()}
 * <p>
 * 2. 场景化配置参考：{@link PusherSettingFragment} 与 {@link #setPushScene(int, boolean)} 您可以根据您的 App 使用设定不同的推流场景，SDK 内部会自动选择相关配置，让您可以快速搭建
 * 注：一般客户建议直接使用场景化配置；若您是专业级客户，推荐您参考 {@link TXLivePushConfig} 进行个性化配置
 * <p>
 * 3. 美颜功能使用参考: {@link }
 * <p>
 * 4. 性能数据查看参考： {@link #onNetStatus(Bundle)}
 * <p>
 * 5. 处理 SDK 回调事件参考： {@link #onPushEvent(int, Bundle)}
 * <p>
 * 6. 混响、变声、码率自适应、硬件加速，使用参考： {@link PusherSettingFragment} 与 {@link PusherSettingFragment.OnSettingChangeListener}
 * <p>
 * 7. 横竖屏推流、静音、静画、观众端镜像、闪光灯、调试信息、水印、对焦、缩放功能，使用参考： {@link PusherMoreFragment} 与 {@link PusherMoreFragment.OnMoreChangeListener}
 * <p>
 * 8. 横屏推流使用参考：该功能较为复杂，需要区分Activity是否可以旋转。
 * A. 不可旋转情况下开启横屏推流：直接参考 {@link #onOrientationChange(boolean)} 即可
 * B. 可旋转情况下开启横屏推流，参考： {@link ActivityRotationObserver} 与 {@link #setRotationForActivity()}
 * <p>
 * 9. mute功能：muteLocalVideo、muteLocalAudio
 * <p>
 * 10. BGM功能: {@link PusherBGMFragment}
 */
public class TengXunCameraPusherFragment extends LiveCaptureToolBaseFragment implements ITXLivePushListener,
        PusherSettingFragment.OnSettingChangeListener, PusherMoreFragment.OnMoreChangeListener, PusherBGMFragment.OnBGMControllCallback, TXLivePusher.OnBGMNotify {
    private static final String TAG = TengXunCameraPusherFragment.class.getSimpleName();
    /**
     * SDK 提供的类
     */
    private TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    private TXLivePusher mLivePusher;                    // SDK 推流类
    private TXCloudVideoView mPusherView;                    // SDK 推流本地预览类

    /**
     * 控件
     */
    private TextView mTvNetBusyTips;                 // 网络繁忙Tips
    private Button mBtnStartPush;                  // 开启推流的按钮
    private PusherMoreFragment mPushMoreFragment;              // 更多Fragment
    private PusherSettingFragment mPushSettingFragment;           // 设置Fragment
    private PusherBGMFragment mPushBGMFragment;               // BGM Fragment
    private PusherTroubleshootingFragment mPusherTroublieshootingFragment;// 问题排查Fragment
    private ImageView mIvRTMP, mIvFlv, mIvHls, mIvAccRTMP;                  // RTMP、FLV、HLS、ACCRTMP 二维码地址控件
    private TxFilterDialog mTxFilterDialog;//美颜弹框
    /**
     * 默认美颜参数
     */
    private int mBeautyLevel = 5;            // 美颜等级
    private int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH; // 美颜样式
    private int mWhiteningLevel = 3;            // 美白等级
    private int mRuddyLevel = 2;            // 红润等级
    private boolean mFrontCamera = true;
    /**
     * 其他参数
     */
    private int mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;   // 当前分辨率
    private boolean mIsPushing;                     // 当前是否正在推流
    private Bitmap mWaterMarkBitmap;               // 水印


    /**
     * BGM 相关
     */
    private int mBGMLoopTimes;                 // 当前BGM循环次数
    private boolean mBGMIsOnline;                  // BGM是否在线链接
    private String mBGMURL;                       // BGM URL（本地 、 在线）


    private PhoneStateListener mPhoneListener = null;         // 当前电话监听Listener
    private ActivityRotationObserver mActivityRotationObserver;     // 监听Activity旋转
    //父类互相调用
    private OnActivityUiLinsenr mOnActivityUiLinsenr;
    private String roomId;
    private String batchId;
    private PublishParam mPublishParam;
    //是否初始化成功
    private boolean isInitPush = false;
    //是否为后置摄像头
    private boolean isRearCamera = false;

    public TengXunCameraPusherFragment(OnActivityUiLinsenr mOnActivityUiLinsenr, String roomId, String batchId, int postion) {
        this.mOnActivityUiLinsenr = mOnActivityUiLinsenr;
        this.roomId = roomId;
        this.batchId = batchId;
        setShowPostion(postion);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initPar();
        super.onActivityCreated(savedInstanceState);
        //获取直播间信息
        getLiveInfo(batchId, "");
        checkPublishPermission();  // 检查权限
        initPusher();              // 初始化 SDK 推流器
        initTitleBar();            // 初始化 Title Bar
        initToolBar();             // 初始化顶部工具栏
        initListener();            // 初始化电话监听
        initMainView();            // 初始化一些核心的 View
        initToolBottom();          // 初始化底部工具栏
        initFragment();            // 初始化底部工具栏的两个Fragment
        initPush();                //初始化推流配置
    }

    /**
     * 获取传递过来的参数
     */
    private void initPar() {
        mPublishParam = (PublishParam) getArguments().getSerializable(PublishParam.EXTRA_PARAMS);
        if (mPublishParam == null) {
            return;
        }
        switch (mPublishParam.definition) {
            case PublishParam.HD:
                mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_1080_1920;
                break;
            case PublishParam.SD:
                mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280;
                break;
            case PublishParam.LD:
                mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                break;
        }
    }

    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {
        mLivePusher = new TXLivePusher(getContext());
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(5);
        mLivePusher.setConfig(mLivePushConfig);
        mWaterMarkBitmap = decodeResource(getResources(), R.drawable.watermark);
    }

    public void destroyController() {

    }

    /**
     * 聊天室退出
     */
    public void onLiveEndPlay() {
        showLivePlayEnd();
        stopRTMPPush();
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      View初始化相关
    //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化两个配置的 Fragment
     */
    private void initFragment() {
        if (mPushSettingFragment == null) {
            mPushSettingFragment = new PusherSettingFragment();
            mPushSettingFragment.loadConfig(getContext());
            mPushSettingFragment.setOnSettingChangeListener(TengXunCameraPusherFragment.this);
        }
        if (mPushMoreFragment == null) {
            mPushMoreFragment = new PusherMoreFragment();
            mPushMoreFragment.loadConfig(getContext());
            mPushMoreFragment.setMoreChangeListener(TengXunCameraPusherFragment.this);
        }
        if (mPushBGMFragment == null) {
            mPushBGMFragment = new PusherBGMFragment();
            mPushBGMFragment.setBGMControllCallback(this);
        }
        if (mPusherTroublieshootingFragment == null) {
            mPusherTroublieshootingFragment = new PusherTroubleshootingFragment();
        }
    }

    private void startPlay() {
        getLivieModel().upRoomStatus("2").observe(this, it -> {
            mBtnStartPush.setEnabled(true);
            mBtnStartPush.setVisibility(View.GONE);
            if (it.getStatus() >= 0) {
                mOnActivityUiLinsenr.onStartLivePlay();
                //设置默认的观看人数
                setDefaultLiveSum();
                //去开始循环获取观看人数
                startLoopCheckLive();
                boolean isSucess = startRTMPPush();
                if (isSucess) {
                    mBtnStartPush.setVisibility(View.GONE);
                }
            } else {
                ToastHelper.showToast("开始直播失败");
            }

        });
        getLivieModel().getErrMsg().observe(this, it -> {
            ToastHelper.showToast(it);
            mBtnStartPush.setEnabled(true);
        });
        getLivieModel().getMException().observe(this, v -> {
            mBtnStartPush.setEnabled(true);
        });
    }

    /**
     * 初始化底部工具栏
     */
    private void initToolBottom() {
        mBtnStartPush = (Button) findViewById(R.id.btn_star_live);
        mBtnStartPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsPushing) {
                    startPlay();
                } else {
                    stopRTMPPush();
                }
            }
        });


        findViewById(R.id.pusher_btn_show_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPusherTroublieshootingFragment.isVisible()) {
                    try {
                        mPusherTroublieshootingFragment.dismissAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // mPusherTroublieshootingFragment.show(getFragmentManager(), "push_trouble_shooting_fragment");
                }
            }
        });
        findViewById(R.id.pusher_btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPushSettingFragment.isVisible()) {
                    try {
                        mPushSettingFragment.dismissAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //    mPushSettingFragment.show(getChildFragmentManager(), "push_setting_fragment");
                }
            }
        });
        findViewById(R.id.pusher_btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPushMoreFragment.isVisible()) {
                    try {
                        mPushMoreFragment.dismissAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //     mPushMoreFragment.show(getFragmentManager(), "push_more_fragment");
                }
            }
        });
        findViewById(R.id.pusher_btn_bgm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPushBGMFragment.isVisible()) {
                    try {
                        mPushBGMFragment.dismissAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // mPushBGMFragment.show(getFragmentManager(), "push_bgm_fragment");
                }
            }
        });
        findViewById(R.id.pusher_btn_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 表明当前是前摄像头
                if (v.getTag() == null || (Boolean) v.getTag()) {
                    v.setTag(false);
                    v.setBackgroundResource(R.mipmap.camera_back);
                    mFrontCamera = false;
                } else {
                    v.setTag(true);
                    v.setBackgroundResource(R.mipmap.camera_front);
                    mFrontCamera = true;
                }
                mLivePusher.switchCamera();
            }
        });
    }

    /**
     * 初始化 美颜、log、二维码 等 view
     */
    private void initMainView() {
        mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);

        mTvNetBusyTips = (TextView) findViewById(R.id.pusher_tv_net_error_warning);
        mIvAccRTMP = (ImageView) findViewById(R.id.pusher_iv_rtmp_acc_url);
        mIvRTMP = (ImageView) findViewById(R.id.pusher_iv_rtmp_url);
        mIvFlv = (ImageView) findViewById(R.id.pusher_iv_flv_url);
        mIvHls = (ImageView) findViewById(R.id.pusher_iv_hls_url);
        if (mTxFilterDialog == null) {
            mTxFilterDialog = new TxFilterDialog(getContext());
            mTxFilterDialog.setTXLivePusher(mLivePusher);
        }

//        PusherBeautyKit manager = new PusherBeautyKit(mLivePusher);
//        mBeautyPanelView.setProxy(manager);
    }


    /**
     * 初始化顶部工具栏
     */
    private void initToolBar() {
    }

    /**
     * 初始化状态栏
     */
    private void initTitleBar() {

    }


    /**
     * 显示网络繁忙的提示
     */
    private void showNetBusyTips() {
        if (mTvNetBusyTips.isShown()) {
            return;
        }
        mTvNetBusyTips.setVisibility(View.VISIBLE);
        mTvNetBusyTips.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      Activity声明周期相关
    //
    /////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        super.onResume();
        if (mPusherView != null) {
            mPusherView.onResume();
        }

        if (mIsPushing && mLivePusher != null) {
            // 如果当前是隐私模式，那么不resume
            if (!mPushMoreFragment.isPrivateMode()) {
                mLivePusher.resumePusher();
            }
            mLivePusher.resumeBGM();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mPusherView != null) {
            mPusherView.onPause();
        }

        if (mIsPushing && mLivePusher != null) {
            // 如果当前已经是隐私模式，那么则不pause
            if (!mPushMoreFragment.isPrivateMode()) {
                mLivePusher.pausePusher();
            }
            mLivePusher.pauseBGM();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRTMPPush(); // 停止推流
        if (mPusherView != null) {
            mPusherView.onDestroy(); // 销毁 View
        }
        unInitPhoneListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || TextUtils.isEmpty(data.getExtras().getString("result"))) {
            return;
        }
        String result = data.getExtras().getString("result");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRotationForActivity(); // Activity 旋转
    }


    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      权限相关回调接口
    //
    /////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }
        return true;
    }

    /**
     * 生成推流地址
     *
     * @return
     */
    private String getPushUrl() {
        String inputUrl = mPublishParam.pushUrl;
        String tRTMPURL = "";
        if (!TextUtils.isEmpty(inputUrl)) {
            String url[] = inputUrl.split("###");
            if (url.length > 0) {
                tRTMPURL = url[0];
            }
        }
        return tRTMPURL;
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      SDK 推流相关
    //
    /////////////////////////////////////////////////////////////////////////////////
    //初始化推流
    private boolean initPush() {
        String tRTMPURL = getPushUrl();
        if (TextUtils.isEmpty(tRTMPURL) || (!tRTMPURL.trim().toLowerCase().startsWith("rtmp://"))) {
            Toast.makeText(getContext(), "推流地址不合法，目前支持rtmp推流!", Toast.LENGTH_SHORT).show();
            // 输出状态log
            Bundle params = new Bundle();
            params.putString(TXLiveConstants.EVT_DESCRIPTION, "检查地址合法性");
            mPusherTroublieshootingFragment.setLogText(null, params, TXPushVisibleLogView.CHECK_RTMP_URL_FAIL);
            return false;
        }
        // 显示本地预览的View
        mPusherView.setVisibility(View.VISIBLE);

        // 输出状态log
        Bundle params = new Bundle();
        params.putString(TXLiveConstants.EVT_DESCRIPTION, "检查地址合法性");
        mPusherTroublieshootingFragment.setLogText(null, params, TXPushVisibleLogView.CHECK_RTMP_URL_OK);


        // 添加播放回调
        mLivePusher.setPushListener(this);
        mLivePusher.setBGMNofify(this);

        // 添加后台垫片推流参数
        Bitmap bitmap = decodeResource(getResources(), R.drawable.pause_publish);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseImg(300, 5);
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);// 设置暂停时，只停止画面采集，不停止声音采集。

        // 设置推流分辨率
        mLivePushConfig.setVideoResolution(mCurrentVideoResolution);

        // 设置美颜
        mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);

        // 如果当前Activity可以自动旋转的话，那么需要进行设置
        if (mPushMoreFragment.isActivityCanRotation(getContext())) {
            setRotationForActivity();
        }

        Log.i(TAG, "startRTMPPush: mPushMore = " + mPushMoreFragment.toString());

        // 开启麦克风推流相关
        mLivePusher.setMute(mPushMoreFragment.isMuteAudio());

        // 横竖屏推流相关
        int renderRotation = 0;
        if (mPushMoreFragment.isPortrait()) {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
            renderRotation = 0;
        } else {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
            renderRotation = 90; // 因为采集旋转了，那么保证本地渲染是正的，则设置渲染角度为90度。
        }
        mLivePusher.setRenderRotation(renderRotation);

        //根据activity方向调整横竖屏
        setRotationForActivity();

        setOpenMirror();
        // 是否打开调试信息
        mPusherView.showLog(mPushMoreFragment.isDebugInfo());

        // 是否添加水印
        if (mPushMoreFragment.isWaterMarkEnable()) {
            mLivePushConfig.setWatermark(null, 0.02f, 0.05f, 0.2f);
        } else {
            mLivePushConfig.setWatermark(null, 0, 0, 0);
        }

        // 是否打开曝光对焦
        mLivePushConfig.setTouchFocus(mPushMoreFragment.isFocusEnable());

        // 是否打开手势放大预览画面
        mLivePushConfig.setEnableZoom(mPushMoreFragment.isZoomEnable());

        mLivePushConfig.enableAudioEarMonitoring(mPushSettingFragment.isEarmonitoringEnable());

        mLivePushConfig.enablePureAudioPush(mPushMoreFragment.isPureAudio());

        // 设置推流配置
        mLivePusher.setConfig(mLivePushConfig);

        // 设置场景
        setPushScene(mPushSettingFragment.getQualityType(), mPushSettingFragment.isEnableAdjustBitrate());

        // 设置本地预览View
        mLivePusher.startCameraPreview(mPusherView);
        if (!mFrontCamera) mLivePusher.switchCamera();

        isInitPush = true;
        return true;
    }

    /**
     * 开始 RTMP 推流
     * <p>
     * 推荐使用方式：
     * 1. 配置好 {@link TXLivePushConfig} ， 配置推流参数
     * 2. 调用 {@link TXLivePusher#setConfig(TXLivePushConfig)} ，设置推流参数
     * 3. 调用 {@link TXLivePusher#startCameraPreview(TXCloudVideoView)} ， 开始本地预览
     * 4. 调用 {@link TXLivePusher#startPusher(String)} ， 发起推流
     * <p>
     * 注：步骤 3 要放到 2 之后，否则横屏推流、聚焦曝光、画面缩放功能配置不生效
     *
     * @return
     */
    private boolean startRTMPPush() {
        String tRTMPURL = getPushUrl();
        if (!isInitPush) {
            if (!initPush()) {
                return false;
            }
        }


        // 发起推流
        int ret = mLivePusher.startPusher(tRTMPURL.trim());
        if (ret == -5) {
            ToastHelper.showToast("开始失败，请重试");
            return false;
        }

        // 设置混响
        mLivePusher.setReverb(mPushSettingFragment.getReverbIndex());

        // 设置变声
        mLivePusher.setVoiceChangerType(mPushSettingFragment.getVoiceChangerIndex());

        mIsPushing = true;

        mBtnStartPush.setBackgroundResource(R.mipmap.pusher_stop);

        return true;
    }


    /**
     * 停止 RTMP 推流
     */
    private void stopRTMPPush() {
        // 清除log状态
        mPusherTroublieshootingFragment.clear();

        // 停止BGM
        mLivePusher.stopBGM();
        // 停止本地预览
        mLivePusher.stopCameraPreview(true);
        // 移除监听
        mLivePusher.setPushListener(null);
        // 停止推流
        mLivePusher.stopPusher();
        // 隐藏本地预览的View
        mPusherView.setVisibility(View.GONE);
        // 移除垫片图像
        mLivePushConfig.setPauseImg(null);
        // 关闭隐私模式
        if (mPushMoreFragment != null)
            mPushMoreFragment.closePrivateModel();

        mIsPushing = false;
        isInitPush = false;

        mBtnStartPush.setBackgroundResource(R.mipmap.pusher_start);
    }

    /**
     * 根据当前 Activity 的旋转方向，配置推流器
     */
    private void setRotationForActivity() {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
        int mobileRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_180:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_UP;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }
        mLivePusher.setRenderRotation(0);                                   // 因为activity也旋转了，本地渲染相对正方向的角度为0。
        mLivePushConfig.setHomeOrientation(pushRotation);                   // 根据Activity方向，设置采集角度
        // 当前正在推流，
        if (mLivePusher.isPushing()) {
            mLivePusher.setConfig(mLivePushConfig);
            // 不是隐私模式，则开启摄像头推流。
            if (!mPushMoreFragment.isPrivateMode()) {
                mLivePusher.stopCameraPreview(true);
                mLivePusher.startCameraPreview(mPusherView);
            }
        }
    }


    /**
     * 推流器状态回调
     *
     * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 推流事件列表}.
     * @param param
     */
    @Override
    public void onPushEvent(int event, Bundle param) {
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        String pushEventLog = "receive event: " + event + ", " + msg;
        Log.d(TAG, pushEventLog);
        mPusherTroublieshootingFragment.setLogText(null, param, event);

        // 如果开始推流，设置了隐私模式。 需要在回调里面设置，不能直接start之后直接pause
        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            if (mPushMoreFragment.isPrivateMode()) {
                mLivePusher.pausePusher();
            }
        }
        // Toast错误内容
        if (event < 0) {
            Toast.makeText(getContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }
        if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            // 遇到以上错误，则停止推流
            stopRTMPPush();
        } else if (event == TXLiveConstants.PUSH_EVT_CONNECT_SUCC) {//直播开始
            if (mOnActivityUiLinsenr != null) {//直播开始
                mOnActivityUiLinsenr.onStartLivingFinished();
            }
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            // 开启硬件加速失败
            Toast.makeText(getContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mLivePusher.setConfig(mLivePushConfig);
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_RESOLUTION) {
            Log.d(TAG, "change resolution to " + param.getInt(TXLiveConstants.EVT_PARAM2) + ", bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_BITRATE) {
            Log.d(TAG, "change bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            showNetBusyTips();
        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
//            int encType = param.getInt(TXLiveConstants.EVT_PARAM1);
//            boolean hwAcc = (encType == TXLiveConstants.ENCODE_VIDEO_HARDWARE);
//            Toast.makeText(CameraPusherActivity.this, "是否启动硬编：" + hwAcc, Toast.LENGTH_SHORT).show();
        } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
            // 只有后置摄像头可以打开闪光灯，若默认需要开启闪光灯。 那么在打开摄像头成功后，才可以进行配置。 若果当前是前置，设定无效；若是后置，打开闪光灯。
            mLivePusher.turnOnFlashLight(mPushMoreFragment.isFlashEnable());
        }
    }


    @Override
    public void onNetStatus(Bundle status) {
        String str = getStatus(status);
        Log.d(TAG, "Current status, CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
        mPusherTroublieshootingFragment.setLogText(status, null, 0);
//        if (mLivePusher != null){
//            mLivePusher.onLogRecord("[net state]:\n"+str+"\n");
//        }
    }


    /**
     * 获取当前推流状态
     *
     * @param status
     * @return
     */
    private String getStatus(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-8s %-8s %-8s %-8s\n%-14s %-14s %-12s\n%-14s %-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "GOP:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_GOP) + "s",
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_CACHE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_DROP) + "|" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_DROP),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AUDIO:" + status.getString(TXLiveConstants.NET_STATUS_AUDIO_INFO));
        return str;
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      MoreFragment功能回调
    //
    /////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPrivateModeChange(boolean enable) {
        // 隐私模式下，会进入垫片推流
        if (mLivePusher.isPushing()) {
            if (enable) {
                mLivePusher.pausePusher();
            } else {
                mLivePusher.resumePusher();
            }
        }
    }

    @Override
    public void onMuteAudioChange(boolean enable) {
        mLivePusher.setMute(enable);
    }

    /**
     * 横竖屏推流切换
     *
     * @param isPortrait
     */
    @Override
    public void onOrientationChange(boolean isPortrait) {
        int renderRotation = 0;
        if (isPortrait) {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
            renderRotation = 0;
        } else {
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
            renderRotation = 90; // 因为采集旋转了，那么保证本地渲染是正的，则设置渲染角度为90度。
        }
        if (mLivePusher.isPushing()) {
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.setRenderRotation(renderRotation);
        }
    }

    /**
     * 镜像切换
     *
     * @param enable
     */
    @Override
    public void onMirrorChange(boolean enable) {
        mLivePusher.setMirror(enable);
    }

    /**
     * 闪光灯切换
     *
     * @param enable
     */
    @Override
    public void onFlashLightChange(boolean enable) {
        mLivePusher.turnOnFlashLight(enable);
    }

    @Override
    public void onDebugInfoChange(boolean enable) {
        mPusherView.showLog(enable);
    }

    /**
     * 水印
     *
     * @param enable
     */
    @Override
    public void onWaterMarkChange(boolean enable) {
        if (enable) {
            mLivePushConfig.setWatermark(mWaterMarkBitmap, 0.02f, 0.05f, 0.2f);
        } else {
            mLivePushConfig.setWatermark(null, 0, 0, 0);
        }
        if (mLivePusher.isPushing()) {
            // 水印变更不需要重启推流，直接应用配置项即可
            mLivePusher.setConfig(mLivePushConfig);
        }
    }

    /**
     * 使用硬件加速
     *
     * @param enable
     */
    @Override
    public void onHwAccChange(boolean enable) {
        if (enable) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE); // 启动硬编
        } else {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE); // 启动软编
        }
        if (mLivePusher.isPushing()) {
            // 硬件加速变更不需要重启推流，直接应用配置项即可
            mLivePusher.setConfig(mLivePushConfig);
        }
    }

    /**
     * 手动对焦
     *
     * @param enable
     */
    @Override
    public void onFocusChange(boolean enable) {
        mLivePushConfig.setTouchFocus(enable);
        if (mLivePusher.isPushing()) {
            Toast.makeText(getContext(), "当前正在推流，启动或关闭对焦需要重新推流", Toast.LENGTH_SHORT).show();
            stopRTMPPush();
            startRTMPPush();
        }
    }

    /**
     * 缩放
     *
     * @param enable
     */
    @Override
    public void onZoomChange(boolean enable) {
        mLivePushConfig.setEnableZoom(enable);
        if (mLivePusher.isPushing()) {
            Toast.makeText(getContext(), "当前正在推流，启动或关闭缩放需要重新推流", Toast.LENGTH_SHORT).show();
            stopRTMPPush();
            startRTMPPush();
        }
    }

    /**
     * 点击截图
     */
    @Override
    public void onClickSnapshot() {
        if (mLivePusher != null) {
            mLivePusher.snapshot(new TXLivePusher.ITXSnapshotListener() {
                @Override
                public void onSnapshot(final Bitmap bmp) {
                    if (mLivePusher.isPushing()) {
                        if (bmp != null) {
                            saveAndSharePic(bmp);
                        } else {
                            Toast.makeText(getContext(), "截图失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "截图失败，请先发起推流", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onSendMessage(String msg) {
        if (mLivePusher != null) {
            mLivePusher.sendMessageEx(msg.getBytes());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      SettingFragment功能回调
    //
    /////////////////////////////////////////////////////////////////////////////////


    /**
     * 码率自适应
     *
     * @param enable
     */
    @Override
    public void onAdjustBitrateChange(boolean enable) {
        setPushScene(mPushSettingFragment.getQualityType(), mPushSettingFragment.isEnableAdjustBitrate());
    }

    /**
     * SDK 场景与清晰度设置
     *
     * @param type
     */
    @Override
    public void onQualityChange(int type) {
        setPushScene(type, mPushSettingFragment.isEnableAdjustBitrate());
    }

    /**
     * 混响配置
     *
     * @param type
     */
    @Override
    public void onReverbChange(int type) {
        if (mLivePusher != null) {
            mLivePusher.setReverb(type);
        }
    }

    /**
     * 变音配置
     *
     * @param type
     */
    @Override
    public void onVoiceChange(int type) {
        if (mLivePusher != null) {
            mLivePusher.setVoiceChangerType(type);
        }
    }

    /**
     * 耳返开关
     *
     * @param enable
     */
    @Override
    public void onEarmonitoringChange(boolean enable) {
        if (mLivePusher != null) {
            mLivePusher.getConfig().enableAudioEarMonitoring(enable);
            mLivePusher.setConfig(mLivePusher.getConfig());
        }
    }

    /**
     * 设置推流场景
     * <p>
     * SDK 内部将根据具体场景，进行推流 分辨率、码率、FPS、是否启动硬件加速、是否启动回声消除 等进行配置
     * <p>
     * 适用于一般客户，方便快速进行配置
     * <p>
     * 专业客户，推荐通过 {@link TXLivePushConfig} 进行逐一配置
     */
    public void setPushScene(int type, boolean enableAdjustBitrate) {
        Log.i(TAG, "setPushScene: type = " + type + " enableAdjustBitrate = " + enableAdjustBitrate);
        // 码率、分辨率自适应都关闭
        boolean autoBitrate = enableAdjustBitrate;
        boolean autoResolution = false;
        switch (type) {
            case TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION: /*360p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION: /*540p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION: /*720p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION: /*1080p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_1080_1920;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER: /*连麦大主播*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER: /*连麦小主播*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_480;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT: /*实时*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT, autoBitrate, autoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            default:
                break;
        }
        // 设置场景化配置后，SDK 内部会根据场景自动选择相关的配置参数，所以我们这里把内部的config获取出来，赋值到外部。
        mLivePushConfig = mLivePusher.getConfig();

        // 是否开启硬件加速
        if (mPushSettingFragment.isHWAcc()) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE);
            mLivePusher.setConfig(mLivePushConfig);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      BGM
    //
    /////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onStartPlayBGM(final String url, int loopTimes, boolean isOnline) {
        mBGMURL = url;
        mBGMLoopTimes = loopTimes;
        mBGMIsOnline = isOnline;
        if (mLivePusher != null) {
            mLivePusher.stopBGM();
            if (isOnline) {
                // 在线音乐，由于刚开始需要进行网络请求，所以需要抛到子线程调用，否则有ARN风险
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mLivePusher.playBGM(url);
                    }
                });
            } else {
                mLivePusher.playBGM(url);
            }
        }
    }

    @Override
    public void onResumeBGM() {
        if (mLivePusher != null)
            mLivePusher.resumeBGM();
    }

    @Override
    public void onPauseBGM() {
        if (mLivePusher != null)
            mLivePusher.pauseBGM();
    }

    @Override
    public void onStopBGM() {
        mBGMURL = null;
        mBGMLoopTimes = 0;
        mBGMIsOnline = false;
        if (mLivePusher != null) {
            mLivePusher.stopBGM();
        }
    }

    @Override
    public void onBGMVolumeChange(float volume) {
        if (mLivePusher != null) {
            mLivePusher.setBGMVolume(volume);
        }
    }

    @Override
    public void onMICVolumeChange(float volume) {
        if (mLivePusher != null) {
            mLivePusher.setMicVolume(volume);
        }
    }

    @Override
    public void onBGMPitchChange(float pitch) {
        if (mLivePusher != null) {
            mLivePusher.setBGMPitch(pitch);
        }
    }


    @Override
    public void onBGMStart() {

    }

    @Override
    public void onBGMProgress(long progress, long duration) {

    }

    @Override
    public void onBGMComplete(int err) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLivePusher.stopBGM();
                mBGMLoopTimes--;
                if (mBGMLoopTimes != 0) {
                    if (mBGMIsOnline) {
                        // 在线音乐，由于刚开始需要进行网络请求，所以需要抛到子线程调用，否则有ARN风险
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                mLivePusher.playBGM(mBGMURL);
                            }
                        });
                    } else {
                        mLivePusher.playBGM(mBGMURL);
                    }
                }
            }
        });
    }


    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      监听相关
    //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化电话监听、系统是否打开旋转监听
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        mActivityRotationObserver = new ActivityRotationObserver(new Handler(Looper.getMainLooper()));
        mActivityRotationObserver.startObserver();
    }


    /**
     * 销毁
     */
    private void unInitPhoneListener() {
        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
        mActivityRotationObserver.stopObserver();
    }

    @Override
    public void openCloseFilter(boolean isOpenFilter) {
        //if (isOpenFilter) {
        mTxFilterDialog.show();
        //  }
//        if (mBeautyPanelView.isShown()) {
//            mBeautyPanelView.setVisibility(View.GONE);
//        } else {
//            mBeautyPanelView.setVisibility(View.VISIBLE);
//        }
    }

    @NotNull
    @Override
    public String getGetCurrType() {
        return LivePagerType.LIVE_PLAY.getType();
    }

    @NotNull
    @Override
    public String getGetRoomId() {
        return batchId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tengxun_camera_pusher;
    }


    /**
     * 电话监听
     */
    private static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) pusher.resumePusher();
                    break;
            }
        }
    }

    /**
     * 观察屏幕旋转设置变化
     */
    private class ActivityRotationObserver extends ContentObserver {
        ContentResolver mResolver;

        public ActivityRotationObserver(Handler handler) {
            super(handler);
            mResolver = getActivity().getContentResolver();
        }

        //屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (mPushMoreFragment.isActivityCanRotation(getContext())) {
                mPushMoreFragment.hideOrientationButton();
                setRotationForActivity();
            } else {
                mPushMoreFragment.showOrientationButton();
                // 恢复到正方向
                mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
                // 恢复渲染角度
                mLivePusher.setRenderRotation(0);
                if (mLivePusher.isPushing())
                    mLivePusher.setConfig(mLivePushConfig);
            }
        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false, this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      工具函数
    //
    /////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取资源图片
     *
     * @param resources
     * @param id
     * @return
     */
    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }


    /**
     * 保存并分享图片
     *
     * @param bmp
     */
    private void saveAndSharePic(final Bitmap bmp) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String bitmapFileName = UUID.randomUUID().toString();//通过UUID生成字符串文件名
                FileOutputStream out = null;

                File sdcardDir = getActivity().getExternalFilesDir(null);
                if (sdcardDir == null) {
                    Log.e(TAG, "sdcardDir is null");
                    return;
                }
                final String path = sdcardDir + File.separator + bitmapFileName + ".png";
                final File file = new File(path);
                try {
                    file.getParentFile().mkdirs();
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file.exists() && file.length() > 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "截图成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);//设置分享行为
                            Uri uri = FileUtils.getUri(getContext(), "com.tencent.liteav.demo", file);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(intent, "图片分享"));
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "截图失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPagerCloce() {
        super.onPagerCloce();
        if (!currPagerIsShow()) {//当前界面不可见
            return;
        }
        if (mLivePusher == null || !mLivePusher.isPushing()) {//直播未开始
            exitCapture("确定要退出直播吗?");
        } else {
            exitCapture("您正在直播，确定要结束直播吗？");
        }
    }

    @Override
    public void switchCam() {
        super.switchCam();
        isRearCamera = !isRearCamera;
        setOpenMirror();
        if (mLivePusher != null) {
            mLivePusher.switchCamera();
        }
    }

    private void setOpenMirror() {
        // 是否开启观众端镜像观看
        mLivePusher.setMirror(!isRearCamera);
    }

    @Override
    public void switchAudio() {
        super.switchAudio();

    }

    @Override
    public void onSetViewPlayStatus(boolean isPlay) {
        super.onSetViewPlayStatus(isPlay);
    }

    /**
     * 退出
     */
    private void exitCapture(String msg) {
        showConfirmDialog("", msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去通知结束直播
                upServerLiveExit();
                getActivity().finish();
            }
        }, null);

    }
}

