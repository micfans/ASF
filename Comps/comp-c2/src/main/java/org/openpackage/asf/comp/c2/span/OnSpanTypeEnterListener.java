package org.openpackage.asf.comp.c2.span;

import org.openpackage.asf.comp.c2.DataSpannableString;

import java.util.Set;

/**
 * Created by micfans on 29/11/2016.
 */

public interface OnSpanTypeEnterListener {

    /**
     * 指定的字符被输入
     *
     * @param type
     * @return
     */
    DataSpannableString onEnter(Character type);

    /**
     * 给定需要监控的字符
     *
     * @return
     */
    Set<Character> getEventChars();
}
