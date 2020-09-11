package cn.sancell.xingqiu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.dialog.listener.OnInviteSureListener;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.login.CodeLoginActivity;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by zj on 2019/11/26.
 */
public class PrivacyDialog extends Dialog {
    private String userXy = "《用户协议》";
    private Activity mActivity;
    private OnInviteSureListener mOnInviteSureListener;
    private TextView tv_content;

    public PrivacyDialog(@NonNull Activity context) {
        super(context, R.style.dialog);
        this.mActivity = context;
        initView(context);

    }

    public PrivacyDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.mActivity = context;
        initView(context);
    }

    private void initView(Activity context) {
        setContentView(R.layout.dialog_privacy_layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display display = ((WindowManager) SCApp.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setCancelable(false);
        layoutParams.width = (int) (display.getWidth() * 0.8);
        window.setAttributes(layoutParams);
        //同意
        findViewById(R.id.tv_ty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mOnInviteSureListener != null) {
                    mOnInviteSureListener.onSure();
                }
            }
        });
        //退出
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        tv_content = findViewById(R.id.tv_content);
        iniyText();
    }

    public void setmOnInviteSureListener(OnInviteSureListener mOnInviteSureListener) {
        this.mOnInviteSureListener = mOnInviteSureListener;
    }

    private void iniyText() {
        String string = mActivity.getResources().getString(R.string.priv_msg);
        int lastIndex = string.lastIndexOf(userXy);
        int lastIndexTwo = string.lastIndexOf("《隐私政策》");

        tv_content.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tv_content.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        SpannableStringBuilder spannable = new SpannableStringBuilder(string);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FA1905")), lastIndex, lastIndex + userXy.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //这个一定要记得设置，不然点击不生效textView.setMovementMethod(LinkMovementMethod.getInstance());

        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FA1905")), lastIndexTwo, lastIndexTwo + userXy.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //这个一定要记得设置，不然点击不生效textView.setMovementMethod(LinkMovementMethod.getInstance());

        spannable.setSpan(new TextClick(1), lastIndex, lastIndex + userXy.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new TextClick(2), lastIndexTwo, lastIndexTwo + userXy.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_content.setText(spannable);

    }

    private class TextClick extends ClickableSpan {
        private int type;

        public TextClick(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View widget) {
            if (type == 1) {//用户协议
                //if (!StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_userAgreementUrl, ""))) {
                    Intent intent = new Intent(mActivity, UrlInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1,"https://mapi.sanshaoxingqiu.cn/m/event/user-agreement");
                    intent.putExtra(Constants.Key.KEY_2, "用户协议");
                    mActivity.startActivity(intent);
                //}
            } else {//隐私政策
                //if (!StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_privacyAgreementUrl, ""))) {
                    Intent intent = new Intent(mActivity, UrlInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, "https://mapi.sanshaoxingqiu.cn/m/event/privacy-agreement");
                    intent.putExtra(Constants.Key.KEY_2, "隐私协议");
                    mActivity.startActivity(intent);
               // }
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
}
