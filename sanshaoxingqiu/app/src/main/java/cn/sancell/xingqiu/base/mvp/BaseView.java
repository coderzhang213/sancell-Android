package cn.sancell.xingqiu.base.mvp;


import com.trello.rxlifecycle3.LifecycleTransformer;

/**
 * @author: huyingying.
 * @date: 2018/7/25
 * @description: base
 */

public interface BaseView {

    void showLoading(boolean show);

    <T> LifecycleTransformer<T> bindToLifecycle();

}
