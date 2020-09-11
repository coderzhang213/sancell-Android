package cn.sancell.xingqiu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.ScreenUtils;


/**
 * 验证码的EditText
 */


public class VerificationCodeEditText extends androidx.appcompat.widget.AppCompatEditText implements
        VerificationAction, TextWatcher {
    private static final int DEFAULT_CURSOR_DURATION = 400;

    public final static int SHOW_TYPE_WORD = 1;
    public final static int SHOW_TYPE_PASSWORD = 2;

    public final static int SHOW_STYLE_SET = 1; //不带边框
    public final static int SHOW_STYLE_PAY = 2; //带边框

    //显示类型，支持密码、明文，默认密码
    private int showType;
    //密码点半径，默认8dp
    private float pointRadius;

    private int mFigures;//需要输入的位数
    private int mVerCodeMargin;//验证码之间的间距
    private int mBottomSelectedColor;//底部选中的颜色
    private int mBottomNormalColor;//未选中的颜色
    private float mBottomLineHeight;//底线的高度
    private int mSelectedBackgroundColor;//选中的背景颜色
    private int mCursorWidth;//光标宽度
    private int mCursorColor;//光标颜色
    private int mCursorDuration;//光标闪烁间隔
    //描边颜色，默认#E1E1E1
    private int mBorderColor;
    //描边宽度，默认1px
    private int mBorderWidth;
    //分割线颜色，默认#E1E1E1
    private int mDividerColor;
    //分割线宽度，默认1px
    private int mDividerWidth;
    //显示样式，支持是否带边框分割线
    private int showStyle;

    private OnVerificationCodeChangedListener onCodeChangedListener;
    private int mCurrentPosition = 0;
    private int mEachRectLength = 0;//每个矩形的边长
    private Paint mSelectedBackgroundPaint;
    private Paint mNormalBackgroundPaint;
    private Paint mBottomSelectedPaint;
    private Paint mBottomNormalPaint;
    private Paint mCursorPaint;
    private Paint mBorderPaint;
    private Paint mDividerPaint;

    // 控制光标闪烁
    private boolean isCursorShowing;
    private TimerTask mCursorTimerTask;
    private Timer mCursorTimer;

    public VerificationCodeEditText(Context context) {
        this(context, null);
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));//防止出现下划线
        initPaint();
        initCursorTimer();
        setFocusableInTouchMode(true);
        super.addTextChangedListener(this);
    }

    /**
     * 初始化paint
     */
    private void initPaint() {
        mSelectedBackgroundPaint = new Paint();
        mSelectedBackgroundPaint.setColor(mSelectedBackgroundColor);
        mNormalBackgroundPaint = new Paint();
        mNormalBackgroundPaint.setColor(getColor(android.R.color.transparent));

        mBottomSelectedPaint = new Paint();
        mBottomNormalPaint = new Paint();
        mBorderPaint = new Paint();
        mDividerPaint = new Paint();
        mBottomSelectedPaint.setColor(mBottomSelectedColor);
        mBottomNormalPaint.setColor(mBottomNormalColor);
        mBorderPaint.setColor(mBorderColor);
        mDividerPaint.setColor(mDividerColor);
        mBottomSelectedPaint.setStrokeWidth(mBottomLineHeight);
        mBottomNormalPaint.setStrokeWidth(mBottomLineHeight);
        /*mBorderPaint.setStrokeWidth(mBorderWidth);
        mDividerPaint.setStrokeWidth(mDividerWidth);*/


        mCursorPaint = new Paint();
        mCursorPaint.setAntiAlias(true);
        mCursorPaint.setColor(mCursorColor);
        mCursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCursorPaint.setStrokeWidth(mCursorWidth);
    }

    /**
     * 初始化Attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VerCodeEditText);
        mFigures = ta.getInteger(R.styleable.VerCodeEditText_figures, 4);
        mVerCodeMargin = (int) ta.getDimension(R.styleable.VerCodeEditText_verCodeMargin, 0);
        mBottomSelectedColor = ta.getColor(R.styleable.VerCodeEditText_bottomLineSelectedColor,
                getCurrentTextColor());
        mBottomNormalColor = ta.getColor(R.styleable.VerCodeEditText_bottomLineNormalColor,
                getColor(android.R.color.darker_gray));
        mBottomLineHeight = ta.getDimension(R.styleable.VerCodeEditText_bottomLineHeight,
                dp2px(5));
        mSelectedBackgroundColor = ta.getColor(R.styleable.VerCodeEditText_selectedBackgroundColor,
                getColor(android.R.color.darker_gray));
        mBorderColor = ta.getColor(R.styleable.VerCodeEditText_borderColor,
                getCurrentTextColor());
        mBorderWidth = (int) ta.getDimension(R.styleable.VerCodeEditText_borderWidth,
                0);
        mDividerColor = ta.getColor(R.styleable.VerCodeEditText_dividerColor,
                getCurrentTextColor());
        mDividerWidth = (int) ta.getDimension(R.styleable.VerCodeEditText_dividerWidth,
                0);
        mCursorWidth = (int) ta.getDimension(R.styleable.VerCodeEditText_cursorWidth, dp2px(1));
        mCursorColor = ta.getColor(R.styleable.VerCodeEditText_cursorColor, getColor(android.R.color.darker_gray));
        mCursorDuration = ta.getInteger(R.styleable.VerCodeEditText_cursorDuration, DEFAULT_CURSOR_DURATION);
        showType = ta.getInt(R.styleable.VerCodeEditText_showType, SHOW_TYPE_PASSWORD);
        showStyle = ta.getInt(R.styleable.VerCodeEditText_showStyle, SHOW_STYLE_SET);
        pointRadius = ta.getDimensionPixelSize(R.styleable.VerCodeEditText_pointRadius, ScreenUtils.dip2px(getContext(), 8));
        ta.recycle();

        // force LTR because of bug: https://github.com/JustKiddingBaby/VercodeEditText/issues/4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setLayoutDirection(LAYOUT_DIRECTION_LTR);
        }
    }

    private void initCursorTimer() {
        mCursorTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 通过光标间歇性显示实现闪烁效果
                isCursorShowing = !isCursorShowing;
                postInvalidate();
            }
        };
        mCursorTimer = new Timer();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 启动定时任务，定时刷新实现光标闪烁
        mCursorTimer.scheduleAtFixedRate(mCursorTimerTask, 0, mCursorDuration);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCursorTimer.cancel();
    }

    @Override
    final public void setCursorVisible(boolean visible) {
        super.setCursorVisible(visible);//隐藏光标的显示
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthResult = 0, heightResult = 0;
        //最终的宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            widthResult = widthSize;
        } else {
            widthResult = getScreenWidth(getContext());
        }
        //每个矩形形的宽度
        mEachRectLength = (widthResult - ((mVerCodeMargin + mDividerWidth) * (mFigures - 1) - mBorderWidth * 2)) / mFigures;
        //最终的高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            heightResult = heightSize;
        } else {
            heightResult = mEachRectLength;
        }
        setMeasuredDimension(widthResult, heightResult);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            setSelection(getText().length());
            showKeyBoard(getContext());
            return false;
        }
        return super.onTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        mCurrentPosition = getText().length();
        int width = mEachRectLength - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        //绘制文字
        String value = getText().toString();
        for (int i = 0; i < value.length(); i++) {
            canvas.save();
            int start = width * i + i * mVerCodeMargin + i * mDividerWidth + mBorderWidth;
            float x = start + width / 2;
            TextPaint paint = getPaint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(getCurrentTextColor());
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2
                    - fontMetrics.top;
            if (showType == SHOW_TYPE_PASSWORD) {
                canvas.drawCircle(x, height / 2, pointRadius, paint);
            } else {
                canvas.drawText(String.valueOf(value.charAt(i)), x, baseline, paint);
            }
            canvas.restore();
        }
        if (showStyle == SHOW_STYLE_SET) {
            for (int i = 0; i < mFigures; i++) {
                canvas.save();
                int start = width * i + i * mVerCodeMargin + i *  mDividerWidth;
                int end = width + start;
                //画一个矩形
                if (i == mCurrentPosition) {//选中的下一个状态
                    canvas.drawRect(start, 0, end, height, mSelectedBackgroundPaint);
                } else {
                    canvas.drawRect(start, 0, end, height, mNormalBackgroundPaint);
                }
                canvas.restore();
            }
            //绘制底线
            for (int i = 0; i < mFigures; i++) {
                canvas.save();
                float lineY = height - mBottomLineHeight / 2;
                int start = width * i + i * mVerCodeMargin;
                int end = width + start;
                if (i == mCurrentPosition) {
                    canvas.drawLine(start, lineY, end, lineY, mBottomSelectedPaint);
                } else {
                    canvas.drawLine(start, lineY, end, lineY, mBottomNormalPaint);
                }
                canvas.restore();
            }
        } else {
            canvas.drawRect(0, 0, getWidth(), mBorderWidth, mBorderPaint);
            canvas.drawRect(0, getHeight() - mBorderWidth, getWidth(), getHeight(), mBorderPaint);
            canvas.drawRect(0, 0, mBorderWidth, getHeight(), mBorderPaint);
            canvas.drawRect(getWidth() - mBorderWidth, 0, getWidth(), getHeight(), mBorderPaint);
            if (mDividerWidth > 0) {
                for (int i = 0; i < mFigures - 1; i++) {
                    final float left = width * (i + 1) + mDividerWidth * i + mBorderWidth;
                    canvas.drawRect(left, 0, left + mDividerWidth, height, mDividerPaint);
                }
            }
        }

        //绘制光标
        if (!isCursorShowing && isCursorVisible() && mCurrentPosition < mFigures && hasFocus()) {
            canvas.save();
            int startX = mCurrentPosition * (width + mVerCodeMargin) + width / 2 + mCurrentPosition * mDividerWidth + mBorderWidth;
            int startY = height / 4;
            int endX = startX;
            int endY = height - height / 4;
            canvas.drawLine(startX, startY, endX, endY, mCursorPaint);
            canvas.restore();
        }
    }

    /**
     * 描边
     */
    private void drawBorder(Canvas canvas) {

    }

    /**
     * 画分割线
     */
    private void drawDivider(Canvas canvas) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mCurrentPosition = getText().length();
        postInvalidate();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCurrentPosition = getText().length();
        postInvalidate();
        if (onCodeChangedListener != null) {
            onCodeChangedListener.onVerCodeChanged(getText(), start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCurrentPosition = getText().length();
        postInvalidate();
        if (getText().length() == mFigures) {
            if (onCodeChangedListener != null) {
                onCodeChangedListener.onInputCompleted(getText());
            }
        } else if (getText().length() > mFigures) {
            getText().delete(mFigures, getText().length());
        }
    }

    @Override
    public void setFigures(int figures) {
        mFigures = figures;
        postInvalidate();
    }

    @Override
    public void setVerCodeMargin(int margin) {
        mVerCodeMargin = margin;
        postInvalidate();
    }

    @Override
    public void setBottomSelectedColor(@ColorRes int bottomSelectedColor) {
        mBottomSelectedColor = getColor(bottomSelectedColor);
        postInvalidate();
    }

    @Override
    public void setBottomNormalColor(@ColorRes int bottomNormalColor) {
        mBottomSelectedColor = getColor(bottomNormalColor);
        postInvalidate();
    }

    @Override
    public void setSelectedBackgroundColor(@ColorRes int selectedBackground) {
        mSelectedBackgroundColor = getColor(selectedBackground);
        postInvalidate();
    }

    @Override
    public void setBottomLineHeight(int bottomLineHeight) {
        this.mBottomLineHeight = bottomLineHeight;
        postInvalidate();
    }

    @Override
    public void setOnVerificationCodeChangedListener(OnVerificationCodeChangedListener listener) {
        this.onCodeChangedListener = listener;
    }

    /**
     * 返回颜色
     */
    private int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    /**
     * dp转px
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 获取手机屏幕的宽度
     */
    static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }
}
