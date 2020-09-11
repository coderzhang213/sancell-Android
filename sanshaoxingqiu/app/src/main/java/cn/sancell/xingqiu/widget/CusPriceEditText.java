package cn.sancell.xingqiu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ai11 on 2019/8/21.
 * 修改红包抵扣价格输入
 */

@SuppressLint("AppCompatCustomView")
public class CusPriceEditText extends EditText {
    private Context context;

    public CusPriceEditText(Context context) {
        super(context);
        this.context = context;
        addTextChangedListener(textWatcher);
    }

    public CusPriceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        addTextChangedListener(textWatcher);
    }

    public CusPriceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    setText(s);
                    setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                setText(s);
                setSelection(2);
            }
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    setText(s.subSequence(0, 1));
                    setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
