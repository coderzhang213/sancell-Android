package cn.sancell.xingqiu.base.entity;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.login.CodeLoginActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author: huyingying.
 * @date: 2018/7/25
 * @description: 自定义Observer
 */

public abstract class BaseObserver<T> implements Observer<BaseEntry<T>> {
    protected Context mContext;


    public BaseObserver(Context cxt) {
        this.mContext = cxt;
    }

    //开始
    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    //获取数据
    @Override
    public void onNext(BaseEntry<T> tBaseEntity) {
        try {
            if (tBaseEntity.getRetCode() == 100216) {
                if (SCApp.getInstance().currentActivity() != null) {
                    SCApp.getInstance().currentActivity().startActivity(new Intent(SCApp.getInstance().currentActivity().getApplicationContext(), CodeLoginActivity.class));
                }else {
                    onSuccess(tBaseEntity);
                }
            } else {
                onSuccess(tBaseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //失败
    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);  //网络错误
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //结束
    @Override
    public void onComplete() {
        onRequestEnd();//请求结束
    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccess(BaseEntry<T> t) throws Exception;


    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    protected void onRequestStart() {

    }

    protected void onRequestEnd() {

    }

}
