package org.openpackage.asf.comp.c2.span.impl;

import android.content.Context;

import org.openpackage.asf.comp.c2.span.DataSpan;
import org.openpackage.asf.comp.c2.span.RenderSpan;
import org.openpackage.asf.comp.c2.span.SpanObject;

/**
 * Created by micfans on 28/11/2016.
 */

public class TopicSpan extends DataSpan implements RenderSpan {

    /**
     *
     */
    public static String SPAN_TYPE = "topic";

    /**
     *
     * @param context
     * @param data
     */
    public TopicSpan(Context context, SpanObject data) {
        super(context, data);
    }

    /**
     *
     * @param context
     * @param data
     * @param clickable
     */
    public TopicSpan(Context context, SpanObject data, boolean clickable) {
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
        return false;
    }
}
