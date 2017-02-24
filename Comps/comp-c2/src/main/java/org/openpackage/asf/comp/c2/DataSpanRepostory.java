package org.openpackage.asf.comp.c2;

import android.content.Context;

import org.openpackage.asf.base.util.Loger;
import org.openpackage.asf.comp.c2.span.RenderSpan;
import org.openpackage.asf.comp.c2.span.Span;
import org.openpackage.asf.comp.c2.span.SpanObject;
import org.openpackage.asf.comp.c2.span.impl.AtSpan;
import org.openpackage.asf.comp.c2.span.impl.EmojiSpan;
import org.openpackage.asf.comp.c2.span.impl.FixSpan;
import org.openpackage.asf.comp.c2.span.impl.LinkSpan;
import org.openpackage.asf.comp.c2.span.impl.TopicSpan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by micfans on 29/11/2016.
 */

public class DataSpanRepostory {

    private final static Loger loger = Loger.getLoger(DataSpanRepostory.class);

    /**
     * 数据区块表
     */
    private final Map<String, Class<? extends Span>> spans = new HashMap<>();

    /**
     * 后知数据区块表
     */
    private final Map<Character, Class<? extends RenderSpan>> rspans = new HashMap<>();

    /**
     *
     */
    DataSpanRepostory(){

        //注册内置区块
        this.regRenderSpanClass(TopicSpan.SPAN_TYPE, '#', TopicSpan.class);
        this.regRenderSpanClass(EmojiSpan.SPAN_TYPE, '/', EmojiSpan.class);
        this.regRenderSpanClass(FixSpan.SPAN_TYPE, '|', FixSpan.class);

        this.regSpanClass(LinkSpan.SPAN_TYPE, LinkSpan.class);
        this.regSpanClass(AtSpan.SPAN_TYPE, AtSpan.class);
    }

    /**
     * 取得仓库中的区块类型
     *
     * @return
     */
    public Set<String> getSpanTypes(){
        return spans.keySet();
    }

    /**
     * 取得仓库中的区块类
     *
     * @param type
     * @return
     */
    public Class<? extends Span> getSpanClass(String type){
        return spans.get(type);
    }

    /**
     *
     * @param type
     * @return
     */
    public Class<? extends Span> getRenderSpanClass(char type){
        return rspans.get(type);
    }

    /**
     *
     * @param type
     * @param clazz
     */
    public void regSpanClass(String type, Class<? extends Span> clazz){
        spans.put(type, clazz);
    }

    /**
     *
     * @param type
     * @param startAndEndChar
     * @param clazz
     */
    public void regRenderSpanClass(String type, Character startAndEndChar, Class<? extends RenderSpan> clazz){
        regSpanClass(type, clazz);
        rspans.put(startAndEndChar, clazz);
    }

    /**
     *
     * @param spanClass
     * @return
     */
    public String getSpanType(Class<? extends Span> spanClass){
        Iterator<Map.Entry<String,Class<? extends Span>>> it = spans.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Class<? extends Span>> en = it.next();
            if(en.getValue().equals(spanClass)){
                return en.getKey();
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public Set<Character> getRenderSpanChars(){
        return rspans.keySet();
    }

    /**
     *
     * @param context
     * @param type
     * @param clickable
     * @param spanObject
     * @return
     */
    public Span createSpan(Context context, String type, boolean clickable, SpanObject spanObject){
        Span span = null;
        Class<? extends Span> spanClass = getSpanClass(type);
        if(spanClass == null){
            return span;
        }
        try {
            span = spanClass.getConstructor(Context.class, SpanObject.class, boolean.class).newInstance(context, spanObject, clickable);
        }catch (Exception ex){
            loger.e("Can not instance span clazz.", ex);
        }
        return span;
    }


    /**
     *
     * @param span
     * @param dataSpanSupportable
     * @return
     */
    public DataSpannableString createDataSpannableString(Span span, DataSpanSupportable dataSpanSupportable){
        if(span == null){
            return null;
        }
        String display = span.getSpanObject().getText();
        DataSpannableString string = new DataSpannableString(display, span, dataSpanSupportable);
        return string;
    }

    /**
     *
     * @param context
     * @param type
     * @param clickable
     * @param spanObject
     * @param dataSpanSupportable
     * @return
     */
    public DataSpannableString createDataSpannableString(Context context, String type, boolean clickable, SpanObject spanObject, DataSpanSupportable dataSpanSupportable){
        Span span = createSpan(context, type, clickable, spanObject);
        if(span == null){
            return null;
        }
        return createDataSpannableString(span, dataSpanSupportable);
    }

    /**
     *
     * @param context
     * @param spanClass
     * @param clickable
     * @param spanObject
     * @param dataSpanSupportable
     * @return
     */
    public DataSpannableString createDataSpannableString(Context context, Class<? extends Span> spanClass, boolean clickable, SpanObject spanObject, DataSpanSupportable dataSpanSupportable){
        loger.i("Create data spannable string for " + spanClass.getSimpleName() + " - " + spanObject.getText());
        String type = getSpanType(spanClass);
        return createDataSpannableString(context, type, clickable, spanObject, dataSpanSupportable);
    }
}
