package com.manyi.mall.utils;

import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by bestar on 2015/3/12.
 */
public class TextViewUtil {


    /**
     * 设置textView 一部分文字的颜色
     * @param tv  需要设置的控件
     * @param color 部分文字的颜色
     * @param str 需要设置特殊颜色的文字
     */
    public static void setTextSpan(TextView tv,int color,String str){
        if (str.trim().length() == 0){
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(tv.getText().toString());

        for (int i=0;i<str.trim().length();i++){
            ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
            String s = str.substring(i,i+1);
            if (!tv.getText().toString().trim().contains(s)){
                continue;
            }
            int index = tv.getText().toString().trim().indexOf(s);
            builder.setSpan(redSpan, index, index+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(builder);
    }


    /**
     * 设置输入框
     * @param view
     * @param position
     */
    public static void setTextSelection(TextView view,int position){
        CharSequence text = view.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable)text;
            Selection.setSelection(spanText, position);
           }
    }
}
