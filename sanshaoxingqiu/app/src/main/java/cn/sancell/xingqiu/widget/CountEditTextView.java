package cn.sancell.xingqiu.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * @author Alan_Xiong
 * @desc: 购物车数量编辑的editText，封装业务逻辑
 * @time 2019-10-25 14:30
 */
public class CountEditTextView extends AppCompatEditText {

    public int buyNum = 0;

    public CountEditTextView(Context context) {
        super(context);
    }

    public CountEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 初始化购买数量
     *
     * @param baseNum   基础购买
     * @param canBuyNum 最多购买数量（库存或者）
     */
    public void initEditText(int baseNum, int canBuyNum) {

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!TextUtils.isEmpty(input)) {
                    try {
                        buyNum = Integer.parseInt(input);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (buyNum == 0) {
                        buyNum = 1;

                    } else if (buyNum <= baseNum) {
                        buyNum = baseNum;

                    } else if (buyNum > 999 || buyNum > canBuyNum) {
                        //取999 与库存的最小
                        if (canBuyNum >= 1000) {
                            buyNum = 999;
                        }else{
                            buyNum = canBuyNum;
                        }

                    } else {
                        buyNum = 0;
                    }
                    setText(String.valueOf(buyNum));

                }
            }
        };
        addTextChangedListener(watcher);
    }

}
