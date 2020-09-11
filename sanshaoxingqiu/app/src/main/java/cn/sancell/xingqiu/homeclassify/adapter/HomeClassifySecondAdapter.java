package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifySecondDataBean;
import cn.sancell.xingqiu.homeclassify.holder.HomeClassifySecondViewHolder;
import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifySecondAdapter extends BaseRecyclerViewAdapter<HomeClassifySecondDataBean.ClassifySecondBean, HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean, HomeClassifySecondViewHolder> {
    private Context ctx;
    private List datas;
    private LayoutInflater mInflater;
    private String coverPic = "";

    public HomeClassifySecondAdapter(Context ctx, List<RecyclerViewData> datas, String coverPic) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
        this.coverPic = coverPic;
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_classify_group, parent, false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_classify_child, parent, false);
    }

    @Override
    public HomeClassifySecondViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new HomeClassifySecondViewHolder(ctx, view, viewType);
    }

    @Override
    public void onBindGroupHolder(HomeClassifySecondViewHolder holder, int groupPos, int position, HomeClassifySecondDataBean.ClassifySecondBean groupData) {
        holder.tv_group_name.setText(groupData.getName());
        if (groupPos == 0) {
            holder.sdv_group_pic.setImageURI(Uri.parse(coverPic));
            holder.sdv_group_pic.setVisibility(View.VISIBLE);
        } else {
            holder.sdv_group_pic.setImageURI(Uri.parse(""));
            holder.sdv_group_pic.setVisibility(View.GONE);

        }
    }


    @Override
    public void onBindChildpHolder(HomeClassifySecondViewHolder holder, int groupPos, int childPos, int position, HomeClassifySecondDataBean.ClassifySecondBean.HomeClassifyThirdDataBean.ClassifyThirdBean childData) {
        holder.tv_child_name.setText(childData.getName());
        holder.sdv_child_pic.setImageURI(childData.getPic());
    }
}
