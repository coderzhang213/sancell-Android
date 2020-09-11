package cn.sancell.xingqiu.live.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * Created by zj on 2019/11/29.
 */
public class TitlemarginUtils {
    public static void setTitleTop(Context context, RelativeLayout view) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
    }
}
