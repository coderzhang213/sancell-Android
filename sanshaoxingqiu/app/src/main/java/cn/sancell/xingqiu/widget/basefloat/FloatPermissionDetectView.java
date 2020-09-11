package cn.sancell.xingqiu.widget.basefloat;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.tencent.rtmp.ui.TXCloudVideoView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.FloatLiveInfo;
import cn.sancell.xingqiu.live.help.TengXunLiveHelp;
import cn.sancell.xingqiu.live.help.TengXunVodHelp;
import cn.sancell.xingqiu.live.listener.OnLivePlayManager;
import cn.sancell.xingqiu.live.listener.OnLiveStatusLinsener;

public class FloatPermissionDetectView extends AbsFloatBase implements View.OnTouchListener {
    private FloatLiveInfo mFloatLiveInfo;
    private TXCloudVideoView video_view;
    private OnLivePlayManager mTengXunLiveHelp;
    private ImageView iv_icon;

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
    private boolean isMove;//判断悬浮窗是否移动

    public FloatPermissionDetectView(Context context, FloatLiveInfo mFloatLiveInfo) {
        super(context);
        this.mFloatLiveInfo = mFloatLiveInfo;
        initPlay();
    }

    @Override
    public void create() {
        super.create();
        mViewMode = WRAP_CONTENT_TOUCHABLE;

        mGravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;

        inflate(R.layout.main_layout_float_permission_detect);
        video_view = findView(R.id.video_view);
        iv_icon = findView(R.id.iv_icon);
        findView(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTengXunLiveHelp.stopPlay();
                remove();
            }
        });
        mInflate.setOnTouchListener(this);
    }

    @Override
    public void remove() {
        super.remove();
        if (mTengXunLiveHelp != null) {
            mTengXunLiveHelp.stopPlay();
        }
    }

    private void initPlay() {
        if (mFloatLiveInfo == null) {
            return;
        }
        if (mFloatLiveInfo.isLivePlay) {
            mTengXunLiveHelp = new TengXunLiveHelp(mContext, video_view, iv_icon);
        } else {
            mTengXunLiveHelp = new TengXunVodHelp(mContext, video_view, iv_icon);
        }
        mTengXunLiveHelp.regOnLiveStatusLinsener(new OnLiveStatusLinsener() {
            @Override
            public void onLivePalyEnd() {

            }
        });
        mTengXunLiveHelp.setActivityType(mFloatLiveInfo.liveType);
        mTengXunLiveHelp.initPlay();
        mTengXunLiveHelp.onStartPlay(mFloatLiveInfo.playUrl);

    }

    @Override
    protected void onAddWindowFailed(Exception e) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                mTouchStartX = (int) event.getRawX();
                mTouchStartY = (int) event.getRawY();
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchCurrentX = (int) event.getRawX();
                mTouchCurrentY = (int) event.getRawY();
                mLayoutParams.x += mTouchCurrentX - mTouchStartX;
                mLayoutParams.y += mTouchCurrentY - mTouchStartY;
                mWindowManager.updateViewLayout(mInflate, mLayoutParams);

                mTouchStartX = mTouchCurrentX;
                mTouchStartY = mTouchCurrentY;
                break;
            case MotionEvent.ACTION_UP:
                mStopX = (int) event.getX();
                mStopY = (int) event.getY();
                if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
                    isMove = true;
                }
                break;
        }
        return true;
    }


}
