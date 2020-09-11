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
 * Created by ai11 on 2019/7/22.
 */

public class ModifyPwdTextWatcher implements TextWatcher {
    private EditText oldPwdEditText, newPwdEditText, newPwdAgainEditText;
    private ImageView iv_clear_pwd;
    private TextView btn_login;
    private Context context;

    public ModifyPwdTextWatcher(EditText oldPwdEditText, EditText newPwdEditText, EditText newPwdAgainEditText, TextView btn_login, ImageView iv_clear_pwd, Context context) {
        this.oldPwdEditText = oldPwdEditText;
        this.newPwdEditText = newPwdEditText;
        this.newPwdAgainEditText = newPwdAgainEditText;
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
        if (sourceStr.length() > 0 && newPwdEditText != null && newPwdEditText.getText().toString().length() > 0 && newPwdAgainEditText != null && newPwdAgainEditText.getText().toString().length() > 0) {
            btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite));
            btn_login.setEnabled(true);
        }

        if (iv_clear_pwd != null) {
            if (sourceStr.length() > 0) {
                iv_clear_pwd.setVisibility(View.VISIBLE);
                iv_clear_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldPwdEditText.setText("");
                    }
                });

            } else {
                iv_clear_pwd.setVisibility(View.INVISIBLE);
            }
        }
    }
}
