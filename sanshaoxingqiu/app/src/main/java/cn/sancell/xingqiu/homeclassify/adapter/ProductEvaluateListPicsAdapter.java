package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;

/**
 * Created by ai11 on 2019/7/17.
 */

public class ProductEvaluateListPicsAdapter extends BaseAdapter {
    private Context mContext;
    private List<EvaluateListDataBean.EvaluateBean.PicArr> dataList;

    public ProductEvaluateListPicsAdapter(Context context, List<EvaluateListDataBean.EvaluateBean.PicArr> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList == null ? "" : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_evaluatelist_pics, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemImg = (SimpleDraweeView) convertView.findViewById(R.id.sdv_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemImg.setImageURI(Uri.parse(dataList.get(position).getCoverPic()));


        return convertView;
    }


    class ViewHolder {
        SimpleDraweeView itemImg;
    }
}
