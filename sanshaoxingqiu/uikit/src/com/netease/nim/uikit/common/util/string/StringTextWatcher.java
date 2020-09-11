package com.netease.nim.uikit.common.util.string;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.netease.nim.uikit.common.ToastHelper;

/**
 * EditText字符数限制
 * Created by hzxuwen on 2015/5/22.
 */
public class StringTextWatcher implements TextWatcher {
    private int length;
    private EditText editText;
    private Context context;

    public StringTextWatcher(Context context,int length, EditText editText) {
        this.length = length;
        this.editText = editText;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int editEnd = editText.getSelectionEnd();
        editText.removeTextChangedListener(this);
        while (s.toString().length() > length && editEnd > 0) {
            s.delete(editEnd - 1, editEnd);
            editEnd--;
            ToastHelper.showToast(context,"字数不能超过"+length+"个");
        }
        editText.setSelection(editEnd);
        editText.addTextChangedListener(this);
    }
}
