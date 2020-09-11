package cn.sancell.xingqiu.homeshoppingcar.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/14.
 */

public class HomeShoppingCarAdapter extends BaseQuickAdapter<HomeShoppingCarDataBean.ShopingCarProductBean, BaseViewHolder> {

    List<HomeShoppingCarDataBean.ShopingCarProductBean> data;
    List<HomeShoppingCarDataBean.ShopingCarProductBean> selectData;

    public HomeShoppingCarAdapter(int layoutResId, @Nullable List<HomeShoppingCarDataBean.ShopingCarProductBean> data) {
        super(layoutResId, data);
    }

    public HomeShoppingCarAdapter(@Nullable List<HomeShoppingCarDataBean.ShopingCarProductBean> data) {
        super(R.layout.item_home_shoppingcar, data);
        selectData = new ArrayList<>();
        this.data = data;
        setSelectData();
    }

    public void setSelectData() {
        selectData.clear();
        for (HomeShoppingCarDataBean.ShopingCarProductBean temp : data
                ) {
            if (temp.getIsSelected() == 1 && temp.getType() == 1) {
                addSelectData(temp);
            }
        }
    }

    public HomeShoppingCarAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 获得中的商品信息
     */
    public List<HomeShoppingCarDataBean.ShopingCarProductBean> getSelectData() {
        return selectData;
    }

    /**
     * 添加一个当前商品中的选中的商品信息
     *
     * @param item
     */
    public void addSelectData(HomeShoppingCarDataBean.ShopingCarProductBean item) {
        this.selectData.add(item);
    }

    /**
     * 移除一个当前商品中的选中的商品信息
     *
     * @param item
     */
    public void removeSelectData(HomeShoppingCarDataBean.ShopingCarProductBean item) {
        try {
            this.selectData.remove(item);
        } catch (Exception e) {
        }

    }

    /**
     * 获取当前选中的商品总价
     *
     * @return
     */
    public int getTotlePrice() {
        int totle = 0;
        for (HomeShoppingCarDataBean.ShopingCarProductBean j : selectData) {
            totle += j.getUserRealPriceE2() * j.getGoodsNum();
        }
        return totle;
    }

    /**
     * 获取当前选中的商品获取的购物红包
     *
     * @return
     */
    public int getTotleRedPacketReward() {
        int totle = 0;
        for (HomeShoppingCarDataBean.ShopingCarProductBean j : selectData) {
            totle += j.getUserRealAllProfitSharingE2();
        }
        return totle;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        RelativeLayout rl_invalid = holder.getView(R.id.rl_invalid);
        RelativeLayout rl_nostock = holder.getView(R.id.rl_nostock);
        ImageView iv_select = holder.getView(R.id.iv_select);
        TextView tv_arrival_reminder = holder.getView(R.id.tv_arrival_reminder);
        RelativeLayout rl_operate = holder.getView(R.id.rl_operate);
        ImageView iv_jian = holder.getView(R.id.iv_jian);
        ImageView iv_jia = holder.getView(R.id.iv_jia);
        holder.addOnClickListener(R.id.tv_clear_invalid);
        holder.addOnClickListener(R.id.tv_clear_nostock);
        holder.addOnClickListener(R.id.tv_arrival_reminder);
        holder.addOnClickListener(R.id.iv_jia);
        holder.addOnClickListener(R.id.iv_jian);
        holder.addOnClickListener(R.id.rl_item);
        holder.addOnClickListener(R.id.tv_product_num);
        switch (data.get(position).getType()) {
            case 1:  //正常
                rl_invalid.setVisibility(View.GONE);
                rl_nostock.setVisibility(View.GONE);
                tv_arrival_reminder.setVisibility(View.GONE);
                rl_operate.setVisibility(View.VISIBLE);
                holder.addOnClickListener(R.id.iv_select);
                if (data.get(position).getIsSelected() == 1) {
                    iv_select.setImageResource(R.mipmap.icon_car_select_yes);
                } else {
                    iv_select.setImageResource(R.mipmap.icon_car_select_no);
                }
                if (data.get(position).getGoodsNum() <= (data.get(position).getGoodsMinBuyNum() == 0 ? 1 : data.get(position).getGoodsMinBuyNum())) {
                    iv_jian.setImageResource(R.mipmap.icon_productinfo_jian_invalid);
                } else {
                    iv_jian.setImageResource(R.mipmap.icon_productinfo_jian);
                }
                if (data.get(position).getGoodsNum() == data.get(position).getGoodsStockNumber()||data.get(position).getGoodsNum()==200) {
                    iv_jia.setImageResource(R.mipmap.icon_productinfo_jia_invalid);
                } else {
                    iv_jia.setImageResource(R.mipmap.icon_productinfo_jia);
                }

                break;
            case 2:  // 库存不足
                iv_select.setImageResource(R.mipmap.icon_car_nostock);
                tv_arrival_reminder.setVisibility(View.VISIBLE);
                rl_operate.setVisibility(View.GONE);
                if (position != 0) {
                    if (data.get(position).getType() != data.get(position - 1).getType()) {
                        rl_invalid.setVisibility(View.GONE);
                        rl_nostock.setVisibility(View.VISIBLE);
                    } else {
                        rl_invalid.setVisibility(View.GONE);
                        rl_nostock.setVisibility(View.GONE);
                    }
                } else {
                    rl_invalid.setVisibility(View.GONE);
                    rl_nostock.setVisibility(View.VISIBLE);
                }
                break;
            case 3:  //无效商品
                tv_arrival_reminder.setVisibility(View.GONE);
                rl_operate.setVisibility(View.GONE);
                iv_select.setImageResource(R.mipmap.icon_car_invalid);
                if (position != 0) {
                    if (data.get(position).getType() != data.get(position - 1).getType()) {
                        rl_invalid.setVisibility(View.VISIBLE);
                        rl_nostock.setVisibility(View.GONE);
                    } else {
                        rl_invalid.setVisibility(View.GONE);
                        rl_nostock.setVisibility(View.GONE);
                    }
                } else {
                    rl_invalid.setVisibility(View.VISIBLE);
                    rl_nostock.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeShoppingCarDataBean.ShopingCarProductBean item) {
        RelativeSizeTextView tv_price = helper.getView(R.id.tv_price);
        tv_price.setStartText("¥");
        tv_price.setStartProportion(0.75f);
        tv_price.setTagText(StringUtils.getPrice(item.getUserRealPriceE2()));
        tv_price.setEndProportion(0.75f);
        tv_price.setEndText(StringUtils.getPriceDecimal(item.getUserRealPriceE2()));
        helper.setText(R.id.tv_product_num, item.getGoodsNum() + "");
        helper.setText(R.id.tv_product_name, item.getGoodsTitle());
        helper.setText(R.id.tv_specs, item.getSpecification());
        if (item.getGoodsMinBuyNum() > 1) {
            helper.getView(R.id.tv_min_buynum).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_min_buynum, item.getGoodsMinBuyNum() + "件起购");
            if (item.getGoodsNum() < item.getGoodsMinBuyNum()) {
                ((TextView) helper.getView(R.id.tv_min_buynum)).setTextColor(mContext.getResources().getColor(R.color.color_function_error));
            } else {
                ((TextView) helper.getView(R.id.tv_min_buynum)).setTextColor(mContext.getResources().getColor(R.color.color_text3));
            }
        } else {
            helper.getView(R.id.tv_min_buynum).setVisibility(View.GONE);
        }
        //helper.setText(R.id.tv_price, item.getPrice() + "");
        if(!StringUtils.isTextEmpty(item.getCoverPicThumb())) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPicThumb()));
        }else {
            ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(""));
        }
        final LinearLayout ll_delete = helper.getView(R.id.ll_delete);
        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_delete.setVisibility(View.GONE);
            }
        });
        helper.addOnClickListener(R.id.tv_delete);
        RelativeLayout rl_item = helper.getView(R.id.rl_item);
        rl_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ll_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        if (item.getSeckillInfo() != null && item.getSeckillInfo().getSeckillGoodsStatus() == 1) {  //秒杀中
            helper.getView(R.id.iv_seckill_mark).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_seckill_mark).setVisibility(View.GONE);
        }

    }
}
