package org.openpackage.asf.comp.c2.span.impl;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import org.openpackage.asf.comp.c2.span.DataSpan;
import org.openpackage.asf.comp.c2.span.SpanObject;

/**
 * Created by micfans on 28/11/2016.
 */

public class AtSpan extends DataSpan {

    /**
     *
     */
    public static final String SPAN_TYPE = "at";

    /**
     *
     * @param context
     * @param data
     */
    public AtSpan(Context context, SpanObject data) {
        super(context, data);
    }

    /**
     *
     * @param context
     * @param data
     * @param clickable
     */
    public AtSpan(Context context, SpanObject data, boolean clickable) {
        super(context, data, clickable);
    }

    /**
     *
     * @return
     */
    @Override
    public String getSpanType() {
        return SPAN_TYPE;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isWhole() {
        return true;
    }


}
