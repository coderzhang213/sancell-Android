package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import cn.sancell.xingqiu.R;

/**
 * Created by ai11 on 2019/6/6.
 */

public class TimeCount extends CountDownTimer {
    private TextView btn_getcode;
    private Context context;

    public TimeCount(Context context, long millisInFuture, long countDownInterval, TextView btn_getcode) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.btn_getcode = btn_getcode;
        this.context = context;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        btn_getcode.setText(context.getResources().getString(R.string.re_send_code));
        btn_getcode.setTextColor(context.getResources().getColor(R.color.color_131413));
        btn_getcode.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        btn_getcode.setClickable(false);
        btn_getcode.setTextColor(context.getResources().getColor(R.color.color_BEBFBE));
        btn_getcode.setText(context.getResources().getString(R.string.re_send_code) + millisUntilFinished / 1000
                + context.getResources().getString(R.string.second_text));
    }
}
