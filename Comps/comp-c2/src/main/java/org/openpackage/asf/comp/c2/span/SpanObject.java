package org.openpackage.asf.comp.c2.span;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by micfans on 29/11/2016.
 */

public class SpanObject {

    /**
     * 可展示文本
     */
    private String text = "";

    /**
     * 附加属性集
     */
    private Map<String,String> attrs = new HashMap<>();

    /**
     *
     */
    public SpanObject(){

    }

    /**
     *
     * @param text
     */
    public SpanObject(String text){
        this.setText(text);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("text=" + text);
        Iterator<String> it = attrs.keySet().iterator();
        while(it.hasNext()){
            String  k = it.next();
            sb.append(", ").append(k).append("=").append(attrs.get(k));
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getText(){
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }

    /**
     *
     * @param attrs
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
