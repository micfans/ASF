package org.openpackage.asf.comp.c2;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.span.OnSpanClickListener;
import org.openpackage.asf.comp.c2.span.OnSpanTypeEnterListener;
import org.openpackage.asf.comp.c2.span.Span;
import org.openpackage.asf.comp.c2.span.SpanObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by micfans on 29/11/2016.
 */

public class CustomSpannableTextView extends TextView implements DataSpanSupportable {

    private static final Loger loger = Loger.getLoger(CustomSpannableTextView.class);

    /**
     *
     */
    private DataSpanRepostory dataSpanRepostory = new DataSpanRepostory();

    /**
     *
     */
    private OnSpanClickListener onSpanClickListener;

    /**
     *
     * @param context
     */
    public CustomSpannableTextView(Context context) {
        super(context);
        init();
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public CustomSpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomSpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init() {

    }

    /**
     * 取得指定位置的普通文本或区块富文本
     *
     * @param os
     * @param pos
     * @return
     */
    private String getString(String os, int pos) {
        String _sp = "</span>";
        char c = os.charAt(pos);
        if (c != '<') {
            return c + "";
        }
        int i = os.indexOf(_sp);
        if(i > -1) {
            int ek = os.indexOf(_sp) + _sp.length();
            String str = os.substring(pos, ek);
            return str;
        }else{
            return c + "";
        }

    }

    /**
     * 设置富文本
     *
     * @param s
     */
    public void setCustomString(CharSequence s) {
        loger.i("Custom str: " + s);
        this.setText("");
        String _rex = "\\s(\\w+)=\"(\\w*[^\"]+)\"";

        Pattern pat = Pattern.compile(_rex);
        Matcher mat;

        String os = s.toString();

        int pos = 0;
        while (os.length() > 0) {
            String str = getString(os, pos);
            if (str.length() == 1) {
                this.append(str);
            } else {
                loger.i("Find a span string: " + str);
                String text = str.substring(str.indexOf(">") + 1, str.lastIndexOf("<"));
                loger.i("Span text: " + text);
                String type = null;
                boolean clickable = false;
                mat = pat.matcher(str);
                SpanObject so = new SpanObject(text);
                int st = 0;
                while (mat.find()) {
                    String atName = mat.group(1);
                    String atValue = mat.group(2);
                    if (atName.equals("type")) {
                        type = atValue;
                    } else if (atName.equals("clickable")) {
                        clickable = Boolean.valueOf(atValue);
                    } else {
                        so.getAttrs().put(atName, atValue);
                    }
                }
                DataSpannableString dstr = dataSpanRepostory.createDataSpannableString(this.getContext(), type, clickable, so, this);
                dstr.setOnClickListener(onSpanClickListener);
                this.append(dstr);
            }
            os = os.substring(pos + str.length());
            pos = 0;
        }


    }

    /**
     *
     * @return
     */
    @Override
    public DataSpanRepostory getDataSpanRepostory() {
        return this.dataSpanRepostory;
    }

    /**
     *
     * @param what
     */
    @Override
    public void onSpanUpdated(Span what) {
        TextDataSpanHelper.update(this, what);
    }

    /**
     *
     * @return
     */
    @Override
    public Span[] getDataSpans() {
        return TextDataSpanHelper.getSpans(this.getSpannable());
    }

    /**
     *
     * @param start
     * @param end
     * @param string
     */
    @Override
    public void replaceDataSpannableString(int start, int end, DataSpannableString string) {
        Editable editable = (Editable) this.getText();
        editable.replace(start, end, string);
    }

    /**
     *
     * @return
     */
    @Override
    public Spannable getSpannable() {
        if (this.getText() instanceof Spannable) {
            return (Spannable) this.getText();
        }
        return null;
    }

    /**
     *
     * @param onSpanTypeEnterListener
     */
    @Override
    public void setOnSpanTypeEnterListener(OnSpanTypeEnterListener onSpanTypeEnterListener) {
        return;
    }

    /**
     *
     * @param onSpanClickListener
     */
    @Override
    public void setOnSpanClickListener(OnSpanClickListener onSpanClickListener) {
        this.onSpanClickListener = onSpanClickListener;
        this.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
