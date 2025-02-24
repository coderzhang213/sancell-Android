package cn.sancell.xingqiu.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle3.components.support.RxFragment;

import java.io.Serializable;

import butterknife.ButterKnife;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.network.NetWorkMonitorManager;
import cn.sancell.xingqiu.constant.network.NetWorkState;
import cn.sancell.xingqiu.constant.network.onNetWorkStateChangeLinsener;

/**
 * Created by zj on 2018/7/2 0002.
 */
public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements BaseView, onNetWorkStateChangeLinsener {
    protected P mPresenter;
    protected Context mContext;
    protected View mRoot;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;
    //加载dialog
    private StatisticsLifecycleObserver mStatisticsLifecycleObserver;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.takeView(getMVPView());
        initView();
        initData();
        initViewListener();
        NetWorkMonitorManager.getInstance().register(this);
        /*
         * 监听生命周期,用来添加统计信息
         */
        mStatisticsLifecycleObserver = new StatisticsLifecycleObserver(this, true, initAnalyticsScreenName());
        getLifecycle().addObserver(mStatisticsLifecycleObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected abstract P createPresenter();

    protected abstract BaseView getMVPView();

    /**
     * 初始化动态权限类
     */
    protected void initDynamicPermissionsHelp() {

    }

    /**
     * 为友盟、GA统计初始化页面名称,
     * <p>
     * 页面名称和类名一致的字符串，禁止使用Object.class.getClassName()方式获取
     * </p>
     * <p>
     * 返回值： 返回String（表示页面名称） 或者 返回StatModel型
     * </p>
     */
    public abstract Object initAnalyticsScreenName();

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);

    }

    @Override
    public void onNetWorkStateChangeLinener(NetWorkState netWorkState) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ActivityManage:", this.getClass().getName());// 打印出每个activity的类名
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
            // Do something
            onBindViewBefore(mRoot);
            // Bind view
            ButterKnife.bind(this, mRoot);
            // Get savedInstanceState
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            // Init
            initWidget(mRoot);
        }
        return mRoot;
    }

    protected void onBindViewBefore(View root) {
        // ...
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //页面销毁的时候，如果有dialog在显示，也先关闭
        mBundle = null;
        NetWorkMonitorManager.getInstance().unregister(this);
    }

    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }

    protected void initWidget(View root) {

    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }


    protected void setText(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    protected void setText(int viewId, String text, String emptyTip) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setText(emptyTip);
            return;
        }
        textView.setText(text);
    }

    protected void setTextEmptyGone(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setText(text);
    }

    protected <T extends View> T setGone(int id) {
        T view = findView(id);
        view.setVisibility(View.GONE);
        return view;
    }

    protected <T extends View> T setVisibility(int id) {
        T view = findView(id);
        view.setVisibility(View.VISIBLE);
        return view;
    }

    protected void setInVisibility(int id) {
        findView(id).setVisibility(View.INVISIBLE);
    }

    protected void onRestartInstance(Bundle bundle) {

    }

    /**
     * 初始化监听
     */
    protected abstract void initViewListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 申请权限结果的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //动态权限框架
    }

    @Override
    public void showLoading(boolean show) {

    }
}
