package cn.sancell.xingqiu.base;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.base.mvp.BaseView;


/**
 * @author huyingying
 * @date 2018/5/15 11:51
 * @description MVP模式下的Activity的基类
 */
public abstract class BaseMVPActivity<P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.takeView(getView());
        }


        mUnbinder = ButterKnife.bind(this);
        initial();
        AppManager.getInstance().addActivity(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.dropView();
            mPresenter = null;
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }


    protected abstract P createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initial();

    protected abstract BaseView getView();

}
