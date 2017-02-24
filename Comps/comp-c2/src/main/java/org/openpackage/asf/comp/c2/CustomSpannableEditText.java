package org.openpackage.asf.comp.c2;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.span.OnSpanClickListener;
import org.openpackage.asf.comp.c2.span.OnSpanTypeEnterListener;
import org.openpackage.asf.comp.c2.span.RenderSpan;
import org.openpackage.asf.comp.c2.span.Span;
import org.openpackage.asf.comp.c2.span.SpanObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by micfans on 28/11/2016.
 */

public class CustomSpannableEditText extends EditText implements DataSpanSupportable {

    private static final Loger loger = Loger.getLoger(CustomSpannableEditText.class);

    /**
     *
     */
    private DataSpanRepostory dataSpanRepostory = new DataSpanRepostory();
    /**
     *
     */
    private OnSpanTypeEnterListener onSpanTypeEnterListener;
    /**
     *
     */
    private OnSpanClickListener onSpanClickListener;

    /**
     * 格式化后的富文本（供返回）
     */
    private String spanText = "";

    /**
     * @param context
     */
    public CustomSpannableEditText(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomSpannableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomSpannableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @param selStart
     * @param selEnd
     */
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        loger.d("Select event position (" + selStart + "," + selEnd + ")");
        Editable _thisText = CustomSpannableEditText.this.getText();

        //已有区域选中，则判断是否允许
        if (selStart != selEnd) {
            Span span = getSpanByLocation(selStart, selEnd);
            if (span != null && !span.deleteable()) {
                //此区块不允许删除，所以要去除选中状态
                int spanEnd = _thisText.getSpanEnd(span);
                int spanStart = _thisText.getSpanStart(span);
                if (span.moveable()) {
                    //允许移动的区块，移至块首
                    CustomSpannableEditText.this.setSelection(spanStart);
                } else {
                    //不允许移动的Span，移至块尾
                    CustomSpannableEditText.this.setSelection(spanEnd);
                }
                //不再接受原选中范围
                return;
            }
        }

        Span span = getSpanByPosition(selStart);
        if (span != null) {
            int spanEnd = _thisText.getSpanEnd(span);
            int spanStart = _thisText.getSpanStart(span);
            if (selStart > spanStart && selStart < spanEnd) {
                //想选中整体块的中间？
                if (span.isWhole()) {
                    // 不允许选择 whole span，直接移至块尾
                    CustomSpannableEditText.this.setSelection(spanEnd);
                }
            }
        }

        loger.i("Curr pos is " + CustomSpannableEditText.this.getSelectionStart() + ", " + CustomSpannableEditText.this.getSelectionEnd());
    }


    /**
     *
     */
    private void init() {

        //处理键盘事件
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                loger.i("Key event code = " + keyCode + ", type = " + event.getAction());
                loger.d("Position is " + CustomSpannableEditText.this.getSelectionStart() + "," + CustomSpannableEditText.this.getSelectionEnd());
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //禁用回车键
                    return true;
                }
                if ((keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && event.getAction() == KeyEvent.ACTION_UP) {
                    //不理会键盘的UP事件
                    loger.i("Key del or left or right continue by up");
                    return true;
                }
                //响应 删除／左移／右移 的DOWN事件
                if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    loger.i("Key del or left or right invoked by down");
                    Editable _thisText = CustomSpannableEditText.this.getText();
                    int _st = CustomSpannableEditText.this.getSelectionStart();
                    int _en = CustomSpannableEditText.this.getSelectionEnd();
                    if (_st != _en) {
                        //说明已有选中，那就允许执行键盘功能
                        return false;
                    }
                    int select = _st;
                    //取当前位置的Span
                    Span span = getSpanByPosition(select);
                    if (span != null) {
                        int spanEnd = _thisText.getSpanEnd(span);
                        int spanStart = _thisText.getSpanStart(span);
                        loger.d("Current focus position " + select + ", checking span: " + span.getClass().getSimpleName() + " (" + spanStart + "," + spanEnd + ") ");
                        if (span.isWhole()) {
                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                if (select > spanStart) {
                                    if (span.deleteable()) {
                                        //整体Span 不允许单个删除，直接变为整体选中
                                        CustomSpannableEditText.this.setSelection(spanStart, spanEnd);
                                        loger.d("Selected Position is " + CustomSpannableEditText.this.getSelectionStart() + "," + CustomSpannableEditText.this.getSelectionEnd());
                                        loger.i("Removing whole span: " + span.getClass().getSimpleName() + " (" + spanStart + "," + spanEnd + ") on position " + select);
                                        return true;
                                    }

                                }
                            } else {
                                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    if (select == spanEnd) {
                                        //如果是在Span内向左移动，则直接到最左侧
                                        loger.i("Go left to " + spanStart);
                                        CustomSpannableEditText.this.setSelection(spanStart);
                                        return true;
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                    if (select == spanStart) {
                                        loger.i("Go right to " + spanEnd);
                                        //如果是在Span内向右移动，则直接到最右侧
                                        CustomSpannableEditText.this.setSelection(spanEnd);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });

        //字符输入事件
        this.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                loger.d("Text inputting, source = " + source.toString() + ", start=" + start + ", end=" + end + ", dest=" + dest + ", dstart=" + dstart + ", dend=" + dend);

                if(source.length() > 0){
                    //换到回车换行号
                    String s = source.toString();
                    s = s.replace('\n', ' ').replace('\r', ' ');
                    if(s.equals(" ")){
                        return s;
                    }
                    //输入文字
                    //如果之后存在不可移动区块，则不接受输入
                    Span span = lastNoMovableSpan(dstart);
                    if (span != null) {
                        loger.i("Find a last no moveable span, not allow input: " + source);
                        return "";
                    }
                    //输入监控字符事件
                    if(source.length() > 0) {
                        Character cs = source.charAt(end - 1);
                        if (onSpanTypeEnterListener != null && onSpanTypeEnterListener.getEventChars().contains(cs)) {
                            DataSpannableString str = onSpanTypeEnterListener.onEnter(cs);
                            return str;
                        }
                    }

                }else{
                    //删除文字
                    //如果之后存在不可移动区块，则不接受删除
                    Span span = lastNoMovableSpan(dstart);
                    if (span != null) {
                        return dest.subSequence(dstart, dend);
                    }
                    //如果之前的not Span.deletable，则不接受删除
                    span = spanEnd(dend);
                    if(span != null){
                        if(!span.deleteable()) {
                            return dest.subSequence(dstart, dend);
                        }else if(span.isWhole()){
                            int _st = CustomSpannableEditText.this.getSelectionStart();
                            int _en = CustomSpannableEditText.this.getSelectionEnd();
                            if (_st != _en) {
                                //说明已有选中，那就允许执行
                                return null;
                            }
                            int spanEnd = dest.getSpanEnd(span);
                            int spanStart = dest.getSpanStart(span);
                            CustomSpannableEditText.this.setSelection(spanStart, spanEnd);
                            return dest.subSequence(dstart, dend);
                        }
                    }
                }

                return null;
            }
        }});

        //处理文本变化事件
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                loger.d("Before Text change, s = " + s.toString() + ", start=" + start + ", count=" + count + ", after=" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loger.d("Text changed, s = " + s.toString() + ", start=" + start + ", before=" + before + ", count=" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (originText.equals(s.toString())) {
//                    return;
//                }
//                originText = s.toString();
                renderSpanText();
                loger.i("Span text: " + spanText);
            }
        });
    }

    /**
     * 替换关键字，因为富文本是用<>来组织的，所以要换掉用户的可能输入
     *
     * @param str
     * @return
     */
    protected String replaceChars(String str) {
        return str.replace('<', '＜').replace('>', '＞');
    }

    /**
     * 当前位置是不是某个不可移动区块的开始位置
     *
     * @return
     */
    private boolean isNoMovableRenderSpanStart() {
        int sel = this.getSelectionEnd();
        Span[] spans = this.getDataSpans();
        for (Span span : spans) {
            if (!span.moveable()) {
                int s = this.getSpannable().getSpanStart(span);
                if (sel == s) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找到前位置后面的最后一个不可移动块
     *
     * @param pos
     * @return
     */
    private Span lastNoMovableSpan(int pos) {
        Span[] spans = getDataSpans();
        for (int i = spans.length - 1; i >= 0; i--) {
            Span span = spans[i];
            int s = this.getSpannable().getSpanStart(span);
            if (!span.moveable() && s >= pos) {
                return span;
            }
        }
        return null;
    }

    /**
     * @param pos
     * @return
     */
    private Span spanStart(int pos) {
        Span span = getSpanByPosition(pos);
        if (span != null) {
            return this.getText().getSpanStart(span) == pos ? span : null;
        }
        return null;
    }

    private Span spanEnd(int pos) {
        Span span = getSpanByPosition(pos);
        if (span != null) {
            return this.getText().getSpanEnd(span) == pos ? span : null;
        }
        return null;
    }

    /**
     * 指定位置是否有Span
     *
     * @param pos
     * @return
     */
    private Span getSpanByPosition(int pos) {
        Span[] spans = getDataSpans();
        for (Span span : spans) {
            int st = this.getText().getSpanStart(span);
            int en = this.getText().getSpanEnd(span);
            if (pos >= st && pos <= en) {
                return span;
            }
        }
        return null;
    }

    /**
     * 给出的区域位置是否Span
     *
     * @param start
     * @param end
     * @return
     */
    private Span getSpanByLocation(int start, int end) {
        Span[] spans = getDataSpans();
        for (Span span : spans) {
            int st = this.getText().getSpanStart(span);
            int en = this.getText().getSpanEnd(span);
            if (start >= st && end <= en) {
                return span;
            }
        }
        return null;
    }

    /**
     * 指定位置内是存在指定的区块
     *
     * @param spanClass
     * @param start
     * @param end
     * @return
     */
    private RenderSpan renderSpanExists(Class<? extends Span> spanClass, int start, int end) {
        RenderSpan[] spans = this.getSpannable().getSpans(0, this.length(), RenderSpan.class);
        for (RenderSpan span : spans) {
            int s = this.getSpannable().getSpanStart(span);
            int e = this.getSpannable().getSpanEnd(span);
            if (span.getClass().equals(spanClass) && s == start && e == end) {
                return span;
            }
        }
        return null;
    }

    /**
     * 渲染 renderSpan
     */
    protected void renderSpans() {
        Editable _thisText = this.getText();
        int select = _thisText.length();

        List<RenderSpan> _rspans = Arrays.asList(_thisText.getSpans(0, select, RenderSpan.class));
        List<RenderSpan> rspans = new ArrayList<>();
        rspans.addAll(_rspans);

        int start = -1;
        Character ochar = null;
        for (int i = 0; i < select; i++) {
            char c = _thisText.charAt(i);
            if (start >= 0) {
                if (c == ' ') {
                    start = -1;
                }
            }
            Set<Character> rset = dataSpanRepostory.getRenderSpanChars();
            for (Character cr : rset) {
                if (cr == c) {
                    Class spanClass = dataSpanRepostory.getRenderSpanClass(c);
                    if (start < 0 || (ochar != null && ochar != c)) { //first
                        ochar = c;
                        start = i;
                    } else {
                        if (ochar == c) {
                            int end = i + 1;
                            final CharSequence t = _thisText.subSequence(start, end).toString();
                            loger.i("Searched a render span: " + t.toString());
                            if (t.length() > 2) {
                                //中间不是空的
                                RenderSpan span = renderSpanExists(spanClass, start, end);
                                if (span != null) {
                                    //现有区块，不做处理
                                    for (Span _s : rspans) {
                                        if (span.equals(_s)) {
                                            rspans.remove(span);
                                            loger.i("Span " + spanClass.getSimpleName() + "(" + start + "," + end + ") not change, continue.");
                                            break;
                                        }
                                    }
                                } else {
                                    DataSpannableString str = dataSpanRepostory.createDataSpannableString(this.getContext(), spanClass, false, new SpanObject(t.toString()), this);
                                    str.setOnClickListener(onSpanClickListener);
                                    loger.i("Replacing render Span " + spanClass.getSimpleName() + "(" + start + "," + end + ") created.");
                                    _thisText.replace(start, end, str).toString();
                                }
                            }
                            start = -1;
                            // found break
                            break;
                        }
                    }

                }
            }
        }
        for (Span span : rspans) {
            if (!span.isWhole()) {
                //删除余下的非整体数据区块
                loger.i("Remove not whole span: " + span.getClass().getSimpleName() + " - " + span.getSpanText());
                span.removeFrom(_thisText);
            }
        }
    }

    /**
     * 返回富本文
     *
     * @return
     */
    public String getFormattedText() {
        return spanText;
    }

    /**
     * @return
     */
    @Override
    public Span[] getDataSpans() {
        return TextDataSpanHelper.getSpans(this.getSpannable());
    }

    /**
     * @param start
     * @param end
     * @param string
     */
    @Override
    public void replaceDataSpannableString(int start, int end, DataSpannableString string) {
        this.getText().replace(start, end, string);
    }

    /**
     * @return
     */
    @Override
    public Spannable getSpannable() {
        return this.getText();
    }


    /**
     * 转当前文本为富文本
     */
    protected void renderSpanText() {
        renderSpans();
        Editable _thisText = this.getText();
        int select = _thisText.length();
        Span[] spans = this.getDataSpans();
        StringBuilder html = new StringBuilder();
        //如果没有任何span，则说明为全部普通字串，直接使用
        if (spans.length == 0) {
            spanText = replaceChars(_thisText.toString());
            return;
        }
        int start = 0;
        for (Span span : spans) {
            int spanStart = _thisText.getSpanStart(span);
            int spanEnd = _thisText.getSpanEnd(span);
            String _text = "";
            if (spanStart > start) {
                //当前的span起始位置大于当前位置，所以为普通字串，取出
                _text = _thisText.subSequence(start, spanStart).toString();
                _text = replaceChars(_text);
            }
            start = spanEnd;
            html.append(_text); //加入普通字串
            html.append(span.toSpanString()); //加入普通字串后的span字串
        }
        if (start < select) {
            //最后一个普通字串，加入
            String _text = _thisText.subSequence(start, select).toString();
            _text = replaceChars(_text);
            html.append(_text);
        }
        spanText = html.toString();
    }

    /**
     * @return
     */
    @Override
    public DataSpanRepostory getDataSpanRepostory() {
        return this.dataSpanRepostory;
    }

    /**
     * @param what
     */
    @Override
    public void onSpanUpdated(Span what) {
        TextDataSpanHelper.update(this, what);
        this.requestFocus();
        this.setSelection(this.length());
    }

    /**
     * @param onSpanTypeEnterListener
     */
    @Override
    public void setOnSpanTypeEnterListener(OnSpanTypeEnterListener onSpanTypeEnterListener) {
        this.onSpanTypeEnterListener = onSpanTypeEnterListener;
    }

    /**
     * @param onSpanClickListener
     */
    @Override
    public void setOnSpanClickListener(OnSpanClickListener onSpanClickListener) {
        this.onSpanClickListener = onSpanClickListener;
    }
}
