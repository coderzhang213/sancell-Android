package cn.sancell.xingqiu.constant;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.util.PreferencesUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: huyingying.
 * @date: 2018/7/27
 * @description: 读取cookie
 */

public class CookieReadInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Cookie", (String) PreferencesUtils.get("cookiess", ""));
        return chain.proceed(builder.build());
    }
}
