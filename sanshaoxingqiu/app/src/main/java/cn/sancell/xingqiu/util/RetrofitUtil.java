package cn.sancell.xingqiu.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.live.config.DemoServers;
import cn.sancell.xingqiu.service.Api;
import cn.sancell.xingqiu.service.ApiService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: liumingran.
 * @date: 2018/7/25
 * @description: retrofit请求工具类
 */

public class RetrofitUtil {
    /**
     * 超时时间
     */
    private static volatile RetrofitUtil mInstance;
    private ApiService allApi;

    /**
     * 单例封装
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Retrofit
     */
    public ApiService initRetrofit() {
        return getServerRetrofit(Constants.Link.getHost()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(ApiService.class);
    }

    private Retrofit.Builder getServerRetrofit(String baseUrl) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        return new Retrofit.Builder()
                .client(SCApp.initOKHttp())
                // 设置请求的域名
                .baseUrl(baseUrl)
                // 设置解析转换工厂
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    /**
     * 用于 适配协程
     *
     * @return
     */
    public Api initApi() {
        return getServerRetrofit(Constants.Link.getHost()).addCallAdapterFactory(CoroutineCallAdapterFactory.create()).build().create(Api.class);
    }

    /**
     * 不同的域名
     * @return
     */
    public Api initH5Api() {
        return getServerRetrofit(DemoServers.apiServer()).addCallAdapterFactory(CoroutineCallAdapterFactory.create()).build().create(Api.class);
    }
}
