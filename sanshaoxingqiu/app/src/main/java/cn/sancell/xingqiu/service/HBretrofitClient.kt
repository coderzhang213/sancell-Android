package cn.sancell.xingqiu.service

import cn.sancell.xingqiu.constant.Constants
import okhttp3.OkHttpClient


object HandBankretrofitClient : BaseRetrofitClient() {

    val service by lazy {
        if (true) {
            getService(Api::class.java, Constants.Link.getHost())
        } else {
            getService(Api::class.java, Constants.Link.getHost())
        }
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chains ->
            var request = chains.request()
            val response = chains.proceed(request)
            response.newBuilder()
                .build()
            response
        }
    }
}