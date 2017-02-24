package org.openpackage.asf.comp.c2;

import android.content.Context;
import android.text.style.ImageSpan;

import org.openpackage.asf.comp.c2.span.Span;

/**
 * Created by micfans on 30/11/2016.
 */

public interface SpanImageLoader {

    /**
     *
     * @param context
     * @param span
     */
    void loadImage(final Context context, final ImageLoadSpan span);

}
