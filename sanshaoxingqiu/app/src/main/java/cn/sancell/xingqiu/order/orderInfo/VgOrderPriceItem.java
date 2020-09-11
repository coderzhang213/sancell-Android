package cn.sancell.xingqiu.order.orderInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;

/**
 * @author Alan_Xiong
 * @desc: 订单价格
 * @time 2020-01-06 14:30
 */
public class VgOrderPriceItem extends RelativeLayout {

    AppCompatTextView tv_right_price;
    AppCompatTextView tv_head;

    public VgOrderPriceItem(Context context) {
        this(context,null);
    }

    public VgOrderPriceItem(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }

    public VgOrderPriceItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.include_order_item_price, (ViewGroup) getRootView(), false);
        addView(view);
        initView(view);
    }

    private void initView(View view) {
        tv_right_price = view.findViewById(R.id.tv_right_price);
        tv_head = view.findViewById(R.id.tv_head);
    }

    public void setData(String headStr, String priceStr) {
        tv_right_price.setText(priceStr);
        tv_head.setText(headStr);
    }
}
