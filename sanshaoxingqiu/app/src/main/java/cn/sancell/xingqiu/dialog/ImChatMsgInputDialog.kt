package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.interfaces.OnSendChatMsgLinsener
import cn.sancell.xingqiu.live.activity.InputActivity
import com.netease.nim.uikit.common.ToastHelper

/**
 * Created by zj on 2020/4/2.
 */
class ImChatMsgInputDialog(context: Context) : Dialog(context, R.style.dialogChatPwdWindowStyle) {
    var mEditText: EditText? = null
    var mRootView: View? = null
    var mOnSendChatMsgLinsener: OnSendChatMsgLinsener? = null
    private var screenHeight = 0
    private var keyboardOldHeight = -1
    private var keyboardNowHeight = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nim_message_dialog_text_layout)
        val window: Window = getWindow()!!
        /**
         * 位于底部
         */
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        /**
         * 设置弹出动画
         */
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom_pay_pwd)
        // 设置点击外围解散
        setCanceledOnTouchOutside(true)
        findIdView()
    }

    /**
     * 发送消息监听
     */

    fun setOnSendChatMsgLinsener(mOnSendChatMsgLinsener: OnSendChatMsgLinsener) {
        this.mOnSendChatMsgLinsener = mOnSendChatMsgLinsener;
    }


    fun findIdView() {
        mRootView = findViewById(R.id.textMessageLayout)
        mRootView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                window!!.decorView.getWindowVisibleDisplayFrame(r)
                if (screenHeight == 0) {
                    screenHeight = r.bottom
                }
                keyboardNowHeight = screenHeight - r.bottom
                if (keyboardOldHeight != -1 && keyboardNowHeight != keyboardOldHeight && keyboardNowHeight <= 0) {
                    // 只适用于没有表情和更多布局的修改
                    dismiss()
                }
                keyboardOldHeight = keyboardNowHeight
            }
        })

        mEditText = findViewById(R.id.editTextMessage)
        mEditText?.setImeOptions(EditorInfo.IME_ACTION_SEND)
        mEditText?.setFocusable(true)
        mEditText?.setFocusableInTouchMode(true)
        mEditText?.requestFocus()
        mEditText?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                Log.i("keey", "actionId:" + actionId)
//                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
//                    //处理事件
//                    sendMsg()
//                    return true
//                }
                return false
            }
        })
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        findViewById<TextView>(R.id.buttonSendMessage).setOnClickListener {
            sendMsg()
        }
    }

    /**
     * 发送消息
     */
    fun sendMsg() {
        val msg = mEditText?.text?.toString()
        if (TextUtils.isEmpty(msg)) {
            ToastHelper.showToast("发送消息不能为空")
            return
        }
        //隐藏软键盘

        mEditText?.setText("")
        mOnSendChatMsgLinsener?.onSendTextMsg(msg!!)
        hideInputMethod()
        dismiss()
    }

    override fun dismiss() {
        super.dismiss()

    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    fun showInput(et: EditText) {
        et.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideInputMethod() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText?.getWindowToken(), 0)
    }
}