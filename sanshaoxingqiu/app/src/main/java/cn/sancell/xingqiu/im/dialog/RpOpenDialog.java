package cn.sancell.xingqiu.im.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

import cn.sancell.xingqiu.R;

/**
 * @author Alan_Xiong
 * @desc: 红包loading
 * @time 2020-01-05 14:39
 */
public class RpOpenDialog extends Dialog {

    private onRpOpenListener mListener;
    boolean isOpenIng = false;

    public RpOpenDialog(@NonNull Context context) {
        super(context, R.style.common_centerDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rp_open, null);
        setContentView(view);
        AppCompatImageView iv_open = view.findViewById(R.id.iv_open);
        AppCompatImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpenIng){
                    dismiss();
                }
            }
        });
        iv_open.setOnClickListener(v -> {
            //动画
//            Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rp_rotate_anim);
//            iv_open.setAnimation(rotate);
//            iv_open.startAnimation(rotate);
            isOpenIng = true;
            Glide.with(getContext()).load(R.drawable.icon_open_btn).into(iv_open);
            if (mListener != null){
                mListener.onOpen();
            }
        });


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.55f;
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWindow.setAttributes(lp);

        setCanceledOnTouchOutside(false);
        setOnKeyListener((dialog, keyCode, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                if (!isOpenIng){
                    dismiss();
                    return false;
                }
            }
            return true;
        });

    }


    public interface onRpOpenListener{
        void onOpen();
    }

    public void setOpenListener(onRpOpenListener listener){
        mListener = listener;
    }


}
