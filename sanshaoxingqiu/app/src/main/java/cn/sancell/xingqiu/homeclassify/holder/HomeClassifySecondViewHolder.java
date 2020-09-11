package cn.sancell.xingqiu.homeclassify.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.sancell.xingqiu.R;
import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * Created by ai11 on 2019/6/11.
 */

public class HomeClassifySecondViewHolder extends BaseViewHolder {

    public TextView tv_group_name;
    public SimpleDraweeView sdv_child_pic, sdv_group_pic;
    public TextView tv_child_name;

    /**
     * 初始化你的View(这里包括GroupView,和childView)
     */
    public HomeClassifySecondViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
        tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
        sdv_child_pic = (SimpleDraweeView) itemView.findViewById(R.id.sdv_child_pic);
        tv_child_name = (TextView) itemView.findViewById(R.id.tv_child_name);
        sdv_group_pic = (SimpleDraweeView) itemView.findViewById(R.id.sdv_group_pic);
    }

    /**
     * @return 返回你的GroupView 布局文件中根节点的ID
     */
    @Override
    public int getGroupViewResId() {
        return R.id.group;
    }

    /**
     * @return 返回你的ChildView 布局文件中根节点的ID
     */
    @Override
    public int getChildViewResId() {
        return R.id.child;
    }
}
