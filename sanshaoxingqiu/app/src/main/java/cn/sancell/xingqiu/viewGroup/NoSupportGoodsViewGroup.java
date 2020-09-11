package cn.sancell.xingqiu.viewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.viewGroup.adapter.SupportGoodsAdapter;
import cn.sancell.xingqiu.viewGroup.adapter.SupportSingleGoodsAdapter;
import cn.sancell.xingqiu.viewGroup.adapter.TestModel;

/**
  * @author Alan_Xiong
  *
  * @desc: 不支持配送单位
  * @time 2019-12-12 14:48
  */
public class NoSupportGoodsViewGroup extends LinearLayout {

    private AppCompatTextView tv_goods_count;
    private RecyclerView rv_common;
    private SupportGoodsAdapter mAdapter;
    private SupportSingleGoodsAdapter mSingleAdapter;

    public NoSupportGoodsViewGroup(Context context) {
        this(context,null);
    }

    public NoSupportGoodsViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public NoSupportGoodsViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.vg_no_support_goods, (ViewGroup) getRootView(),false);
        addView(view);
        initView(view);
    }

    public void initView(View view){
        tv_goods_count = view.findViewById(R.id.tv_goods_count);
        rv_common = view.findViewById(R.id.rv_common);
        rv_common.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.shape_item_divider_10));
        rv_common.addItemDecoration(decoration);
    }

    /**
     * {@link cn.sancell.xingqiu.homeshoppingcar.ProductCreateOrderActivity}
     * @param datas
     */
    public void setData(List<HomeShoppingCarDataBean.ShopingCarProductBean> datas){

        tv_goods_count.setText(String.format(getResources().getString(R.string.support_count),datas.size()+""));

        if (mAdapter == null){
            mAdapter = new SupportGoodsAdapter(datas);
        }else{
            mAdapter.setNewData(datas);
        }
        rv_common.setAdapter(mAdapter);
    }


    /**
     * {@link cn.sancell.xingqiu.homeshoppingcar.SingleProductCreateOrderActivity}
     * @param url
     */
    public void setData(String url){

        List<String> data = new ArrayList<>();
        data.add(url);
        tv_goods_count.setText(String.format(getResources().getString(R.string.support_count),data.size()+""));
        if (mSingleAdapter == null){
            mSingleAdapter = new SupportSingleGoodsAdapter(data);

        }else {
            mSingleAdapter.setNewData(data);
        }
        rv_common.setAdapter(mSingleAdapter);



    }


}
