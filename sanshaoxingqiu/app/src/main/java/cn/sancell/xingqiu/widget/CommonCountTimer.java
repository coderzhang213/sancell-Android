package cn.sancell.xingqiu.widget;

import android.os.CountDownTimer;

import cn.sancell.xingqiu.interfaces.OnTimeBackListener;

/**
 * @author Alan_Xiong
 * @desc: 倒计时
 * @time 2020-01-04 14:55
 */
public class CommonCountTimer extends CountDownTimer {


    private OnTimeBackListener mListener;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CommonCountTimer(long millisInFuture, long countDownInterval, OnTimeBackListener listener) {
        super(millisInFuture, countDownInterval);
        mListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        millisUntilFinished = millisUntilFinished / 1000;
        int hours = (int) (millisUntilFinished / (60 * 60));
        int leftSeconds = (int) (millisUntilFinished % (60 * 60));
        int minutes = leftSeconds / 60;
        int seconds = leftSeconds % 60;
        if (mListener != null) {
            mListener.onTick(hours < 10 ? "0" + hours : hours + "", minutes < 10 ? "0" + minutes : minutes + ""
                    , seconds < 10 ? "0" + seconds : seconds + "");
        }
    }

    @Override
    public void onFinish() {
        mListener.onFinish();
    }

    public void setListener(OnTimeBackListener listener){
        mListener = listener;
    }
}
