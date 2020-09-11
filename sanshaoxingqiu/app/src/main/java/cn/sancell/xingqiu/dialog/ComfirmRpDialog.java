package cn.sancell.xingqiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2019/7/26.
 */
public class ComfirmRpDialog extends Dialog {
    private Context contexts;
    //动态权限所需要的类
    private TextView tv_call;
    private AppCompatTextView tv_title;
    private OnPhoneCallListener mListener;

    public ComfirmRpDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        initView(context);
    }

    public ComfirmRpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected ComfirmRpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }


    private void initView(Context context) {
        this.contexts = context;
        setContentView(R.layout.dialog_confirm_use_rp);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (ScreenUtil.screenWidth * 0.8);
        tv_call = findViewById(R.id.tv_call);
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_call.setOnClickListener(new View.OnClickListener() {//拨打电话
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCallListener();
                }
                dismiss();
            }
        });

    }

    public void setTitleMsg(String msg) {
        tv_title.setText(msg);
    }


    public interface OnPhoneCallListener {
        void onCallListener();
    }

    public void setListener(OnPhoneCallListener listener){
        mListener = listener;
    }
}
