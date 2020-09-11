package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.SmartUtil;

import cn.sancell.xingqiu.R;

/**
 * Created by ai11 on 2019/6/28.
 */

public class SancellToobarClassicsHeader extends LinearLayout implements RefreshHeader {
    private TextView mHeaderText;//标题文本
    private ImageView mProgressView;//刷新动画视图
    //private ProgressDrawable mProgressDrawable;//刷新动画

    public SancellToobarClassicsHeader(Context context) {
        this(context, null);
    }

    public SancellToobarClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        setOrientation(VERTICAL);
        setPadding(0, SmartUtil.dp2px(24), 0, SmartUtil.dp2px(12));
        mHeaderText = new TextView(context);
        mHeaderText.setTextColor(0xff191A19);
        mHeaderText.setTextSize(11);
        //mProgressDrawable = new ProgressDrawable();
        mProgressView = new ImageView(context);
        //mProgressDrawable.setColor(0xff0F9954);
        //mProgressView.setImageDrawable(mProgressDrawable);
        Glide.with(this).asGif().load(R.drawable.icon_im_loading).into(mProgressView);
        addView(mProgressView, SmartUtil.dp2px(24), SmartUtil.dp2px(24));
        addView(new Space(context), SmartUtil.dp2px(2), SmartUtil.dp2px(2));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(SmartUtil.dp2px(79));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        //mProgressDrawable.start();//开始动画
        GifDrawable drawable = (GifDrawable) mProgressView.getDrawable();
        if(drawable!=null&&!drawable.isRunning()) {
            drawable.start();
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        //mProgressDrawable.stop();//停止动画
        GifDrawable drawable = (GifDrawable) mProgressView.getDrawable();
        if(drawable!=null&&drawable.isRunning()) {
            drawable.stop();
        }
        //mProgressView.setVisibility(GONE);//隐藏动画
        if (success) {
            mHeaderText.setText("刷新完成");
        } else {
            mHeaderText.setText("刷新失败");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mHeaderText.setText("下拉刷新");
                mProgressView.setVisibility(VISIBLE);//隐藏动画
                break;
            case Refreshing:
                mHeaderText.setText("正在刷新");
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToRefresh:
                mHeaderText.setText("松开刷新");
                break;
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

//        @Override
//        public void onPulling(float percent, int offset, int height, int maxDragHeight) {
//
//        }
//        @Override
//        public void onReleasing(float percent, int offset, int height, int maxDragHeight) {
//
//        }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
