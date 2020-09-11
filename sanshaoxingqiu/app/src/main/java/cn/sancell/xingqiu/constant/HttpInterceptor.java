package cn.sancell.xingqiu.constant;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zj on 2020/1/13.
 */
public class HttpInterceptor implements Interceptor {
    private String versionName;

    public HttpInterceptor(String versionName) {
        this.versionName = versionName;
    }

    @Override
    @NotNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //GET、POST
        String method = request.method();
        if ("GET".equals(method)) {
            HttpUrl url = request.url();
            HttpUrl newHttpUrl = url.newBuilder()
                    .addQueryParameter("clientId", "3")
                    .build();
            Request updateRequest = request.newBuilder().url(newHttpUrl).build();
            return chain.proceed(updateRequest);

        } else if ("POST".equals(method)) {
            FormBody.Builder builder = new FormBody.Builder();
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                //将以前的参数添加
                for (int i = 0; i < formBody.size(); i++) {
                    builder.add(formBody.encodedName(i), formBody.encodedValue(i));

                }
            }

            builder.add("clientId2", "3");
            RequestBody body = builder.build();

            Request updateRequest = request.newBuilder()
                    .post(body)
                    .build();
            return chain.proceed(updateRequest);

        } else {
            return chain.proceed(request);
        }
    }
}
