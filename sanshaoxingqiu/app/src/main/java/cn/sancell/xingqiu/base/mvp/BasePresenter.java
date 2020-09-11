package cn.sancell.xingqiu.base.mvp;


import java.lang.ref.WeakReference;


/**
 * @author: huyingying.
 * @date: 2018/7/25
 * @description: base presenter
 */
public abstract class BasePresenter<V extends BaseView> implements IBasePresenter<V> {

    protected WeakReference<V> mViewRef;

    @Override
    public void takeView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    @Override
    public void dropView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected V getView() {
        if (mViewRef != null) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isViewAttached() {
        return mViewRef.get() != null;
    }

}

