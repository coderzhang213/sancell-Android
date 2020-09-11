package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.custom.vg.list.CustomAdapter;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
 * Created by ai11 on 2019/6/17.
 */

public class ProductInfoTagsAdapter extends CustomAdapter {

    private List<ProductInfoDataBean.TagInfoListData.TagInfoBean> data;
    private LayoutInflater mInflater = null;

    private Context context;

    public ProductInfoTagsAdapter(Context context,
                                  List<ProductInfoDataBean.TagInfoListData.TagInfoBean> data) {
        super();
        this.data = data;
        this.context = context.getApplicationContext();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_productinfo_tags, null);
            holder = new ViewHolder();
            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_tags_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item_name.setText(data.get(position).getTagName());
        return convertView;
    }


    class ViewHolder {
        public TextView tv_item_name;

    }
}
