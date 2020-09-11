package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ImageOverlyView extends View {


    public int maxPinCount; //最多人数

    public int mAleardyCount; //已参加人数


    public ImageOverlyView(Context context) {
        super(context);
    }

    public ImageOverlyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageOverlyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }
}
