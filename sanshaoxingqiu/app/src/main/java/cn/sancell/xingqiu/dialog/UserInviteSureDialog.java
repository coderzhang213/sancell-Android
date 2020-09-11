package cn.sancell.xingqiu.dialog;

import android.app.Dialog;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseDialogFragment;
import cn.sancell.xingqiu.dialog.listener.OnInviteSureListener;
import cn.sancell.xingqiu.util.ScreenUtils;

/**
  * @author Alan_Xiong
  *
  * @desc: 邀请人信息浮窗
  * @time 2019-10-22 14:45
  */
public class UserInviteSureDialog extends BaseDialogFragment {

    private SimpleDraweeView dv_inviter_avatar;
    private AppCompatTextView tv_inviter_name;
    private AppCompatTextView tv_sure_bind;
    private AppCompatTextView tv_back;
    private String uName, uAvatar;
    private OnInviteSureListener mListener;

    public static UserInviteSureDialog newInstance(String name, String avatar) {

        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("avatar", avatar);
        UserInviteSureDialog fragment = new UserInviteSureDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData() {
        super.getData();
        uName = getArguments().getString("name");
        uAvatar = getArguments().getString("avatar");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_user_invite, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        dv_inviter_avatar = view.findViewById(R.id.dv_inviter_avatar);
        tv_inviter_name = view.findViewById(R.id.tv_inviter_name);
        tv_sure_bind = view.findViewById(R.id.tv_sure_bind);
        tv_back = view.findViewById(R.id.tv_back);
    }

    public void initData() {
        tv_back.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onBack();
            }
            dismiss();
        });
        tv_sure_bind.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onSure();
            }
        });
        dv_inviter_avatar.setImageURI(Uri.parse(uAvatar));
        tv_inviter_name.setText(uName);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(mContext, R.style.common_centerDialog);
        dialog.setContentView(R.layout.dialog_user_invite);
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

    public void setListener(OnInviteSureListener listener){
        mListener = listener;
    }
}
