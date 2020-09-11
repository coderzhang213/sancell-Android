package cn.sancell.xingqiu.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseDialogFragment;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
 * @author Alan_Xiong
 * @desc: 订单异常的提示
 * @time 2019-12-12 14:24
 */
public class OrderTipDialogFgm extends BaseDialogFragment {


    private AppCompatTextView tv_title;
    private AppCompatButton btn_remove;
    private AppCompatTextView tv_cancel;

    private String tvDesc, btnStr, cancelStr;

    private OnDialogListener mlistener;

    public static OrderTipDialogFgm newInstance(String desc, String sureStr, String cancelStr) {

        Bundle args = new Bundle();

        args.putString("desc", desc);
        args.putString("sureStr", sureStr);
        args.putString("cancelStr", cancelStr);
        OrderTipDialogFgm fragment = new OrderTipDialogFgm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData() {
        super.getData();
        tvDesc = getArguments().getString("desc");
        btnStr = getArguments().getString("sureStr");
        cancelStr = getArguments().getString("cancelStr");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_tip, container, false);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tv_title = view.findViewById(R.id.tv_title);
        btn_remove = view.findViewById(R.id.btn_remove);
        tv_cancel = view.findViewById(R.id.tv_cancel);

        initData();
    }

    void initData() {

        tv_title.setText(tvDesc);
        btn_remove.setText(btnStr);

        if (TextUtils.isEmpty(cancelStr)) {
            tv_cancel.setVisibility(View.GONE);
        } else {
            tv_cancel.setVisibility(View.VISIBLE);
        }
        tv_cancel.setOnClickListener(v -> dismiss());
        btn_remove.setOnClickListener(v -> {
            if (mlistener != null){
                mlistener.onSure();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(mContext, R.style.common_centerDialog);
        dialog.setContentView(R.layout.dialog_order_tip);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_white_8));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.width = ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext, 50);
            lp.dimAmount = 0.55f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }

    public interface OnDialogListener{
        void onSure();
    }

    public void setOnDialogListener(OnDialogListener listener){
        mlistener = listener;
    }

}
