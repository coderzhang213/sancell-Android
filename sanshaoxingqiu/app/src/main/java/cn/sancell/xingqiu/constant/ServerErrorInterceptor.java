package cn.sancell.xingqiu.constant;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.sancell.xingqiu.util.observer.ObserverKey;
import cn.sancell.xingqiu.util.observer.ObserverManger;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zj on 2018/6/21 0021.
 * 拦截错误
 */
public class ServerErrorInterceptor implements Interceptor {
    private static Handler mHandler;
    private Gson mGson;

    public ServerErrorInterceptor() {
        mGson = new Gson();
        if (mHandler == null) {
            initHander();
        }
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //去执行网络请求
        Response response = chain.proceed(request);
        if (response.code() != 200) {
            Message message = mHandler.obtainMessage();
            message.what = 2;
            message.obj = null;
            //错误502
            mHandler.sendMessage(message);

        }
        return response;
    }

    /**
     * 初始化一个回调的Handler
     */
    private void initHander() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0://token失效
                        break;
                    case 1://上传成功
                        break;
                    case 2://通知跳转到宕机界面
                        ObserverManger.getInstance(ObserverKey.SERVER_ERROR).notifyObserver(null);
                        break;

                }
            }
        };
    }
}
