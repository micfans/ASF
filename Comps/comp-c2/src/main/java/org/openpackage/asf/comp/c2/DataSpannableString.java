package org.openpackage.asf.comp.c2;

import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import org.openpackage.asf.comp.c2.span.OnSpanClickListener;
import org.openpackage.asf.comp.c2.span.Span;

import java.lang.ref.WeakReference;

/**
 * Created by micfans on 29/11/2016.
 */

public class DataSpannableString extends SpannableString {

    /**
     * 所含数据区块（只能一个）
     */
    private Span span;

    /**
     *
     * @param source 文本
     * @param span 数据区块
     * @param dataSpanSupportable 文本附加对象
     */
    public DataSpannableString(CharSequence source, Span span, DataSpanSupportable dataSpanSupportable) {
        super(source);
        WeakReference<DataSpanSupportable> textWeakReference = new WeakReference<>(dataSpanSupportable);
        this.span = span;
        span.apply(this, textWeakReference);
    }

    /**
     *
     * @param onSpanClickListener
     */
    public void setOnClickListener(OnSpanClickListener onSpanClickListener){
        span.setOnClickListener(onSpanClickListener);
    }

    /**
     *
     * @return
     */
    public Span getDataSpan(){
        return span;
    }

}
