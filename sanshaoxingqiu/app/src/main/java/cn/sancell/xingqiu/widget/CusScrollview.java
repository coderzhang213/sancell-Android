package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by camelliae on 2017/7/24.
 * 解决ScrollView.setOnScrollChangeListener()API23以下不可用
 */

public class CusScrollview extends ScrollView {
    private Context context;
    private ScrollViewListenner listenner;
    private CusScrollview currentView;
    private Scroller mScroller;

    public CusScrollview(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        mScroller = new Scroller(context);
    }

    public CusScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        mScroller = new Scroller(context);
    }

    public CusScrollview(Context context, AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        this.context = context;
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        currentView = this;
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        if (null != listenner) {
            this.listenner.onScrollChanged(currentView, l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface ScrollViewListenner {
        public void onScrollChanged(CusScrollview view, int l,
                                    int t, int oldl, int oldt);
    }

    public void setScrollViewListenner(ScrollViewListenner listenner) {
        this.listenner = listenner;
    }

    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int fx, int fy, int duration) {
        int dx = fx - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = fy - getScrollY();  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy,int duration) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy,duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    /**
     * 滑动事件，这是控制手指滑动的惯性速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 4);
    }
}
