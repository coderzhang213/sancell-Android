package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifyFirstDataBean;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifyFirstAdapter extends BaseQuickAdapter<HomeClassifyFirstDataBean.ClassifyFirstListBean, BaseViewHolder> {

    private Context context;
    private List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data;

    public HomeClassifyFirstAdapter(int layoutResId, @Nullable List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data) {
        super(layoutResId, data);
    }

    public HomeClassifyFirstAdapter(Context context, @Nullable List<HomeClassifyFirstDataBean.ClassifyFirstListBean> data) {
        super(R.layout.item_first_classify, data);
        this.context = context;
        this.data = data;
    }

    public HomeClassifyFirstAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeClassifyFirstDataBean.ClassifyFirstListBean item) {
        helper.setText(R.id.tv_name, item.getName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (data.get(position).isSelect()) {
            holder.getView(R.id.select_mark).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_name).setBackgroundResource(R.color.colorTran);
            /*if (position == 0) {
                holder.getView(R.id.tv_name).setBackgroundResource(R.mipmap.icon_first_classify_selected_first);
            } else {
                holder.getView(R.id.tv_name).setBackgroundResource(R.mipmap.icon_first_classify_selected);
            }*/
            ((TextView) holder.getView(R.id.tv_name)).getPaint().setFakeBoldText(true);
        } else {
            holder.getView(R.id.select_mark).setVisibility(View.INVISIBLE);
            if (position > 0 && data.get(position - 1).isSelect()) {
                holder.getView(R.id.tv_name).setBackgroundResource(R.drawable.round_color_stroke5_tr_4);
            } else if (position + 1 < data.size() && data.get(position + 1).isSelect()) {
                if (position == 0) {
                    holder.getView(R.id.tv_name).setBackgroundResource(R.drawable.round_color_stroke5_rl_rb_4);
                } else {
                    holder.getView(R.id.tv_name).setBackgroundResource(R.drawable.round_color_stroke5_br_4);
                }
            } else {
                if (position == 0) {
                    holder.getView(R.id.tv_name).setBackgroundResource(R.drawable.round_color_stroke5_tr_4);
                } else {
                    holder.getView(R.id.tv_name).setBackgroundResource(R.color.color_stroke5);
                }
            }

            ((TextView) holder.getView(R.id.tv_name)).getPaint().setFakeBoldText(false);
        }

    }
}
