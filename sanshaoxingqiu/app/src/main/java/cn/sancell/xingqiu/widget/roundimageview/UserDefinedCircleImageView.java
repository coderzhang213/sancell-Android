package cn.sancell.xingqiu.widget.roundimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2020/1/6.
 */
public class UserDefinedCircleImageView extends AppCompatImageView {
    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    private List<Integer> type_round = new ArrayList<>();
    //圆形
    public static final int TYPE_CIRCLE = 0;
    //自定义角度
    public static final int TYPE_CUSTOMIZE = 1;
    //圆角
    public static final int TYPE_ROUND = 2;


    //左上为直角
    public static final int TOP_LEFT = 2;
    //左下为直角
    public static final int BOTTOM_LEFT = 3;
    //右上为直角
    public static final int TOP_RIGHT = 4;
    //右下为直角
    public static final int BOTTOM_RIGHT = 5;
    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;
    private RectF mRoundRect;

    public UserDefinedCircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);

        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BODER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));// 默认为10dp
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle
        if (type == TYPE_CUSTOMIZE) {
            type_round.add(a.getInt(R.styleable.RoundImageView_r_corner_radius_bottom_left, BOTTOM_LEFT));
            type_round.add(a.getInt(R.styleable.RoundImageView_r_corner_radius_bottom_right, BOTTOM_RIGHT));
            type_round.add(a.getInt(R.styleable.RoundImageView_r_corner_radius_top_left, TOP_LEFT));
            type_round.add(a.getInt(R.styleable.RoundImageView_r_corner_radius_top_right, TOP_RIGHT));
        }

        a.recycle();
    }

    public UserDefinedCircleImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        if (bmp == null) {
            return;
        }
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_CUSTOMIZE || type == TYPE_ROUND) {
            Log.e("TAG",
                    "b'w = " + bmp.getWidth() + " , " + "b'h = "
                            + bmp.getHeight());
            if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
                        getHeight() * 1.0f / bmp.getHeight());
            }

        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "onDraw");
        if (getDrawable() == null) {
            return;
        }
        setUpShader();

        if (type == TYPE_CUSTOMIZE) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
            //哪个角不是圆角我再用矩形画出来
            for (int i = 0; i < type_round.size(); i++) {
                //1.把左上角重新绘制成矩形
                if (type_round.get(i) == TOP_LEFT) {
                    canvas.drawRect(0, 0, mBorderRadius, mBorderRadius, mBitmapPaint);
                }
                //2.把右上角重新绘制成矩形
                if (type_round.get(i) == TOP_RIGHT) {
                    canvas.drawRect(mRoundRect.right - mBorderRadius, 0, mRoundRect.right, mBorderRadius, mBitmapPaint);
                }
                //3.把左下角重新绘制成矩形
                if (type_round.get(i) == BOTTOM_LEFT) {
                    canvas.drawRect(0, mRoundRect.bottom - mBorderRadius, mBorderRadius, mRoundRect.bottom, mBitmapPaint);
                }
                //4.把右下角重新绘制成矩形
                if (type_round.get(i) == BOTTOM_RIGHT) {
                    canvas.drawRect(mRoundRect.right - mBorderRadius, mRoundRect.bottom - mBorderRadius, mRoundRect.right, mRoundRect.bottom, mBitmapPaint);
                }
            }

        } else if (type == TYPE_ROUND) {//
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
            // drawSomeThing(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 圆角图片的范围
        if (type == TYPE_CUSTOMIZE || type == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, w, h);
        }
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    /**
     * 圆形图片或四个角都为圆角时调用
     *
     * @param type
     */
    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_CUSTOMIZE && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    /**
     * 自定义某一个或多个角为圆角时用
     *
     * @param type
     * @param whereNeedKeepRightAngle 传入需要保持直角的位置
     */
    public void setType(int type, int... whereNeedKeepRightAngle) {
        for (int item : whereNeedKeepRightAngle) {
            type_round.add(item);
        }
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_CUSTOMIZE && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}
