package org.openpackage.asf.comp.c2;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;

import org.openpackage.asf.comp.c2.span.Span;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by micfans on 30/11/2016.
 */

public class TextDataSpanHelper {

    /**
     *
     * @param sp
     * @return
     */
    public static Span[] getSpans(final Spannable sp){
        int select = sp.length();
        Span[] spans = sp.getSpans(0, select, Span.class);
        Arrays.sort(spans, new Comparator<Span>() {
            @Override
            public int compare(Span o1, Span o2) {
                int s1 = sp.getSpanStart(o1);
                int s2 = sp.getSpanStart(o2);
                return s1 > s2 ? 1 : -1;
            }
        });
        return spans;
    }

    /**
     *
     * @param da
     * @param what
     */
    public static void update(DataSpanSupportable da, Span what){
        Span[] spans = getSpans(da.getSpannable());
        for(Span span : spans){
            if(span.equals(what)){
                int st = da.getSpannable().getSpanStart(span);
                int ed = da.getSpannable().getSpanEnd(span);
                DataSpannableString spannableString = da.getDataSpanRepostory().createDataSpannableString(span, da);
                da.replaceDataSpannableString(st, ed, spannableString);
                break;
            }
        }
    }
}
