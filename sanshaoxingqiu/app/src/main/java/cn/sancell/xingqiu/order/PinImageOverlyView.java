package cn.sancell.xingqiu.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.glide.ImageLoaderUtils;
import cn.sancell.xingqiu.live.bean.GroupInfo;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;

/**
 * @author Alan_Xiong
 * @desc: 拼团头像view
 * @time 2020-01-03 11:02
 */
public class PinImageOverlyView extends RelativeLayout {


    private AppCompatImageView iv_first;
    private AppCompatTextView tv_first_phone;

    private AppCompatTextView tv_second_phone;
    private AppCompatImageView iv_second;
    private AppCompatImageView iv_third;

    private AppCompatImageView iv_end;
    private AppCompatTextView tv_end_phone;

    private LinearLayout ll_end;
    private LinearLayout ll_second;


    public PinImageOverlyView(Context context) {
        this(context, null);
    }

    public PinImageOverlyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinImageOverlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vg_pin_img_overly, (ViewGroup) getRootView(), false);
        addView(view);
        initView(view);
    }

    public void initView(View view) {
        iv_first = view.findViewById(R.id.iv_first);
        tv_first_phone = view.findViewById(R.id.tv_first_phone);
        tv_second_phone = view.findViewById(R.id.tv_second_phone);
        iv_second = view.findViewById(R.id.iv_second);
        iv_third = view.findViewById(R.id.iv_third);
        iv_end = view.findViewById(R.id.iv_end);
        tv_end_phone = view.findViewById(R.id.tv_end_phone);

        ll_end = view.findViewById(R.id.ll_end);
        ll_second = view.findViewById(R.id.ll_second);
    }

    /**
     * 设置数据
     *
     * @param pinMaxNum 团总人数
     * @param joinNum   参加团的人数
     * @param datas     图片集合
     */
    public void setData(int pinMaxNum, int joinNum, List<PinDetailRes.GrouponUsersInfo> datas) {

        List<PinDetailRes.GrouponUsersInfo> infos = new ArrayList<>();
        //排序团长在第一个
        if (datas != null && datas.size() > 0) {
            for (PinDetailRes.GrouponUsersInfo item : datas) {
                if (item.isGrouponOwner.equals("1")) {
                    infos.add(0, item);
                } else {
                    infos.add(item);
                }
            }
        }
        //拼团人数<=3时
        if (pinMaxNum <= 3) {

            iv_third.setVisibility(GONE);
            if (pinMaxNum == 1){
                ll_second.setVisibility(GONE);
                ll_end.setVisibility(GONE);
            }else if (pinMaxNum == 2){
                ll_end.setVisibility(GONE);
            }else{
                ll_second.setVisibility(VISIBLE);
                ll_end.setVisibility(VISIBLE);
                tv_second_phone.setVisibility(VISIBLE);
            }

            if (infos.size() >= 1) {
                tv_first_phone.setText(infos.get(0).nickName);
                ImageLoaderUtils.loadCircleImage(getContext(), infos.get(0).gravatar, iv_first);
                iv_second.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_member_empty));
                iv_end.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_member_empty));
            }
            if (infos.size() >= 2) {
                tv_second_phone.setText(infos.get(1).nickName);
                ImageLoaderUtils.loadCircleImage(getContext(), infos.get(1).gravatar, iv_second);
                iv_end.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_member_empty));
            }
            if (infos.size() >= 3) {
                tv_end_phone.setText(infos.get(2).nickName);
                ImageLoaderUtils.loadCircleImage(getContext(), infos.get(2).gravatar, iv_end);
            }

        } else {

            tv_second_phone.setVisibility(GONE);
            if (joinNum >= 1) {
                //只有一个中间显示省略号
                tv_end_phone.setVisibility(GONE);
                iv_third.setVisibility(GONE);
                iv_second.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_pin_member_more));
                //加载一个img
                tv_first_phone.setText(infos.get(0).nickName);
                ImageLoaderUtils.loadCircleImage(getContext(), infos.get(0).gravatar, iv_first);
                iv_end.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_member_empty));
            }
            if (joinNum <= pinMaxNum && joinNum > 1) {
                //third显示省略号
                tv_end_phone.setVisibility(GONE);
                iv_third.setVisibility(VISIBLE);
                iv_third.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_pin_member_more));
                iv_end.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pin_member_empty));

                //设置第一个和第二个头像
                tv_second_phone.setText(infos.get(1).nickName);
                ImageLoaderUtils.loadCircleImage(getContext(), infos.get(1).gravatar, iv_second);

                if (joinNum == pinMaxNum) {
                    //全满
                    tv_end_phone.setVisibility(VISIBLE);
                    //设置头像
                    tv_end_phone.setText(infos.get(infos.size() - 1).nickName);
                    ImageLoaderUtils.loadCircleImage(getContext(), infos.get(infos.size() - 1).gravatar, iv_end);
                }
            }


        }

    }


}
