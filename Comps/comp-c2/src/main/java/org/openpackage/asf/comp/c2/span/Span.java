package org.openpackage.asf.comp.c2.span;

import android.text.Spannable;

import org.openpackage.asf.comp.c2.DataSpanSupportable;
import org.openpackage.asf.comp.c2.DataSpannableString;

import java.lang.ref.WeakReference;

/**
 * Created by micfans on 28/11/2016.
 *
 * 数据区块的接口类
 */

public interface Span {
    /**
     *
     * @return
     */
    String toSpanString();

    /**
     *
     * @return
     */
    SpanObject getSpanObject();

    /**
     *
     * @return
     */
    String getSpanText();

    /**
     *
     * @return
     */
    String getSpanType();

    /**
     * 是否整本区块
     *
     * @return
     */
    boolean isWhole();

    /**
     * 是否可删除
     *
     * @return
     */
    boolean deleteable();

    /**
     * 是否可移动
     *
     * @return
     */
    boolean moveable();

    /**
     *
     * @param onSpanClickListener
     */
    void setOnClickListener(OnSpanClickListener onSpanClickListener);

    /**
     *
     * @param dataSpannableString
     * @param textWeakReference
     */
    void apply(DataSpannableString dataSpannableString, WeakReference<DataSpanSupportable> textWeakReference);

    /**
     * 增加附加区块
     *
     * @param what
     */
    void addAdditionSpan(Object what);

    /**
     * 从指定字串中删除
     *
     * @param spannable
     */
    void removeFrom(Spannable spannable);
}
