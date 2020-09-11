package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ClassifyScreeningDataBean;

/**
 * Created by ai11 on 2019/6/13.
 */

public class ClassifyScreeningAdapter extends BaseQuickAdapter<ClassifyScreeningDataBean.ScreeningItemBean, BaseViewHolder> {
    private Context context;
    public ClassifyScreeningAdapter(int layoutResId, @Nullable List<ClassifyScreeningDataBean.ScreeningItemBean> data) {
        super(layoutResId, data);
    }

    public ClassifyScreeningAdapter(Context context,@Nullable List<ClassifyScreeningDataBean.ScreeningItemBean> data) {
        super(R.layout.item_classify_screening, data);
        this.context=context;
    }

    public ClassifyScreeningAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassifyScreeningDataBean.ScreeningItemBean item) {
        helper.setText(R.id.tv_name,item.getName());
        if(item.isSelect()){
            ((TextView)helper.getView(R.id.tv_name)).setTextColor(context.getResources().getColor(R.color.colorWhite));
            ((TextView)helper.getView(R.id.tv_name)).setBackgroundResource(R.drawable.round_theme_4);
        }else {
            ((TextView)helper.getView(R.id.tv_name)).setTextColor(context.getResources().getColor(R.color.color_text3));
            ((TextView)helper.getView(R.id.tv_name)).setBackgroundResource(R.drawable.round_stroke3_4);
        }
        helper.addOnClickListener(R.id.tv_name);
    }
}
