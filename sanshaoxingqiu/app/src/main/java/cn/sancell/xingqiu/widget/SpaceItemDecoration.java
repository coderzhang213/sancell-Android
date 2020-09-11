package cn.sancell.xingqiu.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by ai11 on 2019/6/15.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing, leftrightspacing, bottomspacing, topspacing;
    private int spanCount;

    public SpaceItemDecoration(int spanCount, int itemspacing, int leftrightspacing, int bottomspacing, int topspacing) {
        this.spanCount = spanCount;
        this.spacing = itemspacing;
        this.leftrightspacing = leftrightspacing;
        this.bottomspacing = bottomspacing;
        this.topspacing = topspacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        /*int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if(position==0||position==1){
            outRect.top=topspacing;
        }
        if (position % 2 != 0) {  //右边
            outRect.left = spacing;
            outRect.bottom = bottomspacing;
            outRect.right = leftrightspacing;
        } else {
            outRect.left = leftrightspacing;
            outRect.right = spacing;
            outRect.bottom = bottomspacing;
        }*/

        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int spanIndex = 0;
        RecyclerView.Adapter adapter = parent.getAdapter();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (adapter == null || layoutManager == null) {
            return;
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        }
        //普通Item的尺寸
        int itemCount = adapter.getItemCount();
        int childCount = layoutManager.getChildCount();
        if (position < itemCount && spanCount == 2) {
            if (spanIndex != GridLayoutManager.LayoutParams.INVALID_SPAN_ID) {
                //getSpanIndex方法不管控件高度如何，始终都是左右左右返回index
                if (spanIndex % 2 == 0) {
                    //这个是左边item
                    outRect.left = leftrightspacing;
                    outRect.right = spacing;
                    outRect.bottom = bottomspacing;
                } else {
                    //这个是右边item
                    outRect.left = spacing;
                    outRect.bottom = bottomspacing;
                    outRect.right = leftrightspacing;
                }
                if (position == 0 || position == 1) {
                    outRect.top = topspacing;
                }
            }
        }

    }
}
