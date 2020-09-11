package cn.sancell.xingqiu.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ai11 on 2019/8/16.
 */

public class SpaceItemDecorationBuyMember extends RecyclerView.ItemDecoration {
    private int spacing, leftrightspacing;

    public SpaceItemDecorationBuyMember(int itemspacing, int leftrightspacing) {
        this.spacing = itemspacing;
        this.leftrightspacing = leftrightspacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position == 0) {
            outRect.left = leftrightspacing;
            outRect.right = spacing;
        } else if (position == 2) {
            outRect.left = spacing;
            outRect.right = leftrightspacing;
        } else {
            outRect.left = spacing;
            outRect.right = spacing;
        }
    }
}
