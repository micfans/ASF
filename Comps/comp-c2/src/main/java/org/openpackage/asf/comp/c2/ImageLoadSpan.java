package org.openpackage.asf.comp.c2;

/**
 * Created by micfans on 02/12/2016.
 */

public interface ImageLoadSpan extends LoadSpan {

    /**
     *
     * @return
     */
    SpanImageLoader getSpanImageLoader();

    /**
     *
     * @return
     */
    int getImageBoundWidth();

    /**
     *
     * @return
     */
    int getImageBoundHeight();
}
