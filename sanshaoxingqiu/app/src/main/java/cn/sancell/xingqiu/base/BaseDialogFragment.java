package cn.sancell.xingqiu.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    public Context mContext;
    public boolean isTransparent;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onCurrentAttach(context);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onCurrentAttach(activity);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isTransparent) {
            //设置背景半透明
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getData();
        }
    }

    /**
     * 重写此方法替代onAttach()高低版本不兼容
     *
     * @param mContext
     */
    protected void onCurrentAttach(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 统一获取传参
     */
    protected void getData() {

    }

    /**
     * 绑定控件
     */
    protected void initView(View view) {

    }

    /**
     * 判断弹窗是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }


    /**
     * 关闭DialogFragment
     *
     * @param isResume 在Fragment中使用可直接传入isResumed()
     *                 在FragmentActivity中使用可自定义全局变量 boolean isResumed 在onResume()和onPause()中分别传人判断为true和false
     */
    public void dismiss(boolean isResume) {
        if (isResume) {
            dismiss();
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (isShowing()) {
            super.dismissAllowingStateLoss();
        }
    }
}
