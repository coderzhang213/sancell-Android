package handbank.hbwallet

import android.util.Log
import cn.sancell.xingqiu.util.RetrofitUtil


open class BaseRepository {
    val mServe by lazy {  RetrofitUtil.getInstance().initApi() }
    suspend fun <T : Any> apiCall(call: suspend () -> ResResponse<T>): ResResponse<T> {
        return call.invoke()
    }

}