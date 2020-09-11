package cn.sancell.xingqiu.util

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by zj on 2019/12/20.
 */
object CommonUtils {
    /**
     * HashMap params convert to Json String
     * @return RequestBody
     */
    @JvmStatic
    fun convertToJson(param: HashMap<String, String>): RequestBody {
        val strEntity = Gson().toJson(param)
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity)
    }
}