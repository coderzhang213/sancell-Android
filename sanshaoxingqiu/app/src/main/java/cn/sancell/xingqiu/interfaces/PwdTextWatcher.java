package cn.sancell.xingqiu.interfaces;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sancell.xingqiu.R;

/**
 * Created by ai11 on 2019/6/4.
 */

public class PwdTextWatcher implements TextWatcher {
    private EditText phoneNumberEditText, pwdEditText;
    private ImageView iv_clear_pwd;
    private TextView btn_login;
    private CheckBox cb_password;
    String tmp;
    private Context context;

    public PwdTextWatcher(EditText numberEditText, EditText pwdEditText, TextView btn_login, CheckBox cb_password, ImageView iv_clear_pwd, Context context) {
        this.phoneNumberEditText = numberEditText;
        this.pwdEditText = pwdEditText;
        this.cb_password = cb_password;
        this.iv_clear_pwd = iv_clear_pwd;
        this.btn_login = btn_login;
        this.context = context;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite_tran66));
        btn_login.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String sourceStr = s.toString();
        if (sourceStr.length() > 0 && phoneNumberEditText != null && phoneNumberEditText.getText().toString().length() > 0) {
            btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite));
            btn_login.setEnabled(true);
        }

        if (iv_clear_pwd != null) {
            if (sourceStr.length() > 0) {
                cb_password.setVisibility(View.VISIBLE);
                iv_clear_pwd.setVisibility(View.VISIBLE);
                iv_clear_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pwdEditText.setText("");
                    }
                });

            } else {
                cb_password.setVisibility(View.INVISIBLE);
                iv_clear_pwd.setVisibility(View.INVISIBLE);
            }
        }
    }
}
