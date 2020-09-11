package cn.sancell.xingqiu.constant

import cn.sancell.xingqiu.util.RxTimerUtil

/**
 * Created by zj on 2020/1/3.
 */
class CountdownManager {
    private var currentTime: Long? = 0
    private var mRxTimerUtil: RxTimerUtil

    init {
        mRxTimerUtil = RxTimerUtil()

    }

    companion object {
        private var instance: CountdownManager? = null
            get() {
                if (field == null) {
                    field = CountdownManager()
                }
                return field
            }

        fun get(): CountdownManager {
            return instance!!
        }
    }

    fun onCancer() {
        currentTime = 0L
        mRxTimerUtil.cancel()
    }

    fun initTime(currentEmpTime: Long) {
        //服务器给的是秒
        if (currentTime == 0L) {
            currentTime = currentEmpTime + 1
            mRxTimerUtil.interval(1000, object : RxTimerUtil.IRxNext {
                override fun doNext(number: Long) {
                    val empTime = currentTime!!
                    currentTime = empTime + 1
                }
            })
        }

    }

    /**
     * 获取当前时间
     */
    fun getCurrTime(): Long {
        return currentTime!!
    }
}