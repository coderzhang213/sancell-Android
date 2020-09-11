package cn.sancell.xingqiu.order.orderInfo;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.interfaces.OnOrderItemFunListener;


/**
  * @author Alan_Xiong
  *
  * @desc: 订单item
  * @time 2020-01-06 14:26
  */
public class VgOrderItem extends LinearLayout {

    private AppCompatTextView tv_head;

    private AppCompatTextView tv_second_head;

    private AppCompatTextView tv_fun;

    private OnOrderItemFunListener mListener;

    public VgOrderItem(Context context) {
      this(context,null);
    }

    public VgOrderItem(Context context, @Nullable AttributeSet attrs) {
      this(context,attrs,0);
    }

    public VgOrderItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.include_item_order, (ViewGroup) getRootView(),false);
        addView(view);
        initView(view);
    }


    public void initView(View view){
        tv_fun = view.findViewById(R.id.tv_fun);
        tv_second_head = view.findViewById(R.id.tv_second_head);
        tv_head = view.findViewById(R.id.tv_head);
        tv_fun.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onClick();
            }
        });
    }

    public void setData(String headStr,String secondStr,String funStr){
        tv_head.setText(headStr);
        tv_second_head.setText(secondStr);
        if (TextUtils.isEmpty(funStr)){
            tv_fun.setVisibility(GONE);
        }else{
            tv_fun.setVisibility(VISIBLE);
            tv_fun.setText(funStr);
        }
    }

    public void setListener(OnOrderItemFunListener listener){
        mListener = listener;
    }


}
