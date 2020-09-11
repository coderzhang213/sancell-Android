package cn.sancell.xingqiu.base;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle3.components.support.RxFragment;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.widget.Loading_view;

/**
 * Created by ai11 on 2019/5/8.
 */

public abstract class BaseMVPFragment<P extends BasePresenter> extends RxFragment implements BaseView {
    protected P mPresenter;
    protected Loading_view loading_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.takeView(getMVPView());
        loading_view = new Loading_view(getActivity(), R.style.LoadingDialog);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
        mPresenter = null;
    }


    protected abstract P createPresenter();

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ActivityManage:", this.getClass().getName());
    }

    protected abstract BaseView getMVPView();

    protected P getMPresenter() {
        return mPresenter;
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
}
