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
import cn.sancell.xingqiu.goods.adapter.PinMarqueeAdapter;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.interfaces.OnPinOpListener;
import cn.sancell.xingqiu.widget.marque.XMarqueView;

/**
 * @author Alan_Xiong
 * @desc: 拼团规则与轮播
 * @time 2019-12-25 17:46
 */
public class VgPinAct extends LinearLayout implements View.OnClickListener {

    private XMarqueView queue_view;
    private AppCompatTextView tv_rule_desc;
    private PinMarqueeAdapter mMarqueeViewAdapter;
    private RelativeLayout rl_rule;
    private OnRuleClickListener mListener;
    private LinearLayout ll_queue;


    public VgPinAct(Context context) {
        this(context, null);
    }

    public VgPinAct(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VgPinAct(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vg_goods_pin_tuan, (ViewGroup) getRootView(), false);
        addView(view);
        initView(view);
    }

    private void initView(View view) {
        tv_rule_desc = view.findViewById(R.id.tv_rule_desc);
        queue_view = view.findViewById(R.id.queue_view);
        rl_rule = view.findViewById(R.id.rl_rule);
        ll_queue = view.findViewById(R.id.ll_queue);
        rl_rule.setOnClickListener(this);
    }


    public void setData(ProductInfoDataBean.PinGroupInfo groupInfo, OnPinOpListener mListener) {

        boolean isSingle = false;
        if (groupInfo.grouponOrders != null && groupInfo.grouponOrders.size() > 0) {
            ll_queue.setVisibility(VISIBLE);
            if (groupInfo.grouponOrders.size()<=2){
                queue_view.setFlippingLessCount(false);
            }
            if (groupInfo.grouponOrders.size() == 1) {
                isSingle = true;
            }
        }else{
            ll_queue.setVisibility(GONE);
        }

        queue_view.setItemCount(isSingle ? 1 : 2);
        if (mMarqueeViewAdapter == null) {

            mMarqueeViewAdapter = new PinMarqueeAdapter(groupInfo.grouponOrders);
            mMarqueeViewAdapter.setOnJonListener(new OnPinOpListener() {
                @Override
                public void onJoin(ProductInfoDataBean.PinGroupInfo.GroupRecommend item) {
                    if (mListener != null) {
                        mListener.onJoin(item);
                    }
                }

                @Override
                public void onPinEnded() {
                    if (mListener != null) {
                        mListener.onPinEnded();
                    }
                }
            });
            queue_view.setAdapter(mMarqueeViewAdapter);
        } else {
            mMarqueeViewAdapter.setNewData(groupInfo.grouponOrders);
        }

        tv_rule_desc.setText(String.format(getContext().getString(R.string.pin_rule_desc),groupInfo.buyerNum,groupInfo.grouponFinishHour));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_rule) {
            if (mListener != null) {
                mListener.onRuleClick();
            }
        }
    }

    public void cancelTimer() {
        if (mMarqueeViewAdapter != null) {
            mMarqueeViewAdapter.removeTimer();
        }
    }

    public interface OnRuleClickListener {
        void onRuleClick();
    }

    public void setRuleListener(OnRuleClickListener listener) {
        mListener = listener;
    }
}
