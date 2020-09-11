package cn.sancell.xingqiu.goods.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.glide.ImageLoaderUtils;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.interfaces.OnPinOpListener;
import cn.sancell.xingqiu.interfaces.OnTimeBackListener;
import cn.sancell.xingqiu.util.FontUtils;
import cn.sancell.xingqiu.widget.CommonCountTimer;
import cn.sancell.xingqiu.widget.marque.XMarqueView;
import cn.sancell.xingqiu.widget.marque.XMarqueeViewAdapter;

public class PinMarqueeAdapter extends XMarqueeViewAdapter<ProductInfoDataBean.PinGroupInfo.GroupRecommend> {

    private OnPinOpListener mListener;

    private SparseArray<CommonCountTimer> mapTimer = new SparseArray<>();
    private boolean isSingle;

    public PinMarqueeAdapter(List<ProductInfoDataBean.PinGroupInfo.GroupRecommend> datas) {
        super(datas);
        //刷新释放timer
        removeTimer();
    }

    public void setNewData(List<ProductInfoDataBean.PinGroupInfo.GroupRecommend> items) {
        removeTimer();
        setData(items);
    }

    //刷新等重新设置进行计时
    public void removeTimer() {
        if (mapTimer != null && mapTimer.size() > 0) {
            for (int i = 0; i < mapTimer.size(); i++) {
                CommonCountTimer timer = mapTimer.valueAt(i);
                if (timer != null) {
                    timer.cancel();
                }
            }
            mapTimer.clear();
        }
    }

    @Override
    public View onCreateView(XMarqueView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_pin_tuan, parent, false);
    }


    public void setOnJonListener(OnPinOpListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindView(View parent, View view, int position) {
        int defPosition = position;
        AppCompatImageView iv_img = view.findViewById(R.id.iv_img);
        AppCompatTextView tv_user_name = view.findViewById(R.id.tv_user_name);
        AppCompatTextView tv_remain_need = view.findViewById(R.id.tv_remain_need);
        AppCompatTextView tv_remain_time = view.findViewById(R.id.tv_remain_time);
        AppCompatTextView tv_join = view.findViewById(R.id.tv_join);

        ImageLoaderUtils.loadCircleImage(parent.getContext(), mDatas.get(position).gravatar, iv_img);
        tv_user_name.setText(mDatas.get(position).nickName);
        tv_remain_need.setText(FontUtils.getInstance().changeTextColor(parent.getContext().getResources().getColor(R.color.color_theme),
                String.format(parent.getContext().getString(R.string.pin_need_num), mDatas.get(position).lastUserNum), mDatas.get(position).lastUserNum + "人"));
        tv_join.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onJoin(mDatas.get(position));
            }
        });

        long timeDiff = mDatas.get(position).finishTime - mDatas.get(position).currentTime;


        //奇数加载最后一个
        if (mapTimer.size() > 0 && position == 0) {
            defPosition = mapTimer.size();
        }

        if ((mapTimer.get(defPosition) == null)) {
            CommonCountTimer mTimer = new CommonCountTimer(timeDiff * 1000, 1000, new OnTimeBackListener() {
                @Override
                public void onTick(String hour, String minute, String second) {
                    tv_join.setEnabled(true);
                    tv_remain_time.setText(String.format(parent.getContext().getString(R.string.distance_pin_end_time), hour, minute, second));
                }

                @Override
                public void onFinish() {
                    if (mapTimer.get(position) != null) {
                        mapTimer.get(position).cancel();
                    }
                    //结束后刷新activity
                    if (mListener != null) {
                        mListener.onPinEnded();
                    }
                    tv_remain_time.setText("距离结束 00:00:00");
                    tv_join.setEnabled(false);
                }
            });
            mTimer.start();
            mapTimer.put(position, mTimer);
        }

    }
}
