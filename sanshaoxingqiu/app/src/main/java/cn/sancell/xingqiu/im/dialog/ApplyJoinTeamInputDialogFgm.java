package cn.sancell.xingqiu.im.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseDialogFragment;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
  * @author Alan_Xiong
  *
  * @desc: 申请验证输入
  * @time 2019-11-24 23:53
  */
public class ApplyJoinTeamInputDialogFgm extends BaseDialogFragment {

    private AppCompatEditText et_ver;
    private AppCompatTextView btn_sure;

    private onApplyVerListener mListener;

    public static ApplyJoinTeamInputDialogFgm newInstance() {

        Bundle args = new Bundle();

        ApplyJoinTeamInputDialogFgm fragment = new ApplyJoinTeamInputDialogFgm();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_apply_info,container,false);
        initView(view);
        return view;
    }

    public void initView(View view){
        et_ver = view.findViewById(R.id.et_ver);
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onApplyMsg(et_ver.getText().toString());
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(mContext, R.style.common_centerDialog);
        dialog.setContentView(R.layout.dialog_input_apply_info);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_white_8));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.width = ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext,50);
            lp.dimAmount = 0.55f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        return dialog;
    }

    public interface onApplyVerListener{
        void onApplyMsg(String str);
    }

    public void setOnApplyListener(onApplyVerListener listener){
        mListener = listener;
    }
}
