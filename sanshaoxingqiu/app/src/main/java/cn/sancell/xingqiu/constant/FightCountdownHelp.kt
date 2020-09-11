package cn.sancell.xingqiu.constant

import android.util.Log
import android.widget.TextView
import cn.sancell.xingqiu.util.RxTimerUtil
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger

/**
 * Created by zj on 2020/1/3.
 */
class FightCountdownHelp {
    private var textView: TextView? = null
    private var mEndTime: Long? = 0
    private var mCurrTime: Long? = 0
    private var mRxTimerUtil: RxTimerUtil
    private var id = ""

    init {
        mRxTimerUtil = RxTimerUtil()
    }

    constructor(tvView: TextView, mEmpCurrTime: Long, endTime: Long) {
        textView = tvView
        mCurrTime = mEmpCurrTime
        mEndTime = endTime
    }

    fun start() {
        mRxTimerUtil.interval(1000, object : RxTimerUtil.IRxNext {
            override fun doNext(number: Long) {
                mCurrTime = mCurrTime!! + 1000
                val startTime = mEndTime!! - mCurrTime!!
                if (startTime > 0) {
                    textView?.setText(muntTime(startTime))
                } else {
                    cencar()
                    //通知刷新列表
                    ObserverManger.getInstance(ObserverKey.UP_FIGHT_LIST).notifyObserver(null)
                    textView?.setText("已结束")
                }
            }
        })
    }

    fun cencar() {
        mRxTimerUtil.cancel()
    }

    fun muntTime(time: Long): String {
        val day: Long = time / (1000 * 24 * 60 * 60) //单位天


        val hour: Long = (time - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60)
        //单位时
        val minute: Long = (time - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60)
        //单位分
        val second: Long = (time - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
        //单位秒
        var strHour = ""
        var strMinute = ""
        var strSecond = ""
        if (hour < 10) {
            strHour = "0" + hour.toString()
        } else {
            strHour = hour.toString()
        }
        if (minute < 10) {
            strMinute = "0" + minute.toString()
        } else {
            strMinute = minute.toString()
        }

        if (second < 10) {
            strSecond = "0" + second.toString()
        } else {
            strSecond = second.toString()
        }
        return strHour + ":" + strMinute + ":" + strSecond
    }
}