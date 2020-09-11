package cn.sancell.xingqiu.dialog;

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
import androidx.appcompat.widget.AppCompatTextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseDialogFragment;
import cn.sancell.xingqiu.dialog.listener.OnInviteTipListener;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
  * @author Alan_Xiong
  *
  * @desc: 邀请人信息浮窗
  * @time 2019-10-22 14:45
  */
public class UserInviteTipDialog extends BaseDialogFragment {

    private AppCompatTextView tv_inviter_msg;
    private AppCompatTextView tv_sure_bind;
    private AppCompatTextView tv_back;
    private String uMsg;
    private String btnStr;
    private OnInviteTipListener mListener;


    public static UserInviteTipDialog newInstance(String msg,String btnStr) {

        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putString("btnStr",btnStr);
        UserInviteTipDialog fragment = new UserInviteTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData() {
        super.getData();
        uMsg = getArguments().getString("msg");
        btnStr = getArguments().getString("btnStr");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_user_invite_tip, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tv_inviter_msg = view.findViewById(R.id.tv_inviter_msg);
        tv_sure_bind = view.findViewById(R.id.tv_sure_bind);
        tv_back = view.findViewById(R.id.tv_back);
    }

    public void initData() {
        tv_back.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onUnBind();
            }
            dismiss();
        });
        tv_sure_bind.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onBind();
            }
            dismiss();
        });
        tv_inviter_msg.setText(uMsg);
        tv_sure_bind.setText(btnStr);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(mContext, R.style.common_centerDialog);
        dialog.setContentView(R.layout.dialog_user_invite_tip);
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

    public void setTipListener(OnInviteTipListener listener){
        mListener = listener;
    }
}
