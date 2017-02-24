package org.openpackage.asf.comp.c2;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.span.OnSpanClickListener;
import org.openpackage.asf.comp.c2.span.OnSpanTypeEnterListener;
import org.openpackage.asf.comp.c2.span.Span;
import org.openpackage.asf.comp.c2.span.SpanObject;
import org.openpackage.asf.comp.c2.span.impl.AtSpan;
import org.openpackage.asf.comp.c2.span.impl.FixSpan;
import org.openpackage.asf.comp.c2.span.impl.LinkSpan;
import org.openpackage.asf.comp.c2.span.impl.TopicSpan;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    private static final Loger loger = Loger.getLoger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c2_activity_main);
        setTitle(this.getLocalClassName());
        final CustomSpannableEditText editor = (CustomSpannableEditText) this.findViewById(R.id.editor);
        final CustomSpannableTextView viewer = (CustomSpannableTextView) this.findViewById(R.id.viewer);
        final CustomSpannableTextView viewer2 = (CustomSpannableTextView) this.findViewById(R.id.viewer2);


        editor.setOnSpanTypeEnterListener(new OnSpanTypeEnterListener() {
            @Override
            public DataSpannableString onEnter(Character type) {
                if (type == '@') {
                    DataSpannableString str = editor.getDataSpanRepostory().createDataSpannableString(MainActivity.this, AtSpan.SPAN_TYPE, true, new SpanObject("@Nick"), editor);
                    return str;
                }
                if (type == '#') {
                    DataSpannableString str = editor.getDataSpanRepostory().createDataSpannableString(MainActivity.this, TopicSpan.SPAN_TYPE, true, new SpanObject("#Topic#"), editor);
                    return str;
                }
                return null;
            }

            @Override
            public Set<Character> getEventChars() {
                Set<Character> sets = new HashSet<Character>();
                sets.add('@');
                sets.add('#');
                return sets;
            }
        });
        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewer.setCustomString(editor.getFormattedText());
                viewer2.setText(editor.getFormattedText());
            }
        });

        viewer.setOnSpanClickListener(new OnSpanClickListener() {
            @Override
            public void onClick(Span span) {
                loger.d("Clicked " + span.getSpanType());
            }
        });

        /*

        SpanObject so1 = new SpanObject("不可移动删除(主动)");
        editor.setText(editor.getDataSpanRepostory().createDataSpannableString(MainActivity.this, FixSpan.SPAN_TYPE, false, so1, editor));

        editor.append("|不可移动删除1(被动)|");

        SpanObject so2 = new SpanObject("[网页链接1]");
        so2.getAttrs().put("id", "http://www.baidu.com");
        editor.append(editor.getDataSpanRepostory().createDataSpannableString(MainActivity.this, LinkSpan.SPAN_TYPE, true, so2, editor));


        editor.append("|不可移动删除2(被动)|");

        SpanObject so3 = new SpanObject("[网页链接2]");
        so2.getAttrs().put("id", "http://www.baidu.com");
        editor.append(editor.getDataSpanRepostory().createDataSpannableString(MainActivity.this, LinkSpan.SPAN_TYPE, true, so3, editor));

*/

    }

}
