package cn.sancell.xingqiu.constant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {
    private String versionName = "";

    public BaseInterceptor(String versionName) {
        this.versionName = versionName;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
//添加请求参数
        //    HttpUrl url = original.url().newBuilder()
        //   .addQueryParameter("platform", "ios")
        //   .build();
//添加请求头
        Request request = original.newBuilder()
                .addHeader("version", versionName)
                .addHeader("clientId", "3")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}