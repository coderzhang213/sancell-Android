package cn.sancell.xingqiu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;

import cn.sancell.xingqiu.R;


/**
 * Created by camelliae on 2019/3/28.
 * 自定义适配字体大小
 */

@SuppressLint("AppCompatCustomView")
public class ScaleTextView extends TextView {
    private int baseScreenHeight = 780;

    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView);//获得属性值
        int i = type.getInteger(R.styleable.ScaleTextView_textSizePx, 25);
        baseScreenHeight = type.getInteger(R.styleable.ScaleTextView_baseScreenHeight, 780);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(i));
    }

    /**
     * 获取当前手机屏幕分辨率，然后根据和设计图的比例对照换算实际字体大小
     *
     * @return
     */
    private int getFontSize(int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int rate = (int) (textSize * (float) screenHeight / baseScreenHeight);
        return rate;
    }
}
