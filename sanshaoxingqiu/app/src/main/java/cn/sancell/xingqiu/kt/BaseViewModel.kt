package handbank.hbwallet


import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel(), LifecycleObserver {

    val mException: MutableLiveData<Exception> = MutableLiveData()

    val errMsg: MutableLiveData<String> = MutableLiveData()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
        // GlobalScope.launch(Dispatchers.Main) { block() }

    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
        }
    }


    private suspend fun tryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                Log.i("keey", "eeee:" + e.message)
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    //  ToastHelper.showToast("网络异常")
                    mException.value = e
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

    suspend fun executeResponse(
            response: ResResponse<Any>, successBlock: suspend CoroutineScope.() -> Unit,
            errorBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            if (response.retCode != 0) errorBlock()
            else successBlock()
        }
    }

}
