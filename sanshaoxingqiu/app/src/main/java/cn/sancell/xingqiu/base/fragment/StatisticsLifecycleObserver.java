package cn.sancell.xingqiu.base.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by zj on 2019/5/23.
 * 跟activity fragment  生命周期同步
 */
public class StatisticsLifecycleObserver implements LifecycleObserver {
    private boolean bNeedStatAnalytics;// 是否需要使用默认的友盟&GA统计方式
    private Context context;
    private Activity mActivity;
    private boolean isActivity;
    private Fragment fragment;

    public StatisticsLifecycleObserver(Activity mActivity, boolean bNeedStatAnalytics, Object object) {
        this.bNeedStatAnalytics = bNeedStatAnalytics;
        this.mActivity = mActivity;
        this.context = mActivity.getApplication();
        this.isActivity = true;
        initMode(object);
    }

    private void initMode(Object analystic) {
        String screen_name = "";

    }

    public StatisticsLifecycleObserver(Fragment fragment, boolean bNeedStatAnalytics, Object object) {
        this.bNeedStatAnalytics = bNeedStatAnalytics;
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.isActivity = false;
        initMode(object);
    }

    /**
     * 是否打开关闭
     *
     * @param isColse
     */
    public void isCloseStatistics(boolean isColse) {
        this.bNeedStatAnalytics = isColse;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCraete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {

    }
}
