package cn.sancell.xingqiu.goods.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.util.BigDecimalUtils;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.PriceUtils;

/**
 * @author Alan_Xiong
 * @desc: 商品信息与价格vg
 * @time 2019-12-25 14:29
 */
public class VgGoodsInfo extends LinearLayout {

    private AppCompatImageView iv_pin;
    private AppCompatTextView tv_real_price;
    private AppCompatTextView tv_line_price;
    private AppCompatTextView tv_goods_title;
    private AppCompatTextView tv_goods_desc;
    private AppCompatTextView tv_goods_sku;
    //拼团
    private AppCompatTextView tv_pin_real_price;
    private AppCompatTextView tv_pin_line_price;
    private AppCompatTextView tv_pin_member;
    private AppCompatTextView tv_goods_pin_limit;
    private AppCompatTextView tv_pin_end_time;
    private RelativeLayout rl_pin_info;


    public VgGoodsInfo(Context context) {
        this(context, null);
    }

    public VgGoodsInfo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VgGoodsInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vg_goods_info, (ViewGroup) getRootView(), false);
        addView(view);
        initView(view);
    }

    public void initView(View view) {
        iv_pin = view.findViewById(R.id.iv_pin);
        tv_real_price = view.findViewById(R.id.tv_real_price);
        tv_line_price = view.findViewById(R.id.tv_line_price);
        tv_goods_title = view.findViewById(R.id.tv_goods_title);
        tv_goods_desc = view.findViewById(R.id.tv_goods_desc);
        tv_goods_sku = view.findViewById(R.id.tv_goods_sku);

        tv_pin_real_price = view.findViewById(R.id.tv_pin_real_price);
        tv_pin_line_price = view.findViewById(R.id.tv_pin_line_price);
        tv_pin_member = view.findViewById(R.id.tv_pin_member);
        tv_goods_pin_limit = view.findViewById(R.id.tv_goods_pin_limit);
        tv_pin_end_time = view.findViewById(R.id.tv_pin_end_time);
        rl_pin_info = view.findViewById(R.id.rl_pin_info);
    }


    public void setGoodsInfo(ProductInfoDataBean res) {

        rl_pin_info.setVisibility(VISIBLE);
        //设置属性
        tv_pin_real_price.setText(PriceUtils.getInstance().getMainPrice(getContext(), res.grouponInfo.grouponPriceE2, 14));
        tv_pin_line_price.setText(String.format(getResources().getString(R.string.rmb), BigDecimalUtils.div(res.getMarketPriceE2() + "", "100", 2)));
        tv_pin_line_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_pin_member.setVisibility(View.GONE);
        //  tv_pin_member.setText(String.format(getResources().getString(R.string.pin_number),res.grouponInfo.buyerNum));
        tv_goods_pin_limit.setVisibility(View.GONE);
        //  tv_goods_pin_limit.setText(String.format(getResources().getString(R.string.pin_goods_limit),res.grouponInfo.grouponSumStorck));
      //  tv_pin_end_time.setText(DateUtils.getTimeByStamp(res.grouponInfo.grouponEndTime));

        iv_pin.setVisibility(View.VISIBLE);
        if (res.productType == 1){
            iv_pin.setImageResource(R.mipmap.icon_goods_one_star);
        }else if (res.productType == 2){
            iv_pin.setImageResource(R.mipmap.icon_goods_two_star);
        }else if (res.productType == 3){
            iv_pin.setImageResource(R.mipmap.icon_goods_three_star);
        }else{
            iv_pin.setVisibility(View.GONE);
        }


        tv_goods_title.setText(res.getTitle());
        tv_goods_desc.setText(res.getDesc());
        tv_goods_sku.setText(res.getSpecification());

    }


}
