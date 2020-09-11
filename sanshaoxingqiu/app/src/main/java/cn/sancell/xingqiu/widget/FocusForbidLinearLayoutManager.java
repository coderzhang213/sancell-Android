package cn.sancell.xingqiu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jun.sheng on 2018/1/31.
 */

public class FocusForbidLinearLayoutManager extends LinearLayoutManager {

    public FocusForbidLinearLayoutManager(Context context) {
        super(context);
    }

    public FocusForbidLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FocusForbidLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        return true;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
        return false;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        return false;
    }
}
