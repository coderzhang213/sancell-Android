package cn.sancell.xingqiu.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import cn.sancell.xingqiu.R

/**
 * 尾部删除按钮的editText
 */
class EditTextWithDel(mContext: Context, mAttributeSet: AttributeSet) : AppCompatEditText(mContext, mAttributeSet) {

    var delDrawable: Drawable? = null

    init {
        initListener()
    }

    private fun initListener() {
        delDrawable = context.resources.getDrawable(R.mipmap.icon_edit_clear)
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setDrawable()
            }

        })
    }

    fun setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, delDrawable, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val eventX: Int = event.rawX.toInt()
            val eventY: Int = event.rawY.toInt()
            val rect = Rect()
            getGlobalVisibleRect(rect)
            rect.left = rect.right - 100
            if (rect.contains(eventX, eventY)) {
                setText("")
            }
        }
        return super.onTouchEvent(event)
    }


}