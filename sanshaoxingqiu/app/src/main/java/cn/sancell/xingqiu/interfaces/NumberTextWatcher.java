package cn.sancell.xingqiu.interfaces;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sancell.xingqiu.R;

/**
 * Created by camelliae on 2016/12/21.
 */
public class NumberTextWatcher implements TextWatcher {
    private EditText phoneNumberEditText;
    private EditText codeEditText;
    private TextView btn_dialog_getcode;
    private ImageView btn_delete_phone;
    private TextView btn_login;
    String tmp;
    private Context context;

    int start, count, beforeSelectionStart;

    public NumberTextWatcher(EditText numberEditText, EditText codeEditText, TextView btn_dialog_getcode, ImageView btn_delete_phone, TextView btn_login, Context context) {
        this.phoneNumberEditText = numberEditText;
        this.btn_dialog_getcode = btn_dialog_getcode;
        this.codeEditText = codeEditText;
        this.btn_login = btn_login;
        this.btn_delete_phone = btn_delete_phone;
        this.context = context;
    }

    public NumberTextWatcher(EditText numberEditText, EditText codeEditText, ImageView btn_delete_phone, TextView btn_login, Context context) {
        this.phoneNumberEditText = numberEditText;
        this.codeEditText = codeEditText;
        this.btn_login = btn_login;
        this.btn_delete_phone = btn_delete_phone;
        this.context = context;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.count = count;
        if (btn_dialog_getcode != null) {
            btn_dialog_getcode.setTextColor(context.getResources().getColor(R.color.color_text4));
            btn_dialog_getcode.setClickable(false);
        }
        if (btn_login != null) {
            btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite_tran66));
            btn_login.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeSelectionStart = phoneNumberEditText.getSelectionStart();
    }

    @Override
    public void afterTextChanged(Editable s) {
        char c = ' ';
        String sourceStr = s.toString();
        if (tmp != null && tmp.equals(sourceStr)) {
            return;
        }
        tmp = separateString(sourceStr, 3, 4, c);
        if (!tmp.equals(sourceStr)) {
            phoneNumberEditText.setText(tmp);
            if (start == 0) {
                if (s.length() == (count - 1)) {
                    phoneNumberEditText.setSelection(tmp.length());
                } else if (s.length() == count) {
                    phoneNumberEditText.setSelection(beforeSelectionStart);
                }

            }
            //phoneNumberEditText.setSelection(tmp.length());
        }
        if (btn_dialog_getcode != null) {
            if (tmp.length() == 13 && !btn_dialog_getcode.getText().toString().contains("s")) {
                btn_dialog_getcode.setTextColor(context.getResources().getColor(R.color.color_text1));
                btn_dialog_getcode.setClickable(true);
            }
        }

        if (btn_delete_phone != null) {
            if (sourceStr.length() > 0) {
                btn_delete_phone.setVisibility(View.VISIBLE);
                btn_delete_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        phoneNumberEditText.setText("");
                    }
                });
            } else {
                btn_delete_phone.setVisibility(View.INVISIBLE);
            }
        }
        if (btn_login != null && codeEditText != null) {
            if (sourceStr.length() == 13 && codeEditText.getText().toString().length() > 0) {
                btn_login.setTextColor(context.getResources().getColor(R.color.colorWhite));
                btn_login.setEnabled(true);
            }
        }
    }

    /**
     * 用指定字符分隔格式化字符串
     * <br/>（如电话号为1391235678 指定startIndex为3，step为4，separator为'-'经过此处理后的结果为 <br/> 139-1234-5678）
     *
     * @param source     需要分隔的字符串
     * @param startIndex 开始分隔的索引号
     * @param step       步长
     * @param separator  指定的分隔符
     * @return 返回分隔格式化处理后的结果字符串
     */
    private String separateString(String source, int startIndex, int step, char separator) {
        int times = 0;
        StringBuilder tmpBuilder = new StringBuilder(source);
        for (int i = 0; i < tmpBuilder.length(); i++) {
            if (i == startIndex + step * times + times) {//if(i == 3 || i == 8){
                if (separator != tmpBuilder.charAt(i)) {
                    tmpBuilder.insert(i, separator);
                }
                times++;
            } else {
                if (separator == tmpBuilder.charAt(i)) {
                    tmpBuilder.deleteCharAt(i);
                    i = -1;
                    times = 0;
                }
            }
        }
        return tmpBuilder.toString();
    }
}
