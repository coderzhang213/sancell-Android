package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
 * Created by shichaohui on 2019/8/9 0010.
 * <p/>
 * 页码指示器类，获得此类实例后，可通过{@link PageIndicatorView#initIndicator(int)}方法初始化指示器
 * </P>
 */

public class PageIndicatorView extends LinearLayout {
    private Context mContext = null;
    private int dotSize_w_yes = 12; // 指示器的大小（dp）
    private int dotSize_w_no = 6; // 指示器的大小（dp）
    private int dotSize_h = 2; // 指示器的大小（dp）
    private int margins = 2; // 指示器间距（dp）
    private List<View> indicatorViews = null; // 存放指示器

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        dotSize_w_yes = ScreenUtils.dip2px(context, dotSize_w_yes);
        dotSize_w_no = ScreenUtils.dip2px(context, dotSize_w_no);
        dotSize_h = ScreenUtils.dip2px(context, dotSize_h);
        margins = ScreenUtils.dip2px(context, margins);
    }

    /**
     * 初始化指示器，默认选中第一页
     *
     * @param count 指示器数量，即页数
     */
    public void initIndicator(int count) {

        if (indicatorViews == null) {
            indicatorViews = new ArrayList<>();
        } else {
            indicatorViews.clear();
            removeAllViews();
        }
        View view;

        for (int i = 0; i < count; i++) {
            LayoutParams params;
            if (i == 0) {
                params = new LayoutParams(dotSize_w_yes, dotSize_h);
            } else {
                params = new LayoutParams(dotSize_w_no, dotSize_h);
            }
            params.setMargins(margins, 0, margins, 0);
            view = new View(mContext);
            view.setBackgroundResource(R.drawable.indicator_select_no);
            addView(view, params);
            indicatorViews.add(view);
        }
        if (indicatorViews.size() > 0) {
            indicatorViews.get(0).setBackgroundResource(R.drawable.indicator_select_yes);
        }
    }

    /**
     * 设置选中页
     *
     * @param selected 页下标，从0开始
     */
    public void setSelectedPage(int selected) {
        if (indicatorViews == null) {
            return;
        }
        for (int i = 0; i < indicatorViews.size(); i++) {
            if (i == selected) {
                LayoutParams params = new LayoutParams(dotSize_w_yes, dotSize_h);
                params.setMargins(margins, 0, margins, 0);
                indicatorViews.get(i).setLayoutParams(params);
                indicatorViews.get(i).setBackgroundResource(R.drawable.indicator_select_yes);
            } else {
                LayoutParams params = new LayoutParams(dotSize_w_no, dotSize_h);
                params.setMargins(margins, 0, margins, 0);
                indicatorViews.get(i).setLayoutParams(params);
                indicatorViews.get(i).setBackgroundResource(R.drawable.indicator_select_no);
            }
        }
    }
}
