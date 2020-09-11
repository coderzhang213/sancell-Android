package cn.sancell.xingqiu.goods.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.DialogUtil;

/**
  * @author Alan_Xiong
  *
  * @desc:商品配送等服务vg
  * @time 2019-12-25 15:27
  */
public class VgGoodsDeliver extends LinearLayout implements View.OnClickListener {

    private RelativeLayout rl_deliver;
    private RelativeLayout rl_server;

    private AppCompatTextView tv_server_desc;
    private AppCompatTextView tv_deliver_desc;
    private AppCompatTextView tv_purchase_desc;

    public VgGoodsDeliver(Context context) {
      this(context,null);
    }

    public VgGoodsDeliver(Context context, @Nullable AttributeSet attrs) {
       this(context,attrs,0);
    }

    public VgGoodsDeliver(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vg_goods_delivery, (ViewGroup) getRootView(),false);
        addView(view);
        initView(view);

    }

    private void initView(View view) {

        rl_deliver = view.findViewById(R.id.rl_deliver);
        rl_server = view.findViewById(R.id.rl_server);
        tv_server_desc = view.findViewById(R.id.tv_server_desc);
        tv_deliver_desc = view.findViewById(R.id.tv_deliver_desc);

        rl_deliver.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_deliver){

        }
    }
}
