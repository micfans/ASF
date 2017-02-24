package org.openpackage.asf.comp.c2.span.impl;

import android.content.Context;

import org.openpackage.asf.comp.c2.span.DataSpan;
import org.openpackage.asf.comp.c2.span.SpanObject;

/**
 * Created by micfans on 01/12/2016.
 */

public class LinkSpan  extends DataSpan {

    /**
     *
     */
    public static final String SPAN_TYPE = "link";

    /**
     *
     * @param context
     * @param data
     */
    public LinkSpan(Context context, SpanObject data) {
        super(context, data);
    }

    /**
     *
     * @param context
     * @param data
     * @param clickable
     */
    public LinkSpan(Context context, SpanObject data, boolean clickable) {
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
