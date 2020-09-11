package cn.sancell.xingqiu.base.fragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.mvp.BasePresenter;

/**
 * Created by zj on 2017/5/3.
 */

public abstract class BaseNotDataFragment<P extends BasePresenter> extends BaseFragment<P> implements View.OnTouchListener {
    private View notView;
    private View netWorkError;
    private View loadData;//数据加载匡
    private ImageView iv_not_data_icon;//暂无数据图标
    private TextView tv_not_data_text;//暂无数据文字
    //加载动画
    private AnimationDrawable mLadAnimation;
    private ImageView loading_iv;//动画加载img

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout viewById = null;
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            //加一层布局
            View mR = inflater.inflate(R.layout.base_not_title_layout, container, false);
            viewById = mR.findViewById(R.id.ll_add_content);
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mRoot.setOnTouchListener(this);
            if (isLoadNotDat()) {//是否需要增加暂无数据
                RelativeLayout load = (RelativeLayout) inflater.inflate(R.layout.load_layout, container, false);
                load.addView(mRoot);
                notView = inflater.inflate(R.layout.not_data_layout, container, false);
                notView.setVisibility(View.GONE);
                load.addView(notView);
                netWorkError = inflater.inflate(R.layout.network_error_data_layout, container, false);
                netWorkError.setVisibility(View.GONE);
                load.addView(netWorkError);
                netWorkError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用重新加载数据
                        onReloadData();
                    }
                });
                //加载匡
                loadData = inflater.inflate(R.layout.data_load_layout, container, false);
                loadData.setVisibility(View.GONE);
                loading_iv = loadData.findViewById(R.id.loading_iv);
                load.addView(loadData);
                viewById.addView(load);
            } else {
                viewById.addView(mRoot);
            }

            mRoot = viewById;
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

    protected abstract void onReloadData();

    /**
     * 设置没有数据
     */
    public void setNotData(String msg, int iconId) {
        if (!isLoadNotDat()) {
            return;
        }
        if (iv_not_data_icon == null) {
            iv_not_data_icon = notView.findViewById(R.id.iv_not_data_icon);
        }
        if (tv_not_data_text == null) {
            tv_not_data_text = notView.findViewById(R.id.tv_not_data_text);
        }
        iv_not_data_icon.setBackgroundResource(iconId);
        tv_not_data_text.setText(msg);
        notView.setVisibility(View.VISIBLE);

    }


    /**
     * 判断当前是否暂无数据了
     *
     * @return
     */
    public boolean getNotDataIsVisib() {
        if (notView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        onMyTouch();
        if (null != getActivity().getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 点击了布局
     */
    public void onMyTouch() {//点击其他地方的时候，EditText失去焦点

        mRoot.setFocusable(true);
        mRoot.setFocusableInTouchMode(true);
        mRoot.requestFocus();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 设置没有数据
     */
    public void setNotData(String msg) {
        setNotData(msg, R.mipmap.common_no_data);
    }

    /**
     * 设置没有数据
     */
    public void setNotData(int iconId) {
        setNotData(getActivity().getResources().getString(R.string.not_data), iconId);
    }


    /**
     * 默认图标
     */
    public void setNotData() {
        setNotData(getActivity().getResources().getString(R.string.not_data), R.mipmap.common_no_data);
    }

    /**
     * 隐藏我的
     */
    public void hideNotData() {
        if (!isLoadNotDat()) {
            return;
        }
        if (notView.getVisibility() == View.VISIBLE) {
            notView.setVisibility(View.GONE);
        }

    }

    /**
     * 暂无网络
     */
    public void showNewWorkError() {
        if (isLoadNotDat() && netWorkError.getVisibility() == View.GONE) {
            netWorkError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏掉暂无网络
     */
    public void goneNewWorkError() {
        if (isLoadNotDat() && netWorkError.getVisibility() == View.VISIBLE) {
            netWorkError.setVisibility(View.GONE);
        }
    }

    /**
     * 是否需要添加暂无数据
     *
     * @return
     */
    public abstract boolean isLoadNotDat();

    /**
     * 显示加载数据匡
     */
    public void showLoadData() {
        if (isLoadNotDat()) {
            if (mLadAnimation == null) {
                mLadAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.audio_animate);
                loading_iv.setImageDrawable(mLadAnimation);
            }
            mLadAnimation.start();
            loadData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 停止动画
     */
    private void stiopAnimation() {
        if (mLadAnimation != null && mLadAnimation.isRunning()) {
            mLadAnimation.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stiopAnimation();
    }

    /**
     * 隐藏加载框
     */
    public void hideLoadData() {
        goneNewWorkError();
        if (isLoadNotDat()) {
            stiopAnimation();
            loadData.setVisibility(View.GONE);
        }
    }
    @Override
    public void showLoading(boolean show) {

    }
}
