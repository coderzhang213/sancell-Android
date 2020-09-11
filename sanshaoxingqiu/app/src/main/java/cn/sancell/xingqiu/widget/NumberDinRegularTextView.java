package cn.sancell.xingqiu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.sancell.xingqiu.SCApp;

/**
 * Created by ai11 on 2019/9/3.
 */

@SuppressLint("AppCompatCustomView")
public class NumberDinRegularTextView extends TextView {
    public NumberDinRegularTextView(Context context) {
        super(context);
        //设置字体
        setTypeface(SCApp.getInstance().getTypefaceRegular());
    }

    public NumberDinRegularTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置字体
        setTypeface(SCApp.getInstance().getTypefaceRegular());
    }

    public NumberDinRegularTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置字体
        setTypeface(SCApp.getInstance().getTypefaceRegular());
    }
}
