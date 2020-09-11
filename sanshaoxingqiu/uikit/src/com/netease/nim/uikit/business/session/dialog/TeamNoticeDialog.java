package com.netease.nim.uikit.business.session.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.netease.nim.uikit.R;

/**
  * @author Alan_Xiong
  *
  * @desc: 群公告提示
  * @time 2019-12-06 14:04
  */
public class TeamNoticeDialog extends Dialog {

    private Context mContext;
    private AppCompatTextView tv_desc;
    private AppCompatTextView btn_sure;
    private String mNoticeStr;

    public TeamNoticeDialog(Context context,String msg) {
        super(context, R.style.common_centerDialog);
        this.mContext = context;
        this.mNoticeStr = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_im_team_notice,null);
        setContentView(view);

        tv_desc = view.findViewById(R.id.tv_desc);
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(v -> dismiss());

        tv_desc.setText(mNoticeStr);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
}
