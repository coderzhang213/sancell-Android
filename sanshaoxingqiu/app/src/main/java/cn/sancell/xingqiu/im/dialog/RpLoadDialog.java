package cn.sancell.xingqiu.im.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
public class RpLoadDialog extends Dialog {

    public RpLoadDialog(@NonNull Context context) {
        super(context, R.style.common_centerDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rp_load, null);
        setContentView(view);
        AppCompatImageView iv_loading = view.findViewById(R.id.iv_loading);
        Glide.with(getContext()).load(R.drawable.icon_rp_loading).into(iv_loading);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.55f;
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWindow.setAttributes(lp);
    }


}
