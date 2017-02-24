package org.openpackage.asf.comp.c2.span;

import android.content.Context;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
import android.view.View;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.DataSpanSupportable;
import org.openpackage.asf.comp.c2.DataSpannableString;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by micfans on 28/11/2016.
 */

public abstract class DataSpan extends CharacterStyle implements Span, UpdateAppearance {

    private static final Loger loger = Loger.getLoger(DataSpan.class);

    /**
     * 数据对象
     */
    protected SpanObject data = new SpanObject("unknown");

    /**
     * 附加区块
     */
    protected Set additionSpans = new HashSet<>();

    /**
     * 数据区块支持体的弱引用
     */
    private WeakReference<DataSpanSupportable> textWeakReference;

    /**
     * 是否支持点击
     */
    private boolean clickable = false;

    /**
     * 点击事件监听器
     */
    private OnSpanClickListener spanClickListener;

    /**
     * 应用上下文
     */
    protected Context context;

    /**
     * @param context
     * @param data
     * @param clickable
     */
    public DataSpan(Context context, SpanObject data, boolean clickable) {
        this.data = data;
        this.context = context;
        setClickable(clickable);
    }

    /**
     * @param context
     * @param data
     */
    public DataSpan(Context context, SpanObject data) {
        this(context, data, false);
    }

    /**
     * @return
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * @param clickable
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        if (clickable) {
            additionSpans.add(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    if (spanClickListener != null) {
                        loger.d("Clicked span, data: " + DataSpan.this.getSpanObject().toString());
                        spanClickListener.onClick(DataSpan.this);
                    }
                }
            });
        } else {
            for (Object span : additionSpans) {
                if (span instanceof ClickableSpan) {
                    additionSpans.remove(span);
                    break;
                }
            }
        }
    }

    /**
     * @param spanClickListener
     */
    @Override
    public void setOnClickListener(final OnSpanClickListener spanClickListener) {
        this.spanClickListener = spanClickListener;
    }

    /**
     * @param tp
     */
    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(tp.linkColor);
        tp.setUnderlineText(false);
    }

    /**
     * @return
     */
    @Override
    public boolean deleteable() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean moveable() {
        return true;
    }

    /**
     * @param what
     */
    @Override
    public void addAdditionSpan(Object what) {
        if(what instanceof Span){
            throw new IllegalArgumentException("Addition span must not instance of Span type.");
        }
        additionSpans.add(what);
        textWeakReference.get().onSpanUpdated(this);
    }

    /**
     * @return
     */
    @Override
    public SpanObject getSpanObject() {
        return data;
    }


    /**
     * @return
     */
    @Override
    public String getSpanText() {
        return data.getText();
    }

    /**
     * @return 区块类型
     */
    public abstract String getSpanType();

    /**
     * 将自身应用到区块字串
     *
     * @param dataSpannableString
     * @param textWeakReference
     */
    @Override
    public void apply(DataSpannableString dataSpannableString, WeakReference<DataSpanSupportable> textWeakReference) {
        this.textWeakReference = textWeakReference;
        applySpan(dataSpannableString, this);
        for (Object sp : additionSpans) {
            applySpan(dataSpannableString, sp);
        }
    }

    /**
     * 将给定的区块应用到区块字串
     *
     * @param dataSpannableString
     * @param span
     */
    protected void applySpan(DataSpannableString dataSpannableString, Object span) {
        dataSpannableString.setSpan(span, 0, dataSpannableString.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 译为富文本
     *
     * @return
     */
    @Override
    public String toSpanString() {
        String str = "<span type=\"" + getSpanType() + "\" ";
        str += "clickable=\"" + clickable + "\" ";
        Iterator<Map.Entry<String, String>> it = data.getAttrs().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            str += en.getKey() + "=\"" + en.getValue() + "\" ";
        }
        str += ">" + getSpanText() + "</span>";
        return str;
    }

    @Override
    public void removeFrom(Spannable spannable) {
        spannable.removeSpan(this);
        for (Object sp : additionSpans) {
            spannable.removeSpan(sp);
        }
    }

}
