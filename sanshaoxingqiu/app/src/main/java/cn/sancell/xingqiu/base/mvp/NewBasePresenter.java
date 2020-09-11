package cn.sancell.xingqiu.base.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
  * @author Alan_Xiong
  *
  * @desc: 基础presenter
  * @time 2019-11-13 17:13
  */
public class NewBasePresenter<V extends BaseView> implements IBasePresenter<V> {

    protected WeakReference<V> mViewRef;

    private Context mContext;

    public NewBasePresenter(Context context) {
        mContext = context;
    }

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
