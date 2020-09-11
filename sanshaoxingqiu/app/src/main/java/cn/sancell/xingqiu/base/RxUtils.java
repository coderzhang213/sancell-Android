package cn.sancell.xingqiu.base;

import cn.sancell.xingqiu.base.mvp.BaseView;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Alan_Xiong
 * @desc: 管理rx请求与生命周期
 * @time 2019-10-22 10:50
 */
public class RxUtils {

    public static <T> ObservableTransformer<T, T> transform(BaseView view) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(view.bindToLifecycle());
    }

    /**
     * 展示loading
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> transformerWithLoading(BaseView view) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    view.showLoading(true);
                }).observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    view.showLoading(false);
                }).compose(view.bindToLifecycle());
    }

    public static <T> ObservableTransformer<T, T> transform() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
