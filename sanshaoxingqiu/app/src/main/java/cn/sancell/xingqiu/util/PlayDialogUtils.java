package cn.sancell.xingqiu.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.interfaces.OnPlayPasswordLinsenr;
import cn.sancell.xingqiu.widget.VerificationCodeEditText;

public class PlayDialogUtils {

    /**
     * 输入支付密码对话框
     */

    private Dialog dialog_check_pay_pwd;

    public void showCheckPayPwdDialog(Context context, OnPlayPasswordLinsenr mOnPlayPasswordLinsenr) {
        View view = View.inflate(context, R.layout.dialog_create_order_check_pay_pwd,
                null);
        dialog_check_pay_pwd = new Dialog(context, R.style.dialogPayPwdWindowStyle);
        dialog_check_pay_pwd.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog_check_pay_pwd.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom_pay_pwd);
        // 设置点击外围解散
        dialog_check_pay_pwd.setCanceledOnTouchOutside(true);
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        VerificationCodeEditText dialog_ed_pay_pwd = view.findViewById(R.id.ed_pay_pwd);
        dialog_ed_pay_pwd.requestFocus();
        TextView tv_error_tip = view.findViewById(R.id.tv_error_tip);
        iv_dialog_close.setOnClickListener(v -> {
            ScreenUtils.hideKeyboard(dialog_ed_pay_pwd);
            dialog_check_pay_pwd.dismiss();
        });

        dialog_ed_pay_pwd.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    tv_error_tip.setText("");
                }
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                String pay_pwd = s.toString();
                if (mOnPlayPasswordLinsenr != null) {
                    mOnPlayPasswordLinsenr.onComrinPlayPaasord(pay_pwd);
                }
                //createOrder(pay_pwd);
            }
        });

        dialog_check_pay_pwd.show();
    }

    public void dismiss() {
        if (dialog_check_pay_pwd != null) {
            dialog_check_pay_pwd.dismiss();
        }
    }

}
