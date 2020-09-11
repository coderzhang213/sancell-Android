package cn.sancell.xingqiu.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2019/7/26.
 */
public class ComfirmDialog extends Dialog {
    private Context contexts;
    //动态权限所需要的类
    private OnCutCityLinsener mOnCallPhoneLinsener;
    private TextView tv_call;
    private TextView tv_commit;

    public ComfirmDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        initView(context);
    }

    public ComfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected ComfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public void setOnCutCityLinsener(OnCutCityLinsener mOnCallPhoneLinsener) {
        this.mOnCallPhoneLinsener = mOnCallPhoneLinsener;
    }

    private void initView(Context context) {
        this.contexts = context;
        setContentView(R.layout.view_cut_city_layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (ScreenUtil.screenWidth * 0.8);
        tv_call = findViewById(R.id.tv_call);
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        findViewById(R.id.tv_cancer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCallPhoneLinsener != null) {
                    mOnCallPhoneLinsener.onCancerLinsener();
                }
                dismiss();
            }
        });
        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(new View.OnClickListener() {//拨打电话
            @Override
            public void onClick(View view) {
                if (mOnCallPhoneLinsener != null) {
                    mOnCallPhoneLinsener.onConfirmLinsener();
                }
                dismiss();
            }
        });

    }

    public void setMsg(String msg) {
        tv_call.setText(msg);
    }

    /**
     * 设置弹框确定框提示
     */
    public void setCommitMsg(String msg) {
        tv_commit.setText(msg);
    }


    public interface OnCutCityLinsener {
        void onConfirmLinsener();

        void onCancerLinsener();
    }
}
