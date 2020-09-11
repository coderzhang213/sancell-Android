package cn.sancell.xingqiu.interfaces;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/6/4.
 */

public class CodeTextWatcher implements TextWatcher {
    private EditText phoneNumberEditText, codeEditText;
    private TextView btn_login;
    private ImageView iv_clear_code;
    String tmp;
    private Context context;

    public CodeTextWatcher(EditText numberEditText, EditText codeEditText, TextView btn_login, ImageView iv_clear_code, Context context) {
        this.phoneNumberEditText = numberEditText;
        this.btn_login = btn_login;
        this.codeEditText = codeEditText;
        this.context = context;
        this.iv_clear_code = iv_clear_code;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite_tran66));
        btn_login.setEnabled(false);
        btn_login.setClickable(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String sourceStr = s.toString();
        if (sourceStr.length() > 0 && phoneNumberEditText != null && phoneNumberEditText.getText().toString().length() == 13) {
            btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite));
            btn_login.setEnabled(true);
            btn_login.setClickable(true);
        }
        if (iv_clear_code != null) {
            if (sourceStr.length() > 0) {
                iv_clear_code.setVisibility(View.VISIBLE);
                iv_clear_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        codeEditText.setText("");
                    }
                });
            } else {
                iv_clear_code.setVisibility(View.INVISIBLE);
            }
        }
    }
}
