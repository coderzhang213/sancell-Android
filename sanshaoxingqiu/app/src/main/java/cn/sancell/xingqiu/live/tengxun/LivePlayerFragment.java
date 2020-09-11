package cn.sancell.xingqiu.live.tengxun;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.bean.LivePlayInfo;
import cn.sancell.xingqiu.ktenum.LivePagerType;
import cn.sancell.xingqiu.live.base.LiveToolBaseFragment;
import cn.sancell.xingqiu.live.constant.LiveConstant;
import cn.sancell.xingqiu.live.help.TengXunLiveHelp;
import cn.sancell.xingqiu.live.help.TengXunPlayeBaseHelp;
import cn.sancell.xingqiu.live.help.TengXunVodHelp;
import cn.sancell.xingqiu.live.interfacep.OnActivityUiLinsenr;
import cn.sancell.xingqiu.live.interfacep.OnPlayLinsenr;
import cn.sancell.xingqiu.live.interfacep.PLAY_STATE;
import cn.sancell.xingqiu.live.listener.OnGetCurrPagerIsShowLinsener;
import cn.sancell.xingqiu.live.listener.OnLivePlayManager;
import cn.sancell.xingqiu.live.listener.OnLiveStatusLinsener;
import cn.sancell.xingqiu.live.nim.NimController;

/**
 * 腾讯云 {@link TXLivePlayer} 直播播放器使用参考 Demo
 * <p>
 * 有以下功能参考 ：
 * <p>
 * 1. 基本功能参考： 启动推流 {@link #startPlay()}与 结束推流 {@link #stopPlay()}
 * <p>
 * 2. 硬件加速： 使用硬解码
 * <p>
 * 3. 性能数据查看参考： {@link #(Bundle)}
 * <p>
 * 5. 处理 SDK 回调事件参考： {@link #(int, Bundle)}
 * <p>
 * 6. 渲染角度、渲染模式切换： 横竖屏渲染、铺满与自适应渲染
 * <p>
 * 7. 缓存策略选择：{@link #} 缓存策略：自动、极速、流畅。 极速模式：时延会尽可能低、但抗网络抖动效果不佳；流畅模式：时延较高、抗抖动能力较强
 */
public class LivePlayerFragment extends LiveToolBaseFragment implements OnClickListener, OnPlayLinsenr, OnLiveStatusLinsener, OnGetCurrPagerIsShowLinsener {
    private static final String TAG = LivePlayerFragment.class.getSimpleName();

    /**
     * SDK player 相关
     */
    private TXCloudVideoView mPlayerView;

    /**
     * 相关控件
     */
    private ImageView mLoadingView;
    //默认图片背景
    private ImageView iv_def_bg;
    private boolean mIsAcc = false;                               // 播放加速流地址 (用于测试
    //是否正在播放
    private long mStartPlayTS = 0;
    private int mActivityType;

    //是否直播
    private boolean isLivePlay = false;
    private String roomId;
    private String batchId;
    private boolean isStart = false;
    private String mType = LivePagerType.LIVE_PLAY.getType();//1直播；2回放
    private int postion = 0;
    protected String mVideoPath; //文件路径
    //父类互相调用
    private OnActivityUiLinsenr mOnActivityUiLinsenr;
    private String mLType;
    //是否暂停
    private boolean isPauser = false;
    private OnLivePlayManager mTengXunLiveHelp;

    public void setmOnActivityUiLinsenr(OnActivityUiLinsenr mOnActivityUiLinsenr) {
        this.mOnActivityUiLinsenr = mOnActivityUiLinsenr;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        /**
         * 获取传递的参数
         */
        initPar();
        super.onActivityCreated(savedInstanceState);
        setContentView();
        //请求直播详情
        getLiveInfo(batchId, mLType);
        if (isStart) {
            startPlay();
        }
    }

    private void initPar() {
        Bundle arguments = getArguments();
        isLivePlay = arguments.getBoolean(LiveConstant.IS_LIVE_PLAY);
        if (isLivePlay) {
            mActivityType = TengXunPlayeBaseHelp.ACTIVITY_TYPE_LIVE_PLAY;
        } else {
            mActivityType = TengXunPlayeBaseHelp.ACTIVITY_TYPE_VOD_PLAY;
        }
        roomId = arguments.getString(NimController.EXTRA_ROOM_ID);
        postion = arguments.getInt(LiveConstant.LIVE_SHOW_POSTION);
        setShowPostion(postion);
        mVideoPath = arguments.getString(LiveConstant.VOID_PATH);
        isStart = arguments.getBoolean(LiveConstant.IS_START);
        batchId = arguments.getString(LiveConstant.YXID);
        roomId = arguments.getString(NimController.EXTRA_ROOM_ID);

        if (isLivePlay) {
            mType = LivePagerType.LIVE_PLAY.getType();
        } else {
            mType = LivePagerType.REPLAY.getType();
        }
        mLType = arguments.getString(LiveConstant.LIVE_TYPE);

    }

    /**
     * @param roomId     房间id(云信那边的)
     * @param yxId       房间ID(我们自己平台的)
     * @param videoPath  推流地址
     * @param isStart    是否马上播放
     * @param isLivePlay 是直播还是回放
     * @param postion    如果是列表，这个处于列表的索引
     * @return
     */
    public static LivePlayerFragment getInstance(OnActivityUiLinsenr mOnActivityUiLinsenr, String roomId, String yxId, String videoPath,
                                                 boolean isStart, boolean isLivePlay, int postion, String mType) {
        LivePlayerFragment mLiveRoomFragment = new LivePlayerFragment();
        mLiveRoomFragment.setmOnActivityUiLinsenr(mOnActivityUiLinsenr);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LiveConstant.IS_START, isStart);
        bundle.putBoolean(LiveConstant.IS_LIVE_PLAY, isLivePlay);
        bundle.putString(LiveConstant.MEDIA_TYPE, "livestream");
        bundle.putString(NimController.EXTRA_ROOM_ID, roomId);
        bundle.putString(LiveConstant.DECODE_TYPE, "software");
        bundle.putString(LiveConstant.VOID_PATH, videoPath);
        bundle.putString(LiveConstant.YXID, yxId);
        bundle.putInt(LiveConstant.LIVE_SHOW_POSTION, postion);
        bundle.putString(LiveConstant.LIVE_TYPE, mType);
        mLiveRoomFragment.setArguments(bundle);
        return mLiveRoomFragment;
    }

    private void setContentView() {


        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLoadingView = (ImageView) findViewById(R.id.loadingImageView);
        iv_def_bg = (ImageView) findViewById(R.id.iv_def_bg);

        if (isLivePlay) {
            mTengXunLiveHelp = new TengXunLiveHelp(getContext(), mPlayerView, mLoadingView);
        } else {
            mTengXunLiveHelp = new TengXunVodHelp(getContext(), mPlayerView, mLoadingView);
        }
        mTengXunLiveHelp.regOnLiveStatusLinsener(this);
        mTengXunLiveHelp.setOnGetCurrPagerIsShowLinsener(this);
        mTengXunLiveHelp.setActivityType(mActivityType);
        mTengXunLiveHelp.initPlay();
        checkPublishPermission();

    }


    /**
     * 开始播放
     *
     * @return
     */
    private boolean startPlay() {
        String playUrl = mVideoPath;
        mTengXunLiveHelp.onStartPlay(playUrl);
        return true;
    }

    private void stopPlay() {
        mTengXunLiveHelp.stopPlay();
    }


    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      权限检测相关
    //
    /////////////////////////////////////////////////////////////////////////////////
    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
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


    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      Activity 声明周期相关
    //
    /////////////////////////////////////////////////////////////////////////////////

    public void onBackPressed() {
        stopPlay();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "vrender onDestroy");
        mTengXunLiveHelp.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100 || data == null || data.getExtras() == null || TextUtils.isEmpty(data.getExtras().getString("result"))) {
            return;
        }
        String result = data.getExtras().getString("result");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_tengxun_play;
    }

    @NotNull
    @Override
    public String getGetCurrType() {
        return mType;
    }

    @NotNull
    @Override
    public String getGetRoomId() {
        return batchId;
    }

    @Override
    public void onBaseStart() {

        if (mTengXunLiveHelp == null) {
            return;

        }
        if (isPauser) {//如果是暂停，就直接恢复
            Log.i("keey", "resume");
            mTengXunLiveHelp.resume();
        } else {
            Log.i("keey", "startPlay");
            startPlay();
        }
        // startPlay();
        isPauser = false;
        setRePlayStatus(true, false);

    }

    @Override
    public void onBaseStop() {
        //stopPlay();
        if (mTengXunLiveHelp == null) {
            return;
        }
        if (mTengXunLiveHelp.isPlaying()) {
            isPauser = true;
            mTengXunLiveHelp.pause();
        } else {
            mTengXunLiveHelp.stopPlay();
        }

    }

    @NotNull
    @Override
    public PLAY_STATE onBasePlayStates() {
        if (mTengXunLiveHelp == null) {
            return PLAY_STATE.PLAYT_STOP;
        }
        if (mTengXunLiveHelp.isPlaying()) {
            return PLAY_STATE.PLAYT;
        }
        return PLAY_STATE.PLAYT_STOP;
    }

    @Override
    public void onPagerCloce() {
        super.onPagerCloce();
        if (!currPagerIsShow()) {
            return;
        }

        exitCapture("确定退出?");
    }

    @Override
    public void bingParViewText(@NotNull LivePlayInfo mLivePlayInfo) {
        super.bingParViewText(mLivePlayInfo);

        //设置默认显示图
//        Glide.with(getContext()).load(mLivePlayInfo.getBatchInfo().getIcon()).addListener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                Context context = getContext();
//                if (context != null) {
//                    iv_def_bg.setImageBitmap(new RenderScriptGaussianBlur(context).gaussianBlur(20, BitmapUtils.drawableToBitmap(resource)));
//
//                }
//
//                return false;
//            }
//        }).preload();
    }

    /**
     * 退出
     */
    private void exitCapture(String msg) {
        showConfirmDialog("", msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopPlay();
                getActivity().finish();
            }
        }, null);

    }

    @Override
    public void onLiveEndPlay() {
        stopPlay();
        showLivePlayEnd();
    }


    @Override
    public void onPlayStatus(boolean isPlay) {
        super.onPlayStatus(isPlay);
        if (mTengXunLiveHelp == null) {
            return;
        }
        if (isPlay) {
            mTengXunLiveHelp.resume();
        } else {
            mTengXunLiveHelp.pause();
        }
    }

    @Override
    public void onLivePalyEnd() {
        setRePlayStatus(false, false);
    }

    @Override
    public boolean onGetCurrPagerIsShow() {
        return currPagerIsShow();
    }

    @Override
    public void onPlayError(@NotNull String eror) {
        if (mOnActivityUiLinsenr != null) {
            //  mOnActivityUiLinsenr.hideSendMsg();

        }
        if (getGetCurrType().equals(LivePagerType.LIVE_PLAY.getType())) {//如果是直播先去检查
            getLivieModel().checkLiveRoomStatus(batchId).observe(this, it -> {
                        if (it.getBatchStatus() == 2) {////2 直播中 3已结束 1预告 4 时间段内未直播 5.急停
                            //显示主播离开
                            showLivePlayError(eror);

                        } else {
                            onLiveEndPlay();
                        }
                    }
            );
        } else {
            showLivePlayError(eror);
        }


    }

    @Override
    public void onRePlay() {
        super.onRePlay();
        startPlay();
    }

    @Override
    public void onSetViewPlayStatus(boolean isPlay) {
        super.onSetViewPlayStatus(isPlay);
        if (mTengXunLiveHelp != null) {
            mTengXunLiveHelp.onSetViewPlayStatus(isPlay);
        }

    }

    @Override
    public void isShowStreamline(boolean isShow) {
        super.isShowStreamline(isShow);
        if (mOnActivityUiLinsenr != null) {
            mOnActivityUiLinsenr.onShowStreamline(isShow);

        }

    }


    @Override
    public void onSetPlayStatus(boolean isPlay) {

    }
}