package org.openpackage.asf.comp.c2;

import android.text.Editable;
import android.text.Spannable;

import org.openpackage.asf.comp.c2.span.OnSpanClickListener;
import org.openpackage.asf.comp.c2.span.OnSpanTypeEnterListener;
import org.openpackage.asf.comp.c2.span.Span;

/**
 * Created by micfans on 29/11/2016.
 *
 * 数据区块支持接口
 */

public interface DataSpanSupportable{

    /**
     * 取得数据区块仓库
     *
     * @return
     */
    DataSpanRepostory getDataSpanRepostory();

    /**
     * 指定的区块存在更新
     *
     * @param what
     */
    void onSpanUpdated(Span what);

    /**
     * 取得文本中的所有数据区块
     *
     * @return
     */
    Span[] getDataSpans();

    /**
     * 替换指定位置的区块文本
     *
     * @param start
     * @param end
     * @param string
     */
    void replaceDataSpannableString(int start, int end, DataSpannableString string);

    /**
     *
     * @return
     */
    Spannable getSpannable();

    /**
     *
     * @param onSpanTypeEnterListener
     */
    void setOnSpanTypeEnterListener(OnSpanTypeEnterListener onSpanTypeEnterListener);

    /**
     *
     * @param onSpanClickListener
     */
    void setOnSpanClickListener(OnSpanClickListener onSpanClickListener);
}
