package cn.sancell.xingqiu.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import cn.sancell.xingqiu.SCApp;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
  * @author Alan_Xiong
  *
  * @desc: 文本工具类
  * @time 2019-11-22 13:08
  */
public class FontUtils {

    private FontUtils() {

    }

    public static FontUtils getInstance() {
        return FontHolder.INSTAMCE;
    }

    private static class FontHolder {
        private static final FontUtils INSTAMCE = new FontUtils();
    }

    /**
     * 改变文字颜色
     * * @param textColor
     *
     * @param content
     * @param changeTextArray
     * @return
     */
    public SpannableStringBuilder changeTextColor(
            @ColorInt int textColor,
            @NonNull String content,
            @NonNull String... changeTextArray) {
        if (changeTextArray.length == 0) {
            return null;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        int start;
        int end = 0;
        for (String changeText : changeTextArray) {
            start = content.indexOf(changeText, end);
            end = start + changeText.length();

            builder.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public SpannableStringBuilder changeTextColor(
            @ColorInt int textColor,
            @NonNull String content,
            @IntRange(from = 0) int start,
            @IntRange(from = 1) int end) {

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    /**
     * 改变文字大小
     *
     * @param textSize        sp
     * @param content
     * @param changeTextArray
     * @return
     */
    public SpannableStringBuilder changeTextSize(
            @NonNull Context context,
            @IntRange(from = 0) int textSize,
            @NonNull String content,
            @NonNull String... changeTextArray) {
        if (changeTextArray.length == 0) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        int start;
        int end = 0;
        int length = changeTextArray.length;
        int i = 0;
        while (i < length) {
            String changeText = changeTextArray[i];
            start = content.indexOf(changeText, end);
            end = start + changeText.length();

            builder.setSpan(new AbsoluteSizeSpan(ScreenUtils.sp2px(context, textSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            i++;
        }
        return builder;
    }

    public SpannableStringBuilder changeTextSize(
            @NonNull Context context,
            @IntRange(from = 0) int textSize,
            @NonNull String content,
            @IntRange(from = 0) int start,
            @IntRange(from = 1) int end) {

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new AbsoluteSizeSpan(ScreenUtils.sp2px(context, textSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    /**
     * 改变文字大小和颜色
     *
     * @param textColor
     * @param textSize
     * @param content
     * @param changeTextArray
     * @return
     */
    public SpannableStringBuilder changeTextSizeColor(
            @NonNull Context context,
            @ColorInt int textColor,
            @IntRange(from = 0) int textSize,
            @NonNull String content,
            @NonNull String... changeTextArray) {
        if (changeTextArray.length == 0) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        int start;
        int end = 0;
        int length = changeTextArray.length;
        for (String changeText : changeTextArray) {
            start = content.indexOf(changeText, end);
            end = start + changeText.length();
            builder.setSpan(new AbsoluteSizeSpan(ScreenUtils.sp2px(context, textSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(ScreenUtils.sp2px(context, textColor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return builder;
    }

    /**
     * 复制文本
     * @param context
     * @param str
     */
    public void copyText(Context context,@NonNull String str){
        ClipboardManager mClipboardManager =(ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
        // 将ClipData内容放到系统剪贴板里。
        mClipboardManager.setPrimaryClip(mClipData);
        SCApp.getInstance().showSystemCenterToast("复制成功");
    }
}
