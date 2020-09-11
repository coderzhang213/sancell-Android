package cn.sancell.xingqiu.constant;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.util.PreferencesUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author: huyingying.
 * @date: 2018/7/25
 * @description:
 */

public class CookiesSaveInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            String header = originalResponse.header("Set-Cookie");
            PreferencesUtils.put("cookiess", header);
        }
        return originalResponse;
    }

}
