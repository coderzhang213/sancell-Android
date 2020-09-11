package cn.sancell.xingqiu.base;

import android.os.Bundle;
import android.util.Log;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.widget.Loading_view;

/**
 * Created by ai11 on 2019/6/12.
 */

public abstract class BaseLazyMVPFragment<P extends BasePresenter> extends BaseLazyFragment implements BaseView {
    protected P mPresenter;
    protected Loading_view loading_view;
    private String TAG = "fragment_life";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.takeView(getMVPView());
        loading_view = new Loading_view(getActivity(), R.style.LoadingDialog);
        Log.i(TAG, this.getClass().getName() + " -onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
        mPresenter = null;
        Log.i(TAG, this.getClass().getName() + " -onDestroy");
    }


    protected abstract P createPresenter();


    protected abstract BaseView getMVPView();

    protected P getMPresenter() {
        return mPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ActivityManage:", this.getClass().getName()); // 打印出每个activity的类名
        Log.i(TAG, this.getClass().getName() + " -onDestroy");
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            if (loading_view != null) {
                loading_view.show();
            }
        } else {
            if (loading_view != null) {
                loading_view.dismiss();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, this.getClass().getName() + " -onStart");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, this.getClass().getName() + " -onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, this.getClass().getName() + " -onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, this.getClass().getName() + " -onDestroyView");
    }
}
