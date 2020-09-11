package cn.sancell.xingqiu.im;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;

/**
  * @author Alan_Xiong
  *
  * @desc: im 空view
  * @time 2019-11-14 15:24
  */
public class ImEmptyView extends LinearLayout {

    private AppCompatButton btn_empty;
    private AppCompatTextView tv_empty_desc;
    private AppCompatImageView iv_empty;
    private onBtnClickListener mListener;

    public ImEmptyView(Context context) {
        this(context,null);
    }

    public ImEmptyView(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }

    public ImEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_im_empty, (ViewGroup) getRootView(),false);
        initView(view);
        addView(view);
    }

    public void initView(View v){
        iv_empty = v.findViewById(R.id.iv_empty);
        btn_empty = v.findViewById(R.id.btn_empty);
        tv_empty_desc = v.findViewById(R.id.tv_empty_desc);
        btn_empty.setOnClickListener(v1 -> {
            if (mListener != null){
                mListener.onBtnClick();
            }
        });
    }

    /**
     * 描述
     * @param desc
     */
    public void setEmptyDesc(@NonNull String desc){
        tv_empty_desc.setText(desc);
    }

    /**
     * 按钮文字
     * @param str
     */
    public void setBtnStr(@NonNull String str){
        btn_empty.setText(str);
    }


    public interface onBtnClickListener{
        void onBtnClick();
    }

    public void setListener(onBtnClickListener listener){
        mListener = listener;
    }


}
