package org.openpackage.asf.comp.c2.span.impl;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;

import org.openpackage.asf.comp.c2.R;
import org.openpackage.asf.comp.c2.span.DataSpan;
import org.openpackage.asf.comp.c2.span.RenderSpan;
import org.openpackage.asf.comp.c2.span.SpanObject;

/**
 * Created by micfans on 01/12/2016.
 */

public class FixSpan extends DataSpan implements RenderSpan{

    /**
     *
     */
    public static final String SPAN_TYPE = "fix";

    /**
     *
     * @param context
     * @param data
     */
    public FixSpan(Context context, SpanObject data) {
        super(context, data);
    }

    /**
     *
     * @param context
     * @param data
     * @param clickable
     */
    public FixSpan(Context context, SpanObject data, boolean clickable) {
        super(context, data, clickable);
    }

    /**
     *
     * @param tp
     */
    @Override
    public void updateDrawState(TextPaint tp) {
        super.updateDrawState(tp);
        tp.setColor(Color.GRAY);
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

    /**
     *
     * @return
     */
    @Override
    public boolean deleteable() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean moveable(){
        return false;
    }
}
