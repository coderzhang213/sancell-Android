package cn.sancell.xingqiu.glide;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zj on 2018/1/24 0024.
 */

public class ImgLogInterceptor implements Interceptor {
    private boolean isDebug;
    //可以从连几次

    public ImgLogInterceptor(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (isDebug) {
            Log.i("HttpRequestImg:", "okhttp3:" + request.toString());//输出请求前整个url
        }
        //去执行网络请求
        Response response = chain.proceed(request);
        if (isDebug) {
            Log.i("HttpResponseImg", "requestUrl:" + request.url().toString()+"___" + "code:" + response.code());
        }
        return response;
    }
}
