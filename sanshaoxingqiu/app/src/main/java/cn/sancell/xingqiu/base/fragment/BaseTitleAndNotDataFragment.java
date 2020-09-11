package cn.sancell.xingqiu.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.widget.Toolbar;

import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.mvp.BasePresenter;

/**
 * Created by zj on 2017/5/3.
 * 带title的
 */

public abstract class BaseTitleAndNotDataFragment<P extends BasePresenter> extends BaseFragment<P> implements View.OnTouchListener {
    private View notView;
    private View netWorkError;
    private View loadData;//数据加载匡
    private ImageView iv_not_data_icon;//暂无数据图标
    private TextView tv_not_data_text;//暂无数据文字
    private Toolbar toolbar_navigation;//title
    private TextView tv_title;//显示title名字

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ActivityManage:", this.getClass().getName());// 打印出每个activity的类名
        LinearLayout viewById = null;
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            //加一层布局
            View mR = inflater.inflate(R.layout.base_load_layout, container, false);
            toolbar_navigation = mR.findViewById(R.id.toolbar_navigation);
            //设置返回
            initTitle();
            viewById = mR.findViewById(R.id.ll_add_content);
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mRoot.setOnTouchListener(this);
            if (isLoadNotDat()) {//是否需要增加暂无数据

                RelativeLayout load = (RelativeLayout) inflater.inflate(R.layout.load_layout, container, false);
                netWorkError = inflater.inflate(R.layout.network_error_data_layout, container, false);

                netWorkError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onReloadData();
                    }
                });
                load.addView(mRoot);
                //暂无数据布局
                notView = inflater.inflate(R.layout.not_data_layout, container, false);
                notView.setVisibility(View.GONE);
                load.addView(notView);
                //添加网络出差显示的布局
                netWorkError.setVisibility(View.GONE);
                load.addView(netWorkError);
                //加载匡
                loadData = inflater.inflate(R.layout.data_load_layout, container, false);
                loadData.setVisibility(View.GONE);
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
            iv_not_data_icon = (ImageView) notView.findViewById(R.id.iv_not_data_icon);
        }
        if (tv_not_data_text == null) {
            tv_not_data_text = (TextView) notView.findViewById(R.id.tv_not_data_text);
        }
        iv_not_data_icon.setBackgroundResource(iconId);
        tv_not_data_text.setText(msg);
        notView.setVisibility(View.VISIBLE);

    }

    private void initTitle() {
        tv_title = mRoot.findViewById(R.id.tv_title);
        toolbar_navigation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    /**
     * 隐藏我的
     */
    public void hideNotData() {
        if (!isLoadNotDat()) {
            return;
        }
        notView.setVisibility(View.GONE);
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


    /**
     * 设置没有数据
     */
    public void setNotData(String msg) {
        setNotData(msg, R.mipmap.common_no_data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * @param textName
     */
    protected void setTitleName(String textName) {
        tv_title.setText(textName);
    }

    /**
     * 设置没有数据
     */
    public void setNotData(int iconId) {
        setNotData(getActivity().getResources().getString(R.string.not_data), iconId);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 默认图标
     */
    public void setNotData() {
        setNotData(getActivity().getResources().getString(R.string.not_data), R.mipmap.common_no_data);
    }

    /**
     * 暂无网络
     */
    public void setNewWorkError() {

    }

    /**
     * 隐藏掉暂无网络
     */
    public void gonNewWorkError() {
        if (isLoadNotDat() && netWorkError != null) {
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
    public void setShowLoadData() {
        if (isLoadNotDat()) {
            loadData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏加载框
     */
    public void setHideLoadData() {
        if (isLoadNotDat()) {
            loadData.setVisibility(View.GONE);
        }
    }
    @Override
    public void showLoading(boolean show) {

    }
}
